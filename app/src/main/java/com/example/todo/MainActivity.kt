package com.example.todo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.item_todo.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var toDoAdapter: ToDoAdapter
    lateinit var db : DataBaseHander
    var listToDos = mutableListOf<DetailTodo>()

    companion object {
        val TAG: Int = 1
        val REQUEST_EDIT: Int = 0x9345
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)

        //tạo list dữ liệu
        var context = this
        db = DataBaseHander(context)
        listToDos = db.getAllTodo()

        //initData()

        // tạo adapter cho recycler view
        recyclerview.adapter = ToDoAdapter(listToDos,this)

        recyclerview.layoutManager = LinearLayoutManager(this)
        
        //sự kiện click trên recycler view


        fab.setOnClickListener(this)
        recyclerview.adapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_EDIT && resultCode == MainActivity.TAG){
            var bundle = data?.getBundleExtra("package return")
            var result = bundle?.getSerializable("todo return") as DetailTodo

            updateTodo(result)
            db.updateData(result)
            recyclerview.adapter!!.notifyDataSetChanged()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottomappbar_menu,menu )
        return super.onCreateOptionsMenu(menu)
    }
    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                R.id.fab -> {
                    val dialog = BottomSheetDialog(this)
                    val view = layoutInflater.inflate(R.layout.fragment_bottom_sheet_dialog,null)

                    //xác nhận
                    val done = view.findViewById<Button>(R.id.Done)
                    val title = view.findViewById<EditText>(R.id.title_content_bottomSheet)
                    val content = view.findViewById<EditText>(R.id.content_bottomSheet)
                    done.setOnClickListener {
                        var todotitle: String = title.text.toString()
                        var todocontent: String = content.text.toString()
                        if(todotitle.isEmpty())
                        {
                            return@setOnClickListener
                        }else if (todocontent.isEmpty()){
                            return@setOnClickListener
                        }

                        val newtodo = DetailTodo()
                        newtodo.title = todotitle
                        newtodo.content = todocontent

                       // Toast.makeText(this,"${newtodo.title} ${newtodo.isComplete} ${newtodo.content}",Toast.LENGTH_LONG).show()
                        db.insertData(newtodo)
                        title.text.clear()
                        content.text.clear()

                        val todoGet = db.getOneData(newtodo.title)
                        listToDos.add(todoGet!!)

                        recyclerview.adapter!!.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    dialog.setContentView(view)
                    dialog.show()
                }
            }
        }
    }

    fun DeleteOneItem(todo: DetailTodo){
        listToDos.remove(todo)
        db.deleteData(todo)

        Log.e("AAA",todo.id.toString())
        recyclerview.adapter!!.notifyDataSetChanged()
    }

    fun updateTodo(newtodo: DetailTodo){
        for ((index,value) in listToDos.withIndex()){
            if (value.id == newtodo.id){
                listToDos.set(index,newtodo)

                recyclerview.adapter!!.notifyDataSetChanged()
                return
            }
        }
    }
}
