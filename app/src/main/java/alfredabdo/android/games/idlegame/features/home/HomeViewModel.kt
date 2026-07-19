package alfredabdo.android.games.idlegame.features.home

import alfredabdo.android.games.idlegame.base.viewmodel.BaseViewModel
import alfredabdo.android.games.idlegame.features.home.data.DeleteSaveUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val deleteSaveUseCase: DeleteSaveUseCase,
) : BaseViewModel() {

    private val _isConfirmingDelete = MutableStateFlow(false)
    val isConfirmingDelete get() = _isConfirmingDelete.asStateFlow()

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting get() = _isDeleting.asStateFlow()

    fun showDeleteConfirmation() {
        viewModelScope.launch {
            _isConfirmingDelete.update { true }
        }
    }

    fun hideDeleteConfirmation() {
        viewModelScope.launch {
            _isConfirmingDelete.update { true }
        }
    }

    fun confirmDelete() {
        viewModelScope.launch {
            _isConfirmingDelete.update { false }
            _isDeleting.value = true
            runCoroutineCatching {
                deleteSaveUseCase(Unit)
            }
        }
    }

    fun hideIsDeletingAlert() {
        _isDeleting.update { false }
    }
}