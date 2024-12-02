package plugin.artofluxis.project

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import plugin.artofluxis.project.util.other.sendLocale

object GlobalListener : Listener {
    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        event.player.sendLocale("plugin.test")
        event.player.sendLocale("plugin.test2")
    }
}