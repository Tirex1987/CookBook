package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.cookbook.data.Category
import ru.netology.cookbook.databinding.FilterFragmentBinding
import ru.netology.cookbook.viewModel.RecipeViewModel

class FilterFragment: Fragment() {

    val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity() as? AppActivity)?.showBottomNav(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FilterFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        binding.filterAmerican.isChecked = viewModel.enabledCategories.isEnabled(Category.American)
        binding.filterAsian.isChecked = viewModel.enabledCategories.isEnabled(Category.Asian)
        binding.filterEastern.isChecked = viewModel.enabledCategories.isEnabled(Category.Eastern)
        binding.filterEuropean.isChecked = viewModel.enabledCategories.isEnabled(Category.European)
        binding.filterMediterranean.isChecked = viewModel.enabledCategories.isEnabled(Category.Mediterranean)
        binding.filterPanasian.isChecked = viewModel.enabledCategories.isEnabled(Category.Panasian)
        binding.filterRussian.isChecked = viewModel.enabledCategories.isEnabled(Category.Russian)

          binding.saveButton.setOnClickListener {
              viewModel.enabledCategories.setEnabled(Category.American, binding.filterAmerican.isChecked)
              viewModel.enabledCategories.setEnabled(Category.Asian, binding.filterAsian.isChecked)
              viewModel.enabledCategories.setEnabled(Category.Eastern, binding.filterEastern.isChecked)
              viewModel.enabledCategories.setEnabled(Category.European, binding.filterEuropean.isChecked)
              viewModel.enabledCategories.setEnabled(Category.Mediterranean, binding.filterMediterranean.isChecked)
              viewModel.enabledCategories.setEnabled(Category.Panasian, binding.filterPanasian.isChecked)
              viewModel.enabledCategories.setEnabled(Category.Russian, binding.filterRussian.isChecked)
              setFragmentResult(REQUEST_KEY, Bundle())
              findNavController().popBackStack()
          }

    }.root

    override fun onDestroy() {
        (requireActivity() as? AppActivity)?.showBottomNav(true)
        super.onDestroy()
    }

    companion object {
        const val REQUEST_KEY = "filterFragmentRequestKey"
    }
}