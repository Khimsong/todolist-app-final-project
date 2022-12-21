package com.example.todolisttutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolisttutorial.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), TaskItemClickListener
{
    private lateinit var button : Button
    private lateinit var builder : AlertDialog.Builder
    private lateinit var binding: ActivityMainBinding
    //private lateinit var taskViewModel: TaskViewModel
    private val taskViewModel: TaskViewModel by viewModels {
        TaskItemModelFactory((application as TodoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        binding.newTaskButton.setOnClickListener {
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }

        setRecyclerView()


    }

    private fun setRecyclerView()
    {
        val mainActivity = this
        taskViewModel.taskItems.observe(this){
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)
            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem)
    {
        NewTaskSheet(taskItem).show(supportFragmentManager,"newTaskTag")
    }

    override fun deleteTaskItem(taskItem: TaskItem)
    {
        builder = AlertDialog.Builder(this)
        builder.setTitle(HtmlCompat.fromHtml("<font color='red'>DELETE</font>",HtmlCompat.FROM_HTML_MODE_LEGACY))
            .setMessage("Please confirm to delete this todo from the list.")
            .setCancelable(true)
            .setPositiveButton(HtmlCompat.fromHtml("<font color='red'>CONFIRM</font>",HtmlCompat.FROM_HTML_MODE_LEGACY)){dailogInterface,it ->
                taskViewModel.deleteTaskItem(taskItem)
            }
            .setNegativeButton(HtmlCompat.fromHtml("<font color='silver'>CANCEL</font>",HtmlCompat.FROM_HTML_MODE_LEGACY)){dailogInterface,it ->
                dailogInterface.cancel()
            }
            .show()

    }

    override fun completeTaskItem(taskItem: TaskItem)
    {
        taskViewModel.setCompleted(taskItem)
    }


}







