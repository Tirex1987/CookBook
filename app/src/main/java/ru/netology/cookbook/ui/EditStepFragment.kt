package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.netology.cookbook.databinding.EditStepFragmentBinding

class EditStepFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = EditStepFragmentBinding.inflate(layoutInflater, container, false).also {

    }.root
}