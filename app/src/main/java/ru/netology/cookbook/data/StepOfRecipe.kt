package ru.netology.cookbook.data

data class StepOfRecipe (
    val id: Long,
    val order: Int,
    val content: String,
    val stepImage: String? = null
) : java.io.Serializable