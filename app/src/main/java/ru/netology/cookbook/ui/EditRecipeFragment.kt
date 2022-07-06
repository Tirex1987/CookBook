package ru.netology.cookbook.ui

import android.graphics.BitmapFactory
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
import androidx.recyclerview.widget.ItemTouchHelper
import ru.netology.cookbook.R
import ru.netology.cookbook.adapter.StepsAdapter
import ru.netology.cookbook.adapter.helper.HandleItemTouchHelperCallback
import ru.netology.cookbook.data.*
import ru.netology.cookbook.databinding.EditRecipeFragmentBinding
import ru.netology.cookbook.utils.*
import ru.netology.cookbook.viewModel.RecipeViewModel

class EditRecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val orderPermission = OrderPermission(this)
    private val openImageIntent = OpenImageIntent(this)

    override fun onResume() {
        super.onResume()
        (requireActivity() as? AppActivity)?.showBottomNav(false)
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
        binding.recipeTitle.setText(editableRecipe.title)
        binding.recipeAuthor.setText(editableRecipe.authorName)
        if (editableRecipe.id != RecipeRepository.NEW_RECIPE_ID) {
            binding.autoCompleteTextView.setText(
                editableRecipe.category.getText(requireContext()),
                false
            )
        }
        if (!editableRecipe.preview.isNullOrBlank() && orderPermission.checkPermission()) {
            binding.photoPreview.loadBitmapFromPath(editableRecipe.preview)
        }

        val recipeEditor = RecipeEditor(viewModel.currentRecipe) {
            viewModel.currentStep = it
            viewModel.navigateToEditStepFragment.call()
        }

        val adapter = StepsAdapter(recipeEditor)
        binding.stepsRecipesRecyclerView.adapter = adapter

        val helper = ItemTouchHelper(HandleItemTouchHelperCallback(adapter))
        helper.attachToRecyclerView(binding.stepsRecipesRecyclerView)

        viewModel.currentRecipe.observe(viewLifecycleOwner) { recipe ->
            adapter.submitList(recipe?.steps)
        }

        viewModel.navigateToEditStepFragment.observe(viewLifecycleOwner) {
            val direction =
                EditRecipeFragmentDirections.actionEditRecipeFragmentToEditStepFragment()
            findNavController().navigate(direction)
        }

        binding.deletePhotoButton.setOnClickListener {
            val editedRecipe = checkNotNull(viewModel.currentRecipe.value)
            if (!editedRecipe.preview.isNullOrBlank()) {
                viewModel.currentRecipe.value = editedRecipe.copy(
                    preview = null
                )
                binding.photoPreview.setImageResource(R.drawable.no_image)
            }
        }

        binding.addStep.setOnClickListener {
            viewModel.currentStep = recipeEditor.createNewStep()
            viewModel.navigateToEditStepFragment.call()
        }

        binding.fab.setOnClickListener {
            onSaveClicked(binding)
        }

        val selectPhotoLauncher = openImageIntent.registerForActivityResult {
            val imagePath = it ?: return@registerForActivityResult
            val editedRecipe = checkNotNull(viewModel.currentRecipe.value)
            viewModel.currentRecipe.value = editedRecipe.copy(
                preview = imagePath
            )
            val bitmap = BitmapFactory.decodeFile(imagePath)
            binding.photoPreview.setImageBitmap(bitmap)
        }

        binding.selectPhoto.setOnClickListener {
            orderPermission.requestStoragePermission {
                binding.selectPhoto.callOnClick()
            }
            if (!orderPermission.isPermissionStorage()) {
                return@setOnClickListener
            }
            selectPhotoLauncher.launch(Unit)
        }

        setFragmentResultListener(
            requestKey = EditStepFragment.REQUEST_KEY
        ) { requestKey, _ ->
            if (requestKey != EditStepFragment.REQUEST_KEY) return@setFragmentResultListener
            recipeEditor.saveStep(viewModel.currentStep)
        }

    }.root

    override fun onDestroy() {
        (requireActivity() as? AppActivity)?.showBottomNav(true)
        super.onDestroy()
    }

    private fun onSaveClicked(binding: EditRecipeFragmentBinding) {
        var textWarning = ""
        if (binding.recipeTitle.text.isNullOrBlank())
            textWarning =
                getString(R.string.requiredFieldStart) + getString(R.string.title) + getString(R.string.requiredFieldEnd)
        if (binding.recipeAuthor.text.isNullOrBlank())
            textWarning +=
                getString(R.string.requiredFieldStart) + getString(R.string.author) + getString(
                    R.string.requiredFieldEnd
                )
        if (binding.autoCompleteTextView.text.isNullOrBlank()) {
            textWarning +=
                getString(R.string.requiredFieldStart) + getString(R.string.category) + getString(
                    R.string.requiredFieldEnd
                )
        }
        val editableRecipe = checkNotNull(viewModel.currentRecipe.value)
        if (editableRecipe.steps.isEmpty()) {
            textWarning += getString(R.string.blankStepError)
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