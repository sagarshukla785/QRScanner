package com.example.sagarshukla.kkk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class Act2 extends AppCompatActivity {
    ListView lv;
    ArrayAdapter<String> adapter ;
    Serializable arrayList = new ArrayList<String>();
    ImageButton btn;
    URLUtil url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act2);
        url = new URLUtil();
        lv = (ListView) findViewById(R.id.listView);
        btn = (ImageButton) findViewById(R.id.imageButton);
        Bundle wrapedReceivedList = getIntent().getExtras();
        arrayList= wrapedReceivedList.getCharSequenceArrayList("ArrayList");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, (List<String>) arrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);
                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.parseColor("#009688"));
                tv.setTextSize(15);
                // Generate ListView Item using TextView
                return view;
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = (String) adapterView.getItemAtPosition(i);
                if(url.isValidUrl(value)) {
                    Toast.makeText(Act2.this,"URL Type!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
                    startActivity(intent);
                }
                else
                    Toast.makeText(Act2.this,value,Toast.LENGTH_SHORT).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Act2.this);
                alertDialogBuilder.setTitle("ALERT!");
                alertDialogBuilder.setMessage("Are You Sure?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!((List<String>) arrayList).isEmpty()) {
                            DataBase dataBase = new DataBase(Act2.this);
                            Cursor cursor = dataBase.history();
                            while(cursor.moveToNext()){
                                dataBase.deleteData(cursor.getString(0));
                            }
                            ((List<String>) arrayList).clear();
                            adapter = new ArrayAdapter<String>(Act2.this, android.R.layout.simple_list_item_1, ((List<String>) arrayList));
                            lv.setAdapter(adapter);
                            Toast.makeText(Act2.this,"Deleted!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Act2.this, "No History!", Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertDialogBuilder.show();
            }
        });
    }
}
