package signature

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

const val HEIGHT_TAG = 15

fun String.center(size: Int): String {
    if (this.length >= size) {
        return this
    }
    val numSpaces: Int = size - this.length
    return " ".repeat(numSpaces / 2) + this + " ".repeat(numSpaces / 2  + numSpaces % 2)
}

fun String.addToSides(sideString: String): String {
    return "$sideString$this$sideString"
}

data class Letter(var char: Char, var height: Int, var width: Int, var body: MutableList<String>) {

}

fun buildAlphabet(fileName: String): MutableList<Letter> {
    val alphabet: MutableList<Letter>

    BufferedReader(FileReader(fileName)).use {
        reader ->
        var i = 0
        var k = 0
        val (heightString, numLettersString) = reader.readLine().split(" ")
        val height = heightString.toInt()
        val numLetters = numLettersString.toInt()
        alphabet = MutableList<Letter>(numLetters.toInt()) { Letter(' ', height, 0, mutableListOf<String>()) }
        reader.forEachLine {
            line ->
                val (firstString, secondString) = line.split(" ")
                if (firstString.toCharArray().isNotEmpty() && secondString.toIntOrNull() != null) {
                    i = 0
                    val char = firstString.toCharArray().first()
                    val width = secondString.toInt()
                    alphabet[k].char = char
                    alphabet[k].width = width
                } else {
                    alphabet[k].body.add(line)
                    i++
                    if (i == height) k++
                }
        }
    }
    return alphabet
}

fun generateTag(nameSurname: String, status: String): MutableList<String> {

    val alphabetRoman = buildAlphabet("C:\\Users\\Davide\\IdeaProjects\\ASCII Text Signature\\ASCII Text Signature\\task\\src\\roman.txt")
    val alphabetMedium = buildAlphabet("C:\\Users\\Davide\\IdeaProjects\\ASCII Text Signature\\ASCII Text Signature\\task\\src\\medium.txt")

    var rowsTag: MutableList<String> = MutableList<String>(HEIGHT_TAG) { "" }

    // Build the row with letters
    val currInd = 1
    for (letter in nameSurname) {
        for (j in 0 until alphabetRoman[0].height) {
            rowsTag[currInd + j] += alphabetRoman.find { it.char == letter }?.body?.get(j) ?: "          "
        }
    }

    // Add Status
    val currIndStatus = 11
    for (letter in status) {
        for (j in 0 until alphabetMedium[0].height) {
            rowsTag[currIndStatus + j] += alphabetMedium.find { it.char == letter }?.body?.get(j) ?: "     "
        }
    }


    // Complete lines
    if (rowsTag[currInd].length > rowsTag[currIndStatus].length) {
        for (j in 0 until alphabetRoman[0].height) {
            rowsTag[currInd + j] = rowsTag[currInd + j].addToSides("  ")
        }
        for (j in 0 until alphabetMedium[0].height) {
            rowsTag[currIndStatus + j] =  rowsTag[currIndStatus + j].center(rowsTag[currInd].length)
        }
    } else {
        for (j in 0 until alphabetMedium[0].height) {
            rowsTag[currIndStatus + j] =  rowsTag[currIndStatus + j].addToSides("  ")
        }
        for (j in 0 until alphabetRoman[0].height) {
            rowsTag[currInd + j] = rowsTag[currInd + j].center(rowsTag[currIndStatus].length)
        }
    }

    for (j in currInd until rowsTag.lastIndex) {
        rowsTag[j] = rowsTag[j].addToSides("88")
    }

    rowsTag[0] = "8".repeat(rowsTag[currInd].length)
    rowsTag[rowsTag.lastIndex] = "8".repeat(rowsTag[currInd].length)
    return rowsTag
}

fun printTag(tag: MutableList<String>) {
    for (line in tag) {
        println(line)
    }
}

fun main() {
    // Build Alphabets
    //val path = System.getProperty("user.dir")
    print("Enter name and surname: ")
    val nameSurname = readln()
    print("Enter person's status: ")
    val status = readln()



    val tag = generateTag(nameSurname, status)
    printTag(tag)

}
