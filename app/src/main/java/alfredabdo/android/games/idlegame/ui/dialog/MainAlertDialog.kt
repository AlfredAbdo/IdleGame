package alfredabdo.android.games.idlegame.ui.dialog

import alfredabdo.android.games.idlegame.ui.action.MainButton
import alfredabdo.android.games.idlegame.ui.action.MainTextButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy

@Composable
fun MainAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = MainAlertDialogDefaults.dialogProperties(),
) {
    AlertDialog(
        onDismissRequest,
        confirmButton,
        modifier,
        dismissButton,
        icon,
        title,
        text,
        shape,
        containerColor,
        iconContentColor,
        titleContentColor,
        textContentColor,
        tonalElevation,
        properties,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = MainAlertDialogDefaults.dialogProperties(),
    content: @Composable () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest,
        modifier,
        properties,
        content,
    )
}

object MainAlertDialogDefaults {

    fun dialogProperties(
        dismissOnBackPress: Boolean = true,
        dismissOnClickOutside: Boolean = true,
        securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
        usePlatformDefaultWidth: Boolean = true,
        decorFitsSystemWindows: Boolean = true,
        windowTitle: String = "",
    ) = DialogProperties(
        dismissOnBackPress,
        dismissOnClickOutside,
        securePolicy,
        usePlatformDefaultWidth,
        decorFitsSystemWindows,
        windowTitle,
    )

    @Composable
    fun TitleText(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        autoSize: TextAutoSize? = null,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontStyle: FontStyle? = null,
        fontWeight: FontWeight? = null,
        fontFamily: FontFamily? = null,
        letterSpacing: TextUnit = TextUnit.Unspecified,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        lineHeight: TextUnit = TextUnit.Unspecified,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
        onTextLayout: ((TextLayoutResult) -> Unit)? = null,
        style: TextStyle = MaterialTheme.typography.headlineSmall,
    ) {
        BodyText(
            text,
            modifier,
            color,
            autoSize,
            fontSize,
            fontStyle,
            fontWeight,
            fontFamily,
            letterSpacing,
            textDecoration,
            textAlign,
            lineHeight,
            overflow,
            softWrap,
            maxLines,
            minLines,
            onTextLayout,
            style,
        )
    }

    @Composable
    fun BodyText(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        autoSize: TextAutoSize? = null,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontStyle: FontStyle? = null,
        fontWeight: FontWeight? = null,
        fontFamily: FontFamily? = null,
        letterSpacing: TextUnit = TextUnit.Unspecified,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        lineHeight: TextUnit = TextUnit.Unspecified,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
        onTextLayout: ((TextLayoutResult) -> Unit)? = null,
        style: TextStyle = MaterialTheme.typography.bodyMedium,
    ) {
        Text(
            text,
            modifier,
            color,
            autoSize,
            fontSize,
            fontStyle,
            fontWeight,
            fontFamily,
            letterSpacing,
            textDecoration,
            textAlign,
            lineHeight,
            overflow,
            softWrap,
            maxLines,
            minLines,
            onTextLayout,
            style,
        )
    }

    @Composable
    fun ConfirmButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shape: Shape = ButtonDefaults.textShape,
        colors: ButtonColors = ButtonDefaults.textButtonColors(),
        elevation: ButtonElevation? = null,
        border: BorderStroke? = null,
        contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
        interactionSource: MutableInteractionSource? = null,
        content: @Composable RowScope.() -> Unit,
    ) {
        MainButton(
            onClick,
            modifier,
            enabled,
            shape,
            colors,
            elevation,
            border,
            contentPadding,
            interactionSource,
            content,
        )
    }

    @Composable
    fun DismissButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shape: Shape = ButtonDefaults.textShape,
        colors: ButtonColors = ButtonDefaults.textButtonColors(),
        elevation: ButtonElevation? = null,
        border: BorderStroke? = null,
        contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
        interactionSource: MutableInteractionSource? = null,
        content: @Composable RowScope.() -> Unit,
    ) {
        MainTextButton(
            onClick,
            modifier,
            enabled,
            shape,
            colors,
            elevation,
            border,
            contentPadding,
            interactionSource,
            content,
        )
    }
}