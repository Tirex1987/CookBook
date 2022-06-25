package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.cookbook.adapter.RecipesAdapter
import ru.netology.cookbook.databinding.FeedFragmentBinding
import ru.netology.cookbook.viewModel.RecipeViewModel

class FeedFragment : Fragment() {

    //private val viewModel by viewModels<RecipeViewModel>()
    val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipesAdapter(viewModel)

        binding.recipesRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }

        binding.fab.setOnClickListener {
            TODO()
        }

        viewModel.navigateToPreviewContentFragment.observe(viewLifecycleOwner) { recipe ->
            val direction = FeedFragmentDirections.actionFeedFragmentToPreviewRecipeFragment(recipe)
            findNavController().navigate(direction)
        }

        viewModel.navigateToEditRecipeFragment.observe(viewLifecycleOwner) { recipe ->
            val direction = FeedFragmentDirections.actionFeedFragmentToEditRecipeFragment(recipe)
            findNavController().navigate(direction)
        }

        setFragmentResultListener(
            requestKey = EditRecipeFragment.REQUEST_KEY
        ) { requestKey, _ ->
            if (requestKey != EditRecipeFragment.REQUEST_KEY) return@setFragmentResultListener
            viewModel.saveRecipe(viewModel.currentRecipe.value ?: return@setFragmentResultListener)
        }

    }.root
}