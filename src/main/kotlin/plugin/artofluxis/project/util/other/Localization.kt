package plugin.artofluxis.project.util.other

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

const val defaultLanguage = "en"

class Locale(
    private val key: String,
    private val lang: String = defaultLanguage
) {
    fun translate(): Component {
        return (localization[lang]?.get(key) ?: "${Color.RED}Unable to translate '$key' to $lang").deserialize()
    }
}

fun Player.sendLocale(message: String) {
    this.sendMessage(Locale(message).translate())
}