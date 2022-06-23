package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.cookbook.adapter.RecipesAdapter
import ru.netology.cookbook.databinding.FavoriteFragmentBinding
import ru.netology.cookbook.viewModel.RecipeViewModel

class FavoriteFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FavoriteFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipesAdapter(viewModel)

        binding.recipesRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes.filter { it.favorite })
        }

        viewModel.navigateToPreviewContentFragment.observe(viewLifecycleOwner) { recipe ->
            val direction = FavoriteFragmentDirections.actionFavoriteFragmentToPreviewRecipeFragment(recipe)
            findNavController().navigate(direction)
        }

        viewModel.navigateToEditRecipeFragment.observe(viewLifecycleOwner) { recipe ->
            val direction =
                FavoriteFragmentDirections.actionFavoriteFragmentToEditRecipeFragment(recipe)
            findNavController().navigate(direction)
        }

    }.root
}