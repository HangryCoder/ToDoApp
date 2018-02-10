package sonia.example.com.todoapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hanks.library.AnimateCheckBox;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sonia.example.com.todoapp.activity.MainActivity;
import sonia.example.com.todoapp.interfaces.ToDoCallBack;
import sonia.example.com.todoapp.model.ToDo;
import sonia.example.com.todoapp.R;

/**
 * Created by genora-sonia on 10/2/18.
 */

public class ToDoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_ITEM = 0;
    private static final int TASK_ITEM = 1;
    private Context context;
    private ArrayList<ToDo> toDoArrayList = new ArrayList<>();
    private ToDoCallBack toDoCallBack;

    public ToDoAdapter(MainActivity mainActivity, ArrayList<ToDo> toDoArrayList, ToDoCallBack toDoCallBack) {
        this.context = mainActivity;
        this.toDoArrayList = toDoArrayList;
        this.toDoCallBack = toDoCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_ITEM:
                view = LayoutInflater.from(context).inflate(R.layout.header_layout, parent, false);
                return new HeaderHolder(view);
            case TASK_ITEM:
                view = LayoutInflater.from(context).inflate(R.layout.to_do_task_item, parent, false);
                return new ToDoTaskHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ToDo toDo = toDoArrayList.get(position);

        if (holder instanceof ToDoTaskHolder) {
            ((ToDoTaskHolder) holder).todoTitle.setText(toDo.getTitle());
            ((ToDoTaskHolder) holder).todoTaskContent.setText(toDo.getContent());
            if (toDo.isImportant()) {
                ((ToDoTaskHolder) holder).checkBox.setChecked(true);
            } else {
                ((ToDoTaskHolder) holder).checkBox.setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return toDoArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ToDo toDo = toDoArrayList.get(position);
        if (toDo.isHeader()) {
            return HEADER_ITEM;
        } else {
            return TASK_ITEM;
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public class ToDoTaskHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.todo_title)
        TextView todoTitle;
        @BindView(R.id.todo_main_lay)
        CardView todoMainLay;
        @BindView(R.id.todo_task_content)
        TextView todoTaskContent;
        @BindView(R.id.checkbox)
        AnimateCheckBox checkBox;

        public ToDoTaskHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            todoMainLay.setOnClickListener(view -> {
                if (toDoCallBack != null) {
                    toDoCallBack.editTask(getAdapterPosition());
                }
            });

            todoMainLay.setOnLongClickListener(view -> {
                if (toDoCallBack != null) {
                    toDoCallBack.deleteTask(getAdapterPosition());
                }
                return false;
            });
        }
    }

}
