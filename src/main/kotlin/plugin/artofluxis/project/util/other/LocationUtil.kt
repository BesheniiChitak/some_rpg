package plugin.artofluxis.project.util.other

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

/**
 * Преобразует строку в объект Location.
 * Формат строки: "worldName, x, y, z".
 * @param location Строка, содержащая имя мира и координаты, разделённые запятыми.
 * @return Объект Location, соответствующий указанным данным.
 */
fun toLocation(location: String): Location {
    val split = location.split(", ")
    return Location(
        Bukkit.getWorld(split[0]),  // Извлекаем мир по его имени.
        split[1].toDouble(),  // Преобразуем строку координат в double.
        split[2].toDouble(),
        split[3].toDouble()
    )
}

/**
 * Преобразует строку в объект Location.
 * Формат строки: "x, y, z".
 * @param world Мир в котором нужно создать координату
 * @param location Строка, содержащая координаты, разделённые запятыми.
 * @return Объект Location, соответствующий указанным данным.
 */
fun toLocation(world: World, location: String): Location {
    val split = location.split(", ")
    return Location(
        world, // Извлекаем мир.
        split[0].toDouble(),  // Преобразуем строку координат в double.
        split[1].toDouble(),
        split[2].toDouble()
    )
}

/**
 * Преобразует объект Location в строку.
 * Формат строки: "worldName, x, y, z".
 * @param location Объект Location для преобразования в строку.
 * @return Строковое представление Location.
 */
fun toString(location: Location): String {
    return "${location.world?.name ?: "undefined"}, ${location.x}, ${location.y}, ${location.z}"  // Добавлено значение по умолчанию для мира.
}
