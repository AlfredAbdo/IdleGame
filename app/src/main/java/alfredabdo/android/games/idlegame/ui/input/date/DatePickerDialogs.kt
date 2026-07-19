package alfredabdo.android.games.idlegame.ui.input.date

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun MainDatePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButtonContent: @Composable RowScope.() -> Unit,
    onConfirmButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    dismissButtonContent: (@Composable RowScope.() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    content: @Composable ColumnScope.() -> Unit,
) {
    DatePickerDialog(
        onDismissRequest,
        confirmButton = {
            MainDatePickerButton(
                onClick = onConfirmButtonClick,
                content = confirmButtonContent,
            )
        },
        modifier,
        dismissButton = dismissButtonContent?.let { dismissButtonContent ->
            {
                MainDatePickerButton(
                    onClick = onDismissRequest,
                    content = dismissButtonContent,
                )
            }
        },
        shape = DatePickerDefaults.shape,
        tonalElevation = DatePickerDefaults.TonalElevation,
        colors = DatePickerDefaults.colors(),
        properties,
        content,
    )
}

@Composable
fun MainDatePickerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick,
        modifier,
        content = content,
    )
}

@Composable
fun MainDatePicker(
    state: DatePickerState,
    modifier: Modifier = Modifier,
    dateFormatter: DatePickerFormatter = remember { DatePickerDefaults.dateFormatter() },
    colors: DatePickerColors = DatePickerDefaults.colors(),
    title: (@Composable () -> Unit)? = {
        MainDatePickerTitle(
            text = "Select Date",
            modifier = Modifier.padding(PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp)),
            color = colors.titleContentColor,
        )
    },
    headline: (@Composable () -> Unit)? = {
        DatePickerDefaults.DatePickerHeadline(
            selectedDateMillis = state.selectedDateMillis,
            displayMode = state.displayMode,
            dateFormatter = dateFormatter,
            modifier = Modifier.padding(PaddingValues(start = 24.dp, end = 12.dp, bottom = 12.dp)),
            contentColor = colors.headlineContentColor,
        )
    },
    showModeToggle: Boolean = false,
    focusRequester: FocusRequester? = remember { FocusRequester() },
) {
    DatePicker(
        state,
        modifier,
        dateFormatter,
        colors,
        title,
        headline,
        showModeToggle,
        focusRequester,
    )
}

@Composable
fun MainDatePickerTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = DatePickerDefaults.colors().titleContentColor,
) {
    Text(
        text,
        modifier.padding(PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp)),
        color = color,
    )
}