package healthcare.astrocyte.common.main.store

import com.arkivanov.mvikotlin.core.store.Store
import healthcare.astrocyte.common.main.AstrocyteItem
import healthcare.astrocyte.common.main.store.AstrocyteMainStore.Intent
import healthcare.astrocyte.common.main.store.AstrocyteMainStore.State

internal interface AstrocyteMainStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class SetItemDone(val id: Long, val isDone: Boolean) : Intent()
        data class DeleteItem(val id: Long) : Intent()
        data class SetText(val text: String) : Intent()
        object AddItem : Intent()
    }

    data class State(
        val items: List<AstrocyteItem> = emptyList(),
        val text: String = ""
    )
}
