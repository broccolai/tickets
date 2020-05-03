package co.uk.magmo.puretickets.storage.tables

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.text
import me.liuwj.ktorm.schema.uuid

object Tickets : Table<Nothing>("ticket") {
    val id by int("id").primaryKey()
    val uuid by uuid("uuid")
    val status by text("status")
    val picker by uuid("picker")
}