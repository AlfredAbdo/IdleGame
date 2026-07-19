package alfredabdo.android.games.idlegame.ui.input.date

import alfredabdo.android.games.idlegame.ui.input.MainTextField
import alfredabdo.android.games.idlegame.ui.input.MainTextFieldDefaults
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun MainDateTextField(
    value: LocalDate?,
    onValueChanged: (value: LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Attached(),
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    shape: Shape = MainTextFieldDefaults.shape,
    colors: TextFieldColors = MainTextFieldDefaults.colors(),
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding(),
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    scrollState: ScrollState = rememberScrollState(),
    textFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"),
    datePickerTitle: String = "Select Date",
    descriptionFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy"),
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
) {
    var isShowingPicker: Boolean by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState()

    LaunchedEffect(value) {
        textFieldState.setTextAndPlaceCursorAtEnd(
            value?.let { textFormatter.format(it) }.orEmpty()
        )
    }


    MainTextField(
        textFieldState,
        modifier,
        enabled,
        readOnly = true,
        textStyle,
        labelPosition,
        label,
        placeholder,
        leadingIcon,
        trailingIcon,
        prefix,
        suffix,
        supportingText,
        isError,
        keyboardOptions = KeyboardOptions(
            showKeyboardOnFocus = false,
            keyboardType = KeyboardType.Unspecified,
        ),
        onTextLayout = onTextLayout,
        scrollState = scrollState,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
        interactionSource = remember { MutableInteractionSource() }
            .apply {
                LaunchedEffect(this) {
                    interactions.collect {
                        if (it is PressInteraction.Release) {
                            isShowingPicker = true
                        }
                    }
                }
            },
    )

    if (isShowingPicker) {
        val datePickerFormatter = remember {
            object : DatePickerFormatter {
                override fun formatMonthYear(monthMillis: Long?, locale: CalendarLocale): String? {
                    return monthMillis?.let {
                        textFormatter.format(Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC))
                    }
                }

                override fun formatDate(
                    dateMillis: Long?,
                    locale: CalendarLocale,
                    forContentDescription: Boolean
                ): String? {
                    return dateMillis?.let {
                        (if (forContentDescription) descriptionFormatter else textFormatter)
                            .format(Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC))
                    }
                }
            }
        }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = (value ?: LocalDate.now())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
            selectableDates = selectableDates,
        )

        MainDatePickerDialog(
            onDismissRequest = { isShowingPicker = false },
            confirmButtonContent = { Text("Select Date") },
            onConfirmButtonClick = {
                isShowingPicker = false

                val selection = datePickerState.selectedDateMillis
                    ?.let { Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC).toLocalDate() }
                onValueChanged(selection)
            },
            dismissButtonContent = { Text("Cancel") },
        ) {
            MainDatePicker(
                datePickerState,
                dateFormatter = datePickerFormatter,
                title = { MainDatePickerTitle(datePickerTitle) },
            )
        }
    }
}