package healthcare.astrocyte.common.edit.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map
import healthcare.astrocyte.common.database.AstrocyteItemEntity
import healthcare.astrocyte.common.database.AstrocyteSharedDatabase
import healthcare.astrocyte.common.edit.AstrocyteItem
import healthcare.astrocyte.common.edit.store.AstrocyteEditStoreProvider.Database

internal class AstrocyteEditStoreDatabase(
    private val database: AstrocyteSharedDatabase
) : Database {

    override fun load(id: Long): Maybe<AstrocyteItem> =
        database
            .select(id = id)
            .map { it.toItem() }

    private fun AstrocyteItemEntity.toItem(): AstrocyteItem =
        AstrocyteItem(
            text = text,
            isDone = isDone
        )

    override fun setText(id: Long, text: String): Completable =
        database.setText(id = id, text = text)

    override fun setDone(id: Long, isDone: Boolean): Completable =
        database.setDone(id = id, isDone = isDone)
}
