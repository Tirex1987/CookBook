package ru.netology.cookbook.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.cookbook.R
import ru.netology.cookbook.data.RecipeRepository
import ru.netology.cookbook.databinding.EditStepFragmentBinding
import java.lang.RuntimeException

class EditStepFragment : Fragment() {

    private val receivedStep by lazy {
        val args by navArgs<EditStepFragmentArgs>()
        args.step
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = EditStepFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        var imagePath: String? = null

        if (receivedStep.id != RecipeRepository.NEW_STEP_ID) {
            binding.stepContent.setText(receivedStep.content)
            if (!receivedStep.stepImage.isNullOrBlank()) {
                try {
                    binding.stepPhotoView.setImageURI(Uri.parse("file:/" + receivedStep.stepImage))
                } catch (e: RuntimeException) {
                    binding.stepPhotoView.setImageResource(R.drawable.no_image)
                    imagePath = null
                }
            }
        }

        binding.deletePhotoButton.setOnClickListener {
            if (! imagePath.isNullOrBlank()) {
                imagePath = null
                binding.stepPhotoView.setImageResource(R.drawable.no_image)
            }
        }

        binding.saveButton.setOnClickListener {
            val text = binding.stepContent.text.toString()
            if (!text.isBlank()) {
                val resultBundle = Bundle()
                resultBundle.putString(RESULT_KEY_CONTENT, text)
                if (! imagePath.isNullOrBlank()) {
                    resultBundle.putString(RESULT_KEY_IMAGE_PATH, imagePath)
                }
                setFragmentResult(REQUEST_KEY, resultBundle)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), getString(R.string.blankContentStepMessage), Toast.LENGTH_LONG).show()
            }
        }

    }.root

    companion object{
        const val RESULT_KEY_CONTENT = "stepNewContent"
        const val RESULT_KEY_IMAGE_PATH = "stepPhotoPath"
        const val REQUEST_KEY = "editStepRequestKey"
    }
}