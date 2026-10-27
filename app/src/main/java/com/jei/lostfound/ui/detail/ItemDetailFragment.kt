package com.jei.lostfound.ui.detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jei.lostfound.R
import com.jei.lostfound.data.entity.Item
import com.jei.lostfound.databinding.FragmentItemDetailBinding
import kotlinx.coroutines.launch

class ItemDetailFragment : Fragment() {
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnClaim.setOnClickListener { showClaimDialog() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.item.collect { item ->
                    if (item == null) return@collect
                    binding.tvDetailTitle.text = item.title
                    binding.tvDetailType.text = item.type
                    binding.tvDetailCategory.text = item.category
                    binding.tvDetailLocation.text = item.location
                    binding.tvDetailDate.text = item.itemDate
                    binding.tvDetailDescription.text = item.description
                    binding.tvDetailStatus.text = item.status
                    val color = when (item.status) {
                        Item.Status.RETURNED -> R.color.badge_returned
                        Item.Status.CLAIMED -> R.color.badge_claimed
                        else -> R.color.badge_open
                    }
                    binding.tvDetailStatus.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color))

                    val mine = item.reporterId == viewModel.currentUserId
                    val canClaim = !mine && item.status == Item.Status.OPEN
                    binding.btnClaim.visibility = if (canClaim) View.VISIBLE else View.GONE
                    binding.tvHint.visibility = if (canClaim) View.GONE else View.VISIBLE
                    binding.tvHint.text = when {
                        mine -> getString(R.string.own_item_hint)
                        item.status == Item.Status.RETURNED -> getString(R.string.returned_hint)
                        else -> getString(R.string.status_claimed)
                    }
                }
            }
        }
    }

    private fun showClaimDialog() {
        val input = EditText(requireContext()).apply {
            hint = getString(R.string.claim_hint)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            setPadding(48, 32, 48, 16)
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.claim_title)
            .setView(input)
            .setPositiveButton(R.string.btn_submit) { _, _ ->
                val message = input.text.toString().trim()
                if (message.isEmpty()) {
                    Snackbar.make(binding.root, R.string.claim_empty, Snackbar.LENGTH_SHORT).show()
                } else {
                    viewModel.submitClaim(message) {
                        Snackbar.make(binding.root, R.string.claim_saved, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
