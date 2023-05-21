package com.example.try2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var notes : StorageList
    private lateinit var recView : RecyclerView
    private lateinit var adapter : Adapter
    private lateinit var searcher : SearchView
    private lateinit var emptyText : TextView
    private lateinit var storageItem : MenuItem
    private lateinit var dbItem : MenuItem

    private val KEY_COUNT = "COUNT"
    var menuStorage : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null)
            menuStorage = savedInstanceState.getBoolean(KEY_COUNT, true)
        notes = if (menuStorage)
            FileList(this.filesDir.toString() + "/notes")
        else
            SQLiteList(this)

        recView = findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(this)
        emptyText = findViewById(R.id.textView)
        adapter = Adapter(notes.allFiles(), this)
        recView.adapter = adapter
        emptyText.isVisible = adapter.listArray.isEmpty()

        searcher = findViewById(R.id.searchView)
        searcher.setOnQueryTextListener(this)

        val btnDel = findViewById<Button>(R.id.buttonDel)
        btnDel.setOnClickListener {
            adapter.selectList.forEach{ el -> selectDel(el) }
            btnDel.visibility = View.INVISIBLE
        }

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            val noteIntent = Intent(this, NewNote::class.java)
            noteIntent.putExtra(NewNote.TYPE, menuStorage)
            startActivity(noteIntent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_COUNT, menuStorage)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        onQueryTextChange(searcher.query.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun selectDel(el : ListItem) {
        notes.deleteFile(el)
        adapter.listArray.remove(el)
        adapter.selectList.remove(el)
        adapter.notifyDataSetChanged()
        emptyText.isVisible = adapter.listArray.isEmpty()
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        searcher.clearFocus()
        return false
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onQueryTextChange(p0: String?): Boolean {
        if (p0 != null)
            adapter.listArray = notes.searchFilesByText(p0)
        else
            adapter.listArray = notes.allFiles()
        adapter.notifyDataSetChanged()
        emptyText.isVisible = adapter.listArray.isEmpty()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        storageItem = menu.findItem(R.id.action_stor)
        dbItem = menu.findItem(R.id.action_db)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_stor && !menuStorage) {
            notes = FileList(this.filesDir.toString() + "/notes")
            adapter = Adapter(notes.allFiles(), this)
            recView.adapter = adapter
            menuStorage = true
            Toast.makeText(this, "Storage notes", Toast.LENGTH_LONG).show()
        } else if (item.itemId == R.id.action_db && menuStorage){
            notes = SQLiteList(this)
            adapter = Adapter(notes.allFiles(), this)
            recView.adapter = adapter
            menuStorage = false
            Toast.makeText(this, "Data base notes", Toast.LENGTH_LONG).show()
        }
        storageItem.isChecked = menuStorage
        dbItem.isChecked = !menuStorage
        return true
    }
}