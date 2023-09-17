package com.example.calllogger

data class LogInfo(
    val number : String,
    val callType : String,
    val callDate : String,
    val duration : String,
    val recordingName : String?
)
