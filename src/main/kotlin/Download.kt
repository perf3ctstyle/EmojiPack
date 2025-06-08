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
 * @param downloadPath - absolute path a local folder to save the file (e.g., "/home/user/files/images")
 * @return list of downloaded files
 */
suspend fun download(links: List<String>, downloadPath: String): List<File> {
    val files = ArrayList<File>()
    for (link in links) {
        val filename = getFilename(link)
        val file = downloadFile(link, downloadPath, filename)
        files.add(file)
    }
    println("Downloaded ${files.size} files")
    return files
}

private fun getFilename(link: String): String {
    val filename = link.substringAfter("emojis/").substringBefore('/')
    return "$filename.png"
}

private suspend fun downloadFile(url: String, downloadPath: String, filename: String): File {
    val httpClient = HttpClient(CIO)
    val outputFilePath = downloadPath + File.separator + filename
    val outputFile = File(outputFilePath).apply { parentFile?.mkdirs() }
    try {
        httpClient.get(url).body<ByteReadChannel>().copyAndClose(outputFile.writeChannel())
        println("Successfully downloaded file ${filename} and saved it to ${outputFilePath}")
    } catch (e: Exception) {
        println("Exception while downloading file $filename, message: ${e.message}")
    } finally {
        httpClient.close()
    }
    return outputFile
}
