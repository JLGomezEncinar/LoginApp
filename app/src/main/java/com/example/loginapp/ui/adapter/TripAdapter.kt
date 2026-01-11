package com.example.loginapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.data.model.Trip

class TripAdapter(private var trips: List<Trip>) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tv_trip_name)
        val descriptionTextView: TextView = view.findViewById(R.id.tv_trip_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]
        holder.nameTextView.text = trip.nombre
        holder.descriptionTextView.text = trip.descripcion
    }

    override fun getItemCount(): Int = trips.size

    fun updateTrips(newTrips: List<Trip>) {
        trips = newTrips
        notifyDataSetChanged()
    }
}
