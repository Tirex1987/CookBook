package ru.netology.cookbook.data

import androidx.lifecycle.MutableLiveData
import ru.netology.cookbook.adapter.EditStepInteractionListener

class RecipeEditor(
    private val recipeLiveData: MutableLiveData<Recipe?>,
    private val onEditClick: (step: StepOfRecipe) -> Unit
) : EditStepInteractionListener {

    override fun onDeleteClicked(step: StepOfRecipe) {
        val recipe = checkNotNull(recipeLiveData.value)
        recipeLiveData.value = recipe.copy(
        steps = recipe.steps
            .also { list ->
                list.find { it == step } ?: return
            }
            .filterNot { it == step }
            .map {
                if (it.order < step.order) it
                else it.copy(order = it.order - 1)
            }
        )
    }

    override fun onEditClicked(step: StepOfRecipe) {
        onEditClick(step)
    }

    fun saveStep(step: StepOfRecipe) {
        val recipe = checkNotNull(recipeLiveData.value)
        recipeLiveData.value = recipe.copy(
            steps = if (step.id == RecipeRepository.NEW_STEP_ID)
                recipe.steps + step.copy(order = getSteps().size + 1)
            else recipe.steps.map {
                if (it.id != step.id) it
                else it.copy(
                    content = step.content,
                    imagePath = step.imagePath
                )
            }
        )
    }

    fun createNewStep() = StepOfRecipe(
        id = RecipeRepository.NEW_STEP_ID,
        recipeId = 0,
        order = 0,
        content = ""
    )

    private fun getSteps() = checkNotNull(recipeLiveData.value).steps
}