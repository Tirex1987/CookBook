package ru.netology.cookbook.data

class EnabledCategories {

    private val listCategories = List(Category.values().size) {
        EnabledCategory(Category.values().get(it))
    }

    fun setEnabled(category: Category, enabled: Boolean) {
        listCategories
            .first { it.category == category }
            .enabled = enabled
    }

    fun isEnabled(category: Category) = listCategories
        .first { it.category == category }.enabled


    class EnabledCategory(val category: Category) {
        var enabled: Boolean = true
    }
}