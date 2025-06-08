package org.example

import java.io.File

fun read(filePaths: List<String>): List<String> {
    val downloadLinks = HashSet<String>()
    for (filePath in filePaths) {
        File(filePath).useLines { lines -> lines.forEach { downloadLinks.add(it) } }
    }
    println("Read ${downloadLinks.size} unique links from the following files: $filePaths")
    return downloadLinks.toList()
}
