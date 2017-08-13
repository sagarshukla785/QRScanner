package com.example.sagarshukla.kkk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    ImageButton btn;
    ArrayList<String> list;
    RelativeLayout rel;
    DataBase dataBase;
    TextToSpeech textToSpeech;
    Button button;
    ConnectivityManager check;
    URLUtil url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        button = (Button) findViewById(R.id.button1);
        url = new URLUtil();
        textToSpeech = new TextToSpeech(this,this);
        btn = (ImageButton) findViewById(R.id.button);
        dataBase = new DataBase(MainActivity.this);
        list = new ArrayList<String>();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Generate.class);
                startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("Scaning");
                intentIntegrator.setCameraId(0);
                intentIntegrator.initiateScan();
                textToSpeech.speak("Scaning",textToSpeech.QUEUE_ADD,null);
            }
        });
    }
    public boolean connect(){
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED || connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){
            if(result.getContents()!=null){
                list.add( result.getContents().toString()+"\n");
                dataBase.insert(list.get(list.size()-1));
                if(url.isValidUrl(result.getContents().toString())){
                    Toast.makeText(MainActivity.this,"URL Type!",Toast.LENGTH_SHORT).show();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(result.getContents().toString()));
                    startActivity(browserIntent);
                }
               else
                    Toast.makeText(MainActivity.this,result.getContents().toString(),Toast.LENGTH_SHORT).show();

            }
        }
        else {
            Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        list.clear();
        if(id == R.id.hit){
            Cursor cursor = dataBase.history();
            while(cursor.moveToNext()) {
                list.add(cursor.getString(0));
            }
            Intent intent = new Intent(MainActivity.this,Act2.class);
            intent.putExtra("ArrayList",list);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onInit(int i) {

    }
}

