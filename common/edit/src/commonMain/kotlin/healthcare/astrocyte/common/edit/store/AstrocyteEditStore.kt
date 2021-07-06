package healthcare.astrocyte.common.edit.store

import com.arkivanov.mvikotlin.core.store.Store
import healthcare.astrocyte.common.edit.AstrocyteItem
import healthcare.astrocyte.common.edit.store.AstrocyteEditStore.Intent
import healthcare.astrocyte.common.edit.store.AstrocyteEditStore.Label
import healthcare.astrocyte.common.edit.store.AstrocyteEditStore.State

internal interface AstrocyteEditStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class SetText(val text: String) : Intent()
        data class SetDone(val isDone: Boolean) : Intent()
    }

    data class State(
        val text: String = "",
        val isDone: Boolean = false
    )

    sealed class Label {
        data class Changed(val item: AstrocyteItem) : Label()
    }
}
