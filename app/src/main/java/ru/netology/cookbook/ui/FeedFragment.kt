package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import ru.netology.cookbook.adapter.RecipesAdapter
import ru.netology.cookbook.adapter.helper.HandleItemTouchHelperCallback
import ru.netology.cookbook.databinding.FeedFragmentBinding
import ru.netology.cookbook.ui.dialog.PermissionDialogFragment
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
            PermissionDialogFragment {
                orderPermission.requestStoragePermission()
            }.show(requireActivity().supportFragmentManager, "permissions")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipesAdapter(viewModel)

        binding.recipesRecyclerView.adapter = adapter

        val helper = ItemTouchHelper(HandleItemTouchHelperCallback(adapter))
        helper.attachToRecyclerView(binding.recipesRecyclerView)

        viewModel.data.observe(viewLifecycleOwner) {
            submitListAdapter(adapter, binding)
        }

        binding.searchEditText.setText(viewModel.getSearchString())

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

        viewModel.navigateToEditRecipeFragment.observe(viewLifecycleOwner) {
            val direction = FeedFragmentDirections.actionFeedFragmentToEditRecipeFragment()
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
                    submitListAdapter(adapter, binding)
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
            val newRecipe = viewModel.currentRecipe.value ?: return@setFragmentResultListener
            viewModel.data.value?.find {
                it.id == newRecipe.id
            }?.steps?.map { step ->
                newRecipe.steps.find { step.id == it.id } ?: viewModel.onRemoveStep(step)
            }
            viewModel.saveRecipe(newRecipe)
        }

        setFragmentResultListener(
            requestKey = FilterFragment.REQUEST_KEY
        ) { requestKey, _ ->
            if (requestKey != FilterFragment.REQUEST_KEY) return@setFragmentResultListener
            viewModel.onApplyFilterClicked()
            adapter.submitList(viewModel.getFilteredData())
        }

    }.root

    private fun submitListAdapter(adapter: RecipesAdapter, binding: FeedFragmentBinding) {
        val filteredData = viewModel.getFilteredData()
        adapter.submitList(filteredData)
        if (filteredData.isNullOrEmpty()) {
            binding.blankPageImage.visibility = View.VISIBLE
            binding.groupRecycler.visibility = View.GONE
        } else {
            binding.blankPageImage.visibility = View.GONE
            binding.groupRecycler.visibility = View.VISIBLE
        }
    }
}