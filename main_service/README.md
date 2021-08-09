# Главный сервис
Использованные технологии:
* Kotlin
* Ktor
* Exposed
* Flyway
* Kodein
* Jwt

###  Конфигурация сервиса
Сервис конфигурируется в файле */src/main/resources/application.conf*. В данном файле конфигурируются следующие параметры:
* Параметры http:
```
http {  
    port = 80  
}
```
* Параметры базы данных:
```
database {  
    url = "jdbc:h2:./target/db"  
 	user = *****  
 	password = *****  
}
```
* Пути до остальных сервисов:
```
routes {  
    opener = "http://localhost:2000/open_slot"  
 	led = "http://localhost:3000"  
}
```
* Параметры генерации токенов:
```
secure {  
    secret = ********************
 	time = 86400000  
}
```
* Параметры логирования:
```
logSource {  
    jobLogFolder = "./log"  
 	serverLogFolder = "./server_log/archive"  
 	baseFileName = "log-"  
 	fileExtension = ".txt"  
}
```

### Базы данных
В качестве базы данных используется H2.

Для генерации схемы базы используется Flyway. Миграции располагаются в папке */src/main/resources/db/migration*. Миграции применяются при каждом запуске приложения.

Для взаимодействия сервиса с базой данных используется библиотека Exposed.

##### Описание таблиц
Для описания таблицы используются `object` классы. Они должны наследоваться от класса Table. Пример описания таблицы:
```kotlin
object Boxes : IntIdTable() {  
    val number = integer("box_number")  
    val taskId = reference("task_id", Tasks).nullable()  
}
```
Если значения id строк генерируются автоматически, то таблицу можно наследовать от IntIdTable (как в приведённом случае) или от подобной. В данном случае будет автоматически добавлено поле `id`, которое будет так же являются первичным ключом.

В теле класса описываются поля, которые являются колонками таблицы. С полным списком доступных типов можно ознакомиться [тут](https://github.com/JetBrains/Exposed/wiki/DataTypes).

##### Операции с данными

Для операций с данными используется DSL. Для выполнения асинхронных запросов в базу используется специальный метод:
```kotlin
suspend fun <T> safetySuspendTransaction(  
    db: Database,  
 	errorMessage: String? = "Ошибка при выполнении запроса в базу данных",  
 	statement: Transaction.() -> T  
): T = withContext(Dispatchers.IO) {  
 try {  
        return@withContext transaction(db, statement)  
    } catch (e: ExposedSQLException) {  
        logger.debug("${e.message}")  
        throw DatabaseException(errorMessage)  
    }  
}
```
Функция `statment` выполняется в контексте своей транзакции. Далее выполняется сама операция с данными. Сначала указывается таблица, к которой обращаемся, далее необходимая операция. Например, поиск ячейки по **id**:
```kotlin
suspend fun getBoxByTaskId(taskId: Int): Box? = safetySuspendTransaction(database) {  
 	Boxes.select { Boxes.taskId eq taskId }.map(::extractBox).firstOrNull()  
}
```
Примеры работы с данным DSL и доступные методы описаны [тут](https://github.com/JetBrains/Exposed/wiki/DSL).

Вся работа с базой данных ведется в пакете _/data/database/*_

### Обслуживание запросов
Все доступные конечные точки описываются в пакете _/presentation/web/*_.

Для описания конечных точек используется следующий DSL:
1. Описывается название модуля:
```kotlin
fun Application.moduleName() {
	/* body */
}
```
2. Описывается адрес группы точек:
```kotlin 
routing {  
	route("/url") {
		/* body */
	}
}
```
3. Описывается метод, который будет обслуживаться:
```kotlin 
get {
	/* body */
}
```
4. Далее следуют необходимые операции

**Каждый запрос должен заканчиваться вызовом метода respond() у объекта call**

Пример одного модуля (модулю работы с ячейками):
```kotlin
fun Application.boxesModule() {  
    val service: BoxesService by closestDI().instance()  
  
    routing {  
 		route("/v1/boxes") {  
 			get {  
 				call.respond(service.getAll())  
            }  
			
			post {  
 				call.safetyReceive<Box> { box ->  
 					call.respond(service.createBox(box))  
            	}  
 			}  
 		
			put {  
 				call.safetyReceive<Box> { box ->  
 					call.respond(service.updateBox(box))  
                }  
			} 
		} 
	}
}
```
Примеры работы с данным DSL и доступные методы описаны [тут](https://ktor.io/docs/routing-in-ktor.html).

### Бизнес логика
Основная логика приложения располагается в пакете _/domain/*_.

##### Простые запросы
Простыми запросами — это запросы, которые не требует каких либо дополнительных преобразований. Для этого выполняется запрос в базу данных и логирование операции. Например, получение списка ячеек:
```kotlin
suspend fun getAll(): List<Box> {  
    val boxes = boxesDao.getAllBoxes()  
    logger.info("Отдал ${boxes.size} ячеек")  
    return boxes  
}
```
##### Запросы изменения состояния
В системе есть состояние ячеек: текущий сотрудник и текущий введенный код. За управление состоянием отвечает класс _SlotService_. Он проверяет корректность введенных данных и изменит состояние.
```kotlin
interface SlotService {  
    suspend fun onCardNumberEntered(card: Int)    
    suspend fun onQrCodeEntered(qrCode: String)  
    suspend fun resetState()  
}
```
Состояние храниться в классе _SlotState_. Там есть необходимые методы для работы с состоянием:
```kotlin
interface SlotState {  
    val isReady: Boolean    
    val cardNumber: Int    
    val qrCode: String  
  
    suspend fun setCardNumber(card: Int)    
    suspend fun setQrNumber(qr: String)   
    suspend fun reset()  
}
```
Если состояние готово к открытию (введены оба значения), сервис откроет соответствующую ячейку:
```kotlin
private suspend fun openSlot() {  
    if (state.isReady) {  
        if (opener.openByQrCode(state.qrCode)) {  
            logsService.log(state.qrCode, state.cardNumber)  
        }  
        state.reset()  
    }  
}
```
За открытие ячеек отвечает _SlotOpener_
```kotlin
interface SlotOpener {  
    suspend fun openByQrCode(qrCode: String): Boolean  
    suspend fun openByBoxNumber(id: Int): Boolean  
}
```
Для открытия ячеек данный класс отправляет запрос к соответствующему сервису:
```kotlin
private suspend fun open(boxNumber: Int): Boolean {  
    val response = client.post<HttpResponse>(urlString = routesConfig.opener) {  
 		contentType(ContentType.Application.Json)  
        body = SlotInfo(boxNumber)  
    }  
 	val result = response.status.isSuccess()  
    if (result) logger.info("Открыт ящик №$boxNumber")  
    return result  
}
```