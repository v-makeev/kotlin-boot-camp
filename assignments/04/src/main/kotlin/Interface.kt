class Interface {
    companion object {
        fun welcome() = println("Welcome to Bulls and Cows game!")

        fun offer() = Dictionary.getWord().also { println("I offered a ${it.length}-letter word, your guess?") }

        fun bullsAndCows(word: String, playerWord: String) =
                println("Bulls: ${Mechanics.getBulls(word, playerWord)}\n" +
                        "Cows: ${Mechanics.getCows(word, playerWord)}\n")

        fun checkWordInput(s: String?, hiddenWord: String): Boolean =
                (s != null) && s.all { !it.isDigit() } && s.length == hiddenWord.length

        private fun checkYNInput(s: String?): Boolean = if (s == null) false else when (s.toLowerCase()) {
            "yes", "y", "no", "n" -> true
            else -> false
        }

        fun tryAgain(): String? {
            println("Incorrect input! Try again:")
            return readLine()
        }

        fun win(turns: Int) = println("You won on turn $turns!")

        fun playAgain(): Boolean {
            println("Wanna play again? Y/N")
            var s = readLine()
            while (!checkYNInput(s)) {
                if (s == null)
                    System.exit(0)
                println("Y/N?").also { s = readLine() }
            }
            return when (s!!.toLowerCase()) {
                "y", "yes" -> true
                else -> false
            }
        }

        fun lose() = println("\nYou lost :(")
    }
}