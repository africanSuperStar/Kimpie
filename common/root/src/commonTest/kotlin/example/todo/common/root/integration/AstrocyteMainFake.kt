package healthcare.astrocyte.common.root.integration

import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import healthcare.astrocyte.common.main.TodoMain
import healthcare.astrocyte.common.main.TodoMain.Model
import healthcare.astrocyte.common.main.TodoMain.Output

class AstrocyteMainFake(
    val output: Consumer<Output>
) : AstrocyteMain {

    override val models: Value<Model> get() = TODO("Not used")

    override fun onItemClicked(id: Long) {
    }

    override fun onItemDoneChanged(id: Long, isDone: Boolean) {
    }

    override fun onItemDeleteClicked(id: Long) {
    }

    override fun onInputTextChanged(text: String) {
    }

    override fun onAddItemClicked() {
    }
}