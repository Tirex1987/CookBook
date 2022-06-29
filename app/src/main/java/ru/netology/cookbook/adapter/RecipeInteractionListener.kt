package ru.netology.cookbook.adapter

import ru.netology.cookbook.data.Recipe

interface RecipeInteractionListener {
    fun onLikeClicked(recipe: Recipe)
    fun onRecipeClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onMove(fromPosition: Int, toPosition: Int)
}