package ru.netology.cookbook.adapter

import ru.netology.cookbook.data.StepOfRecipe

interface EditStepInteractionListener {

    fun onDeleteClicked(step: StepOfRecipe)
    fun onEditClicked(step: StepOfRecipe)
}