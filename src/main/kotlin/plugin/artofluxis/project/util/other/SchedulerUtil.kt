package plugin.artofluxis.project.util.other

import plugin.artofluxis.project.plugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import kotlin.time.Duration

/**
 * Функция для запуска асинхронного цикла.
 * @param period Раз во сколько запускается код
 * @param delay Задержка перед запуском в тиках
 * @param action Код, который нужно в каждую итерацию цикла
 */
fun runTaskTimer(period: Duration, delay: Int = 0, action: (BukkitTask) -> Unit) {
    Bukkit.getScheduler().runTaskTimer(plugin, { task -> action(task) }, delay.toLong(), period.toTicks)
}

// Расширение для конвертации продолжительности (Duration) в количество игровых тиков.
// Один тик Minecraft равен 50 миллисекундам.
val Duration.toTicks: Long
    get() = this.inWholeMilliseconds / 50

/**
 * Функция для запуска задачи с отложенным выполнением через указанное количество тиков.
 * @param delay Задержка перед запуском в тиках
 * @param action Код, который нужно выполнять после задержки
 */
fun runTaskLater(delay: Int, action: BukkitTask.() -> Unit) {
    Bukkit.getScheduler().runTaskLater(plugin, { task -> task.action() }, delay.toLong())
}

/**
 * Функция для запуска задачи асинхронно, чтобы не блокировать основной поток сервера.
 * @param action Код, который нужно выполнять асинхронно
 */
fun runTaskAsync(action: BukkitTask.() -> Unit) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, action)
}
