package com.example.try2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class Adapter (var listArray : ArrayList<ListItem>, private var context : Context)
    : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var selectList = ArrayList<ListItem>()

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvHandle)
        val textPreview: TextView = view.findViewById(R.id.tvContext)
        val chb: CheckBox = view.findViewById(R.id.checkBox)

        fun bind(listItem: ListItem, context: Context) {
            title.text = listItem.handle
            textPreview.text = listItem.textPreview
            chb.isVisible = false

            itemView.setOnClickListener {
                val activity = context as MainActivity
                val noteIntent = Intent(activity, NewNote::class.java)
                noteIntent.putExtra(NewNote.INDEX, listItem.id)
                noteIntent.putExtra(NewNote.TYPE, activity.menuStorage)
                activity.startActivity(noteIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =  LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listArray[position], context)

        holder.itemView.setOnLongClickListener {
            val activity = context as MainActivity
            val btn = activity.findViewById<Button>(R.id.buttonDel)

            if (holder.chb.visibility == View.GONE) {
                holder.chb.visibility = View.VISIBLE
                holder.chb.isChecked = true
                selectList.add(listArray[holder.adapterPosition])
                btn.visibility = View.VISIBLE
            } else {
                holder.chb.visibility = View.GONE
                holder.chb.isChecked = true
                selectList.remove(listArray[holder.adapterPosition])
                if (selectList.isEmpty())
                    btn.visibility = View.INVISIBLE
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return listArray.size
    }
}