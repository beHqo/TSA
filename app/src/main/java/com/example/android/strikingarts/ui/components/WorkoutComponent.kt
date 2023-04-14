package com.example.android.strikingarts.ui.components

//@Composable
//fun WorkoutItem(workout: Workout) {
//    ExpandableListItem(
//        primaryTextId = workout.name,
//        expandedText = getWorkoutDetails(workout)
//    )
//}

//@Composable
//fun WorkoutList(workoutList: List<Workout>) {
//    val maxWidth = LocalConfiguration.current.screenWidthDp
//    val dividerStartIndent = maxWidth.div(3.33)
//
//    LazyColumn(
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.Start,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        items(workoutList) { workout ->
//            WorkoutItem(workout)
//            Divider(startIndent = dividerStartIndent.dp)
//        }
//    }
//}

//
//@Preview
//@Composable
//fun PreviewWorkoutList() {
//    WorkoutList(workoutList = listOf(
//        Workout(name = "Kir1", numberOfRounds = 5, roundsDurationMilli = 3000, restsDurationMilli = 600),
//        Workout(name = "Kir2", numberOfRounds = 10, roundsDurationMilli = 3001, restsDurationMilli = 620),
//        Workout(name = "Kir3", numberOfRounds = 2, roundsDurationMilli = 1, restsDurationMilli = 1),
//        Workout(name = "Kir4", numberOfRounds = 1, roundsDurationMilli = 600, restsDurationMilli = 100)
//    ))
//}