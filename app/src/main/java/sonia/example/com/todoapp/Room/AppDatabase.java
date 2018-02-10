package sonia.example.com.todoapp.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import sonia.example.com.todoapp.model.ToDo;

/**
 * Created by genora-sonia on 10/2/18.
 */

@Database(entities = {ToDo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TodoDao toDoDao();
}
