package co.uk.magmo.puretickets.user

import co.uk.magmo.puretickets.storage.SQLFunctions
import java.util.*
import kotlin.collections.HashMap

class UserManager {
    private val users = HashMap<UUID, UserSettings>()

    operator fun get(uuid: UUID): UserSettings {
        var settings = users[uuid]

        if (SQLFunctions.settingsExist(uuid)) {
            settings = SQLFunctions.retrieveUserSettings(uuid)
        }

        if (settings == null) {
            settings = UserSettings(true)
            SQLFunctions.insertUserSettings(uuid, settings)
        }

        return settings
    }

    fun update(uuid: UUID, action: (UserSettings) -> Unit) {
        val settings = get(uuid).also(action)

        users[uuid] = settings
        SQLFunctions.updateUserSettings(uuid, settings)
    }
}