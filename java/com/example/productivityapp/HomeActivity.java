package com.example.productivityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements AddTask.AddTaskListener{

    ListView taskListView;
    Button addTaskBtn, claimPrizeBtn, logoutBtn;
    TextView txt2;
    Boolean addedTask = false;

    ArrayList<Task> taskListTask = new ArrayList<>();
    ArrayList<String> taskList = new ArrayList<>();
    ArrayAdapter adapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    String userID = user.getUid();






    @Override
    public void applyTexts(String task) {
        taskListTask.add(new Task(task));
        addedTask = true;
        updateListView();
        adapter.notifyDataSetChanged();
    }

    public void updateListView(){
        taskList.clear();
        for( Task t : taskListTask){
            taskList.add(t.getName());
        }
    }

    public void saveData(){
        Gson gson = new Gson();
        String json = gson.toJson(taskListTask);
        editor.putString("task list", json);
        editor.apply();
    }

    public void loadData(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        taskListTask = gson.fromJson(json, type);

        if(taskListTask == null){
            taskListTask = new ArrayList<>();
        }
        updateListView();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        taskListView = findViewById(R.id.taskListView);

        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadData();

        addTaskBtn = findViewById(R.id.addTaskBtn);
        claimPrizeBtn = findViewById(R.id.claimPrizeBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        txt2 = findViewById(R.id.txt2);

        adapter = new ArrayAdapter(HomeActivity.this, android.R.layout.simple_list_item_1, taskList);
        taskListView.setAdapter(adapter);






        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String name = userProfile.name;

                    txt2.setText("Welcome, " + name + "!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Something wrong happened", Toast.LENGTH_LONG);
            }
        });

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTask addTask = new AddTask();
                addTask.show(getSupportFragmentManager(), "example dialogue");
            }
        });



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                int item = position;

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Complete Task?")
                        .setMessage("Do you want to complete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                taskListTask.remove(item);
                                updateListView();
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();



                return true;
            }
        });




        claimPrizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskList.size() > 0){
                    Toast.makeText(HomeActivity.this, "Make sure to complete all of your tasks!", Toast.LENGTH_LONG).show();
                }
                else if(!addedTask){
                    Toast.makeText(HomeActivity.this, "Make sure to add a task first!", Toast.LENGTH_LONG).show();
                }

                else {
                    int[] codes = new int[3];
                    int max = 2;
                    int min = 0;

                    codes[0] = 29827;
                    codes[1] = 90828;
                    codes[2] = 98096;

                    int code = codes[(int) (Math.random() * (max - min + 1) + min)];

                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Prize Claimed!")
                            .setMessage("Well done! The code to play your game is: " + code)
                            .setPositiveButton("Ok", null)
                            .setNegativeButton("Cancel", null)
                            .show();


                }
            }
        });





    }
}