package app

class UserNotFoundException : RuntimeException {
    constructor(username: String) :
            super("User '$username' not found")
}
