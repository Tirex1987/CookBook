package ru.netology.cookbook.data

import androidx.lifecycle.MutableLiveData
import ru.netology.cookbook.adapter.EditStepInteractionListener

class RecipeEditor(
    private val recipeLiveData: MutableLiveData<Recipe?>,
    private val onEditClick: (step: StepOfRecipe) -> Unit
) : EditStepInteractionListener {

    override fun onDeleteClicked(step: StepOfRecipe) {
        recipeLiveData.value = getRecipe().copy(
            steps = getRecipe().steps
                .also { list ->
                    list.find { it.id == step.id } ?: return
                }
                .filterNot { it.id == step.id }
                .map {
                    if (it.order < step.order) it
                    else it.copy(order = it.order - 1)
                }
        )
    }

    override fun onEditClicked(step: StepOfRecipe) {
        onEditClick(step)
    }

    override fun onMove(fromPosition: Int, toPosition: Int, list: List<StepOfRecipe>) {
        if (fromPosition < toPosition) {
            val toOrder = list[toPosition].order
            for (index in toPosition downTo fromPosition + 1) {
                val step = list[index]
                changeStepOrder(step, list[index - 1].order)
            }
            changeStepOrder(list[fromPosition], toOrder)
        } else {
            val fromOrder = list[toPosition].order
            for (index in toPosition until fromPosition) {
                val step = list[index]
                changeStepOrder(step, list[index + 1].order)
            }
            changeStepOrder(list[fromPosition], fromOrder)
        }
    }

    private fun changeStepOrder(step: StepOfRecipe, newOrder: Int) {
        recipeLiveData.value = getRecipe().copy(
            steps = getRecipe().steps
                .map {
                    if (it.id != step.id) it
                    else step.copy(order = newOrder)
                }.sortedBy {
                    it.order
                }
        )
    }

    fun saveStep(step: StepOfRecipe) {
        recipeLiveData.value = getRecipe().copy(
            steps = if (step.id == RecipeRepository.NEW_STEP_ID) {
                getRecipe().steps + step.copy(id = getNextStepId())
            } else getRecipe().steps.map {
                if (it.id != step.id) it
                else it.copy(
                    content = step.content,
                    imagePath = step.imagePath
                )
            }
        )
    }

    fun createNewStep(): StepOfRecipe {
        return StepOfRecipe(
            id = RecipeRepository.NEW_STEP_ID,
            recipeId = getRecipe().id,
            order = getRecipe().steps.size.plus(1),
            content = ""
        )
    }

    private fun getRecipe() = checkNotNull(recipeLiveData.value)

    private fun getNextStepId(): Long {
        if (getRecipe().steps.isEmpty()) return RecipeRepository.NEW_STEP_ID - 1
        val minId = getRecipe().steps.minOf { it.id }
        return if (minId > RecipeRepository.NEW_STEP_ID) RecipeRepository.NEW_STEP_ID - 1
        else minId - 1
    }
}