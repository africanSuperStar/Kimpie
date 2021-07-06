package healthcare.astrocyte.common.main.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import healthcare.astrocyte.common.main.AstrocyteItem
import healthcare.astrocyte.common.main.store.AstrocyteMainStore.Intent
import healthcare.astrocyte.common.main.store.AstrocyteMainStore.State

internal class AstrocyteMainStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database
) {

    fun provide(): AstrocyteMainStore =
        object : AstrocyteMainStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "AstrocyteListStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Result {
        data class ItemsLoaded(val items: List<AstrocyteItem>) : Result()
        data class ItemDoneChanged(val id: Long, val isDone: Boolean) : Result()
        data class ItemDeleted(val id: Long) : Result()
        data class TextChanged(val text: String) : Result()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            database
                .updates
                .observeOn(mainScheduler)
                .map(Result::ItemsLoaded)
                .subscribeScoped(onNext = ::dispatch)
        }

        override fun executeIntent(intent: Intent, getState: () -> State): Unit =
            when (intent) {
                is Intent.SetItemDone -> setItemDone(id = intent.id, isDone = intent.isDone)
                is Intent.DeleteItem -> deleteItem(id = intent.id)
                is Intent.SetText -> dispatch(Result.TextChanged(text = intent.text))
                is Intent.AddItem -> addItem(state = getState())
            }

        private fun setItemDone(id: Long, isDone: Boolean) {
            dispatch(Result.ItemDoneChanged(id = id, isDone = isDone))
            database.setDone(id = id, isDone = isDone).subscribeScoped()
        }

        private fun deleteItem(id: Long) {
            dispatch(Result.ItemDeleted(id = id))
            database.delete(id = id).subscribeScoped()
        }

        private fun addItem(state: State) {
            if (state.text.isNotEmpty()) {
                dispatch(Result.TextChanged(text = ""))
                database.add(text = state.text).subscribeScoped()
            }
        }
    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                is Result.ItemsLoaded -> copy(items = result.items.sorted())
                is Result.ItemDoneChanged -> update(id = result.id) { copy(isDone = result.isDone) }
                is Result.ItemDeleted -> copy(items = items.filterNot { it.id == result.id })
                is Result.TextChanged -> copy(text = result.text)
            }

        private inline fun State.update(id: Long, func: AstrocyteItem.() -> AstrocyteItem): State {
            val item = items.find { it.id == id } ?: return this

            return put(item.func())
        }

        private fun State.put(item: AstrocyteItem): State {
            val oldItems = items.associateByTo(mutableMapOf(), AstrocyteItem::id)
            val oldItem: AstrocyteItem? = oldItems.put(item.id, item)

            return copy(items = if (oldItem?.order == item.order) oldItems.values.toList() else oldItems.values.sorted())
        }

        private fun Iterable<AstrocyteItem>.sorted(): List<AstrocyteItem> = sortedByDescending(AstrocyteItem::order)
    }

    interface Database {
        val updates: Observable<List<AstrocyteItem>>

        fun setDone(id: Long, isDone: Boolean): Completable

        fun delete(id: Long): Completable

        fun add(text: String): Completable
    }
}
