package healthcare.astrocyte.common.root.integration

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.arkivanov.decompose.lifecycle.resume
import com.badoo.reaktive.base.invoke
import healthcare.astrocyte.common.edit.AstrocyteEdit
import healthcare.astrocyte.common.main.AstrocyteMain
import healthcare.astrocyte.common.root.AstrocyteRoot
import healthcare.astrocyte.common.root.AstrocyteRoot.Child
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class AstrocyteRootTest {

    private val lifecycle = LifecycleRegistry().apply { resume() }

    @Test
    fun WHEN_created_THEN_AstrocyteMain_displayed() {
        val root = root()

        assertTrue(root.activeChild is Child.Main)
    }

    @Test
    fun GIVEN_AstrocyteMain_displayed_WHEN_AstrocyteMain_Output_Selected_THEN_AstrocyteEdit_displayed() {
        val root = root()

        root.activeChild.asAstrocyteMain().output(AstrocyteMain.Output.Selected(id = 10L))

        assertTrue(root.activeChild is Child.Edit)
        assertEquals(10L, root.activeChild.asAstrocyteEdit().itemId)
    }

    @Test
    fun GIVEN_AstrocyteEdit_displayed_WHEN_AstrocyteEdit_Output_Finished_THEN_AstrocyteMain_displayed() {
        val root = root()
        root.activeChild.asAstrocyteMain().output(AstrocyteMain.Output.Selected(id = 10L))

        root.activeChild.asAstrocyteEdit().output(AstrocyteEdit.Output.Finished)

        assertTrue(root.activeChild is Child.Main)
    }

    private fun root(): AstrocyteRoot =
        TodoRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            astrocyteMain = { _, output -> AstrocyteMainFake(output) },
            astrocyteEdit = { _, itemId, output -> AstrocyteEditFake(itemId, output) }
        )

    private val AstrocyteRoot.activeChild: Child get() = routerState.value.activeChild.instance

    private val Child.component: Any
        get() =
            when (this) {
                is Child.Main -> component
                is Child.Edit -> component
            }

    private fun Child.asAstrocyteMain(): AstrocyteMainFake = component as AstrocyteMainFake

    private fun Child.asAstrocyteEdit(): AstrocyteEditFake = component as AstrocyteEditFake
}
