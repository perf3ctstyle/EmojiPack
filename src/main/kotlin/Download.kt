package org.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File

/**
 * Download files and return file paths
 *
 * @param links - URLs of files to download (e.g., "https://domain-name.org/files/file.png")
 * @param downloadPath - path a local folder to save the file (e.g., "files/images")
 */
suspend fun download(links: List<String>, downloadPath: String): List<String> {
    val filePaths = ArrayList<String>()
    for (link in links) {
        val filename = getFilename(link)
        val downloadedFilePath = downloadFile(link, downloadPath, filename)
        filePaths.add(downloadedFilePath)
    }
    return filePaths
}

fun getFilename(link: String): String {
    val filename = link.substringAfter("emojis/").substringBefore('/')
    return "$filename.png"
}

suspend fun downloadFile(url: String, downloadPath: String, filename: String): String {
    val httpClient = HttpClient(CIO)
    var outputFilePath = downloadPath + File.separator + filename
    try {
        val outputFile = File(outputFilePath).apply { mkdirs() }
        httpClient.get(url).body<ByteReadChannel>().copyAndClose(outputFile.writeChannel())
        println("Successfully downloaded file ${filename} to folder ${outputFilePath}")
    } catch (e: Exception) {
        println("Exception while downloading file $filename, message: ${e.message}")
        outputFilePath = downloadPath + File.separator + filename + "_NOT_DOWNLOADED"
    } finally {
        httpClient.close()
    }
    return outputFilePath
}
