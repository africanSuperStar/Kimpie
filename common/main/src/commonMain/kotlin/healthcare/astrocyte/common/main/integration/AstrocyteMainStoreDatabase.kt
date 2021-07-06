package healthcare.astrocyte.common.main.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable
import healthcare.astrocyte.common.database.AstrocyteItemEntity
import healthcare.astrocyte.common.database.AstrocyteSharedDatabase
import healthcare.astrocyte.common.main.AstrocyteItem
import healthcare.astrocyte.common.main.store.AstrocyteMainStoreProvider

internal class AstrocyteMainStoreDatabase(
    private val database: AstrocyteSharedDatabase
) : AstrocyteMainStoreProvider.Database {

    override val updates: Observable<List<AstrocyteItem>> =
        database
            .observeAll()
            .mapIterable { it.toItem() }

    private fun AstrocyteItemEntity.toItem(): AstrocyteItem =
        AstrocyteItem(
            id = id,
            order = orderNum,
            text = text,
            isDone = isDone
        )

    override fun setDone(id: Long, isDone: Boolean): Completable =
        database.setDone(id = id, isDone = isDone)

    override fun delete(id: Long): Completable =
        database.delete(id = id)

    override fun add(text: String): Completable =
        database.add(text = text)
}
