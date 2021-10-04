package com.example.marvelapp.ui.character.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapp.R
import kotlinx.android.synthetic.main.character_details_object_fragment.*
import java.util.*

class CharacterDetailsObjectFragment(val tabName: String, private val objectList: List<String>) :
    Fragment() {

    private val adapter = CharacterDetailsObjectAdapter(objectList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.character_details_object_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (objectList.isEmpty()) {
            showEmptyView()
        } else {
            showData()
            setUpAdapter()
        }
    }

    private fun setUpAdapter() {
        characterDetailsObjectRecyclerView.layoutManager = LinearLayoutManager(context)
        characterDetailsObjectRecyclerView.adapter = adapter
    }

    private fun showEmptyView() {
        emptyListView.visibility = View.VISIBLE
        emptyListView.text = String.format(
            getString(R.string.character_details_object_empty),
            tabName.toLowerCase(Locale.ROOT)
        )
        characterDetailsObjectRecyclerView.visibility = View.GONE
    }

    private fun showData() {
        emptyListView.visibility = View.GONE
        characterDetailsObjectRecyclerView.visibility = View.VISIBLE
    }
}