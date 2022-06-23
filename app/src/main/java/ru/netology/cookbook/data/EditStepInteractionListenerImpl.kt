package ru.netology.cookbook.data

import ru.netology.cookbook.adapter.EditStepInteractionListener

class EditStepInteractionListenerImpl(
    private var steps: List<StepOfRecipe>
) : EditStepInteractionListener {

    override fun onDeleteClicked(step: StepOfRecipe) {
        steps
            .filterNot { it.id == step.id }
            .map {
                if (it.order < step.order) it
                else it.copy(order = it.order - 1)
            }
    }

    override fun onEditClicked(step: StepOfRecipe) {
        TODO("Not yet implemented")
    }

    fun steps() = steps
}