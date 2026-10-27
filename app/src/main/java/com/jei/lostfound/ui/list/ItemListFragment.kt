package com.jei.lostfound.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jei.lostfound.R
import com.jei.lostfound.databinding.FragmentItemListBinding
import kotlinx.coroutines.launch

class ItemListFragment : Fragment() {
    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ItemAdapter { item ->
            findNavController().navigate(R.id.action_list_to_detail, bundleOf("itemId" to item.itemId))
        }
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItems.adapter = adapter
        binding.etSearch.doAfterTextChanged { viewModel.setQuery(it?.toString().orEmpty()) }
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checked ->
            viewModel.setFilter(
                when (checked.firstOrNull()) {
                    R.id.chip_lost -> ItemFilter.LOST
                    R.id.chip_found -> ItemFilter.FOUND
                    R.id.chip_returned -> ItemFilter.RETURNED
                    else -> ItemFilter.ALL
                }
            )
        }
        binding.fabReport.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_report)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect { list ->
                    adapter.submitList(list.orEmpty())
                    binding.tvEmpty.visibility = if (list != null && list.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.rvItems.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
