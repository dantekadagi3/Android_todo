package com.example.mydoey1.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydoey1.AddNewTask;
import com.example.mydoey1.MainActivity;
import com.example.mydoey1.R;
import com.example.mydoey1.model.ToDoModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ToDoAdapter(MainActivity mainActivity, List<ToDoModel> todoList) {
        this.todoList = (todoList != null) ? todoList : new ArrayList<>();
        this.activity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.task, parent, false);
        return new MyViewHolder(view);
    }

    public void deleteTask(int position) {
        ToDoModel toDoModel = todoList.get(position);
        if (toDoModel.TaskId != null && !toDoModel.TaskId.isEmpty()) {
            firestore.collection("task").document(toDoModel.TaskId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        todoList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> notifyItemChanged(position));
        }
    }

    public Context getContext() {
        return activity;
    }

    public void editTask(int position) {
        ToDoModel toDoModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("due", toDoModel.getDue());
        bundle.putString("id", toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel toDoModel = todoList.get(position);
        holder.mCheckBox.setText(toDoModel.getTask());

        // Set the due date directly
        holder.mDueDateTv.setText("Due on: " + toDoModel.getDue());

        holder.mCheckBox.setOnCheckedChangeListener(null); // Remove listener
        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (toDoModel.TaskId != null && !toDoModel.TaskId.isEmpty()) {
                firestore.collection("task").document(toDoModel.TaskId)
                        .update("status", isChecked ? 1 : 0)
                        .addOnFailureListener(e -> compoundButton.setChecked(!isChecked));
            }
        });
    }

    private boolean toBoolean(int status) {
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mDueDateTv;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
