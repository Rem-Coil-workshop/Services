package com.remcoil.di.task

import com.remcoil.data.database.task.TasksDao
import com.remcoil.data.repository.TaskRepository
import com.remcoil.domain.useCase.TaskUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.tasksComponents() {
    bind<TasksDao>() with singleton { TasksDao(instance()) }
    bind<TaskRepository>() with singleton { TaskRepository(instance()) }
    bind<TaskUseCase>() with singleton { TaskUseCase(instance(), instance(), instance()) }
}