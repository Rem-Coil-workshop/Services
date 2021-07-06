package com.remcoil.boxes

class BoxesService(private val boxesDao: BoxesDao) {
    fun createBox(box: BoxInfo): Box = boxesDao.createBox(box)

    fun getAll(): List<Box> = boxesDao.getAllBoxes()

    fun updateBox(box: Box): Box {
        val boxFromDB = boxesDao.getBoxById(box.id)
        if (boxFromDB.number != box.number) throw NoSuchBoxException()
        return boxesDao.updateBox(box)
    }
}