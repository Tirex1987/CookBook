package ru.netology.cookbook.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.cookbook.R
import ru.netology.cookbook.databinding.EditStepFragmentBinding
import ru.netology.cookbook.utils.*
import ru.netology.cookbook.viewModel.RecipeViewModel


class EditStepFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val orderPermission = OrderPermission(this)
    private val openImageIntent = OpenImageIntent(this)

    override fun onResume() {
        super.onResume()
        (requireActivity() as? AppActivity)?.showBottomNav(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = EditStepFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        var receivedStep by viewModel::currentStep

        binding.stepContent.setText(receivedStep.content)
        if (!receivedStep.imagePath.isNullOrBlank() && orderPermission.checkPermission()) {
            binding.stepPhotoView.loadBitmapFromPath(receivedStep.imagePath)
        } else {
            binding.stepPhotoView.setImageResource(R.drawable.no_image)
        }

        binding.deletePhotoButton.setOnClickListener {
            if (!receivedStep.imagePath.isNullOrBlank()) {
                receivedStep = receivedStep.copy(imagePath = null)
                binding.stepPhotoView.setImageResource(R.drawable.no_image)
            }
        }

        val selectPhotoLauncher = openImageIntent.registerForActivityResult {
            receivedStep = receivedStep.copy(imagePath = it ?: return@registerForActivityResult)
            val bitmap = BitmapFactory.decodeFile(receivedStep.imagePath)
            binding.stepPhotoView.setImageBitmap(bitmap)
        }

        binding.selectStepPhoto.setOnClickListener {
            orderPermission.requestStoragePermission {
                binding.selectStepPhoto.callOnClick()
            }
            if (!orderPermission.isPermissionStorage()) {
                return@setOnClickListener
            }
            selectPhotoLauncher.launch(Unit)
        }

        binding.saveButton.setOnClickListener {
            val text = binding.stepContent.text.toString()
            if (text.isNotBlank()) {
                receivedStep = receivedStep.copy(content = text)
                setFragmentResult(REQUEST_KEY, Bundle())
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.blankContentStepMessage),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }.root

    companion object {
        const val REQUEST_KEY = "editStepRequestKey"
    }
}