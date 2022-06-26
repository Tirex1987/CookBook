package ru.netology.cookbook.db.recipes

import android.content.Context
import ru.netology.cookbook.data.Category
import ru.netology.cookbook.data.Recipe

internal fun RecipesEntity.toModel(context: Context) = Recipe(
    id = id,
    order = order,
    title = title,
    authorName = authorName,
    category = Category.values().first { it.getText(context) == category },
    favorite = favorite,
    preview = preview,
    steps = emptyList()
)

internal fun Recipe.toEntity(context: Context) = RecipesEntity(
    id = id,
    order = order,
    title = title,
    authorName = authorName,
    category = category.getText(context),
    favorite = favorite,
    preview = preview
)