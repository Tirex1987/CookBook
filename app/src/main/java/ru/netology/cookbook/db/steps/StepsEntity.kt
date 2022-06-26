package ru.netology.cookbook.db.steps

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
class StepsEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "recipeId")
    val recipeId: Long,
    @ColumnInfo(name = "orderNum")
    val order: Int,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "imagePath", defaultValue = "NULL")
    @Nullable
    val imagePath: String? = null
)
