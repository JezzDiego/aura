package com.example.aura.presentation.ui.feature_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aura.core.BaseViewModel
import com.example.aura.core.ResultWrapper
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.User
import com.example.aura.domain.usecase.establishment.EstablishmentUseCases
import com.example.aura.domain.usecase.user.UserUseCases
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ResultsViewMode { LIST, MAP }

data class SearchUiState(
    val query: String = "",
    val selectedFilters: Set<AccessibilityFilter> = emptySet(),
    val filtersExpanded: Boolean = true,
    val viewMode: ResultsViewMode = ResultsViewMode.LIST,
    val results: ResultWrapper<List<Establishment>> = ResultWrapper.Loading,
    /** Incrementado quando a busca veio de outra tela/"Aplicar" — usado para rolar até os resultados. */
    val scrollToResultsSignal: Int = 0,
)

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val establishmentUseCases: EstablishmentUseCases,
    userUseCases: UserUseCases,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val user: StateFlow<User?> = userUseCases.getLocalUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val queryFlow = MutableStateFlow("")
    private val filtersFlow = MutableStateFlow<Set<AccessibilityFilter>>(emptySet())

    init {
        viewModelScope.launch {
            combine(queryFlow.debounce(250), filtersFlow) { q, f -> q to f }
                .distinctUntilChanged()
                .collect { (q, f) -> search(q, f) }
        }
    }

    private fun search(query: String, filters: Set<AccessibilityFilter>) {
        executeSafeCall(
            block = { establishmentUseCases.searchEstablishments(query, filters) },
            onSuccess = { list -> _uiState.update { it.copy(results = ResultWrapper.Success(list)) } },
            onError = { e -> _uiState.update { it.copy(results = ResultWrapper.Error(e)) } },
        )
    }

    fun refresh() = search(queryFlow.value, filtersFlow.value)

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
        queryFlow.value = value
    }

    fun clearQuery() = onQueryChange("")

    fun toggleFilter(filter: AccessibilityFilter) {
        val newFilters = _uiState.value.selectedFilters.let {
            if (filter in it) it - filter else it + filter
        }
        setFilters(newFilters)
    }

    fun clearFilters() = setFilters(emptySet())

    private fun setFilters(filters: Set<AccessibilityFilter>) {
        _uiState.update { it.copy(selectedFilters = filters) }
        filtersFlow.value = filters
    }

    fun toggleFiltersExpanded() {
        _uiState.update { it.copy(filtersExpanded = !it.filtersExpanded) }
    }

    /** "Aplicar Filtros": recolhe o painel e leva a pessoa até a lista de resultados. */
    fun applyFilters() {
        _uiState.update { it.copy(filtersExpanded = false, scrollToResultsSignal = it.scrollToResultsSignal + 1) }
    }

    fun setViewMode(mode: ResultsViewMode) {
        _uiState.update { it.copy(viewMode = mode) }
    }

    fun applyProfileFilters() {
        user.value?.accessibilityFilters?.takeIf { it.isNotEmpty() }?.let { setFilters(it) }
    }

    /** Chamado pela tela Início (busca, filtros rápidos ou "Filtrar pelo meu perfil"). */
    fun applyExternalSearch(query: String, filters: Set<AccessibilityFilter>) {
        _uiState.update {
            it.copy(
                query = query,
                selectedFilters = filters,
                filtersExpanded = filters.isEmpty() && query.isBlank(),
                scrollToResultsSignal = it.scrollToResultsSignal + 1,
            )
        }
        queryFlow.value = query
        filtersFlow.value = filters
        search(query, filters)
    }
}

class SearchViewModelFactory(
    private val establishmentUseCases: EstablishmentUseCases,
    private val userUseCases: UserUseCases,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(establishmentUseCases, userUseCases) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
