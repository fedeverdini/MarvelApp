package com.example.marvelapp.ui.character.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.R
import com.example.marvelapp.databinding.CharacterDetailsObjectItemBinding

class CharacterDetailsObjectAdapter(private var list: List<String> = emptyList()) :
    RecyclerView.Adapter<CharacterDetailsObjectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_details_object_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = list[position]
        holder.bind(name)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CharacterDetailsObjectItemBinding.bind(itemView)
        private val detailsObjectName: TextView = binding.detailsObjectName

        fun bind(detailsObjectName: String) {
            this.detailsObjectName.text = detailsObjectName
        }
    }
}
