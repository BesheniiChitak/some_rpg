package plugin.artofluxis.project.util.other

fun loadConfig() {}

fun main() {
    val b = mapOf("hi" to "hello", "bye" to "goodbye")
    // is equivalent to {"hi": "hello"}
    // cant do `b["hi"] = "hhh"` cuz b contains a non-mutable map

    val c = b.toMutableMap()
    c["hi"] = "hhh" // can do because c is b but mutable

    val d = hashMapOf("123" to "321") // an optimized version of the map, always mutable
    // its optimized because it doesnt have some features, but i dont remember which ones
    // basically its just a mutable map but better
    d["123"] = "135"


    for (i in 1..10) {  // a loop that goes from 1 to 10
        break // stops the loop on the first iteration
    }

    for ((k, v) in d) { // a loop that iterates through a map
        println("$k $v")
    }
    for (entry in d.entries) { // the same thing
        val k = entry.key
        val v = entry.value
        println("$k $v")
    }
    d.forEach { (k, v) -> // the same thing
        println("$k $v")
    }


    val g = hashMapOf("1" to 1, "2" to 2)
    g.map { it.key } // will return listOf("1", "2")
    g.map { it.value } // will return listOf(1, 2)
    g.map { it.key to it.value + 1 } // hashMapOf("1" to 2, "2" to 3) // basically just adds to each value
    g.map { it.key to "---${it.value}---" } // will return hashMapOf("1" to "---1---", "2" to "---2---")

    val h = listOf("123", "4321")
    h.map { it.length } // will return listOf(3, 4)

    // basically .map just allows you to do something to each element of the collection (maps and lists are collections)


    val i = mutableListOf("123")
    val o = i
    o.add("321")
    println(i) // will print ["123", "321"] even though we didnt change the `i` list directly
    // this is because of `links`, the `o` variable just contains a link to the `i` list
    // this doesnt happen with all types, but pretty much any complex type has this and its quite convenient in some cases

    val i2 = mutableListOf("123")
    val o2 = i2.toMutableList() // instead of making a link, we create a new list with the same elements
    // however if elements inside the list are complex too, then we need to use this:
    // val o2 = i2.map { it.copy() } // this doesnt work on primitive types but works on classes, etc
    o2.add("321")
    println(i2) // will print ["123"]
}



