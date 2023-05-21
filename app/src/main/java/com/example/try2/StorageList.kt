package com.example.try2

interface StorageList {
    fun allFiles() : ArrayList<ListItem>
    fun searchFilesByText(sub : String) : ArrayList<ListItem>
    fun searchFileByID(index : Int): ListItem?
    fun saveFile(index : Int, title : String, text : String)
    fun deleteFile(el : ListItem)
}