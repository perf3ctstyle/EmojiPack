package org.example

fun filter(allLinks: List<String>, ignoreLinks: List<String>): List<String> {
    val setOfIgnoreLinks = ignoreLinks.toHashSet()
    return allLinks.filterNot { it in setOfIgnoreLinks }
}
