package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.cookbook.R
import ru.netology.cookbook.data.Category
import ru.netology.cookbook.databinding.FilterFragmentBinding
import ru.netology.cookbook.viewModel.RecipeViewModel

class FilterFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onResume() {
        super.onResume()
        (requireActivity() as? AppActivity)?.showBottomNav(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FilterFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        with(binding) {
            filterAmerican.isChecked = viewModel.enabledCategories.isEnabled(Category.American)
            filterAsian.isChecked = viewModel.enabledCategories.isEnabled(Category.Asian)
            filterEastern.isChecked = viewModel.enabledCategories.isEnabled(Category.Eastern)
            filterEuropean.isChecked = viewModel.enabledCategories.isEnabled(Category.European)
            filterMediterranean.isChecked = viewModel.enabledCategories.isEnabled(Category.Mediterranean)
            filterPanasian.isChecked = viewModel.enabledCategories.isEnabled(Category.Panasian)
            filterRussian.isChecked = viewModel.enabledCategories.isEnabled(Category.Russian)
        }

        binding.saveButton.setOnClickListener {
            with(binding) {
                if (!(
                      filterAmerican.isChecked || filterAsian.isChecked || filterEastern.isChecked ||
                      filterEuropean.isChecked || filterMediterranean.isChecked ||
                      filterPanasian.isChecked || filterPanasian.isChecked || filterRussian.isChecked
                )) {
                    Toast.makeText(requireContext(), getString(R.string.noChooseFilterError), Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }
            with(viewModel) {
                enabledCategories.setEnabled(
                    Category.American,
                    binding.filterAmerican.isChecked
                )
                enabledCategories.setEnabled(
                    Category.Asian,
                    binding.filterAsian.isChecked
                )
                enabledCategories.setEnabled(
                    Category.Eastern,
                    binding.filterEastern.isChecked
                )
                enabledCategories.setEnabled(
                    Category.European,
                    binding.filterEuropean.isChecked
                )
                enabledCategories.setEnabled(
                    Category.Mediterranean,
                    binding.filterMediterranean.isChecked
                )
                enabledCategories.setEnabled(
                    Category.Panasian,
                    binding.filterPanasian.isChecked
                )
                enabledCategories.setEnabled(
                    Category.Russian,
                    binding.filterRussian.isChecked
                )
            }
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