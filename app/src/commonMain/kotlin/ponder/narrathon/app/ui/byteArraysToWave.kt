package ponder.narrathon.app.ui

import java.io.ByteArrayOutputStream
import kotlin.math.min

data class WavFormat(
    val audioFormat: Int,      // 1 = PCM, 3 = IEEE float
    val channels: Int,
    val sampleRate: Int,
    val byteRate: Int,
    val blockAlign: Int,
    val bitsPerSample: Int
)

/**
 * Merge full WAV files by extracting their "data" chunks and concatenating.
 * All inputs must share the same format (channels/rate/bits/audioFormat).
 *
 * @param wavs List of complete RIFF/WAVE files (byte arrays)
 * @return A single WAV (header + combined PCM) as ByteArray
 */
fun mergeWavsToWav(wavs: List<ByteArray>): ByteArray {
    require(wavs.isNotEmpty()) { "Give me at least one WAV, ye scallywag." }

    // Parse first WAV for canonical format
    val firstParsed = parseWav(wavs.first())
    require(firstParsed.format.audioFormat == 1 || firstParsed.format.audioFormat == 3) {
        "Only PCM (1) or IEEE float (3) supported"
    }
    require(firstParsed.dataSize > 0) { "First WAV has no data" }
    val fmt = firstParsed.format

    // Collect data from each WAV, verifying matching format
    val dataChunks = ArrayList<ByteArray>()
    var totalData = 0
    for (wav in wavs) {
        val p = parseWav(wav)
        require(p.format == fmt) { "Mismatched WAV format; all inputs must match" }
        for (seg in p.dataSegments) {
            dataChunks.add(seg)
            totalData += seg.size
        }
    }

    val out = ByteArrayOutputStream()
    val riffChunkSize = 36 + totalData
    val byteRate = fmt.sampleRate * fmt.channels * (fmt.bitsPerSample / 8)
    val blockAlign = fmt.channels * (fmt.bitsPerSample / 8)

    // RIFF header
    out.writeAscii("RIFF")
    out.writeLE32(riffChunkSize)
    out.writeAscii("WAVE")

    // fmt  chunk
    out.writeAscii("fmt ")
    out.writeLE32(16) // PCM size (we write canonical 16 even if source had ext)
    out.writeLE16(fmt.audioFormat.toShort())
    out.writeLE16(fmt.channels.toShort())
    out.writeLE32(fmt.sampleRate)
    out.writeLE32(byteRate)
    out.writeLE16(blockAlign.toShort())
    out.writeLE16(fmt.bitsPerSample.toShort())

    // data chunk
    out.writeAscii("data")
    out.writeLE32(totalData)
    for (seg in dataChunks) out.write(seg)

    return out.toByteArray()
}

/** Minimal WAV parser: grabs fmt and all data segments, skips the rest. */
private data class ParsedWav(
    val format: WavFormat,
    val dataSegments: List<ByteArray>,
    val dataSize: Int
)

private fun parseWav(bytes: ByteArray): ParsedWav {
    require(bytes.size >= 12) { "Too small to be WAV" }
    fun idAt(o: Int) = String(bytes, o, 4, Charsets.US_ASCII)

    require(idAt(0) == "RIFF" && idAt(8) == "WAVE") { "Not a RIFF/WAVE" }
    var offset = 12
    var fmt: WavFormat? = null
    val datas = ArrayList<ByteArray>()
    var totalData = 0

    while (offset + 8 <= bytes.size) {
        val chunkId = idAt(offset)
        val chunkSize = readLE32(bytes, offset + 4)
        val dataStart = offset + 8
        val dataEnd = min(bytes.size, dataStart + chunkSize)
        if (dataStart > bytes.size) break

        when (chunkId) {
            "fmt " -> {
                require(chunkSize >= 16) { "fmt chunk too small" }
                val audioFormat   = readLE16(bytes, dataStart).toInt() and 0xFFFF
                val channels      = readLE16(bytes, dataStart + 2).toInt() and 0xFFFF
                val sampleRate    = readLE32(bytes, dataStart + 4)
                val byteRate      = readLE32(bytes, dataStart + 8)
                val blockAlign    = readLE16(bytes, dataStart + 12).toInt() and 0xFFFF
                val bitsPerSample = readLE16(bytes, dataStart + 14).toInt() and 0xFFFF
                fmt = WavFormat(audioFormat, channels, sampleRate, byteRate, blockAlign, bitsPerSample)
                // ignore any fmt extension beyond 16 bytes
            }
            "data" -> {
                val seg = bytes.copyOfRange(dataStart, dataEnd)
                datas.add(seg)
                totalData += seg.size
            }
            else -> {
                // skip other chunks (LIST, fact, JUNK, etc.)
            }
        }
        // Chunks are word-aligned: pad to even
        val padded = chunkSize + (chunkSize % 2)
        offset = dataStart + padded
    }

    val f = fmt ?: error("Missing fmt chunk")
    return ParsedWav(f, datas, totalData)
}

// -------- little-endian helpers --------
private fun readLE16(b: ByteArray, o: Int): Short {
    val v = (b[o].toInt() and 0xFF) or ((b[o + 1].toInt() and 0xFF) shl 8)
    return v.toShort()
}
private fun readLE32(b: ByteArray, o: Int): Int {
    return (b[o].toInt() and 0xFF) or
            ((b[o + 1].toInt() and 0xFF) shl 8) or
            ((b[o + 2].toInt() and 0xFF) shl 16) or
            ((b[o + 3].toInt() and 0xFF) shl 24)
}
private fun ByteArrayOutputStream.writeAscii(s: String) {
    write(s.encodeToByteArray())
}
private fun ByteArrayOutputStream.writeLE16(v: Short) {
    write(byteArrayOf((v.toInt() and 0xFF).toByte(), ((v.toInt() ushr 8) and 0xFF).toByte()))
}
private fun ByteArrayOutputStream.writeLE32(v: Int) {
    write(byteArrayOf(
        (v and 0xFF).toByte(),
        ((v ushr 8) and 0xFF).toByte(),
        ((v ushr 16) and 0xFF).toByte(),
        ((v ushr 24) and 0xFF).toByte()
    ))
}