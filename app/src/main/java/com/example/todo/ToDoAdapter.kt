package com.example.todo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*

class ToDoAdapter(var todos: MutableList<DetailTodo>, val context: Context) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
    //private var todos: MutableList<DetailTodo> = mutableListOf()


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val tilteTodo = view.title_item
        var isCompleteholder = view.check_isComplete
        val deleteitem = view.item_delete
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       // val holder = LayoutInflater.from(parent.context).inflate(R.layout.item_todo,parent,false)

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo,parent,false))
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.bind(todos[position])
        //lấy tên todo
        holder?.tilteTodo?.text = todos.get(position).title

        // kiểm tra hoàn thành chưa
        if (todos.get(position).isComplete == false)
        {
            holder.isCompleteholder.visibility = View.INVISIBLE
        }

        // xóa một todo
        holder.deleteitem.setOnClickListener {
            (context as MainActivity).DeleteOneItem(todos.get(position))
        }

        holder.itemView.setOnClickListener {
            var currentTodo = DetailTodo()
            currentTodo.id = todos.get(position).id
            currentTodo.title = todos.get(position).title
            currentTodo.content = todos.get(position).content
            currentTodo.isComplete = todos.get(position).isComplete

            val intent = Intent((context as MainActivity),EditActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable("edit todo", currentTodo)
            intent.putExtra("package", bundle)
            (context as MainActivity).startActivityForResult(intent,MainActivity.REQUEST_EDIT)
        }
    }
}