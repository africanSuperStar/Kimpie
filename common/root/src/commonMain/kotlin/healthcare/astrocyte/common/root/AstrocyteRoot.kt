package healthcare.astrocyte.common.root

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import healthcare.astrocyte.common.edit.AstrocyteEdit
import healthcare.astrocyte.common.main.AstrocyteMain

interface AstrocyteRoot {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class Main(val component: AstrocyteMain) : Child()
        data class Edit(val component: AstrocyteEdit) : Child()
    }
}
