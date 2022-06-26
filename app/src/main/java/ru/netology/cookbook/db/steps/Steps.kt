package ru.netology.cookbook.db.steps

import ru.netology.cookbook.data.StepOfRecipe

internal fun StepsEntity.toModel() = StepOfRecipe(
    id = id,
    recipeId = recipeId,
    order = order,
    content = content,
    imagePath = imagePath
)

internal fun StepOfRecipe.toEntity() = StepsEntity(
    id = id,
    recipeId = recipeId,
    order = order,
    content = content,
    imagePath = imagePath
)