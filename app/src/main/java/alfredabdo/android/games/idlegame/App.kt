package alfredabdo.android.games.idlegame

import alfredabdo.android.games.idlegame.base.inject.Injections
import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injections.init(this)

        //...
    }
}