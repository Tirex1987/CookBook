package ru.netology.cookbook.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import ru.netology.cookbook.adapter.StepsAdapter
import ru.netology.cookbook.databinding.PreviewRecipeFragmentBinding
import ru.netology.cookbook.utils.loadBitmapFromPath
import java.lang.RuntimeException


class PreviewRecipeFragment : Fragment() {

    private val receivedRecipe by lazy {
        val args by navArgs<PreviewRecipeFragmentArgs>()
        args.recipe
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PreviewRecipeFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        (requireActivity() as? AppActivity)?.showBottomNav(false)

        binding.title.text = receivedRecipe.title
        binding.authorName.text = receivedRecipe.authorName
        binding.category.text = receivedRecipe.category.getText(requireContext())

        if (!receivedRecipe.preview.isNullOrBlank()) {
            binding.preview.visibility = View.VISIBLE
            binding.preview.loadBitmapFromPath(receivedRecipe.preview)
        }

        val adapter = StepsAdapter(null)

        with(adapter) {
            binding.stepsRecipesRecyclerView.adapter = this
            submitList(receivedRecipe.steps)
        }


    }.root

    override fun onDestroy() {
        (requireActivity() as? AppActivity)?.showBottomNav(true)
        super.onDestroy()
    }
}