package healthcare.astrocyte.common.edit

import com.arkivanov.decompose.value.Value

interface AstrocyteEdit {

    val models: Value<Model>

    fun onTextChanged(text: String)

    fun onDoneChanged(isDone: Boolean)

    fun onCloseClicked()

    data class Model(
        val text: String,
        val isDone: Boolean
    )

    sealed class Output {
        object Finished : Output()
    }
}
