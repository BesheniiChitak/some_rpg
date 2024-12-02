package plugin.artofluxis.project.util.other

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bukkit.Bukkit
import plugin.artofluxis.project.plugin
import java.io.InputStreamReader
import java.lang.reflect.Type

val localized = setOf("ru", "en")
val localization = hashMapOf<String, HashMap<String, String>>()

fun loadConfig() {}

fun loadLocalization() {
    val gson = Gson()

    for (lang in localized) {
        val file = plugin.getResource("localization/$lang.json")?.reader()

        if (file != null) {
            try {
                // Define the type of the map
                val type: Type = object : TypeToken<HashMap<String, String>>() {}.type

                // Parse the JSON file into a map
                val langMap: HashMap<String, String> = gson.fromJson(file, type)

                // Add the parsed map to the localization map for the current language
                localization[lang] = langMap

            } catch (e: Exception) {
                Bukkit.getConsoleSender().sendMessage("Error loading localization for language $lang: ${e.message}")
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("Localization file for language $lang not found")
        }
    }

    // Output the localization data to the console for debugging
    Bukkit.getConsoleSender().sendMessage("Loaded localization: $localization")
}
