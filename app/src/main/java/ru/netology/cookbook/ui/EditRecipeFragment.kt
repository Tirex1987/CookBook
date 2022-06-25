package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.cookbook.R
import ru.netology.cookbook.adapter.StepsAdapter
import ru.netology.cookbook.data.*
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

    private lateinit var currentStep: StepOfRecipe


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity() as? AppActivity)?.showBottomNav(false)

        viewModel.currentRecipe.value = receivedRecipe
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = EditRecipeFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val items = Category.values().map {
            it.getText(requireContext())
        }
        val adapterComplete = ArrayAdapter(requireContext(), R.layout.list_item_category, items)
        binding.autoCompleteTextView.setAdapter(adapterComplete)

        val editableRecipe = checkNotNull(viewModel.currentRecipe.value)
        if (editableRecipe.id != RecipeRepository.NEW_RECIPE_ID) {
            binding.recipeTitle.setText(editableRecipe.title)
            binding.recipeAuthor.setText(editableRecipe.authorName)
            binding.autoCompleteTextView.setText(
                editableRecipe.category.getText(requireContext()),
                false
            )
        }

        val recipeEditor = RecipeEditor(viewModel.currentRecipe) {
            currentStep = it
            viewModel.navigateToEditStepFragment.value = currentStep
        }

        val adapter = StepsAdapter(recipeEditor)
        binding.stepsRecipesRecyclerView.adapter = adapter

        viewModel.currentRecipe.observe(viewLifecycleOwner) { recipe ->
            adapter.submitList(recipe?.steps)
        }

        viewModel.navigateToEditStepFragment.observe(viewLifecycleOwner) { step ->
            val direction =
                EditRecipeFragmentDirections.actionEditRecipeFragmentToEditStepFragment(step)
            findNavController().navigate(direction)
        }

        binding.addStep.setOnClickListener {
            currentStep = recipeEditor.createNewStep()
            viewModel.navigateToEditStepFragment.value = currentStep
        }

        binding.fab.setOnClickListener {
            onSaveClicked(binding)
        }

        setFragmentResultListener(
            requestKey = EditStepFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != EditStepFragment.REQUEST_KEY) return@setFragmentResultListener
            val newStepContent = bundle.getString(
                EditStepFragment.RESULT_KEY_CONTENT
            ) ?: return@setFragmentResultListener
            val newStepImage = bundle.getString(
                EditStepFragment.RESULT_KEY_IMAGE_PATH
            )
            val newStep = currentStep.copy(
                content = newStepContent,
                stepImage = newStepImage
            )
            recipeEditor.saveStep(newStep)
        }

    }.root

    override fun onDestroy() {
        (requireActivity() as? AppActivity)?.showBottomNav(true)
        super.onDestroy()
    }

    private fun onSaveClicked(binding: EditRecipeFragmentBinding) {
        var textWarning = ""
        if (binding.recipeTitle.text.isNullOrBlank())
            textWarning = getString(R.string.requiredFieldStart) + getString(R.string.title) + getString(R.string.requiredFieldEnd)
        if (binding.recipeAuthor.text.isNullOrBlank())
            textWarning = textWarning +
                    getString(R.string.requiredFieldStart) + getString(R.string.author) + getString(R.string.requiredFieldEnd)
        val editableRecipe = checkNotNull(viewModel.currentRecipe.value)
        if (editableRecipe.steps.isEmpty()) {
            textWarning = textWarning + getString(R.string.blankStepError)
        }
        if (textWarning.isNotBlank()) {
            Toast.makeText(requireContext(), textWarning, Toast.LENGTH_LONG).show()
            return
        }
        viewModel.currentRecipe.value = editableRecipe.copy(
            title = binding.recipeTitle.text.toString(),
            authorName = binding.recipeAuthor.text.toString(),
            category = Category.values().first {
                it.getText(requireContext()) == binding.autoCompleteTextView.text.toString()
            }
        )
        setFragmentResult(REQUEST_KEY, Bundle())
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "editRecipeRequestKey"
    }
}