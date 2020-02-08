package save

import network.UserAccount
import util.save.SaveInput
import util.save.SaveableFactory
import java.io.IOException

class UserAccountSaveableFactory : SaveableFactory<UserAccount> {
    @Throws(IOException::class)
    override fun load(input: SaveInput): UserAccount {
        return UserAccount(
                input.readInt(),
                input.readString(),
                input.readPassword())
    }

//    @Throws(IOException::class)
//    override fun storeState(output: SaveOutput) {
//        output.writeInt(id)
//        output.writeString(username)
//        output.writePassword(password)
//    }
}
