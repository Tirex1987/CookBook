package ru.netology.cookbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.cookbook.data.StepOfRecipe
import ru.netology.cookbook.databinding.CardStepBinding
import ru.netology.cookbook.utils.loadBitmapFromPath
import java.lang.RuntimeException

class StepsAdapter (
    private val interactionListener: EditStepInteractionListener?
) : ListAdapter<StepOfRecipe, StepsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardStepBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val binding: CardStepBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var step: StepOfRecipe

        init {
            binding.deleteStepButton.setOnClickListener{ interactionListener?.onDeleteClicked(step) }
            binding.editStepButton.setOnClickListener { interactionListener?.onEditClicked(step) }
        }

        fun bind(step: StepOfRecipe) {
            this.step = step
            with(binding) {
                stepNumber.text = step.order.toString()
                stepContent.text = step.content
                groupEditStep.isVisible = (interactionListener != null)
                if (!step.imagePath.isNullOrBlank()) {
                    try {
                        stepPhotoView.loadBitmapFromPath(step.imagePath)
                        stepPhotoView.visibility = View.VISIBLE
                    } catch (e: RuntimeException) {
                    }
                } else {
                    stepPhotoView.visibility = View.GONE
                }
            }
        }
    }

    private object DiffCallback: DiffUtil.ItemCallback<StepOfRecipe>() {
        override fun areItemsTheSame(oldItem: StepOfRecipe, newItem: StepOfRecipe): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: StepOfRecipe, newItem: StepOfRecipe): Boolean =
            oldItem == newItem
    }
}