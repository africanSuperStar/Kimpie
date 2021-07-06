package healthcare.astrocyte.common.root.integration

import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import healthcare.astrocyte.common.edit.AstrocyteEdit
import healthcare.astrocyte.common.edit.AstrocyteEdit.Model
import healthcare.astrocyte.common.edit.AstrocyteEdit.Output

class AstrocyteEditFake(
    val itemId: Long,
    val output: Consumer<Output>
) : TodoEdit {

    override val models: Value<Model> get() = TODO("Not used")

    override fun onTextChanged(text: String) {
    }

    override fun onDoneChanged(isDone: Boolean) {
    }

    override fun onCloseClicked() {
    }
}