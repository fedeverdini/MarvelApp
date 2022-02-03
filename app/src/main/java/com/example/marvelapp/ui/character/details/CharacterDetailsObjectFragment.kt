package com.example.marvelapp.ui.character.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapp.R
import com.example.marvelapp.databinding.CharacterDetailsObjectFragmentBinding
import java.util.*

class CharacterDetailsObjectFragment(val tabName: String, private val objectList: List<String>) :
    Fragment() {

    private var _binding: CharacterDetailsObjectFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter = CharacterDetailsObjectAdapter(objectList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterDetailsObjectFragmentBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.characterDetailsObjectRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.characterDetailsObjectRecyclerView.adapter = adapter
    }

    private fun showEmptyView() {
        binding.emptyListView.visibility = View.VISIBLE
        binding.emptyListView.text = String.format(
            getString(R.string.character_details_object_empty),
            tabName.toLowerCase(Locale.ROOT)
        )
        binding.characterDetailsObjectRecyclerView.visibility = View.GONE
    }

    private fun showData() {
        binding.emptyListView.visibility = View.GONE
        binding.characterDetailsObjectRecyclerView.visibility = View.VISIBLE
    }
}
