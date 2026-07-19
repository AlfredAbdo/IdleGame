package alfredabdo.android.games.idlegame.ui.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    sheetGesturesEnabled: Boolean = true,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = /*{ MainBottomSheetDefaults.dragHandle }*/null,
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = MainBottomSheetDefaults.modalBottomSheetProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest,
        modifier,
        sheetState,
        sheetMaxWidth,
        sheetGesturesEnabled,
        shape,
        containerColor,
        contentColor,
        tonalElevation,
        scrimColor,
        dragHandle,
        contentWindowInsets,
        properties,
        content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
object MainBottomSheetDefaults {

    val dragHandle
        @Composable
        get() = BottomSheetDefaults.DragHandle()


    fun modalBottomSheetProperties(
        securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
        shouldDismissOnBackPress: Boolean = true,
        shouldDismissOnClickOutside: Boolean = true,
    ): ModalBottomSheetProperties = ModalBottomSheetProperties(
        securePolicy,
        shouldDismissOnBackPress,
        shouldDismissOnClickOutside,
    )

    fun modalBottomSheetProperties(
        isAppearanceLightStatusBars: Boolean,
        isAppearanceLightNavigationBars: Boolean,
        securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
        shouldDismissOnBackPress: Boolean = true,
        shouldDismissOnClickOutside: Boolean = true,
    ): ModalBottomSheetProperties = ModalBottomSheetProperties(
        isAppearanceLightStatusBars,
        isAppearanceLightNavigationBars,
        securePolicy,
        shouldDismissOnBackPress,
        shouldDismissOnClickOutside,
    )
}