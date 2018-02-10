package sonia.example.com.todoapp.activity;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sonia.example.com.todoapp.R;
import sonia.example.com.todoapp.adapter.ToDoAdapter;
import sonia.example.com.todoapp.interfaces.ToDoCallBack;
import sonia.example.com.todoapp.model.ToDo;

public class MainActivity extends AppCompatActivity implements ToDoCallBack {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.add_task)
    FloatingActionButton addTask;
    RecyclerView.LayoutManager layoutManager;
    ToDoAdapter toDoAdapter;
    ArrayList<ToDo> toDoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toDoArrayList.clear();

        addTasks();

        toDoAdapter = new ToDoAdapter(this, toDoArrayList, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(toDoAdapter);

        addTask.setOnClickListener(view ->
                addTaskDialog()
        );
    }

    private void addTaskDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_task_layout, null);
        dialogBuilder.setView(dialogView);

        EditText titleEt = dialogView.findViewById(R.id.title_et);
        EditText taskContentEt = dialogView.findViewById(R.id.task_content_et);
        Button addTaskBtn = dialogView.findViewById(R.id.add_task_btn);

        AlertDialog b = dialogBuilder.create();
        b.show();

        addTaskBtn.setOnClickListener(view -> {
            b.dismiss();
        });


    }

    private void addTasks() {
        ToDo toDo;

        toDo = new ToDo();
        toDo.setHeader(true);
        toDoArrayList.add(toDo);

        toDo = new ToDo(1, "Chores", "Pick Up Laundry", false, false);
        toDoArrayList.add(toDo);

        toDo = new ToDo(2, "Homework", "Finish Maths Paper", false, false);
        toDoArrayList.add(toDo);

        toDo = new ToDo(3, "Lunch", "Buy Olive Oil\nBasil\nLinguini", false, false);
        toDoArrayList.add(toDo);

        toDo = new ToDo(4, "Dog", "Take dog to the vet", true, false);
        toDoArrayList.add(toDo);
    }

    @Override
    public void editTask(int position) {
        Log.e(TAG, "editTask: " + position);
    }

    @Override
    public void deleteTask(int position) {
        Log.e(TAG, "deleteTask: " + position);
    }
}
