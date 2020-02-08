package app

class UserNotFoundException(username: String) : RuntimeException("User '$username' not found")
