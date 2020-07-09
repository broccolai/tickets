package co.uk.magmo.puretickets.integrations;

import co.uk.magmo.puretickets.configuration.Config;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class DiscordManager {
    private final String domain = "https://tickets.magmo.co.uk";

    private final Boolean enabled;
    private final String guild;
    private final String token;

    public DiscordManager(Config config) {
        this.enabled = config.DISCORD__ENABLED;
        this.guild = config.DISCORD__GUILD;
        this.token = config.DISCORD__TOKEN;
    }

    public void sendInformation(String color, String author, Integer id, String action, HashMap<String, String> fields) {
        if (!enabled) return;

        JsonObject json = new JsonObject();

        json.addProperty("color", color);
        json.addProperty("author", author);
        json.addProperty("id", id);
        json.addProperty("action", action);
        json.addProperty("color", color);

        if (!fields.isEmpty()) {
            JsonObject content = new JsonObject();

            fields.forEach(content::addProperty);

            json.add("fields", content);
        }

        URL url;
        int responseCode;

        try {
            url = new URL(domain + "/announce/" + guild + "/" + token);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setConnectTimeout(1000);
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            byte[] out = json.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();

            try (OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            responseCode = http.getResponseCode();
        } catch (IOException e) {
            Bukkit.getLogger().warning("Error connecting to discord integration");
            return;
        }

        if (responseCode != 200) {
            Bukkit.getLogger().warning("Error connecting to discord integration");
            Bukkit.getLogger().warning(url.toString());
        }
    }
}

