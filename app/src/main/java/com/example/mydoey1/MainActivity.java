package com.example.mydoey1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydoey1.adapter.ToDoAdapter;
import com.example.mydoey1.model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    private RecyclerView recyclerView;
    private FloatingActionButton actionButton;
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<ToDoModel>mList;
    private Query query;
    private ListenerRegistration listenerRegistration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });


        recyclerView =findViewById(R.id.recyclerView);
        actionButton=findViewById(R.id.floatingActionButton2);
        firestore=FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }

        });
        mList=new ArrayList<>();
        adapter=new ToDoAdapter(MainActivity.this,mList);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        showData();
    }



    private void  showData(){
       query= firestore.collection("task").orderBy("time" , Query.Direction.DESCENDING);
               listenerRegistration =query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange documentChange:value.getDocumentChanges()){
                    if(documentChange.getType()== DocumentChange.Type.ADDED){
                        String id=documentChange.getDocument().getId();
                        ToDoModel toDoModel=documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                        mList.add(toDoModel);
                        adapter.notifyDataSetChanged();
                    }
                }
                //Collections.reverse(mList);
                //reverse retrieves content from firebase in a random manner hence I switched to queries

                listenerRegistration.remove();
            }
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}