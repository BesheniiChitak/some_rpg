package plugin.artofluxis.project.util.other

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

val ampersand = LegacyComponentSerializer.legacyAmpersand()
fun String.deserialize() = ampersand.deserialize(this)

operator fun Component.plus(other: Component): Component = this.append(other)
operator fun Component.times(other: Component): Component = this.append(other).color(this.color())
operator fun Component.minus(other: Component): Component = this.append(other).color(other.color())

@OptIn(ExperimentalStdlibApi::class)
enum class Color(val rgb: Int) {

    RED(0xff6e6e),
    LIME(0xa6ff6e),
    GRAY(0xabc4d6),
    GOLD(0xffb657);

    companion object {
        val format = HexFormat { number.prefix = "&#"; number.removeLeadingZeros = true }
    }

    override fun toString(): String {
        return this.rgb.toHexString(format)
    }
}

fun Component.color(color: Int) = this.color(TextColor.color(color))
fun String.color(color: Int) = plain(this).color(color)
fun Component.color(color: Color) = this.color(color.rgb)
fun String.color(color: Color) = this.color(color.rgb)

fun plain(text: Component): Component = text.decoration(TextDecoration.ITALIC, false)
fun plain(string: String): Component = plain(text(string))
