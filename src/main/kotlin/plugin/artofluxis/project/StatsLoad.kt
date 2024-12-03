package plugin.artofluxis.project

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import plugin.artofluxis.project.util.other.PlayerStats
import plugin.artofluxis.project.util.other.playersStats
import java.io.File
import java.util.UUID

object StatsLoad {

    private val statsfolder = File(plugin.dataFolder, "variables")
    private val pluginfolder = plugin.dataFolder

    private val saveFile = File(statsfolder, "player_data.json")

    fun load() {
        if (!pluginfolder.exists()) pluginfolder.mkdirs()
        if (!statsfolder.exists()) statsfolder.mkdirs()
        else {
            if (!saveFile.exists()) saveFile.createNewFile()
            playersStats = Json.decodeFromString<HashMap<UUID, PlayerStats>>(saveFile.readText())
        }
    }

    fun save() {
        saveFile.writeText(Json.encodeToString(playersStats))
    }
}
