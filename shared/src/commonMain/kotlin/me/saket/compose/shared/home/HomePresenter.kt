package me.saket.compose.shared.home

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.merge
import com.badoo.reaktive.observable.ofType
import me.saket.compose.shared.Presenter
import me.saket.compose.shared.home.HomeEvent.NewNoteClicked
import me.saket.compose.shared.navigation.Navigator
import me.saket.compose.shared.navigation.ScreenKey
import me.saket.compose.shared.note.NoteRepository
import me.saket.compose.shared.util.consumeOnNext

class HomePresenter(
  private val repository: NoteRepository,
  private val navigator: Navigator
) : Presenter<HomeEvent, HomeUiModel> {

  override fun contentModels(events: Observable<HomeEvent>): Observable<HomeUiModel> {
    return merge(populateNotes(), events.openNewNoteScreen())
  }

  private fun Observable<HomeEvent>.openNewNoteScreen(): Observable<HomeUiModel> =
    ofType<NewNoteClicked>()
        .consumeOnNext {
          navigator.goTo(ScreenKey.NewNote)
        }

  private fun populateNotes(): Observable<HomeUiModel> =
    repository.notes().map {
      HomeUiModel(it.map { note ->
        HomeUiModel.Note(
            adapterId = 0L,
            title = note.title,
            body = note.body
        )
      })
    }

  interface Factory {
    fun create(navigator: Navigator): HomePresenter
  }
}
