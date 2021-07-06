package com.remcoil.boxes

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class BoxesDao(private val database: Database) {
    fun getAllBoxes(): List<BoxFromDB> = transaction(database) {
        Boxes
            .selectAll()
            .map(::extractBox)
    }

    private fun extractBox(row: ResultRow): BoxFromDB = BoxFromDB(
        row[Boxes.id].value,
        row[Boxes.number],
        row[Boxes.taskId]?.value
    )
}