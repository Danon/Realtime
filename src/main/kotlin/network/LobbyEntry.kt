package network

class LobbyEntry(
        var userId: Int,
        var username: String?,
        var isReadyForGame: Boolean,
        var chosenTeamId: Int) {

    @Suppress("unused")
    // Kryonet deserialization
    internal constructor() :
            this(0, null, false, -1)

    override fun toString(): String {
        return String.format("#%d %s (%s)", userId, username, if (isReadyForGame) "ready" else "waiting")
    }

    companion object {
        const val ROOMLESS = -1
    }
}
