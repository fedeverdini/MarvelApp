package com.example.marvelapp.ui.character.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.marvelapp.R
import com.example.marvelapp.databinding.CharacterListFragmentBinding
import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.ui.character.details.CharacterDetailsFragment
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.ui.character.list.view.CharacterView
import com.example.marvelapp.ui.main.MainActivity
import com.example.marvelapp.utils.constants.Constants
import com.example.marvelapp.utils.strings.IStringUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.marvelapp.utils.flow.safeCollect

class CharacterListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val ARG_CHARACTER_ID = "character_id"
        const val ARG_CHARACTER_NAME = "character_name"
        const val ARG_CHARACTER_IMAGE_URL = "character_image_url"
    }

    private var _binding: CharacterListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterListViewModel by viewModel()
    private val stringUtils: IStringUtils by inject()

    private lateinit var adapter: CharacterListAdapter

    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CharacterListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setUpAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.safeCollect { onStateUpdated(it) }
            }
        }

        getData(currentPage, false)
    }

    private fun onStateUpdated(uiState: CharacterListUiState) {
        showLoading(uiState is CharacterListUiState.Loading)

        when (uiState) {
            CharacterListUiState.Loading -> Unit
            CharacterListUiState.ShowEmptyList -> showEmptyList()
            is CharacterListUiState.ShowCharacterList -> showCharacterList(uiState.result)
            is CharacterListUiState.ShowError -> showError(uiState.error)
        }
    }

    private fun getData(page: Int, pullToRefresh: Boolean) {
        viewModel.setPage(page)
        viewModel.setPullToRefresh(pullToRefresh)
        viewModel.forceRefreshState()
    }

    private fun setListeners() {
        binding.swipeToRefreshLayout.setOnRefreshListener {
            onRefresh()
        }

        binding.previousPage.setOnClickListener {
            getData(--currentPage, false)
        }

        binding.nextPage.setOnClickListener {
            getData(++currentPage, false)
        }

        binding.emptyListRefreshButton.setOnClickListener {
            onRefresh()
        }
    }

    override fun onRefresh() {
        binding.swipeToRefreshLayout.isRefreshing = false
        getData(currentPage, true)
    }

    private fun setUpAdapter() {
        val listener = object : CharacterListAdapter.CharacterClickListener {
            override fun onItemClick(view: View, character: CharacterView) {
                character.id?.let {
                    val arguments = Bundle().apply {
                        putInt(ARG_CHARACTER_ID, it)
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
                    findNavController().navigate(R.id.characterDetailsFragment, arguments)
                }
            }
        }

        adapter = CharacterListAdapter(listener = listener)

        binding.characterListRecyclerView.layoutManager =
            GridLayoutManager(context, Constants.CHARACTER_LIST_COLUMNS)
        binding.characterListRecyclerView.adapter = adapter
    }

    private fun hidePageIndicatorViews() {
        binding.pageIndicator.visibility = View.GONE
        binding.nextPage.visibility = View.GONE
        binding.previousPage.visibility = View.GONE
    }

    private fun showLoading(status: Boolean) {
        (activity as MainActivity).showLoading(status)
    }

    private fun showCharacterList(result: CharacterListView) {
        binding.emptyListView.visibility = View.GONE
        binding.emptyListRefreshButton.visibility = View.GONE
        updatePageContainer(result.totalPages)
        binding.swipeToRefreshLayout.isRefreshing = false
        adapter.setCharacterList(result.characterList)
        binding.characterListRecyclerView.visibility = View.VISIBLE
    }

    private fun showEmptyList() {
        showEmptyListView()
        binding.emptyListRefreshButton.visibility = View.GONE
    }

    private fun showError(error: BaseError) {
        showEmptyListView()
        binding.emptyListRefreshButton.visibility = View.VISIBLE
        binding.swipeToRefreshLayout.isRefreshing = false
        (activity as MainActivity).showError(error)
    }

    private fun showEmptyListView() {
        binding.emptyListView.visibility = View.VISIBLE
        binding.swipeToRefreshLayout.isRefreshing = false
        hidePageIndicatorViews()
        binding.characterListRecyclerView.visibility = View.GONE
    }

    private fun updatePageContainer(totalPages: Int?) {
        binding.pageIndicator.visibility = View.VISIBLE
        binding.pageIndicator.text =
            String.format(getString(R.string.page_indicator), currentPage + 1, totalPages)
        when (currentPage + 1) {
            1 -> {
                binding.nextPage.visibility = View.VISIBLE
                binding.previousPage.visibility = View.INVISIBLE
            }
            totalPages -> {
                binding.nextPage.visibility = View.INVISIBLE
                binding.previousPage.visibility = View.VISIBLE
            }
            else -> {
                binding.nextPage.visibility = View.VISIBLE
                binding.previousPage.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
