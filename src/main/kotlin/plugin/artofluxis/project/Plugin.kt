package plugin.artofluxis.project

import plugin.artofluxis.project.util.other.loadConfig
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

lateinit var plugin: Plugin

class Plugin : JavaPlugin() {

    override fun onEnable() {
        plugin = this

        if (!File(plugin.dataFolder, "config.yml").exists()) saveDefaultConfig()
        loadConfig()

        Bukkit.getPluginManager().registerEvents(GlobalListener, this)

        // plugin.getCommand("")!!.setExecutor()


        server.commandMap.getCommand("plugins")?.permission = "*"

        // load()
    }

    override fun onDisable() {
        // save()
    }
}
