package plugin.artofluxis.project

import plugin.artofluxis.project.util.other.loadConfig
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin
import plugin.artofluxis.project.util.other.loadLocalization
import java.io.File

lateinit var plugin: Plugin

class Plugin : JavaPlugin() {

    override fun onEnable() {
        plugin = this

        if (!File(plugin.dataFolder, "config.yml").exists()) saveDefaultConfig()
        loadConfig()

        loadLocalization()

        Bukkit.getPluginManager().registerEvents(GlobalListener, this)

        // plugin.getCommand("")!!.setExecutor()


        server.commandMap.getCommand("plugins")?.permission = "*"

        // load()
    }

    override fun onDisable() {
        // save()
    }
}
