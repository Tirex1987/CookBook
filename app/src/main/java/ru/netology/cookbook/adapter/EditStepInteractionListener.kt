package ru.netology.cookbook.adapter

import ru.netology.cookbook.data.StepOfRecipe

interface EditStepInteractionListener {

    fun onDeleteClicked(step: StepOfRecipe)
    fun onEditClicked(step: StepOfRecipe)
    fun onMove(fromPosition: Int, toPosition: Int, list: List<StepOfRecipe>)
}