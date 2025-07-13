package org.example

import java.io.File

suspend fun main() {
    val workingDir = System.getProperty("user.home") +
            File.separator + "Downloads" +
            File.separator + "ImagesWithKotlin"
    val linksPath = workingDir + File.separator + "Links"
    val downloadPath = workingDir + File.separator + "DownloadedImages" + File.separator + "13_07_2025"
    val resizedPath = workingDir + File.separator + "ResizedImages" + File.separator + "13_07_2025"
    println("Processing is about to start. Config:\nLinks folder: $linksPath\nDownload folder: $downloadPath\nResized folder: $resizedPath")

    val pathsToFilesWithLinks = listOf(
        linksPath + File.separator + "links_13_07_2025.txt",
    )
    val pathsToFilesWithIgnoreLinks = listOf(
        linksPath + File.separator + "links_ignore.txt", // strange and already processed links
    )
    val downloadLinks = read(pathsToFilesWithLinks)
    val ignoreLinks = read(pathsToFilesWithIgnoreLinks)
    val filteredLinks = filter(downloadLinks, ignoreLinks)
    val downloadedFiles = download(filteredLinks, downloadPath)
    resize(downloadedFiles, resizedPath)
}
