package ru.netology.cookbook.data

data class StepOfRecipe (
    val id: Long,
    val recipeId: Long,
    val order: Int,
    val content: String,
    val imagePath: String? = null
) : java.io.Serializable