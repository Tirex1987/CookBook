package ru.netology.cookbook.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.cookbook.adapter.RecipeInteractionListener
import ru.netology.cookbook.data.*
import ru.netology.cookbook.utils.SingleLiveEvent

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener{

    private val repository: RecipeRepository = RecipeRepositoryDb(application)

    val data by repository::data

    val navigateToPreviewContentFragment = SingleLiveEvent<Recipe>()
    val navigateToEditRecipeFragment = SingleLiveEvent<Recipe>()
    val navigateToEditStepFragment = SingleLiveEvent<StepOfRecipe>()
    val navigateToFilterFragment = SingleLiveEvent<Unit>()

    val currentRecipe = MutableLiveData<Recipe?>(null)

    val enabledCategories by repository::enabledCategories
    private var searchString: String = ""

    fun saveRecipe(recipe: Recipe) {
        repository.save(recipe)
    }

    fun onAddClicked() {
        navigateToEditRecipeFragment.value = Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            order = (data.value?.size?.plus(1) ?: 1).toLong(),
            title = "",
            authorName = "",
            category = Category.Russian,
            steps = emptyList()
        )
    }

    fun onFilterClicked() {
        navigateToFilterFragment.call()
    }

    fun getFilteredData(): List<Recipe>? {
        val filteredRecipes = data.value?.filter {
            enabledCategories.isEnabled(it.category)
        }
        if (searchString.isNotBlank()) {
            return filteredRecipes?.filter {
                it.title.contains(searchString, true)
            }
        }
        return filteredRecipes
    }

    fun getSearchString() = searchString

    fun onSearchClicked(searchString: String) {
        this.searchString = searchString.trim()
    }

    fun onApplyFilterClicked() {
        repository.onApplyFilterClicked()
    }

    fun onRemoveStep(step: StepOfRecipe) {
        repository.removeStep(step.id)
    }

    // region RecipeInteractionListener

    override fun onLikeClicked(recipe: Recipe) {
        repository.like(recipe.id)
    }

    override fun onRecipeClicked(recipe: Recipe) {
        navigateToPreviewContentFragment.value = recipe
    }

    override fun onRemoveClicked(recipe: Recipe) {
        repository.delete(recipe)
    }

    override fun onEditClicked(recipe: Recipe) {
        navigateToEditRecipeFragment.value = recipe
    }

    // endregion RecipeInteractionListener
}