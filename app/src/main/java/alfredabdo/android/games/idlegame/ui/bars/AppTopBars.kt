package alfredabdo.android.games.idlegame.ui.bars

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.ui.icons.BackIcon
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title,
        modifier,
        navigationIcon,
        actions,
        expandedHeight = expandedHeight,
        windowInsets = windowInsets,
        colors = TopAppBarDefaults.topAppBarColors(),
        scrollBehavior = scrollBehavior,
    )
}


@Composable
fun NavBackIcon(
    onClick: () -> Unit,
) {
    IconButton(onClick) {
        Icon(
            BackIcon,
            contentDescription = stringResource(R.string.back),
        )
    }
}