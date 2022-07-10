package ru.netology.cookbook.data

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.map
import ru.netology.cookbook.db.AppDb
import ru.netology.cookbook.db.recipes.toEntity
import ru.netology.cookbook.db.recipes.toModel
import ru.netology.cookbook.db.steps.toEntity
import ru.netology.cookbook.db.steps.toModel

class RecipeRepositoryDb(
    private val application: Application
): RecipeRepository {

    private val dao = AppDb.getInstance(application).recipeDao
    private val stepDao = AppDb.getInstance(application).stepDao

    override val data = dao.getAll().map { entities ->
        entities
            .map { it.toModel(application) }
            .map { recipe ->
                recipe.copy(
                    steps = stepDao.getByRecipeId(recipe.id).map { it.toModel() }
                )
            }
    }

    override fun save(recipe: Recipe) {
        if (recipe.id == RecipeRepository.NEW_RECIPE_ID) dao.insert(recipe.toEntity(application))
        else dao.updateContentById(
            id = recipe.id,
            orderNum = recipe.order,
            title = recipe.title,
            authorName = recipe.authorName,
            category = recipe.category.getText(application),
            preview =  recipe.preview
        )
        recipe.steps.map {
            if (it.id <= RecipeRepository.NEW_STEP_ID) stepDao.insert(it.copy(
                id = RecipeRepository.NEW_STEP_ID,
                recipeId = if (recipe.id == RecipeRepository.NEW_RECIPE_ID) dao.getMaxId()
                else recipe.id
            ).toEntity())
            else stepDao.updateContentById(
                id = it.id,
                orderNum = it.order,
                content = it.content,
                imagePath = it.imagePath
            )
        }
    }

    override fun like(recipeId: Long) {
        dao.favoriteById(recipeId)
    }

    override fun delete(recipe: Recipe) {
        dao.removeById(recipe.id)
        val recipes = data.value ?: return
        recipes
            .filterNot { it.id == recipe.id }
            .mapIndexed { _index, _recipe ->
            save(_recipe.copy(order = (recipes.size - _index - 1).toLong()))
        }
    }

    override fun removeStep(stepId: Long) {
        stepDao.removeById(stepId)
    }

    override fun onMoveRecipe(fromPosition: Int, toPosition: Int, list: List<Recipe>) {
        if (fromPosition < toPosition) {
            val toOrder = list[toPosition].order
            for (index in toPosition downTo fromPosition + 1) {
                val recipe = list[index]
                save(recipe.copy(order = list[index - 1].order))
            }
            save(list[fromPosition].copy(order = toOrder))
        } else {
            val fromOrder = list[toPosition].order
            for (index in toPosition until fromPosition) {
                val recipe = list[index]
                save(recipe.copy(order = list[index + 1].order))
            }
            save(list[fromPosition].copy(order = fromOrder))
        }
    }

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    override val enabledCategories = getCategoriesPrefs()

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

    override fun onApplyFilterClicked() {
        setCategoriesPrefs(enabledCategories)
    }

    override fun getFilteredData() = data.value?.filter {
        enabledCategories.isEnabled(it.category)
    }

}