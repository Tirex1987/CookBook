package ru.netology.cookbook.data

import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData

class RecipeRepositoryImpl : RecipeRepository {

    private var lastIdRecipe = 0L
    private var lastIdStep = 0L

    private val recipes
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override val data = MutableLiveData(
        List(100) { index ->
            Recipe(
                id = ++lastIdRecipe,
                order = lastIdRecipe,
                title = "Keks $lastIdRecipe",
                authorName = "Andrey",
                category = Category.Russian,
                steps = MutableList(10) {
                    StepOfRecipe(
                        id = ++lastIdStep,
                        order = it + 1,
                        content = "d\ng\nu\n"
                    )
                }
            )
        }
    )

    override fun like(recipeId: Long) {
        data.value = recipes.map {
            if (it.id != recipeId) it else it.copy(favorite = !it.favorite)
        }
    }

    override fun delete(recipeId: Long) {
        data.value = recipes.filterNot { it.id == recipeId }
    }

    override fun save(recipe: Recipe) {
        verifyStepsId(recipe)
        if (recipe.id == RecipeRepository.NEW_RECIPE_ID) insert(recipe) else update(recipe)
    }

    private fun verifyStepsId(recipe: Recipe) {
        val newRecipe = recipe.copy(
            steps = recipe.steps.map {
                if (it.id != RecipeRepository.NEW_STEP_ID) it
                else it.copy(id = ++lastIdStep)
            }
        )
        update(newRecipe)
    }

    private fun insert(recipe: Recipe) {
        data.value = listOf(
            recipe.copy(id = ++lastIdRecipe)
        ) + recipes
    }

    private fun update(recipe: Recipe) {
        data.value = recipes.map {
            if (it.id != recipe.id) it else recipe
        }
    }

}