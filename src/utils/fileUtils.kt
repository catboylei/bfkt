package utils 

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.chmod
import platform.posix.errno
import platform.posix.fclose
import platform.posix.ferror
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fwrite
import platform.posix.strerror

class FileException(message: String) : Exception(message)

@OptIn(ExperimentalForeignApi::class)
private fun errnoText(): String =
    strerror(errno)?.toKString() ?: "unknown error (errno $errno)"

private const val BUF = 4096

@OptIn(ExperimentalForeignApi::class)
fun readBytes(path: String): ByteArray {
    val f = fopen(path, "rb") ?: throw FileException("cannot open '$path': ${errnoText()}")
    try {
        val chunks = mutableListOf<ByteArray>()
        var total = 0
        memScoped {
            val buf = allocArray<ByteVar>(BUF)
            while (true) {
                val n = fread(buf, 1.convert(), BUF.convert(), f).toInt()
                if (n > 0) { chunks += buf.readBytes(n); total += n }
                if (n < BUF) break 
            }
        }
        if (ferror(f) != 0) throw FileException("cannot read '$path': ${errnoText()}")

        val out = ByteArray(total)
        var pos = 0
        for (c in chunks) { c.copyInto(out, pos); pos += c.size }
        return out
    } finally {
        fclose(f)
    }
}

fun readText(path: String): String = readBytes(path).decodeToString()

@OptIn(ExperimentalForeignApi::class)
fun writeBytes(path: String, data: ByteArray, executable: Boolean = false) {
    val f = fopen(path, "wb") ?: throw FileException("cannot create '$path': ${errnoText()}")
    var failed = false
    if (data.isNotEmpty()) {
        val written = data.usePinned {
            fwrite(it.addressOf(0), 1.convert(), data.size.convert(), f)
        }
        if (written.toLong() != data.size.toLong()) failed = true
    }
    if (failed) { val msg = errnoText(); fclose(f); throw FileException("cannot write '$path': $msg") }
    if (fclose(f) != 0) throw FileException("cannot finish writing '$path': ${errnoText()}")

    if (executable && chmod(path, 493.convert()) != 0)
        throw FileException("cannot make '$path' executable: ${errnoText()}")
}

