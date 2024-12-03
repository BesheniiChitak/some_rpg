package plugin.artofluxis.project

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import plugin.artofluxis.project.util.other.PlayerStats
import plugin.artofluxis.project.util.other.playersStats
import java.io.File
import java.util.UUID

object StatsLoad {

    private val pluginFolder = plugin.dataFolder
    private val statsFolder = File(pluginFolder, "variables")

    private val saveFile = File(statsFolder, "player_data.json")

    fun load() {
        if (!statsFolder.exists()) pluginFolder.mkdirs()
        if (!statsFolder.exists()) statsFolder.mkdirs()
        else {
            if (!saveFile.exists()) saveFile.createNewFile()
            playersStats = Json.decodeFromString<HashMap<UUID, PlayerStats>>(saveFile.readText())
        }
    }

    fun save() {
        saveFile.writeText(Json.encodeToString(playersStats))
    }
}
