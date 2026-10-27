package com.jei.lostfound.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jei.lostfound.data.AppDatabase
import com.jei.lostfound.data.entity.Item
import com.jei.lostfound.data.repository.LostFoundRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class ItemFilter { ALL, LOST, FOUND, RETURNED }

class ItemListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LostFoundRepository(AppDatabase.getDatabase(application))
    private val query = MutableStateFlow("")
    private val filter = MutableStateFlow(ItemFilter.ALL)

    val items: StateFlow<List<Item>?> =
        combine(repository.getAllItems(), query, filter) { all, q, f ->
            val text = q.trim()
            all.filter { item ->
                val matchesFilter = when (f) {
                    ItemFilter.ALL -> true
                    ItemFilter.LOST -> item.type == Item.Type.LOST
                    ItemFilter.FOUND -> item.type == Item.Type.FOUND
                    ItemFilter.RETURNED -> item.status == Item.Status.RETURNED
                }
                val matchesText = text.isEmpty() ||
                    item.title.contains(text, true) ||
                    item.location.contains(text, true) ||
                    item.category.contains(text, true)
                matchesFilter && matchesText
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun setQuery(text: String) { query.value = text }
    fun setFilter(value: ItemFilter) { filter.value = value }
}
