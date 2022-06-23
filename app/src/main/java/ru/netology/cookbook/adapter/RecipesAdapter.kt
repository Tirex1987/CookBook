package ru.netology.cookbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.cookbook.R
import ru.netology.cookbook.data.Recipe
import ru.netology.cookbook.databinding.CardRecipeBinding

class RecipesAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val binding: CardRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var recipe: Recipe

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.remove -> {
                            interactionListener.onRemoveClicked(recipe)
                            true
                        }
                        R.id.edit -> {
                            interactionListener.onEditClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.like.setOnClickListener{ interactionListener.onLikeClicked(recipe) }
            binding.constraintLayoutRecipe.setOnClickListener { interactionListener.onRecipeClicked(recipe) }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
                title.text = recipe.title
                authorName.text = recipe.authorName
                category.text = recipe.category.getText(binding.root.context)
                like.isChecked = recipe.favorite
                options.setOnClickListener { popupMenu.show() }
            }
        }
    }

    private object DiffCallback: DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem == newItem
    }
}