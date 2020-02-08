package network

import save.UserAccountSaveableFactory
import security.Password
import util.save.Saveable

class UserAccount(val id: Int, val username: String, val password: Password) : Saveable {
    constructor(id: Int, username: String, plainPassword: String) : this(id, username, Password(plainPassword))

    companion object Factory : UserAccountSaveableFactory()
}
