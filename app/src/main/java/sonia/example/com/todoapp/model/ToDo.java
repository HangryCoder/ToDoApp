package sonia.example.com.todoapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by genora-sonia on 10/2/18.
 */

@Entity
public class ToDo {

    @PrimaryKey(autoGenerate = true)
    int id;
    String title, content;
    boolean isImportant = false, isHeader = false;

    public ToDo() {

    }

    public ToDo(int id, String title, String content, boolean isImportant, boolean isHeader) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isImportant = isImportant;
        this.isHeader = isHeader;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
