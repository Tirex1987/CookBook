package ru.netology.cookbook.data

data class Recipe(
    val id: Long,
    val order: Long,
    val title: String,
    val authorName: String,
    val category: Category,
    val favorite: Boolean = false,
    val preview: String? = null,
    val steps: List<StepOfRecipe>
) : java.io.Serializable