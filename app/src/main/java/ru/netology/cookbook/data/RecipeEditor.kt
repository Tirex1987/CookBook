package ru.netology.cookbook.data

import androidx.lifecycle.MutableLiveData
import ru.netology.cookbook.adapter.EditStepInteractionListener

class RecipeEditor(
    private val recipeLiveData: MutableLiveData<Recipe?>
) : EditStepInteractionListener {

    override fun onDeleteClicked(step: StepOfRecipe) {
        val recipe = checkNotNull(recipeLiveData.value)
        recipeLiveData.value = recipe.copy(
        steps = recipe.steps
            .filterNot { it.id == step.id }
            .map {
                if (it.order < step.order) it
                else it.copy(order = it.order - 1)
            }
        )
    }

    override fun onEditClicked(step: StepOfRecipe) {
        TODO("Not yet implemented")
    }

    fun steps() = checkNotNull(recipeLiveData.value).steps
}