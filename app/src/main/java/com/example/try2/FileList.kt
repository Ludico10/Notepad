package com.example.try2

import java.io.File

class FileList(private val parent : String) : StorageList {

    override fun allFiles() : ArrayList<ListItem> {
        val list = ArrayList<ListItem>()
        File(parent).listFiles()
            ?.filter { item -> item.toString().endsWith(".txt") }
            ?.filter { item ->  parseFile(item) != null}
            ?.forEach { item -> list.add(parseFile(item)!!)}
        return list
    }

    override fun searchFilesByText(sub : String) : ArrayList<ListItem> {
        val list = ArrayList<ListItem>()
        File(parent).listFiles()
            ?.filter { item -> item.toString().endsWith(".txt") }
            ?.filter{ item -> item.readText().contains(sub, true)}
            ?.filter { item ->  parseFile(item) != null}
            ?.forEach { item -> list.add(parseFile(item)!!)}
        return list
    }

    override fun searchFileByID(index : Int): ListItem? {
        return try {
            val file = File(parent).listFiles()
                ?.first { item -> item.name == "$index.txt" }
            if (file != null) {
                val text = file.readText()
                ListItem(index, text.substringBefore("\n"), text.substringAfter("\n"))
            } else null
        } catch (e : Throwable) {
            null
        }
    }

    override fun saveFile(index : Int, title : String, text : String) {
        val file = File("$parent/$index.txt")
        file.writeText(title + '\n' + text)
    }

    override fun deleteFile(el : ListItem) {
        File(parent).listFiles()
            ?.filter { item -> item.name == el.id.toString() + ".txt" }
            ?.forEach { item -> item.delete() }
    }

    private fun parseFile(file : File) : ListItem? {
        return try {
            val index = file.name.substringBefore(".txt").toInt()
            val text = file.readLines()
            var prev = text[1]
            if (prev.length > 30)
                prev = prev.removeRange(30 until prev.length)
            ListItem(index, text[0], prev)
        } catch (e : Throwable){
            null
        }
    }
}