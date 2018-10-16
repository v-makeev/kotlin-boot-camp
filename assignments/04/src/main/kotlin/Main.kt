fun main(args: Array<String>) {
    Interface.welcome()
    var isPlaying = true
    while (isPlaying) {
        val hiddenWord = Interface.offer()
        for (x in (1..10)) {
            var playerWord = readLine()
            while (!Interface.checkWordInput(playerWord, hiddenWord)) {
                if (playerWord == null)
                    System.exit(0)
                playerWord = Interface.tryAgain()
            }
            if (playerWord.toString().toLowerCase() == hiddenWord) {
                Interface.win(x)
                break
            } else
                Interface.bullsAndCows(hiddenWord, playerWord.toString())
            if (x == 10) Interface.lose()
        }
        isPlaying = Interface.playAgain()
    }
}
