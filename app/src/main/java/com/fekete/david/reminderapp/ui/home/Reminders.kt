package com.fekete.david.reminderapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.fekete.david.reminderapp.data.entitiy.Reminder
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Reminders() {
    //Message, location_x, location_y, reminder_time, creation_time, creator_id, reminder_seen
    val reminderList: List<Reminder> = listOf(
//        Reminder(1, "Buy some meat",Date(1575678752138)),
//        Reminder(2, "Dont forget the laundry", Date(1575678752138)),
//        Reminder(3, "Take out the trash", Date(1675698752138)),
//        Reminder(4, "Dentist", Date(1675698752138)),
//        Reminder(5, "Take the dog for a walk", Date(1675698752138)),
//        Reminder(6, "Buy some meat", Date(1675698752138)),
//        Reminder(7, "Dont forget the laundry", Date(1575678752138)),
//        Reminder(8, "Take out the trash", Date(1675698752138)),
//        Reminder(9, "Dentist", Date(1675698752138)),
//        Reminder(10, "Take the dog for a walk", Date(1675698752138)),
//        Reminder(11, "Buy some meat", Date(1675698752138)),
//        Reminder(12, "Dont forget the laundry", Date(1575678752138)),
//        Reminder(13, "Take out the trash", Date(1675698752138)),
//        Reminder(14, "Dentist", Date(1675698752138)),
//        Reminder(15, "Take the dog for a walk", Date(1675698752138)),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        RemindersList(list = reminderList)
    }
}

@Composable
fun RemindersList(list: List<Reminder>) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            ReminderListItem(
                reminder = item,
                onClick = {}
            )
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onClick: () -> Unit
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick }) {
        val (divider, text, date) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = reminder.message,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(text) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = 0.dp,
                    endMargin = 0.dp
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )
        Text(
            text = reminder.reminder_time.formatToString(),
            maxLines = 1,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = 0.dp,
                    endMargin = 0.dp
                )
                top.linkTo(text.bottom, margin = 8.dp)
                bottom.linkTo(parent.bottom, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )
    }
}

private fun Date.formatToString(): String {
    return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(this)

}