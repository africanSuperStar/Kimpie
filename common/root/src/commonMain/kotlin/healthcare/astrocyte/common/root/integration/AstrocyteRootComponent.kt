package healthcare.astrocyte.common.root.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import healthcare.astrocyte.common.database.AstrocyteSharedDatabase
import healthcare.astrocyte.common.edit.AstrocyteEdit
import healthcare.astrocyte.common.edit.integration.AstrocyteEditComponent
import healthcare.astrocyte.common.main.AstrocyteMain
import healthcare.astrocyte.common.main.integration.AstrocyteMainComponent
import healthcare.astrocyte.common.root.AstrocyteRoot
import healthcare.astrocyte.common.root.AstrocyteRoot.Child
import healthcare.astrocyte.common.utils.Consumer

class AstrocyteRootComponent internal constructor(
    componentContext: ComponentContext,
    private val astrocyteMain: (ComponentContext, Consumer<AstrocyteMain.Output>) -> AstrocyteMain,
    private val astrocyteEdit: (ComponentContext, itemId: Long, Consumer<AstrocyteEdit.Output>) -> AstrocyteEdit
) : AstrocyteRoot, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        database: AstrocyteSharedDatabase
    ) : this(
        componentContext = componentContext,
        astrocyteMain = { childContext, output ->
            AstrocyteMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                database = database,
                output = output
            )
        },
        astrocyteEdit = { childContext, itemId, output ->
            AstrocyteEditComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                database = database,
                itemId = itemId,
                output = output
            )
        }
    )

    private val router =
        router<Configuration, Child>(
            initialConfiguration = Configuration.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Child>> = router.state

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child =
        when (configuration) {
            is Configuration.Main -> Child.Main(astrocyteMain(componentContext, Consumer(::onMainOutput)))
            is Configuration.Edit -> Child.Edit(astrocyteEdit(componentContext, configuration.itemId, Consumer(::onEditOutput)))
        }

    private fun onMainOutput(output: AstrocyteMain.Output): Unit =
        when (output) {
            is AstrocyteMain.Output.Selected -> router.push(Configuration.Edit(itemId = output.id))
        }

    private fun onEditOutput(output: AstrocyteEdit.Output): Unit =
        when (output) {
            is AstrocyteEdit.Output.Finished -> router.pop()
        }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Main : Configuration()

        @Parcelize
        data class Edit(val itemId: Long) : Configuration()
    }
}
