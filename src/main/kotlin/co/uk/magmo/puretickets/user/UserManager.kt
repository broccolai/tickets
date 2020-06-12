package co.uk.magmo.puretickets.user

import co.uk.magmo.puretickets.storage.SQLManager
import java.util.*
import kotlin.collections.HashMap

class UserManager(private val sqlManager: SQLManager) {
    private val users = HashMap<UUID, UserSettings>()

    operator fun get(uuid: UUID): UserSettings {
        var settings = users[uuid]

        if (sqlManager.settings.exists(uuid)) {
            settings = sqlManager.settings.select(uuid)
        }

        if (settings == null) {
            settings = UserSettings(true)
            sqlManager.settings.insert(uuid, settings)
        }

        return settings
    }

    fun update(uuid: UUID, action: (UserSettings) -> Unit) {
        val settings = get(uuid).also(action)

        users[uuid] = settings
        sqlManager.settings.update(uuid, settings)
    }
}