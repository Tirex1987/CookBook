package ru.netology.cookbook.db.steps

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StepDao {
    @Query("SELECT * FROM steps WHERE recipeId = :recipeId ORDER BY orderNum ASC")
    fun getByRecipeId(recipeId: Long): List<StepsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(step: StepsEntity)

    @Query("""
        UPDATE steps SET
        orderNum = :orderNum,
        content = :content,
        imagePath = :imagePath
        WHERE id = :id
    """)
    fun updateContentById(
        id: Long,
        orderNum: Int,
        content: String,
        imagePath: String?
    )

    @Query("DELETE FROM steps WHERE id = :id")
    fun removeById(id: Long)
}