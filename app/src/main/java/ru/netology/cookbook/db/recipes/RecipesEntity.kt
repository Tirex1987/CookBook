package ru.netology.cookbook.db.recipes

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.cookbook.data.StepOfRecipe

@Entity(tableName = "recipes")
class RecipesEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "orderNum")
    val order: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "authorName")
    val authorName: String,

    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "favorite", defaultValue = "0")
    val favorite: Boolean = false,
    @ColumnInfo(name = "preview", defaultValue = "NULL")
    @Nullable
    val preview: String? = null
)