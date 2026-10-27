package com.jei.lostfound.ui.myreports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jei.lostfound.R
import com.jei.lostfound.databinding.FragmentMyReportsBinding
import kotlinx.coroutines.launch

class MyReportsFragment : Fragment() {
    private var _binding: FragmentMyReportsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyReportsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = MyReportAdapter(
            onOpen = { item ->
                findNavController().navigate(R.id.action_reports_to_detail, bundleOf("itemId" to item.itemId))
            },
            onDelete = { item ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.delete_title)
                    .setMessage(R.string.delete_message)
                    .setPositiveButton(R.string.btn_yes) { _, _ -> viewModel.delete(item.itemId) }
                    .setNegativeButton(R.string.btn_no, null)
                    .show()
            },
            onReturned = { item -> viewModel.markReturned(item.itemId) }
        )
        binding.rvReports.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReports.adapter = adapter
        binding.chipGroupStatus.setOnCheckedStateChangeListener { _, checked ->
            viewModel.setReturnedFilter(
                when (checked.firstOrNull()) {
                    R.id.chip_status_open -> false
                    R.id.chip_status_returned -> true
                    else -> null
                }
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.reports.collect { list ->
                        adapter.submitList(list.orEmpty())
                        binding.tvEmpty.visibility = if (list != null && list.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
                launch {
                    viewModel.events.collect { res ->
                        Snackbar.make(binding.root, res, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.rvReports.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
