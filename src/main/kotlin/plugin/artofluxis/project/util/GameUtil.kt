package plugin.artofluxis.project.util

import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.state.BlockState
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.bukkit.craftbukkit.v1_20_R3.block.CraftBlock
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import kotlin.math.max
import kotlin.math.min

// Расширение для получения BlockPos из Location
val Location.blockPos: BlockPos
    get() = BlockPos(x.toInt(), y.toInt(), z.toInt())

// Расширение для получения NMS-игрока из Bukkit-игрока
val Player.nms: ServerPlayer
    get() = (this as CraftPlayer).handle

// Расширение для получения NMS-состояния блока из Bukkit-блока
val Block.nms: BlockState
    get() = (this as CraftBlock).nms

// Расширение для получения NMS-мира из Bukkit-мира
val World.nms: ServerLevel
    get() = (this as CraftWorld).handle

/**
 * Функция для отображения анимации разрушения блока в определённой позиции.
 * @param source - игрок, который инициировал разрушение.
 * @param progress - прогресс разрушения (от 1 до 10).
 * @param showToEveryone - если true, то показывать анимацию всем игрокам в мире.
 */
fun Location.setBlockDestruction(source: Player, progress: Int, showToEveryone: Boolean = false) {
    val dec = progress - 1 // Прогресс отображается от 0 до 9, поэтому уменьшаем значение на 1
    val pos = blockPos // Получаем позицию блока
    val hash = pos.hashCode() // Генерируем уникальный хеш для этой позиции

    // Определяем, каким игрокам показывать разрушение
    val players = if (showToEveryone) world.players else listOf(source)

    // Для каждого игрока отправляем пакет для отображения разрушения блока
    players.forEach { player ->
        player.nms.connection.send(
            ClientboundBlockDestructionPacket(
                hash, // Используем хеш для идентификации разрушения
                pos,  // Позиция блока
                dec   // Прогресс разрушения
            )
        )
    }
}


// Serializable класс для сохранения данных игрока
@Serializable
class PlayerSave

class Region(
    val corner1: Location,
    val corner2: Location,
    private val ignoreY: Boolean = false,
) {
    private val world: World = corner1.world

    init {
        if (world.name != corner2.world.name) throw IllegalArgumentException("Был создан регион из 2 местоположений в разных мирах.")
    }

    private val first = Location(
        world,
        min(corner1.x, corner2.x),
        if (ignoreY) -64.0 else min(corner1.y, corner2.y),
        min(corner1.z, corner2.z)
    )
    private val second = Location(
        world,
        max(corner1.x, corner2.x),
        if (ignoreY) 320.0 else max(corner1.y, corner2.y),
        max(corner1.z, corner2.z)
    )

    // Итерация по всем блокам в регионе с выполнением переданного действия
    fun iterate(action: (x: Int, y: Int, z: Int) -> Unit) {
        for (x in first.x.toInt()..second.x.toInt()) {
            for (y in first.y.toInt()..second.y.toInt()) {
                for (z in first.z.toInt()..second.z.toInt()) {
                    action(x, y, z)
                }
            }
        }
    }

    // Итерация по всем блокам с индексами смещения относительно первой точки
    fun iterateWithIndexes(action: (x: Int, y: Int, z: Int, xIndex: Int, yIndex: Int, zIndex: Int) -> Unit) {
        val firstX = first.x.toInt()
        val firstY = first.y.toInt()
        val firstZ = first.z.toInt()
        val secondX = second.x.toInt()
        val secondY = second.y.toInt()
        val secondZ = second.z.toInt()
        for (x in firstX..secondX) {
            for (y in firstY..secondY) {
                for (z in firstZ..secondZ) {
                    action(x, y, z, x - firstX, y - firstY, z - firstZ)
                }
            }
        }
    }

    /**
     * Копирование всех блоков из текущего региона в новое местоположение с учетом центра исходного региона и центра вставки.
     * @param sourceCenter - центр региона источника, откуда копируются блоки.
     * @param pasteCenter - центр вставки, куда добавляются блоки.
     */
    fun copyTo(sourceCenter: Location, pasteCenter: Location) {

        Bukkit.getConsoleSender()
            .sendMessage("Region size: (${second.x - first.x}, ${second.y - first.y}, ${second.z - first.z})")

        val sourceWorld = sourceCenter.world
        val pasteWorld = pasteCenter.world

        // Определите смещение для вставки на основе углов региона
        val xOffset = pasteCenter.blockX - sourceCenter.blockX
        val yOffset = pasteCenter.blockY - sourceCenter.blockY
        val zOffset = pasteCenter.blockZ - sourceCenter.blockZ

        // Итерация по регионам и копирование блоков
        iterate { x, y, z ->
            val sourceBlock = sourceWorld.getBlockAt(x, y, z)
            val pasteBlock = pasteWorld.getBlockAt(
                (x + xOffset),
                (y + yOffset),
                (z + zOffset)
            )
            pasteBlock.blockData = sourceBlock.blockData
        }
    }
}

/**
 * Преобразует строку в объект Region.
 * @param region - строка, содержащая информацию о мире, игнорировании Y, и координатах углов региона.
 * @return Region - регион, созданный на основе строки.
 */
fun toRegion(region: String): Region {
    val corners = region.split(" | ")
    val world = Bukkit.getWorld(corners[0])
    val corner1List = corners[2].split(", ").map { it.toDouble() }
    val corner2List = corners[3].split(", ").map { it.toDouble() }
    return Region(
        Location(world, corner1List[0], corner1List[1], corner1List[2]),
        Location(world, corner2List[0], corner2List[1], corner2List[2]),
        corners[1].toBoolean()
    )
}
