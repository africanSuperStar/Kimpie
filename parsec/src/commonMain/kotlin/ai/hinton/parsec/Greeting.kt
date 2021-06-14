package ai.hinton.parsec

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}