package com.mzansi.campushub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CampusEventAdapter(
    private var events: List<CampusEvent>,
    private val onItemClick: (CampusEvent) -> Unit
) : RecyclerView.Adapter<CampusEventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.eventThumbnail)
        val titleTextView: TextView = itemView.findViewById(R.id.tvEventTitle)
        val dateTextView: TextView = itemView.findViewById(R.id.tvEventDate)
        val descTextView: TextView = itemView.findViewById(R.id.tvEventDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_campus_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.titleTextView.text = event.title
        holder.dateTextView.text = event.date
        holder.descTextView.text = event.description

        // Map real images based on the event title
        val imageResId = when {
            event.title.contains("Hackathon", ignoreCase = true) -> R.drawable.event_hackathon
            event.title.contains("Career", ignoreCase = true) -> R.drawable.event_career
            else -> R.drawable.event_party
        }
        holder.thumbnailImageView.setImageResource(imageResId)

        holder.itemView.setOnClickListener {
            onItemClick(event)
        }
    }

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<CampusEvent>) {
        events = newEvents
        notifyDataSetChanged()
    }
}