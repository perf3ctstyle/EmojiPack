package org.example

import java.awt.AlphaComposite
import java.awt.Image
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Resize images and save resized versions to a given folder
 *
 * @param filePaths - paths to images
 * @param resizedPath - folder to save the resized images
 * @return paths to resized images
 */
fun resize(originalFiles: List<File>, resizedPath: String): List<File> {
    val resizedFiles = ArrayList<File>()
    for (file in originalFiles) {
        val outputFilePath = resizedPath + File.separator + file.name
        val outputFile = File(outputFilePath).apply { parentFile?.mkdirs() }
        resizeImage(100, 100, "png", file, outputFile)
        resizedFiles.add(outputFile)
    }
    println("Resized ${resizedFiles.size} images")
    return resizedFiles
}

private fun resizeImage(targetWidth: Int, targetHeight: Int, format: String, inputFile: File, outputFile: File) {
    val originalImage: BufferedImage = ImageIO.read(inputFile)

    // Determine image type (use TYPE_INT_ARGB if original has transparency)
    val imageType = if (originalImage.transparency == BufferedImage.TRANSLUCENT) {
        BufferedImage.TYPE_INT_ARGB
    } else {
        originalImage.type
    }

    val (newWidth, newHeight) = calculateDimensions(
        originalImage.width,
        originalImage.height,
        targetWidth,
        targetHeight
    )

    // Create new image with transparency support
    val scaledImage = BufferedImage(newWidth, newHeight, imageType)

    // Get graphics context with transparency
    val graphics = scaledImage.createGraphics().apply {
        // Clear with transparent background
        composite = AlphaComposite.Clear
        fillRect(0, 0, newWidth, newHeight)
        composite = AlphaComposite.SrcOver

        // Set high-quality rendering hints
        setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        )
        setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
    }

    // Draw the scaled image
    graphics.drawImage(
        originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH),
        0, 0, null
    )
    graphics.dispose()

    // Write the result (PNG preserves transparency)
    ImageIO.write(scaledImage, format, outputFile)
    println("Resized image ${inputFile.name} and saved the result to ${outputFile.absolutePath}")
}

private fun calculateDimensions(
    originalWidth: Int,
    originalHeight: Int,
    targetWidth: Int,
    targetHeight: Int
): Pair<Int, Int> {
    return if (targetWidth == 0 || targetHeight == 0) {
        val aspectRatio = originalWidth.toDouble() / originalHeight.toDouble()
        when {
            targetWidth == 0 -> Pair((targetHeight * aspectRatio).toInt(), targetHeight)
            else -> Pair(targetWidth, (targetWidth / aspectRatio).toInt())
        }
    } else {
        Pair(targetWidth, targetHeight)
    }
}
