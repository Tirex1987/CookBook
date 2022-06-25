package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.cookbook.R
import ru.netology.cookbook.adapter.StepsAdapter
import ru.netology.cookbook.data.Category
import ru.netology.cookbook.data.RecipeEditor
import ru.netology.cookbook.data.StepOfRecipe
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

        viewModel.currentRecipe.value = receivedRecipe

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

        val recipeEditor = RecipeEditor(viewModel.currentRecipe)

        val adapter = StepsAdapter(recipeEditor)
        binding.stepsRecipesRecyclerView.adapter = adapter

        viewModel.currentRecipe.observe(viewLifecycleOwner) { recipe ->
            adapter.submitList(recipe?.steps)
        }

        viewModel.navigateToEditStepFragment.observe(viewLifecycleOwner) { step ->
            val direction = EditRecipeFragmentDirections.actionEditRecipeFragmentToEditStepFragment(step)
            findNavController().navigate(direction)
        }

        binding.addStep.setOnClickListener {
            viewModel.navigateToEditStepFragment.value = StepOfRecipe(
                id = 0,
                order = recipeEditor.steps().size + 1,
                content = ""
            )
        }

    }.root

    override fun onDestroy() {
        (requireActivity() as? AppActivity)?.showBottomNav(true)
        super.onDestroy()
    }
}