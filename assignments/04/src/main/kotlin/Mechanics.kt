class Mechanics {
    companion object {
        fun getCows(hiddenWord: String, playerWord: String) = hiddenWord.fold(0) { sum, letter ->
            sum + if (letter in playerWord) {
                playerWord.replaceFirst(letter.toString(), "")
                1
            } else 0
        } - getBulls(hiddenWord, playerWord)

        fun getBulls(hiddenWord: String, playerWord: String) =
                hiddenWord.foldIndexed(0) { index, acc, c ->
                    if (playerWord[index] == c) acc + 1 else acc
                }
    }
}