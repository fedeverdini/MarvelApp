package com.example.marvelapp.ui.character.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.R
import com.example.marvelapp.ui.character.list.view.CharacterView
import com.example.marvelapp.utils.strings.IStringUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_recyclerview_item.view.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CharacterListAdapter(
    private var characterList: List<CharacterView> = emptyList(),
    private var listener: CharacterClickListener? = null
) : RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    fun setCharacterList(data: List<CharacterView>) {
        this.characterList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characterList[position]
        holder.bind(character, listener)
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), KoinComponent {
        private val stringUtils: IStringUtils by inject()

        private val characterContainer: ConstraintLayout =
            itemView.findViewById(R.id.characterContainer)
        private val characterImage: ImageView = itemView.findViewById(R.id.characterImage)
        private val characterName: TextView = itemView.findViewById(R.id.characterName)

        fun bind(character: CharacterView, listener: CharacterClickListener?) {
            characterContainer.setOnClickListener {
                listener?.onItemClick(it.characterImage, character)
            }

            this.characterName.text = character.name
            val imageUrl = stringUtils.createFullImageUrl(character.imageUrl, character.imageExt, https = true)
            Picasso.get().load(imageUrl)
                .error(R.drawable.ic_launcher_foreground)
                .into(this.characterImage)
        }
    }

    interface CharacterClickListener {
        fun onItemClick(view: View, character: CharacterView)
    }
}