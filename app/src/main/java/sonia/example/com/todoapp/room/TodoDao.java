package sonia.example.com.todoapp.room;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import sonia.example.com.todoapp.model.ToDo;

/**
 * Created by genora-sonia on 10/2/18.
 */

@Dao
public interface TodoDao {

    @Query("SELECT * FROM ToDo")
    LiveData<List<ToDo>> getAllTasks();

    @Insert
    void addANewTask(ToDo toDo);

    @Update
    void updateNewTask(ToDo toDo);

    @Delete
    void deleteANewTask(ToDo toDo);

}
