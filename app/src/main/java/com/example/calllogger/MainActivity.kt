package com.example.calllogger

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calllogger.databinding.ActivityMainBinding
import java.io.File
import java.lang.Long
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.also
import kotlin.arrayOf


class MainActivity : AppCompatActivity() {
    companion object {
        private const val CALL_LOG_CODE = 100
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG), 101)
        }
        refreshList()

        binding.refreshButton.setOnClickListener {
            Toast.makeText(applicationContext,"Refreshed!",Toast.LENGTH_SHORT).show()
            refreshList()
        }
    }

    private fun refreshList(){
        val loglist : MutableList<LogInfo> = getCallLogs()
        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = CallLogAdapter(loglist)
        recyclerview.adapter = adapter
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)
        "Last Sync : $current".also { binding.syncTv.text = it }

//        val audioFilePath = "${Environment.getExternalStorageDirectory().path}/call_recording.mp4"


        val folder : File = File("/data/user/0/com.google.android.dialer/files/callrecording")
        val filesInFolder = folder.listFiles()
        if (filesInFolder != null) {
            Log.e("call recording",filesInFolder.toString())
        }
        else Log.e("call recording","not foundddddddddd")
    }

    private fun getCallLogs() : MutableList<LogInfo> {
        val cursor = baseContext.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)!!
        val logList = mutableListOf<LogInfo>()
        val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
        val type = cursor.getColumnIndex(CallLog.Calls.TYPE)
        val date = cursor.getColumnIndex(CallLog.Calls.DATE)
        val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)

        while (cursor.moveToNext()) {
            val phNumber = cursor.getString(number)
            val callType = cursor.getString(type)
            val callDate = cursor.getString(date)
            val callDayTime = Date(Long.valueOf(callDate))
            val formatter = SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss")
            val dateString: String = formatter.format(callDayTime)
            val callDuration = cursor.getString(duration)
            var dir: String = when (callType.toInt()) {
                CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                CallLog.Calls.INCOMING_TYPE -> "Incoming"
                else -> "Missed"
            }
//            Log.e("calls","\nPhone Number : $phNumber\nCall Type : $dir\nCall Date : $dateString\nCall Duration : $callDuration")
            val tempLog = LogInfo(phNumber,dir,dateString,callDuration,null)
            logList.add(0,tempLog)
        }
        cursor.close()
        return logList
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_LOG_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}