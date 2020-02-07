package network

class LobbyEntry(
        var userId: Int,
        var username: String?,
        var isReadyForGame: Boolean,
        var chosenTeamId: Int) {

    internal constructor() :
            this(-1, null, false, 0)

    override fun toString(): String {
        return String.format("#%d %s (%s)", userId, username, if (isReadyForGame) "ready" else "waiting")
    }

    companion object {
        const val ROOMLESS = -1
    }
}
