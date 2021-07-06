package healthcare.astrocyte.common.edit.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import healthcare.astrocyte.common.database.AstrocyteSharedDatabase
import healthcare.astrocyte.common.edit.AstrocyteEdit
import healthcare.astrocyte.common.edit.AstrocyteEdit.Model
import healthcare.astrocyte.common.edit.AstrocyteEdit.Output
import healthcare.astrocyte.common.edit.store.AstrocyteEditStore.Intent
import healthcare.astrocyte.common.edit.store.AstrocyteEditStoreProvider
import healthcare.astrocyte.common.utils.asValue
import healthcare.astrocyte.common.utils.getStore

class AstrocyteEditComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: AstrocyteSharedDatabase,
    itemId: Long,
    private val output: Consumer<Output>
) : AstrocyteEdit, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            AstrocyteEditStoreProvider(
                storeFactory = storeFactory,
                database = AstrocyteEditStoreDatabase(database = database),
                id = itemId
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onTextChanged(text: String) {
        store.accept(Intent.SetText(text = text))
    }

    override fun onDoneChanged(isDone: Boolean) {
        store.accept(Intent.SetDone(isDone = isDone))
    }

    override fun onCloseClicked() {
        output(Output.Finished)
    }
}
