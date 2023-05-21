package com.example.try2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewNote : AppCompatActivity() {

    companion object {
        const val TYPE = "type"
        const val INDEX = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        supportActionBar?.hide()

        val notes = if (intent.getBooleanExtra(TYPE, false))
            FileList(this.filesDir.toString() + "/notes")
        else
            SQLiteList(this)

        var id = intent.getIntExtra(INDEX, -1)
        if (id == -1)
            do {
                id++
            }
            while (notes.searchFileByID(id) != null)
        val curNote = if (notes.searchFileByID(id) == null)
            ListItem(id, " ", " ")
        else
            notes.searchFileByID(id)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        etTitle.setText(curNote!!.handle.toCharArray(), 0, curNote.handle.toCharArray().size - 1)
        val etMultiLines = findViewById<LinedEditText>(R.id.etMultilines)
        etMultiLines.setText(curNote.textPreview.toCharArray(), 0, curNote.textPreview.toCharArray().size - 1)

        val btn = findViewById<Button>(R.id.buttonSave)
        btn.setOnClickListener {
            notes.saveFile(curNote.id, etTitle.text.toString(), etMultiLines.text.toString())
            this.finish()
        }
    }
}