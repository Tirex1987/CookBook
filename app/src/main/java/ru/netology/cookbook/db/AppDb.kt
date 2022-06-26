package ru.netology.cookbook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.cookbook.db.recipes.RecipeDao
import ru.netology.cookbook.db.recipes.RecipesEntity
import ru.netology.cookbook.db.steps.StepDao
import ru.netology.cookbook.db.steps.StepsEntity

@Database(
    entities = [RecipesEntity::class, StepsEntity::class],
    version = 2
)
abstract class AppDb : RoomDatabase() {
    abstract val recipeDao: RecipeDao
    abstract val stepDao: StepDao

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
                .allowMainThreadQueries()
                .build()
    }
}