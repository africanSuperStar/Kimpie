@file:Suppress("EXPERIMENTAL_API_USAGE")

package healthcare.astrocyte.common.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import healthcare.astrocyte.common.root.AstrocyteRoot
import healthcare.astrocyte.common.root.AstrocyteRoot.Child

@Composable
fun AstrocyteRootContent(component: AstrocyteRoot) {
    Children(routerState = component.routerState, animation = crossfadeScale()) {
        when (val child = it.instance) {
            is Child.Main -> AstrocyteMainContent(child.component)
            is Child.Edit -> AstrocyteEditContent(child.component)
        }
    }
}
