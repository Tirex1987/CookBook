package ru.netology.cookbook.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.cookbook.data.StepOfRecipe
import ru.netology.cookbook.databinding.CardStepBinding
import java.lang.RuntimeException

class StepsAdapter : RecyclerView.Adapter<StepsAdapter.ViewHolder>() {
    var list = emptyList<StepOfRecipe>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardStepBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class ViewHolder(private val binding: CardStepBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var step: StepOfRecipe

        fun bind(step: StepOfRecipe) {
            this.step = step
            with(binding) {
                stepContent.text = step.content
                stepNumber.text = step.order.toString()
                if (!step.stepImage.isNullOrBlank()) {
                    try {
                        stepPhotoView.setImageURI(Uri.parse("file:/" + step.stepImage))
                        stepPhotoView.visibility = View.VISIBLE
                    } catch (e: RuntimeException) {
                    }
                } else {
                    stepPhotoView.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}