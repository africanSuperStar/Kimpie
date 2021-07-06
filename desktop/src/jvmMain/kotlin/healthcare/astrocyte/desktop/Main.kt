package healthcare.astrocyte.desktop

import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.coroutinesinterop.asScheduler
import com.badoo.reaktive.scheduler.overrideSchedulers
import healthcare.astrocyte.common.database.DefaultAstrocyteSharedDatabase
import healthcare.astrocyte.common.database.AstrocyteDatabaseDriver
import healthcare.astrocyte.common.root.AstrocyteRoot
import healthcare.astrocyte.common.root.integration.AstrocyteRootComponent
import healthcare.astrocyte.common.ui.AstrocyteRootContent
import kotlinx.coroutines.Dispatchers

fun main() {
    overrideSchedulers(main = Dispatchers.Main::asScheduler)

    Window("Astrocyte") {
        Surface(modifier = Modifier.fillMaxSize()) {
            MaterialTheme {
                DesktopTheme {
                    AstrocyteRootContent(rememberRootComponent(factory = ::astrocyteRoot))
                }
            }
        }
    }
}

private fun astrocyteRoot(componentContext: ComponentContext): AstrocyteRoot =
    AstrocyteRootComponent(
        componentContext = componentContext,
        storeFactory = DefaultStoreFactory,
        database = DefaultAstrocyteSharedDatabase(AstrocyteDatabaseDriver())
    )
