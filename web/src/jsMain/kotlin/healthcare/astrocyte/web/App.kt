package healthcare.astrocyte.web

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.arkivanov.decompose.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import healthcare.astrocyte.common.database.DefaultAstrocyteSharedDatabase
import healthcare.astrocyte.common.database.astrocyteDatabaseDriver
import healthcare.astrocyte.common.root.integration.AstrocyteRootComponent
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.compose.web.ui.Styles
import org.w3c.dom.HTMLElement

fun main() {
    val rootElement = document.getElementById("root") as HTMLElement

    val lifecycle = LifecycleRegistry()

    val root =
        AstrocyteRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            storeFactory = DefaultStoreFactory,
            database = DefaultAstrocyteSharedDatabase(astrocyteDatabaseDriver())
        )

    lifecycle.resume()

    renderComposable(root = rootElement) {
        Style(Styles)

        AstrocyteRootUi(root)
    }
}
