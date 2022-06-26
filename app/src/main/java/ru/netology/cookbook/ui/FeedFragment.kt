package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.cookbook.adapter.RecipesAdapter
import ru.netology.cookbook.databinding.FeedFragmentBinding
import ru.netology.cookbook.utils.OrderPermission
import ru.netology.cookbook.utils.hideKeyboard
import ru.netology.cookbook.viewModel.RecipeViewModel

class FeedFragment : Fragment() {

    val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val orderPermission = OrderPermission(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!orderPermission.checkPermission()) {
            orderPermission.requestStoragePermission()
            //Toast.makeText(requireContext(), getString(R.string.imagesLoadError), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipesAdapter(viewModel)

        binding.recipesRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(viewModel.getFilteredData())
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        binding.filterButton.setOnClickListener {
            viewModel.onFilterClicked()
        }

        viewModel.navigateToPreviewContentFragment.observe(viewLifecycleOwner) { recipe ->
            val direction = FeedFragmentDirections.actionFeedFragmentToPreviewRecipeFragment(recipe)
            findNavController().navigate(direction)
        }

        viewModel.navigateToEditRecipeFragment.observe(viewLifecycleOwner) { recipe ->
            val direction = FeedFragmentDirections.actionFeedFragmentToEditRecipeFragment(recipe)
            findNavController().navigate(direction)
        }

        viewModel.navigateToFilterFragment.observe(viewLifecycleOwner) {
            val direction = FeedFragmentDirections.actionFeedFragmentToFilterFragment()
            findNavController().navigate(direction)
        }

        binding.searchEditText.setOnKeyListener(object: View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    viewModel.onSearchClicked(binding.searchEditText.text.toString())
                    adapter.submitList(viewModel.getFilteredData())
                    binding.searchEditText.clearFocus()
                    binding.searchEditText.hideKeyboard()
                    return true
                }
                return false
            }
        })

        setFragmentResultListener(
            requestKey = EditRecipeFragment.REQUEST_KEY
        ) { requestKey, _ ->
            if (requestKey != EditRecipeFragment.REQUEST_KEY) return@setFragmentResultListener
            viewModel.saveRecipe(viewModel.currentRecipe.value ?: return@setFragmentResultListener)
        }

        setFragmentResultListener(
            requestKey = FilterFragment.REQUEST_KEY
        ) { requestKey, _ ->
            if (requestKey != FilterFragment.REQUEST_KEY) return@setFragmentResultListener
            viewModel.onApplyFilterClicked()
            adapter.submitList(viewModel.getFilteredData())
        }

    }.root
}