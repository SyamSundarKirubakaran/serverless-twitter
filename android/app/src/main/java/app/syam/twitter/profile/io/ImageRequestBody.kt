package app.syam.twitter.profile.io

import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.File

class ImageRequestBody(private val file: File) : RequestBody() {

    override fun contentType(): MediaType? = MediaType.parse("image/jpeg")

    override fun writeTo(sink: BufferedSink) {
        var source: Source? = null

        source = file.source()

        if(source == null){
            Log.d("@@@","File access error")
        }

        val buf = Buffer()
        val size = contentLength()
        var remaining = size
        var readCount: Long = source!!.read(buf, 2048)
        while (readCount != -1L) {
            sink.write(buf, readCount)
            remaining -= readCount
            readCount = source.read(buf, 2048)
        }
    }

}