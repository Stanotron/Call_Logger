package com.example.calllogger

import android.content.Context
import android.provider.CallLog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calllogger.databinding.ActivityMainBinding
import java.lang.Long
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class MainActivityViewModel : ViewModel() {
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    fun refreshList(baseContext: Context, binding: ActivityMainBinding){
        val loglist : MutableList<LogInfo> = getCallLogs(baseContext)
        val recyclerview = binding.recyclerView
        recyclerview.layoutManager = LinearLayoutManager(baseContext)
        val adapter = CallLogAdapter(loglist)
        recyclerview.adapter = adapter
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)
        "Last Sync : $current".also { binding.syncTv.text = it }
    }


    fun getCallLogs(baseContext: Context) : MutableList<LogInfo> {
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


}
