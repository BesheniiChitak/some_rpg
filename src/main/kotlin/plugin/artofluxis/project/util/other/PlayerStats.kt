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
 oops forgor regen
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
    var healthLVL: Int,
    var regenLVL: Int
) {
    val damage: Double
        get() {
            return damageLVL * 0.2 + 3
        }
    val defense: Double
        get() {
            return defenseLVL * 0.5
        }
    val atkSpeed: Double
        get() {
            return 0.02 * atkSpeedLVL
        }
    val walkSpeed: Double
        get() {
            return 0.0025 * walkSpeedLVL
        }
    val critDamage: Double
        get() {
            return (5 * critDamageLVL).toDouble()
        }
    val critChance: Double
        get() {
            return 0.5 * critChanceLVL
        }
    val maxHealth: Double
        get() {
            return healthLVL.toDouble()
        }
    val regen: Double
        get() {
            return regenLVL.toDouble()
        }
}

lateinit var playersStats: HashMap<UUID, PlayerStats>