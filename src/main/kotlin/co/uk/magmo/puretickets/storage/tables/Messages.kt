package co.uk.magmo.puretickets.storage.tables

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.datetime
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.text
import me.liuwj.ktorm.schema.uuid

object Messages : Table<Nothing>("message") {
    val ticket by int("ticket")
    val reason by text("reason")
    val data by text("data")
    val sender by uuid("sender")
    val date by datetime("date")
}