package com.kentrino.db

import org.jetbrains.exposed.dao.LongIdTable

object UfoSightings : LongIdTable() {
    val date = date("date")
}
