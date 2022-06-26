package ru.netology.cookbook.db.recipes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RecipesEntity::class],
    version = 1
)
abstract class AppDb : RoomDatabase() {
    abstract val recipeDao: RecipeDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context, AppDb::class.java, "app.db"
            )
                .allowMainThreadQueries()  //разрешаем запросы на основном потоке (не следует так делать)
                .build()
    }
}