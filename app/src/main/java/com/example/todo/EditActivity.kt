package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    private var _id: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val toolbar = findViewById(R.id.toolbar_edt) as Toolbar?
        setSupportActionBar(toolbar)

        // lấy giá trị vừa nhận đưa ra activity
        var intent = getIntent()
        var bundle: Bundle? = intent.getBundleExtra("package")
        var todo = bundle!!.getSerializable("edit todo") as DetailTodo

        //title_content_edt!!.setText(todo.title)
        _id = todo.id
        title_content_edt.text = todo.title.toEditable()
        content_edt.text = todo.content.toEditable()
        if(todo.isComplete == true){
            cb_iscomplete_edt.isChecked = true
        }else{
            cb_iscomplete_edt.isChecked = false
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_editaitem,menu )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        when(id){
            android.R.id.home ->{
                finish()
            }
            R.id.editItem_done ->{
                Log.e("BBB","good")
                var todo = DetailTodo()
                todo.id = _id
                todo.title = title_content_edt.text.toString()
                todo.content = content_edt.text.toString()
                todo.isComplete = cb_iscomplete_edt.isChecked

                val intent = Intent()
                var bundle = Bundle()
                bundle.putSerializable("todo return",todo)
                intent.putExtra("package return",bundle)
                setResult(MainActivity.TAG,intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)