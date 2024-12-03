package plugin.artofluxis.project.util.other

import kotlinx.serialization.Serializable
import java.util.UUID

/*stats to add:
 damage
 defense
 attack speed
 speed
 crit damage
 crit chance
 health
*/
/*
    current plan: (you can make changes wherever you see fit)
    take uuid -> load stats from uuid or init them
    *add a way to easily modify them.                // it is easily modifiable
*/

@Serializable
class PlayerStats(
    val playerName: String,
    var damageLVL: Int,
    var defenseLVL: Int,
    var atkSpeedLVL: Int,
    var walkSpeedLVL: Int,
    var critDamageLVL: Int,
    var critChanceLVL: Int,
    var healthLVL: Int
) {
    val damage: Double
        get() {
            return damageLVL*0.2+3
        }
    val defense: Double
        get() {
            return TODO()
        }
    val atkSpeed: Double
        get() {
            return TODO()
        }
    val walkSpeed: Double
        get() {
            return TODO()
        }
    val critDamage: Double
        get() {
            return TODO()
        }
    val critChance: Double
        get() {
            return TODO()
        }
    val maxHealth: Double
        get() {
            return TODO()
        }
}

lateinit var playersStats: HashMap<UUID, PlayerStats>