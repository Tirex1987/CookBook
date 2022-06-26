package ru.netology.cookbook.db.recipes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipesEntity)

    @Query("""
        UPDATE recipes SET
        orderNum = :orderNum,
        title = :title,
        authorName = :authorName,
        category = :category,
        preview = :preview
        WHERE id = :id
    """)
    fun updateContentById(
        id: Long,
        orderNum: Long,
        title: String,
        authorName: String,
        category: String,
        preview: String?
    )

    @Query("""
        UPDATE recipes SET
        favorite = CASE WHEN favorite THEN 0 ELSE 1 END
        WHERE id = :id
    """)
    fun favoriteById(id: Long)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeById(id: Long)

    @Query("SELECT MAX(id) as max FROM recipes")
    fun getMaxId(): Long
}