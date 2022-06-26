package ru.netology.cookbook.data

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlin.properties.Delegates

class RecipeRepositoryImpl(
    application: Application
): RecipeRepository {

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private fun getCategoriesPrefs(): EnabledCategories {
        val enabledCategories = EnabledCategories()
        Category.values().map {
            enabledCategories.setEnabled(it, prefs.getBoolean(it.toString(), true))
        }
        return enabledCategories
    }

    private fun setCategoriesPrefs(enabledCategories: EnabledCategories) {
        Category.values().map {
            prefs.edit {putBoolean(it.toString(), enabledCategories.isEnabled(it))}
        }
    }

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
                        recipeId = lastIdRecipe,
                        order = it + 1,
                        content = "d\ng\nu\n"
                    )
                }
            )
        }
    )

    override val enabledCategories = getCategoriesPrefs()

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

    override fun removeStep(stepId: Long) {
        //("Not yet implemented")
    }

    private fun verifyStepsId(recipe: Recipe) {
        val newRecipe = recipe.copy(
            steps = recipe.steps.map {
                if (it.id != RecipeRepository.NEW_STEP_ID) it
                else it.copy(
                    id = ++lastIdStep,
                    recipeId = recipe.id
                )
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

    override fun onApplyFilterClicked() {
        setCategoriesPrefs(enabledCategories)
    }

}