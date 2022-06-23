package ru.netology.cookbook.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.cookbook.adapter.EditStepInteractionListener
import ru.netology.cookbook.adapter.RecipeInteractionListener
import ru.netology.cookbook.data.Recipe
import ru.netology.cookbook.data.RecipeRepository
import ru.netology.cookbook.data.RecipeRepositoryImpl
import ru.netology.cookbook.data.StepOfRecipe
import ru.netology.cookbook.utils.SingleLiveEvent

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener{

    private val repository: RecipeRepository = RecipeRepositoryImpl()

    val data by repository::data

    val navigateToPreviewContentFragment = SingleLiveEvent<Recipe>()
    val navigateToEditRecipeFragment = SingleLiveEvent<Recipe>()

    private val currentRecipe = MutableLiveData<Recipe?>(null)

    // region RecipeInteractionListener

    override fun onLikeClicked(recipe: Recipe) {
        repository.like(recipe.id)
    }

    override fun onRecipeClicked(recipe: Recipe) {
        navigateToPreviewContentFragment.value = recipe
    }

    override fun onRemoveClicked(recipe: Recipe) {
        repository.delete(recipe.id)
    }

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToEditRecipeFragment.value = recipe
    }

    // endregion RecipeInteractionListener
}