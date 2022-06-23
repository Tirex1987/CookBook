package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ru.netology.cookbook.R
import ru.netology.cookbook.adapter.EditStepsAdapter
import ru.netology.cookbook.adapter.RecipesAdapter
import ru.netology.cookbook.data.Category
import ru.netology.cookbook.data.EditStepInteractionListenerImpl
import ru.netology.cookbook.databinding.EditRecipeFragmentBinding
import ru.netology.cookbook.viewModel.RecipeViewModel

class EditRecipeFragment : Fragment() {

    private val receivedRecipe by lazy {
        val args by navArgs<EditRecipeFragmentArgs>()
        args.recipe
    }

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = EditRecipeFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        (requireActivity() as? AppActivity)?.showBottomNav(false)

        val items = Category.values().map {
            it.getText(requireContext())
        }
        val adapterComplete = ArrayAdapter(requireContext(), R.layout.list_item_category, items)
        binding.autoCompleteTextView.setAdapter(adapterComplete)

        if (receivedRecipe.id != 0L) {
            binding.recipeTitle.setText(receivedRecipe.title)
            binding.recipeAuthor.setText(receivedRecipe.authorName)
            binding.autoCompleteTextView.setText(receivedRecipe.category.getText(requireContext()), false)
        }

        //if (receivedRecipe)

        val stepsListener = EditStepInteractionListenerImpl(receivedRecipe.steps)

        val adapter = EditStepsAdapter(stepsListener)
        binding.stepsRecipesRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes.find { it.id == receivedRecipe.id }?.steps)
        }

    }.root

    override fun onDestroy() {
        (requireActivity() as? AppActivity)?.showBottomNav(true)
        super.onDestroy()
    }
}