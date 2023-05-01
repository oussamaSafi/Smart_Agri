package com.example.agrilogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {
    Animation right2left,left2right;
    TextView sunval,soilval,tempval,tankval,windval,slogan1,slogan2;
    ImageView logo;
    FirebaseDatabase database;
    DatabaseReference dref,myRef;
    String sun_val,tank_val,y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        // Création des objet text view

        sunval = (TextView) findViewById(R.id.sunval);
        soilval = (TextView) findViewById(R.id.soilval);
        tempval = (TextView) findViewById(R.id.tempval);
        tankval = (TextView) findViewById(R.id.watertankval);
        windval = (TextView) findViewById(R.id.windval);

        //animation
        left2right = AnimationUtils.loadAnimation(this,R.anim.right_anim);
        right2left = AnimationUtils.loadAnimation(this,R.anim.left_anim);

        slogan1=findViewById(R.id.Slogan1);
        slogan2=findViewById(R.id.Slogan2);
        logo=findViewById(R.id.logo);

        slogan1.setAnimation(left2right);
        slogan2.setAnimation(left2right);
        logo.setAnimation(right2left);

        // firebase
        dref= FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("pompe");
        dref.child("temp3").orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                y = snapshot.getValue().toString();
                System.out.println(y);
                String g = y.substring(y.lastIndexOf("=") + 1);
                String f = g.substring(0,g.length()-1);
                tempval.setText(f + " °C");
                System.out.println(f);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dref.child("humid").orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String y2 = snapshot.getValue().toString();
                System.out.println(y2);
                String g2 = y2.substring(y2.lastIndexOf("=") + 1);
                String f2 = g2.substring(0,g2.length()-1);
                windval.setText(f2 + " g/m\u00B3");
                System.out.println(f2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dref.child("humidsol").orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String y3 = snapshot.getValue().toString();
                System.out.println(y3);
                String g3 = y3.substring(y3.lastIndexOf("=") + 1);
                String f3 = g3.substring(0,g3.length()-1);
                soilval.setText(f3 + " %");
                System.out.println(f3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sun_val=dataSnapshot.child("sunval").getValue().toString();

                if(sun_val.equals("0")){
                    sunval.setText("Nuit");
                }
                else{
                    sunval.setText("Jour");
                }


                tank_val=dataSnapshot.child("tank").getValue().toString();
                if(tank_val.equals("0")){
                    tankval.setText("Vide");
                }
                else{
                    tankval.setText("Plein");
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Dashboard.this, "échec de lecture des données", Toast.LENGTH_SHORT).show();
            }
        });



    }
    public void temp_graph(View v){
        Intent intent = new Intent(Dashboard.this,temp_graph.class);
        startActivity(intent);
    }
    public void humid_graph(View v){
        Intent intent = new Intent(Dashboard.this,humid_graph.class);
        startActivity(intent);
    }
    public void Soil_graph(View v){
        Intent intent = new Intent(Dashboard.this,Soil_graph.class);
        startActivity(intent);
    }

    public void OFF(View v){
        // Write a message to the database

        myRef.setValue("OFF");


    }
    public void ON(View v){
        // Write a message to the database


        myRef.setValue("ON");


    }



}