package com.jei.lostfound.ui.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jei.lostfound.R
import com.jei.lostfound.data.entity.Item
import com.jei.lostfound.databinding.FragmentReportItemBinding
import java.time.LocalDate

class ReportItemFragment : Fragment() {
    private var _binding: FragmentReportItemBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportItemViewModel by viewModels()
    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReportItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.etDate.setText(selectedDate.toString())
        binding.etDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    selectedDate = LocalDate.of(y, m + 1, d)
                    binding.etDate.setText(selectedDate.toString())
                },
                selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth
            ).show()
        }
        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
        binding.btnSubmit.setOnClickListener { submit() }
    }

    private fun submit() {
        val title = binding.etTitle.text?.toString().orEmpty()
        val location = binding.etLocation.text?.toString().orEmpty()
        val description = binding.etDescription.text?.toString().orEmpty()
        if (title.isBlank() || location.isBlank() || description.isBlank()) {
            binding.tvError.setText(R.string.error_required)
            binding.tvError.visibility = View.VISIBLE
            return
        }
        binding.tvError.visibility = View.GONE
        val type = if (binding.rbFound.isChecked) Item.Type.FOUND else Item.Type.LOST
        viewModel.submit(type, title, selectedCategory(), location, selectedDate.toString(), description) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.report_saved, Snackbar.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
    }

    private fun selectedCategory(): String = when (binding.rgCategory.checkedRadioButtonId) {
        R.id.rb_wallet -> "Wallet"
        R.id.rb_electronics -> "Electronics"
        R.id.rb_umbrella -> "Umbrella"
        R.id.rb_books -> "Books"
        R.id.rb_other -> "Other"
        else -> "ID Card"
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
