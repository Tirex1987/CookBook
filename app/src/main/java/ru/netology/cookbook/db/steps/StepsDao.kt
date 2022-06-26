package ru.netology.cookbook.db.steps

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StepsDao {
    @Query("SELECT * FROM steps ORDER BY id DESC")
    fun getAll(): List<StepsEntity>

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
        orderNum: Long,
        content: String,
        imagePath: String?
    )

    @Query("DELETE FROM steps WHERE id = :id")
    fun removeById(id: Long)
}