package uz.coderqwerty.taskwithgooglemap.presentation.ui.screens.main

import kotlinx.coroutines.flow.StateFlow
import uz.coderqwerty.taskwithgooglemap.domain.model.Branch
import uz.coderqwerty.taskwithgooglemap.domain.model.MyClusterItem

sealed interface MainScreenContract {

    data class UiState(
        val branches: List<Branch> = emptyList(),
        val selectedBranch: MyClusterItem? = null,
        val distance: String = "0.0 km"
    )

    interface Model {
        val uiState: StateFlow<UiState>

        fun loadBranches()
        fun onBranchSelected(branch: MyClusterItem)
        fun onBottomSheetClosed()
        fun getDirections(branch: MyClusterItem)

    }

}