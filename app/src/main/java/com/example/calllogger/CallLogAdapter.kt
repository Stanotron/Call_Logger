package com.example.calllogger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CallLogAdapter(private val logs: MutableList<LogInfo>) : RecyclerView.Adapter<CallLogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.call_info_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = logs[position]
        holder.phoneTv.text = itemsViewModel.number
        holder.callState.text = itemsViewModel.callType
        "Date : ${itemsViewModel.callDate} | Dur : ${itemsViewModel.duration} s".also { holder.dateDur.text = it }
        "From : +91-8846399993".also { holder.fromTv.text = it }
        holder.recordingInfo.text = itemsViewModel.recordingName?:"Not Available"
    }

    override fun getItemCount(): Int {
        return logs.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val phoneTv: TextView = this.itemView.findViewById(R.id.phone_no)
        val callState : TextView = this.itemView.findViewById(R.id.state_button)
        val dateDur: TextView = this.itemView.findViewById(R.id.date_duration_info)
        val fromTv: TextView = this.itemView.findViewById(R.id.from_info)
        val recordingInfo : TextView = this.itemView.findViewById(R.id.recording_name)
    }
}
