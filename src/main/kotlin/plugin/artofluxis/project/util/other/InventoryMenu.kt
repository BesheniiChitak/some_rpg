package plugin.artofluxis.project.util.other

import net.kyori.adventure.text.Component
import plugin.artofluxis.project.plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import kotlin.time.Duration.Companion.seconds

/**
 * Утилита для упрощения регистрации и отмены регистрации Listener'ов.
 */
fun Listener.unregister() = HandlerList.unregisterAll(this)
fun Listener.register() = Bukkit.getPluginManager().registerEvents(this, plugin)

/**
 * Класс InventoryMenu представляет меню инвентаря для игрока с настраиваемыми кнопками и действиями.
 * @param player Игрок, для которого открывается меню.
 * @param rows Количество строк в инвентаре.
 * @param title Заголовок инвентаря.
 * @param builder Лямбда-функция для настройки меню.
 */
class InventoryMenu(
    private val player: Player,
    rows: Int,
    title: Component,
    private val builder: InventoryMenu.() -> Unit = {}
) : InventoryHolder, Listener {

    private var closed = false // Флаг для отслеживания закрытия меню.
    private val _inventory = Bukkit.createInventory(this, rows * 9, title) // Создание инвентаря.
    private val objects = HashMap<Int, InventoryObject>() // Хранение объектов инвентаря по слотам.
    private var closeHandler: InventoryCloseEvent.() -> Unit = {} // Обработчик закрытия инвентаря.
    private var updateHandler: () -> Unit = {} // Обработчик обновления инвентаря.

    override fun getInventory(): Inventory = _inventory

    /**
     * Установка обработчика закрытия инвентаря.
     */
    fun onClose(action: InventoryCloseEvent.() -> Unit) {
        closeHandler = action
    }

    /**
     * Запуск обновления меню с заданной периодичностью.
     */
    fun updater(action: () -> Unit) {
        runTaskTimer(0.05.seconds) {
            if (closed) it.cancel() // Останавливаем таймер, если меню закрыто.
            else action() // Выполняем обновляющее действие.
        }
    }

    /**
     * Установка обработчика обновления инвентаря.
     */
    fun update(action: () -> Unit) {
        updateHandler = action
    }

    /**
     * Выполнение обновления меню с помощью заданного обработчика.
     */
    fun update() {
        updateHandler()
    }

    /**
     * Добавление кнопки в указанный слот инвентаря.
     */
    fun addButton(slot: Int, item: ItemStack, action: InventoryClickEvent.() -> Unit): InventoryMenu = apply {
        objects[slot] = InventoryButton(action) // Сохраняем действие кнопки.
        inventory.setItem(slot, item) // Устанавливаем предмет в инвентарь.
    }

    /**
     * Установка предмета в указанный слот инвентаря с возможностью блокировки кликов.
     */
    fun setItem(slot: Int, item: ItemStack, cancelClick: Boolean) {
        objects[slot] = InventoryItem(cancelClick) // Сохраняем действие предмета.
        inventory.setItem(slot, item) // Устанавливаем предмет в инвентарь.
    }

    /**
     * Установка предметов в несколько слотов инвентаря с возможностью блокировки кликов.
     */
    fun setItems(slots: IntArray, item: ItemStack, cancelClick: Boolean) {
        slots.forEach { slot ->
            setItem(slot, item, cancelClick) // Используем существующую логику для установки предметов.
        }
    }

    /**
     * Установка предметов в диапазон слотов инвентаря с возможностью блокировки кликов.
     */
    fun setItems(slots: IntRange, item: ItemStack, cancelClick: Boolean) {
        slots.forEach { slot ->
            setItem(slot, item, cancelClick)
        }
    }

    /**
     * Открытие меню для игрока и регистрация событий.
     */
    fun open(): InventoryMenu {
        builder(this) // Выполняем настройку меню.
        register() // Регистрируем события.
        player.openInventory(inventory) // Открываем инвентарь для игрока.
        return this
    }

    /**
     * Обработчик события клика по инвентарю.
     */
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val clickedInventory = event.clickedInventory ?: return
        if (clickedInventory.holder != this) return // Проверяем, что кликнут инвентарь этого меню.

        val slot = event.slot
        objects[slot]?.apply { // Обрабатываем действие в зависимости от типа объекта.
            when (this) {
                is InventoryButton -> {
                    event.isCancelled = true // Отменяем стандартное действие.
                    clickHandler(event) // Выполняем действие кнопки.
                }
                is InventoryItem -> {
                    event.isCancelled = cancelClick // Блокируем клик, если это настроено.
                }
            }
        }
    }

    /**
     * Обработчик события закрытия инвентаря.
     */
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (event.inventory.holder != this) return
        if (event.reason != InventoryCloseEvent.Reason.PLUGIN) // Проверяем причину закрытия.
            closeHandler(event) // Выполняем обработчик закрытия.
        closed = true
        inventory.clear() // Очищаем инвентарь после закрытия.
        objects.clear() // Очищаем все объекты инвентаря.
        unregister() // Отменяем регистрацию событий.
    }
}

/**
 * Класс для кнопки в инвентаре, выполняющей действие при клике.
 */
data class InventoryButton(
    val clickHandler: InventoryClickEvent.() -> Unit
) : InventoryObject

/**
 * Класс для обычного предмета в инвентаре, который может блокировать клики.
 */
data class InventoryItem(
    val cancelClick: Boolean
) : InventoryObject

/**
 * Интерфейс, представляющий объект инвентаря.
 */
interface InventoryObject

/**
 * Интерфейс для создания и открытия меню.
 */
interface Menu {
    fun open(player: Player): InventoryMenu
}

/**
 * Функция для отправки подтверждающего меню игроку.
 * @param player Игрок, которому отправляется меню.
 * @param title Заголовок меню.
 * @param confirmationInfo Список текста для подтверждения.
 * @param onConfirmation Действие при подтверждении.
 */
fun Menu.sendConfirmation(player: Player, title: String, confirmationInfo: List<Component>, onConfirmation: () -> Unit) {
    InventoryMenu(player, 3, plain(title)) {
        // Здесь можно добавить логику для подтверждающего меню.
    }.open()
}
