package com.example.marvelapp.ui.character.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.marvelapp.R
import com.example.marvelapp.model.DataEvent
import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.ui.character.details.CharacterDetailsFragment
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.ui.character.list.view.CharacterView
import com.example.marvelapp.ui.main.MainActivity
import com.example.marvelapp.utils.constants.Constants
import com.example.marvelapp.utils.strings.IStringUtils
import kotlinx.android.synthetic.main.character_list_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharacterListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val ARG_CHARACTER_NAME = "character_name"
        const val ARG_CHARACTER_IMAGE_URL = "character_image_url"
    }

    private val viewModel: CharacterListViewModel by viewModel()
    private val stringUtils: IStringUtils by inject()

    private lateinit var adapter: CharacterListAdapter

    private var currentPage = 0

    private val uiStateObserver = Observer<DataEvent<CharacterListUiState>> { event ->
        event.getContentIfNotHandled()?.let { state ->
            when (state) {
                CharacterListUiState.Loading -> showLoading(true)
                CharacterListUiState.ShowEmptyList -> showEmptyList()
                is CharacterListUiState.ShowCharacterList -> showCharacterList(state.result)
                is CharacterListUiState.ShowError -> showError(state.error)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.character_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState.observe(viewLifecycleOwner, uiStateObserver)

        setListeners()
        setUpAdapter()

        viewModel.getCharacterList(currentPage)
    }

    override fun onRefresh() {
        swipeToRefreshLayout.isRefreshing = false
        viewModel.getCharacterList(currentPage, pullToRefresh = true)
    }

    private fun setListeners() {
        swipeToRefreshLayout.setOnRefreshListener {
            onRefresh()
        }

        previousPage.setOnClickListener {
            viewModel.getCharacterList(--currentPage)
        }

        nextPage.setOnClickListener {
            viewModel.getCharacterList(++currentPage)
        }

        emptyListRefreshButton.setOnClickListener {
            onRefresh()
        }
    }

    private fun setUpAdapter() {
        val listener = object : CharacterListAdapter.CharacterClickListener {
            override fun onItemClick(view: View, character: CharacterView) {
                character.id?.let {
                    val fragment = CharacterDetailsFragment(it).apply {
                        arguments = Bundle().apply {
                            putString(ARG_CHARACTER_NAME, character.name)
                            putString(
                                ARG_CHARACTER_IMAGE_URL,
                                stringUtils.createFullImageUrl(
                                    character.imageUrl,
                                    character.imageExt,
                                    https = true
                                )
                            )
                        }
                    }
                    (activity as MainActivity).replaceFragment(fragment)
                }
            }
        }

        adapter = CharacterListAdapter(listener = listener)

        characterListRecyclerView.layoutManager =
            GridLayoutManager(context, Constants.CHARACTER_LIST_COLUMNS)
        characterListRecyclerView.adapter = adapter
    }

    private fun hidePageIndicatorViews() {
        pageIndicator.visibility = View.GONE
        nextPage.visibility = View.GONE
        previousPage.visibility = View.GONE
    }

    private fun showLoading(status: Boolean) {
        (activity as MainActivity).showLoading(status)
    }

    private fun showCharacterList(result: CharacterListView) {
        showLoading(false)
        emptyListView.visibility = View.GONE
        emptyListRefreshButton.visibility = View.GONE
        updatePageContainer(result.totalPages)
        swipeToRefreshLayout.isRefreshing = false
        adapter.setCharacterList(result.characterList)
        characterListRecyclerView.visibility = View.VISIBLE
    }

    private fun showEmptyList() {
        showLoading(false)
        showEmptyListView()
        emptyListRefreshButton.visibility = View.GONE
    }

    private fun showError(error: BaseError) {
        showLoading(false)
        showEmptyListView()
        emptyListRefreshButton.visibility = View.VISIBLE
        swipeToRefreshLayout.isRefreshing = false
        (activity as MainActivity).showError(error)
    }

    private fun showEmptyListView() {
        emptyListView.visibility = View.VISIBLE
        swipeToRefreshLayout.isRefreshing = false
        hidePageIndicatorViews()
        characterListRecyclerView.visibility = View.GONE
    }

    private fun updatePageContainer(totalPages: Int?) {
        pageIndicator.visibility = View.VISIBLE
        pageIndicator.text =
            String.format(getString(R.string.page_indicator), currentPage + 1, totalPages)
        when (currentPage + 1) {
            1 -> {
                nextPage.visibility = View.VISIBLE
                previousPage.visibility = View.INVISIBLE
            }
            totalPages -> {
                nextPage.visibility = View.INVISIBLE
                previousPage.visibility = View.VISIBLE
            }
            else -> {
                nextPage.visibility = View.VISIBLE
                previousPage.visibility = View.VISIBLE
            }
        }
    }
}