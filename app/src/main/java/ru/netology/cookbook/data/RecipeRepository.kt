package ru.netology.cookbook.data

import androidx.lifecycle.LiveData

interface RecipeRepository {
    val data: LiveData<List<Recipe>>
    val enabledCategories: EnabledCategories

    fun like(recipeId: Long)
    fun delete(recipeId: Long)
    fun save(recipe: Recipe)
    fun removeStep(stepId: Long)
    fun onApplyFilterClicked()

    companion object {
        const val NEW_RECIPE_ID = 0L
        const val NEW_STEP_ID = 0L
    }
}