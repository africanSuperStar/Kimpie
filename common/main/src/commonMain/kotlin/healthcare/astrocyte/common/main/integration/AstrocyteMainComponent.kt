package healthcare.astrocyte.common.main.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import healthcare.astrocyte.common.database.AstrocyteSharedDatabase
import healthcare.astrocyte.common.main.AstrocyteMain
import healthcare.astrocyte.common.main.AstrocyteMain.Model
import healthcare.astrocyte.common.main.AstrocyteMain.Output
import healthcare.astrocyte.common.main.store.AstrocyteMainStore.Intent
import healthcare.astrocyte.common.main.store.AstrocyteMainStoreProvider
import healthcare.astrocyte.common.utils.asValue
import healthcare.astrocyte.common.utils.getStore

class AstrocyteMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: AstrocyteSharedDatabase,
    private val output: Consumer<Output>
) : AstrocyteMain, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            AstrocyteMainStoreProvider(
                storeFactory = storeFactory,
                database = AstrocyteMainStoreDatabase(database = database)
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onItemClicked(id: Long) {
        output(Output.Selected(id = id))
    }

    override fun onItemDoneChanged(id: Long, isDone: Boolean) {
        store.accept(Intent.SetItemDone(id = id, isDone = isDone))
    }

    override fun onItemDeleteClicked(id: Long) {
        store.accept(Intent.DeleteItem(id = id))
    }

    override fun onInputTextChanged(text: String) {
        store.accept(Intent.SetText(text = text))
    }

    override fun onAddItemClicked() {
        store.accept(Intent.AddItem)
    }
}
