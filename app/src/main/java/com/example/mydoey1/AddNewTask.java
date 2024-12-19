package com.example.mydoey1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewTask  extends BottomSheetDialogFragment {

    public static final String TAG = "com.example.mydoey1.AddNewTask";

     private TextView setDueDate;
     private EditText mTaskEdit;
     private Button  save;

     private FirebaseFirestore firestore;
    //Method to add an instance of the class
    public static AddNewTask newInstance(){

        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.add_new_task,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDueDate=view.findViewById(R.id.set_due_tv);
        mTaskEdit=view.findViewById(R.id.task_edittext);
        save=view.findViewById(R.id.save_button);
        firestore=FirebaseFirestore.getInstance();
    }
}
