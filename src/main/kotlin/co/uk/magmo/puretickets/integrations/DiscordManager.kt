package co.uk.magmo.puretickets.integrations

import co.uk.magmo.puretickets.configuration.Config
import com.github.kittinunf.fuel.Fuel
import com.google.gson.JsonObject

class DiscordManager {
    private val domain = "https://tickets.magmo.co.uk"
    private val enabled = Config.discordEnabled
    private val guild = Config.discordGuild
    private val token = Config.discordToken

    fun sendInformation(color: String, author: String, id: Int, action: String, fields: HashMap<String, String>) {
        if (!enabled) return

        val json = JsonObject()

        json.addProperty("color", color)
        json.addProperty("author", author)
        json.addProperty("id", id)
        json.addProperty("action", action)
        json.addProperty("color", color)

        if (fields.isNotEmpty()) {
            val content = JsonObject()

            fields.forEach { (name, value) ->
                content.addProperty(name, value)
            }

            json.add("fields", content)
        }

        Fuel.post("$domain/announce/$guild/$token")
                .body(json.toString()).timeout(1000).set("Content-Type", "application/json").response()
    }
}

