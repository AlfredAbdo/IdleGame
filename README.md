# IdleGame

This is an example of an idle game implemented using Compose for Android with Clean Architecture.

## Using IdleGameServer

If you wish for this game to communicate with a server (and open the "login" system), you can run the server using the
repo hosted in; [IdleGameServer](https://github.com/AlfredAbdo/IdleGameServer). You then need to update the below values
in the app module's build.gradle.kts:
```kotlin
buildConfigField("boolean", "SHOULD_CONNECT_TO_SERVER", "false")
buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8080/api/\"")

//==>

buildConfigField("boolean", "SHOULD_CONNECT_TO_SERVER", "true")
//you need to change the host if not running on an Android emulator and the server running on localhost;
// the default is set to run properly on an Android emulator, reading from the server running on the
// same machine using localhost. 
buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8080/api/\"")
```

### P.S.:

The values used are not carefully thought out, as I came up with these very quickly to make and test the code of the
game. I also did very few test runs to come up with a decent balance for the costs and upgrade multipliers, as the focus
was to make the game work.
