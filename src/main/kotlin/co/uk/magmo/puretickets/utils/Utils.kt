package co.uk.magmo.puretickets.utils

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

object Utils {
    fun mergeYAML(input: InputStream, destination: File) {
        val inputYaml = YamlConfiguration.loadConfiguration(InputStreamReader(input, "UTF-8"))
        val outputYaml = YamlConfiguration.loadConfiguration(destination)

        inputYaml.getKeys(true).forEach { path -> outputYaml[path] = outputYaml[path, inputYaml[path]] }
        outputYaml.save(destination)
    }
}