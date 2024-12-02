package plugin.artofluxis.project

/*object StatsLoad {

    private val statsfolder = File(plugin.dataFolder, "variables")
    private val pluginfolder = plugin.dataFolder

    private val saveFile = File(statsfolder, "player_data.json")

    fun load() {
        if (!pluginfolder.exists()) pluginfolder.mkdirs()
        if (!statsfolder.exists()) statsfolder.mkdirs()
        else {
            if (!saveFile.exists()) saveFile.createNewFile()
            playersTemp = Json.decodeFromString<HashMap<String, Double>>(saveFile.readText())
        }
    }

    fun save() {
        saveFile.writeText(Json.encodeToString(playersTemp))
    }
}*/
