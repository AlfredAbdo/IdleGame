package alfredabdo.android.games.idlegame.data.exception

class AppException : Exception {

    var code: Int? = null
        private set


    constructor(message: String) : super(message)
    constructor(code: Int?, message: String) : super(message) {
        this.code = code
    }

    constructor(cause: Throwable) : super(cause)
    constructor(code: Int?, cause: Throwable) : super(cause) {
        this.code = code
    }

    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(code: Int?, message: String, cause: Throwable) : super(message, cause) {
        this.code = code
    }


    val isUnauthorized: Boolean get() = code == Codes.UNAUTHORIZED


    object Codes {
        const val UNKNOWN = -1
        const val NETWORK = -2
        const val UNAUTHORIZED = 401
    }
}