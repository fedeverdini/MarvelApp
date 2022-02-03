package com.example.marvelapp.ui.character.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.marvelapp.R
import com.example.marvelapp.databinding.CharacterDetailsFragmentBinding
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.model.character.IDetailObject
import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.ui.character.list.CharacterListFragment
import com.example.marvelapp.ui.main.ErrorDialogFragment
import com.example.marvelapp.ui.main.MainActivity
import com.example.marvelapp.utils.flow.safeCollect
import com.example.marvelapp.utils.viewpager.IViewPagerUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailsFragment : Fragment() {

    private var _binding: CharacterDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val characterDetailsViewModel: CharacterDetailsViewModel by viewModel()
    private val viewPagerUtils: IViewPagerUtils by inject()

    private lateinit var adapter: CharacterDetailsViewPagerAdapter

    private var characterId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CharacterDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDetailsFromArgs()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                characterDetailsViewModel.uiState.safeCollect { onStateUpdated(it) }
            }
        }

        characterDetailsViewModel.setCharacterId(characterId)
        characterDetailsViewModel.forceRefreshState()
    }

    private fun onStateUpdated(uiState: CharacterDetailsUiState) {
        showLoading(uiState is CharacterDetailsUiState.Loading)

        when (uiState) {
            CharacterDetailsUiState.Loading -> Unit
            is CharacterDetailsUiState.ShowCharacterDetails -> showCharacterDetails(uiState.result)
            is CharacterDetailsUiState.ShowError -> showError(uiState.error)
        }
    }

    private fun setUpDetailsFromArgs() {
        arguments?.let { args ->
            val id = args[CharacterListFragment.ARG_CHARACTER_ID].toString().toInt()
            val imageUrl = args[CharacterListFragment.ARG_CHARACTER_IMAGE_URL].toString()
            val name = args[CharacterListFragment.ARG_CHARACTER_NAME].toString()
            this.characterId = id

            binding.characterName.text = name
            Picasso.get().load(imageUrl)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.characterImage)
        }
    }

    private fun showCharacterDetails(details: Character) {

        if (details.description.isBlank()) {
            binding.characterDescription.visibility = View.GONE
        } else {
            binding.characterDescription.text = details.description
            binding.characterDescription.visibility = View.VISIBLE
        }

        val comicsPage =
            CharacterDetailsObjectFragment(
                getString(R.string.comics),
                getDetailsObjectList(details.comics)
            )
        val seriesPage =
            CharacterDetailsObjectFragment(
                getString(R.string.series),
                getDetailsObjectList(details.series)
            )
        val storiesPage =
            CharacterDetailsObjectFragment(
                getString(R.string.stories),
                getDetailsObjectList(details.stories)
            )
        val eventsPage =
            CharacterDetailsObjectFragment(
                getString(R.string.events),
                getDetailsObjectList(details.events)
            )

        adapter = CharacterDetailsViewPagerAdapter(this).apply {
            addPage(comicsPage)
            addPage(storiesPage)
            addPage(eventsPage)
            addPage(seriesPage)
        }

        binding.pager.adapter = adapter
        binding.pager.registerOnPageChangeCallback(getDynamicHeightListener())

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = adapter.getTabName(position)
        }.attach()

        showLoading(false)
    }

    private fun getDetailsObjectList(details: IDetailObject): List<String> {
        return if (details.returned > 0) {
            details.items.map { item -> item.name }
        } else {
            emptyList()
        }
    }

    private fun showLoading(status: Boolean) {
        (activity as MainActivity).showLoading(status)
    }

    private fun showError(error: BaseError) {
        showLoading(false)
        val listener = object : ErrorDialogFragment.ErrorListener {
            override fun onButtonClick() {
                findNavController().navigate(R.id.characterListFragment)
            }
        }
        (activity as MainActivity).showError(error, listener)
    }

    private fun getDynamicHeightListener(): ViewPager2.OnPageChangeCallback {
        return object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val fragment = adapter.getFragment(position)
                fragment.view?.let {
                    viewPagerUtils.updatePagerHeightForChild(it, binding.pager)
                }
            }
        }
    }
}
