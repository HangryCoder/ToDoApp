package sonia.example.com.todoapp.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import sonia.example.com.todoapp.Room.AppDatabase;
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
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toDoArrayList.clear();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait, Loading..");
        progressDialog.setCanceledOnTouchOutside(false);

        //addTasks();

        toDoAdapter = new ToDoAdapter(this, toDoArrayList, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(toDoAdapter);

        GetAllTasksAsync getAllTasksAsync = new GetAllTasksAsync(progressDialog);
        getAllTasksAsync.execute();

        addTask.setOnClickListener(view -> addTaskDialog(false, -1));
    }

    private void addTaskDialog(boolean isEditType, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_task_layout, null);
        dialogBuilder.setView(dialogView);

        EditText titleEt = dialogView.findViewById(R.id.title_et);
        EditText taskContentEt = dialogView.findViewById(R.id.task_content_et);
        Button addTaskBtn = dialogView.findViewById(R.id.add_task_btn);
        ToDo toDo = new ToDo();

        //If its -1 then there is no previous entry..
        //else add the title and content in the edit texts..
        if (position != -1) {
            titleEt.setText(toDoArrayList.get(position).getTitle());
            taskContentEt.setText(toDoArrayList.get(position).getContent());
            toDo.setId(toDoArrayList.get(position).getId());
        }

        AlertDialog b = dialogBuilder.create();
        b.show();

        addTaskBtn.setOnClickListener(view -> {
            b.dismiss();

            toDo.setTitle(titleEt.getText().toString());
            toDo.setContent(taskContentEt.getText().toString());
            toDo.setHeader(false);
            AddOrEditTaskAsync addNewTaskAsync = new AddOrEditTaskAsync(progressDialog, toDo, isEditType, position);
            addNewTaskAsync.execute();
        });


    }

    private void addTasks() {
        ToDo toDo;

        toDo = new ToDo();
        toDo.setHeader(true);
        toDoArrayList.add(toDo);

        AddOrEditTaskAsync addNewTaskAsync = new AddOrEditTaskAsync(progressDialog, toDo, false, -1);
        addNewTaskAsync.execute();

       /* toDo = new ToDo(1, "Chores", "Pick Up Laundry", false, false);
        toDoArrayList.add(toDo);

        toDo = new ToDo(2, "Homework", "Finish Maths Paper", false, false);
        toDoArrayList.add(toDo);

        toDo = new ToDo(3, "Lunch", "Buy Olive Oil\nBasil\nLinguini", false, false);
        toDoArrayList.add(toDo);

        toDo = new ToDo(4, "Dog", "Take dog to the vet", true, false);
        toDoArrayList.add(toDo);*/
    }

    @Override
    public void editTask(int position) {
        addTaskDialog(true, position);
    }

    @Override
    public void deleteTask(int position) {
        Log.e(TAG, "deleteTask: " + position);
        ToDo toDo = toDoArrayList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete " + toDo.getTitle());
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            DeleteATaskAsync deleteATaskAsync = new DeleteATaskAsync(progressDialog, toDo, position);
            deleteATaskAsync.execute();
        });
        builder.setNegativeButton("No", (dialogInterface, i) ->
                dialogInterface.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void markImportant(int position, boolean isChecked) {
        ToDo toDo = toDoArrayList.get(position);
        toDo.setImportant(isChecked);
        AddOrEditTaskAsync addNewTaskAsync = new AddOrEditTaskAsync(progressDialog, toDo, true, position);
        addNewTaskAsync.execute();
    }

    private class GetAllTasksAsync extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        public GetAllTasksAsync(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            toDoArrayList = (ArrayList<ToDo>) AppDatabase
                    .getInstance(getApplicationContext())
                    .toDoDao()
                    .getAllTasks();

            for (int i = 0; i < toDoArrayList.size(); i++) {
                Log.e(TAG, "toDoList " + toDoArrayList.get(i).getTitle());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();

            toDoAdapter = new ToDoAdapter(MainActivity.this, toDoArrayList, MainActivity.this);
            recyclerView.setAdapter(toDoAdapter);
        }
    }

    private class AddOrEditTaskAsync extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private ToDo toDo;
        private boolean isEditType;
        private int position;

        public AddOrEditTaskAsync(ProgressDialog progressDialog, ToDo toDo, boolean isEditType,
                                  int position) {
            this.progressDialog = progressDialog;
            this.toDo = toDo;
            this.isEditType = isEditType;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isEditType) {
                AppDatabase
                        .getInstance(getApplicationContext())
                        .toDoDao()
                        .updateNewTask(toDo);
                toDoArrayList.set(position, toDo);
            } else {
                AppDatabase
                        .getInstance(getApplicationContext())
                        .toDoDao()
                        .addANewTask(toDo);
                toDoArrayList.add(toDo);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
            if (isEditType) {
                toDoAdapter.notifyItemChanged(position);
            } else {
                toDoAdapter.notifyItemInserted(toDoArrayList.size() - 1);
                recyclerView.scrollToPosition(toDoArrayList.size() - 1);
            }
        }
    }

    private class DeleteATaskAsync extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private ToDo toDo;
        private int position;

        public DeleteATaskAsync(ProgressDialog progressDialog, ToDo toDo, int position) {
            this.progressDialog = progressDialog;
            this.toDo = toDo;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            AppDatabase
                    .getInstance(getApplicationContext())
                    .toDoDao()
                    .deleteANewTask(toDo);
            toDoArrayList.remove(toDo);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
            toDoAdapter.notifyItemRemoved(position);
        }
    }
}
