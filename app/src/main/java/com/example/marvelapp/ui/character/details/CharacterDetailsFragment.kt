package com.example.marvelapp.ui.character.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.marvelapp.R
import com.example.marvelapp.model.DataEvent
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.model.character.IDetailObject
import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.ui.character.list.CharacterListFragment
import com.example.marvelapp.ui.main.ErrorDialogFragment
import com.example.marvelapp.ui.main.MainActivity
import com.example.marvelapp.utils.viewpager.IViewPagerUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_details_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailsFragment(private val characterId: Int) : Fragment() {

    private val characterDetailsViewModel: CharacterDetailsViewModel by viewModel()
    private val viewPagerUtils: IViewPagerUtils by inject()

    private lateinit var adapter: CharacterDetailsViewPagerAdapter

    private val uiStateObserver = Observer<DataEvent<CharacterDetailsUiState>> { event ->
        event.getContentIfNotHandled()?.let { state ->
            when (state) {
                CharacterDetailsUiState.Loading -> showLoading(true)
                is CharacterDetailsUiState.ShowCharacterDetails -> showCharacterDetails(state.result)
                is CharacterDetailsUiState.ShowError -> showError(state.error)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.character_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterDetailsViewModel.uiState.observe(viewLifecycleOwner, uiStateObserver)

        setUpDetailsFromArgs()

        characterDetailsViewModel.getCharacterDetails(characterId)
    }

    private fun setUpDetailsFromArgs() {
        arguments?.let { args ->
            val imageUrl = args[CharacterListFragment.ARG_CHARACTER_IMAGE_URL].toString()
            val name = args[CharacterListFragment.ARG_CHARACTER_NAME].toString()

            characterName.text = name
            Picasso.get().load(imageUrl)
                .error(R.drawable.ic_launcher_foreground)
                .into(characterImage)
        }
    }

    private fun showCharacterDetails(details: Character) {

        if (details.description.isBlank()) {
            characterDescription.visibility = View.GONE
        } else {
            characterDescription.text = details.description
            characterDescription.visibility = View.VISIBLE
        }

        val comicsPage =
            CharacterDetailsObjectFragment(getString(R.string.comics), getDetailsObjectList(details.comics))
        val seriesPage =
            CharacterDetailsObjectFragment(getString(R.string.series), getDetailsObjectList(details.series))
        val storiesPage =
            CharacterDetailsObjectFragment(getString(R.string.stories), getDetailsObjectList(details.stories))
        val eventsPage =
            CharacterDetailsObjectFragment(getString(R.string.events), getDetailsObjectList(details.events))

        adapter = CharacterDetailsViewPagerAdapter(this).apply {
            addPage(comicsPage)
            addPage(storiesPage)
            addPage(eventsPage)
            addPage(seriesPage)
        }

        pager.adapter = adapter
        pager.registerOnPageChangeCallback(getDynamicHeightListener())

        TabLayoutMediator(tabLayout, pager) { tab, position ->
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
                (activity as MainActivity).replaceFragment(CharacterListFragment())
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
                    viewPagerUtils.updatePagerHeightForChild(it, pager)
                }
            }
        }
    }
}