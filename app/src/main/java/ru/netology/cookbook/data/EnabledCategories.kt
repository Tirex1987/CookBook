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

    fun getAvailableCategories() : List<Category> {
        val list = emptyList<Category>().toMutableList()
        listCategories.map {
            if (it.enabled) list.add(it.category)
        }
        return list.toList()
    }

    class EnabledCategory(val category: Category) {
        var enabled: Boolean = true
    }
}