package alfredabdo.android.games.idlegame.features.main

import alfredabdo.android.games.idlegame.base.BaseActivity
import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import android.os.Bundle
import androidx.activity.compose.setContent

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                ProvidesDefaultAppViewModelFactory {
                    MainPage()
                }
            }
        }
    }
}