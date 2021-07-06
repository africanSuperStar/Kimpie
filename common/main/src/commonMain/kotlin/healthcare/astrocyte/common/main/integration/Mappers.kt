package healthcare.astrocyte.common.main.integration

import healthcare.astrocyte.common.main.AstrocyteMain.Model
import healthcare.astrocyte.common.main.store.AstrocyteMainStore.State

internal val stateToModel: (State) -> Model =
    {
        Model(
            items = it.items,
            text = it.text
        )
    }