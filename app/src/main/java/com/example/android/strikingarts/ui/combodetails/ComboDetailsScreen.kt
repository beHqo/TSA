package com.example.android.strikingarts.ui.combodetails

import androidx.compose.runtime.Composable


@Composable
fun ComboDetailsScreen() {

}
















//data class Entity(
//    var id: Long = 0,
//    var firstTrait: String = "",
//    var secondTrait: String = "",
//    var thirdTrait: Int = Int.MIN_VALUE,
//)
//
//// because of the large number of states in certain screens, I've grouped them all in a separate
//// class to be able to expose them in the highest level composable needed in one line of code (1)
//data class EntityScreenState(private val entity: Entity) {
//    var firstTrait by mutableStateOf("")
//    var secondTrait by mutableStateOf("")
//    var thirdTrait by mutableStateOf(0)
//    var showSomeComposable by mutableStateOf(false)
//
//}
//
//class EntityViewModel @Inject constructor(private val repository: EntityRepository) : ViewModel() {
//    val entity = repository.getEntity()
//    val state = EntityScreenState(entity)
//
//    // Composables in EntityScreen take lambda parameters like this to change state
//    fun onFirstTraitChange(newTrait: String) {
//        state.firstTrait = newTrait
//    }
//
//    // methods for other mutable stuff...
//
//}
//
//@Composable
//fun EntityScreen(viewModel: EntityViewModel = hiltViewModel()) {
//    // (1) said line of code
//    val state = viewModel.state
//
//    Column {
//        Text(text = state.firstTrait)
//        Button(onClick = {
//            viewModel.onFirstTraitChange(
//                "Show something new (for the first time this button is pressed lol)"
//            )
//        }) {
//            Text(text = "Event Button")
//        }
//        // Rest of the UI
//    }
//}