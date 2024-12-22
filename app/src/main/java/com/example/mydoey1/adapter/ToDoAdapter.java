package com.example.mydoey1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydoey1.MainActivity;
import com.example.mydoey1.R;
import com.example.mydoey1.model.ToDoModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public ToDoAdapter(MainActivity  mainActivity,List<ToDoModel>todoList){
        this.todoList=todoList;
        activity=mainActivity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.task,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      ToDoModel toDoModel=todoList.get(position);
      holder.mCheckBox.setText(toDoModel.getTask());
      holder.mDueDateTv.setText("Due on" + toDoModel.getDue());

      holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));
      holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
           if(isChecked){
            firestore.collection("task").document(toDoModel.TaskId).update("status" ,1);
           }else{
               firestore.collection("task").document(toDoModel.TaskId).update("status" ,0);
           }

          }
      });

    }

    private boolean toBoolean(int status){
        return status !=0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv;
        CheckBox mCheckBox;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        mDueDateTv=itemView.findViewById(R.id.due_date_tv);
        mCheckBox=itemView.findViewById(R.id.mcheckbox);
    }
}
}
