package healthcare.astrocyte.common.database

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.observable.Observable

interface AstrocyteSharedDatabase {

    fun observeAll(): Observable<List<AstrocyteItemEntity>>

    fun select(id: Long): Maybe<AstrocyteItemEntity>

    fun add(text: String): Completable

    fun setText(id: Long, text: String): Completable

    fun setDone(id: Long, isDone: Boolean): Completable

    fun delete(id: Long): Completable

    fun clear(): Completable
}
