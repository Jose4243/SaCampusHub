package com.mzansi.campushub

import android.content.Intent
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

        val imageResId = when {
            event.title.contains("Hackathon", ignoreCase = true) -> R.drawable.event_hackathon
            event.title.contains("Career", ignoreCase = true) -> R.drawable.event_career
            event.title.contains("Workshop", ignoreCase = true) -> R.drawable.event_workshop
            event.title.contains("Soccer", ignoreCase = true) || event.title.contains("Sports", ignoreCase = true) -> R.drawable.event_sports
            event.title.contains("Art", ignoreCase = true) || event.title.contains("Film", ignoreCase = true) -> R.drawable.event_arts
            else -> R.drawable.event_party
        }
        holder.thumbnailImageView.setImageResource(imageResId)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EventDetailActivity::class.java).apply {
                putExtra(EventDetailActivity.EXTRA_EVENT_TITLE, event.title)
                putExtra(EventDetailActivity.EXTRA_EVENT_DATE, event.date)
                putExtra(EventDetailActivity.EXTRA_EVENT_TIME, event.time)
                putExtra(EventDetailActivity.EXTRA_EVENT_LOCATION, event.location)
                putExtra(EventDetailActivity.EXTRA_EVENT_CATEGORY, event.category)
                putExtra(EventDetailActivity.EXTRA_EVENT_DESCRIPTION, event.description)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<CampusEvent>) {
        events = newEvents
        notifyDataSetChanged()
    }
}