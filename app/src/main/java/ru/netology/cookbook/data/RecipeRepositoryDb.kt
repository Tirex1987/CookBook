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
            .map {
                it.copy(
                    steps = stepDao.getByRecipeId(it.id).map { it.toModel() }
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
            if (it.id == RecipeRepository.NEW_STEP_ID) stepDao.insert(it.copy(
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

    override fun delete(recipeId: Long) {
        dao.removeById(recipeId)
    }

    override fun removeStep(stepId: Long) {
        stepDao.removeById(stepId)
    }


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

    override val enabledCategories = getCategoriesPrefs()

    override fun onApplyFilterClicked() {
        setCategoriesPrefs(enabledCategories)
    }

}