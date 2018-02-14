package sonia.example.com.todoapp.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sonia.example.com.todoapp.room.AppDatabase;
import sonia.example.com.todoapp.model.ToDo;

/**
 * Created by genora-sonia on 13/2/18.
 */

public class ToDoViewModel extends AndroidViewModel {

    private LiveData<List<ToDo>> toDoMutableList = new MutableLiveData<>();

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        toDoMutableList = AppDatabase.getInstance(application.getApplicationContext()).toDoDao().getAllTasks();
    }

    public LiveData<List<ToDo>> getAllTasksList() {
        return toDoMutableList;
    }

    public void deleteTask(Activity activity, ToDo toDo) {
        DeleteATaskAsync deleteATaskAsync = new DeleteATaskAsync(activity, toDo);
        deleteATaskAsync.execute();
    }

    public void addOrEditTask(Activity activity, ToDo toDo, boolean isEditType) {
        AddOrEditTaskAsync addOrEditTaskAsync = new AddOrEditTaskAsync(activity, toDo,
                isEditType);
        addOrEditTaskAsync.execute();
    }

    private class DeleteATaskAsync extends AsyncTask<Void, Void, Void> {

        private ToDo toDo;
        private ProgressDialog progressDialog;
        private Activity activity;

        public DeleteATaskAsync(Activity activity, ToDo toDo) {
            this.activity = activity;
            this.toDo = toDo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait, Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase
                    .getInstance(activity)
                    .toDoDao()
                    .deleteANewTask(toDo);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    private class AddOrEditTaskAsync extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private ToDo toDo;
        private boolean isEditType;
        private Activity activity;

        public AddOrEditTaskAsync(Activity activity, ToDo toDo,
                                  boolean isEditType) {
            this.activity = activity;
            this.toDo = toDo;
            this.isEditType = isEditType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait, Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isEditType) {
                AppDatabase
                        .getInstance(activity)
                        .toDoDao()
                        .updateNewTask(toDo);
            } else {
                AppDatabase
                        .getInstance(activity)
                        .toDoDao()
                        .addANewTask(toDo);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
