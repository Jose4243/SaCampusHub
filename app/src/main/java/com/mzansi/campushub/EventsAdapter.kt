package com.mzansi.campushub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Simple RecyclerView adapter that renders a list of [CampusEvent] items
 * using the item_event.xml row layout.
 */
class EventsAdapter(
    private var events: List<CampusEvent>
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvEventDate)
        val tvDescription: TextView = itemView.findViewById(R.id.tvEventDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.tvTitle.text = event.title
        holder.tvDate.text = event.date
        holder.tvDescription.text = event.description
    }

    override fun getItemCount(): Int = events.size

    /**
     * Replaces the currently displayed events with a new list and refreshes
     * the RecyclerView.
     */
    fun updateEvents(newEvents: List<CampusEvent>) {
        events = newEvents
        notifyDataSetChanged()
    }
}
