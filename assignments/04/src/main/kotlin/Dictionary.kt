import java.io.File
import java.util.Random

class Dictionary {
    companion object {
        val dict = File("src/task/dictionary.txt")
                .readText()
                .replace("\r", "")
                .split('\n')
        fun getWord() = dict[Random().nextInt(dict.size)]
    }
}