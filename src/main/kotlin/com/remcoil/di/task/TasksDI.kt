package com.remcoil.di.task

import com.remcoil.data.database.task.TasksDao
import com.remcoil.useCase.service.task.TasksService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.tasksComponents() {
    bind<TasksDao>() with singleton { TasksDao(instance()) }
    bind<TasksService>() with singleton { TasksService(instance()) }
}