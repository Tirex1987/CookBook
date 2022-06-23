package ru.netology.cookbook.data

import android.content.Context
import ru.netology.cookbook.R

enum class Category{
    European,
    Asian,
    Panasian,
    Eastern,
    American,
    Russian,
    Mediterranean;

    fun getText(context: Context) = when(this) {
            European -> context.getString(R.string.european)
            Asian -> context.getString(R.string.asian)
            Panasian -> context.getString(R.string.panasian)
            Eastern -> context.getString(R.string.eastern)
            American -> context.getString(R.string.american)
            Russian -> context.getString(R.string.russian)
            Mediterranean -> context.getString(R.string.mediterranean)
    }
}