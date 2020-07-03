package co.uk.magmo.puretickets.utilities;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtilities {
    public static void mergeYaml(InputStream input, File destination) {
        try {
            YamlConfiguration inputYaml = YamlConfiguration.loadConfiguration(new InputStreamReader(input, "UTF-8"));
            YamlConfiguration outputYaml = YamlConfiguration.loadConfiguration(destination);

            inputYaml.getKeys(true).forEach(path -> outputYaml.set(path, outputYaml.get(path, inputYaml.get(path))));
            outputYaml.save(destination);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Could not merge yaml");
        }
    }
}
