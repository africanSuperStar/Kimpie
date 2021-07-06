package healthcare.astrocyte.common.edit.integration

import healthcare.astrocyte.common.edit.AstrocyteEdit.Model
import healthcare.astrocyte.common.edit.store.AstrocyteEditStore.State

internal val stateToModel: (State) -> Model =
    {
        Model(
            text = it.text,
            isDone = it.isDone
        )
    }
