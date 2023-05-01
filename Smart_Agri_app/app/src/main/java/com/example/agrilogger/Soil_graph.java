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

public class Soil_graph extends AppCompatActivity {
    LineChart linechart1;
    FirebaseDatabase firebaseDatabase1;
    DatabaseReference ref21;
    LineDataSet lineDataSet1= new LineDataSet(null,null);
    ArrayList<ILineDataSet> iLineDataSets1= new ArrayList<>();
    LineData lineData1=new LineData(iLineDataSets1);
    int y1,z1=0,r1,uu1;
    ArrayList<Entry> datavals1 = new ArrayList<Entry>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_soil_graph);

        linechart1= (LineChart) findViewById(R.id.line_chart3);
        firebaseDatabase1=FirebaseDatabase.getInstance();
        ref21 =firebaseDatabase1.getReference("humidsol");
        retreivedata1();


    }

    private void retreivedata1() {


        ref21.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                if(snapshot1.hasChildren()){
                    z1=0;
                    datavals1.clear();

                    for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
                        uu1 =(int) snapshot1.getChildrenCount();
                        r1 =uu1-20;
                        if ( z1>=r1 && datavals1.size()<=uu1 ){
                            y1=dataSnapshot1.getValue(Integer.class);
                            datavals1.add(new Entry(z1,y1));

                        }
                        if(z1<=uu1){
                            z1++;
                        }
                        else{
                            z1=uu1;
                        }
                        System.out.println(datavals1);
                        System.out.println(uu1);
                    }
                    showchart1(datavals1);
                }else {
                    linechart1.clear();
                    linechart1.invalidate();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Soil_graph.this, "échec de lecture des données", Toast.LENGTH_SHORT).show();
            }
        });
        datavals1.clear();

    }

    private void showchart1(ArrayList<Entry> datavals1) {
        lineDataSet1.setValues(datavals1);
        lineDataSet1.setLabel("Humidité de sol");
        lineDataSet1.setDrawFilled(true);
        iLineDataSets1.clear();
        iLineDataSets1.add(lineDataSet1);
        lineData1= new LineData(iLineDataSets1);
        linechart1.clear();
        linechart1.setData(lineData1);
        linechart1.invalidate();
    }


}