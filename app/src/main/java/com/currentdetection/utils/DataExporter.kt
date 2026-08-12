package com.currentdetection.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.currentdetection.data.local.entities.PowerEventEntity
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DataExporter {

    fun exportCSV(context: Context, events: List<PowerEventEntity>) {
        val fileName = "current_detection_history.csv"
        val file = File(context.cacheDir, fileName)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        FileWriter(file).use { writer ->
            writer.append("Date,Start,End,Duration\n")
            
            for (event in events) {
                if (event.endTime != null && event.duration != null) {
                    val dateStr = dateFormat.format(Date(event.startTime))
                    val startStr = timeFormat.format(Date(event.startTime))
                    val endStr = timeFormat.format(Date(event.endTime))
                    val durationStr = formatDuration(event.duration)
                    writer.append("$dateStr,$startStr,$endStr,$durationStr\n")
                }
            }
        }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Export History"))
    }

    private fun formatDuration(millis: Long): String {
        val totalSeconds = millis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
