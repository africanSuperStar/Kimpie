package healthcare.astrocyte.common.main.store

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completableFromFunction
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import healthcare.astrocyte.common.main.AstrocyteItem

internal class TestAstrocyteMainStoreDatabase : AstrocyteMainStoreProvider.Database {

    private val subject = BehaviorSubject<List<AstrocyteItem>>(emptyList())

    var items: List<AstrocyteItem>
        get() = subject.value
        set(value) {
            subject.onNext(value)
        }

    override val updates: Observable<List<AstrocyteItem>> = subject

    override fun setDone(id: Long, isDone: Boolean): Completable =
        completableFromFunction {
            update(id = id) { copy(isDone = isDone) }
        }

    override fun delete(id: Long): Completable =
        completableFromFunction {
            this.items = items.filterNot { it.id == id }
        }

    override fun add(text: String): Completable =
        completableFromFunction {
            val id = items.maxByOrNull(AstrocyteItem::id)?.id?.inc() ?: 1L
            this.items += AstrocyteItem(id = id, order = id, text = text)
        }

    private fun update(id: Long, func: AstrocyteItem.() -> AstrocyteItem) {
        items = items.map { if (it.id == id) it.func() else it }
    }
}
