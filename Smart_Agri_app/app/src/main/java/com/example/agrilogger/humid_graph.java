package com.example.agrilogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class humid_graph extends AppCompatActivity {
    LineChart linechart;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref2,ref3;
    LineDataSet lineDataSet= new LineDataSet(null,null);
    ArrayList<ILineDataSet> iLineDataSets= new ArrayList<>();
    LineData lineData=new LineData(iLineDataSets);;
    int y,z=0,r,uu;
    ArrayList<Entry> datavals = new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_humid_graph);

        linechart= (LineChart) findViewById(R.id.line_chart2);
        firebaseDatabase=FirebaseDatabase.getInstance();
        ref3=firebaseDatabase.getReference();
        ref2 =firebaseDatabase.getReference("humid");
        retreivedata();

    }
    private void retreivedata() {


        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    z=0;
                    datavals.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        uu =(int) snapshot.getChildrenCount();
                        r =uu-20;
                        if ( z>=r && datavals.size()<=uu ){
                            y=dataSnapshot.getValue(Integer.class);
                            datavals.add(new Entry(z,y));

                        }
                        if(z<=uu){
                            z++;
                        }
                        else{
                            z=uu;
                        }
                        System.out.println(datavals);
                        System.out.println(uu);
                    }
                    showchart(datavals);
                }else {
                    linechart.clear();
                    linechart.invalidate();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(humid_graph.this, "échec de lecture des données", Toast.LENGTH_SHORT).show();
            }
        });
        datavals.clear();



    }

    private void showchart(ArrayList<Entry> datavals) {
        lineDataSet.setValues(datavals);
        lineDataSet.setLabel("Humidité");
        lineDataSet.setDrawFilled(true);
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData= new LineData(iLineDataSets);
        linechart.clear();
        linechart.setData(lineData);
        linechart.invalidate();

    }
}