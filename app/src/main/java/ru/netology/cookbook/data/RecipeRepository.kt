package ru.netology.cookbook.data

import androidx.lifecycle.LiveData

interface RecipeRepository {
    val data: LiveData<List<Recipe>>

    fun like(recipeId: Long)
    fun delete(recipeId: Long)
    fun save(recipeId: Long)

    companion object {
        const val NEW_RECIPE_ID = 0
        const val NEW_STEP_ID = 0
    }
}