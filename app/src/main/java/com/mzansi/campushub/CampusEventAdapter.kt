package com.mzansi.campushub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView adapter that binds a list of [CampusEvent] objects to rows
 * rendered from item_campus_event.xml on the main dashboard feed.
 *
 * @param events the events currently displayed
 * @param onItemClick invoked with the tapped [CampusEvent] whenever a row
 *   is clicked - callers (e.g. [MainActivity]) use this to navigate to
 *   [EventDetailActivity].
 */
class CampusEventAdapter(
    private var events: List<CampusEvent>,
    private val onItemClick: (CampusEvent) -> Unit
) : RecyclerView.Adapter<CampusEventAdapter.CampusEventViewHolder>() {

    /**
     * Holds references to the views within a single item_campus_event.xml row.
     */
    class CampusEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEventTitle: TextView = itemView.findViewById<TextView>(R.id.tvEventTitle)
        val tvEventDate: TextView = itemView.findViewById<TextView>(R.id.tvEventDate)
        val tvEventDescription: TextView = itemView.findViewById<TextView>(R.id.tvEventDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampusEventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_campus_event, parent, false)
        return CampusEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: CampusEventViewHolder, position: Int) {
        val event = events[position]
        holder.tvEventTitle.text = event.title
        holder.tvEventDate.text = event.date
        holder.tvEventDescription.text = event.description

        holder.itemView.setOnClickListener {
            onItemClick(event)
        }
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
