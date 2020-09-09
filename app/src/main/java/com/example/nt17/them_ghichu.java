package com.example.nt17;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class them_ghichu extends AppCompatActivity {

    private EditText editText_title,editText_content,editText_ghichu;
    private TextView txt_monhoc;
    private Button btn_luu;
    private database database;
    private list_ghichu listGhichu;
    private ViewModelnote viewModelnote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_ghichu);
        khaibao();
        get_data_from_activity();

        instance_database();
    }

    private void instance_database() {
        database= Room.databaseBuilder(getApplicationContext(), com.example.nt17.database.class,txt_monhoc.getText().toString())
                .allowMainThreadQueries()
                .build();
    }


    private void get_data_from_activity() {
         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 Intent intent=getIntent();
                 String content=intent.getStringExtra("content");
                 String name_database=intent.getStringExtra("monhoc");

                 txt_monhoc.setText(name_database);
                 editText_title.setText(intent.getStringExtra("title"));
                 editText_content.setText(content);

                 instance_viewmodel(name_database);
                 editText_ghichu.setText(viewModelnote.getNote_detail(content).getTxt_ghi_chu());
             }
         },10);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void khaibao() {
        btn_luu=(Button) findViewById(R.id.btn_luu);
        editText_content=(EditText) findViewById(R.id.edit_content);
        editText_title=(EditText) findViewById(R.id.edit_title);
        editText_ghichu=(EditText) findViewById(R.id.edit_ghichu);
        txt_monhoc=(TextView) findViewById(R.id.txt_mon);
    }

    public void save_ghichu(View view) {

        listGhichu=new list_ghichu(editText_content.getText().toString(),editText_title.getText().toString(),editText_ghichu.getText().toString());
        Update_Note_Repositive(txt_monhoc.getText().toString(), listGhichu);
    }

    private void Update_Note_Repositive (String name_database,list_ghichu list_ghichu12) {
        instance_viewmodel(name_database);
        viewModelnote.update_note(list_ghichu12);
        Toast.makeText(this, "viewModel " + viewModelnote, Toast.LENGTH_SHORT).show();
    }

    private void instance_viewmodel(String name_database) {
        viewModelnote=new ViewModelnote(getApplication(),name_database);

    }
}
