package ru.netology.cookbook.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RecipeRepositoryImpl : RecipeRepository {

    private var lastId = 0L

    private val recipes
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override val data = MutableLiveData(
        List(100) { index ->
            Recipe(
                id = ++lastId,
                order = lastId,
                title = "Keks $lastId",
                authorName = "Andrey",
                category = Category.Russian,
                steps = MutableList(10) {
                    StepOfRecipe((it + 1).toLong(), it + 1, "d\ng\nu\n")
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

    override fun save(recipeId: Long) {
        TODO("Not yet implemented")
    }

}