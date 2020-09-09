package com.example.nt17;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStore;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.app.Application;
import android.app.Dialog;
import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edit_title,edit_content;
    private Button btn_tao;
    private Spinner spinner;
    private database database;

    private ArrayList<String> arrayList=new ArrayList<>();
    private Button button_setting_bar;
    private adapter_note adapter_note;
    private List<list_ghichu> list_ghichus;
    private ProgressBar progressBar;
    private  ListView listView;
    private ImageView image_empty;
    private TextView txt_empty;
    private list_ghichu listGhichu;
    private LinearLayout main_content;
    private Dialog dialog;

    private ViewModelnote viewModelnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        khaibao();

        try {
            setdefault_if_empty_note();
        }catch (Exception e) {
            Log.i("Database open: ","Failed!!!");
            e.printStackTrace();
        }

        set_item_in_spinner();
        set_item_note();

        setPopMenu_setting_bar();
        set_click_item_in_listview();


    }

    private void set_click_item_in_listview() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                database= com.example.nt17.database.getDatabase(getApplicationContext(),spinner.getSelectedItem().toString());
                //Khởi tạo database khi click vào item listview

                list_ghichus=database.daoAccess().getAll_list_ghichu();  //get all data from databse to list_ghichus

                Intent intent=new Intent(getApplicationContext(),them_ghichu.class);
                intent.putExtra("monhoc",spinner.getSelectedItem().toString());
                intent.putExtra("title",list_ghichus.get(position).getTitle());
                intent.putExtra("content",list_ghichus.get(position).getTxt_content());
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(intent);
            }
        });
    }


    private void setPopMenu_setting_bar() {
        button_setting_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(getApplicationContext(),button_setting_bar);
                popupMenu.getMenuInflater().inflate(R.menu.setting_bar,popupMenu.getMenu());
                popupMenu.show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }



    private void set_item_in_spinner() {
        SharedPreferences sharedPreferences=getSharedPreferences("list",Context.MODE_PRIVATE);
        arrayList.add("Vật lý");
        arrayList.add("Toán học");
        arrayList.add("Anh văn");
        arrayList.add("Hóa học");
        arrayList.add("Địa lý");
        if(sharedPreferences.getString("list",null) !=null) {
            arrayList.add(sharedPreferences.getString("list",null));
        }
        arrayList.add("Thêm");


        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(MainActivity.this,R.layout.adapter_spinner,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(arrayAdapter);
    }


    private void set_item_note() {
        btn_tao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=edit_title.getText().toString();
                String content=edit_content.getText().toString();

                if(title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Điền lại", Toast.LENGTH_SHORT).show();
                    return;
                }
                closeKeyBoard();
                viewModelnote=new ViewModelnote(getApplication(),spinner.getSelectedItem().toString());
                //khaibao roomdatabase với tên bảng lấy từ spinner item
                //database= com.example.nt17.database.getDatabase(getApplicationContext(),spinner.getSelectedItem().toString());
                //ẩn image empty với txt_empty
                image_empty.setVisibility(View.GONE);
                txt_empty.setVisibility(View.GONE);


                listGhichu=new list_ghichu(content,title);
                viewModelnote.insert_note(listGhichu);

                //database.daoAccess().insert_data_note(listGhichu);
                Log.i("DAO ACCESS","INSERT THÀNH CÔNG");

                listView.setVisibility(View.VISIBLE);
                adapter_note=new adapter_note(database.daoAccess().getAll_list_ghichu(),getApplicationContext(),spinner.getSelectedItem().toString(),adapter_note);
                listView.setAdapter(adapter_note);

            }

            private void closeKeyBoard() {
                edit_title.setText("");
                edit_content.setText("");
                View view=MainActivity.this.getCurrentFocus();
                if(view!=null) {
                    InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
            }

        });

    }



    private void setdefault_if_empty_note() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, final long id) {

                if(spinner.getSelectedItem()=="Thêm") {
                    dialog=new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.dialog_them_items);
                    dialog.setCancelable(false);
                    Button btn_dismis=dialog.findViewById(R.id.btn_dismis);
                    Button btn_save=dialog.findViewById(R.id.btn_save);


                    final EditText editText_them=dialog.findViewById(R.id.edit_them_mon);

                    btn_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         arrayList.add(editText_them.getText().toString());
                         //Lưu giá trị mới vào share preference
                         SharedPreferences sharedPreferences=getSharedPreferences("list",Context.MODE_PRIVATE);
                         SharedPreferences.Editor editor=sharedPreferences.edit();
                         editor.putString("list",editText_them.getText().toString());
                         editor.apply();

                         //thông báo ra snackbar
                            Snackbar snackbar = Snackbar
                                    .make(main_content, "Add: "+editText_them.getText().toString()+ " thành công!!!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            dialog.dismiss();

                            //khi thoát ra set spinner ra giá trị mới được tạo
                            spinner.setSelection(position+1);
                        }
                    });
                    btn_dismis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return;
                }
                database= com.example.nt17.database.getDatabase(getApplicationContext(),spinner.getSelectedItem().toString());

                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        kiemtra(database); //check database= null?
                    }
                },1000);
                progressBar.setVisibility(View.GONE);

                adapter_note=new adapter_note(database.daoAccess().getAll_list_ghichu(),getApplicationContext(),spinner.getSelectedItem().toString(),adapter_note);
                listView.setAdapter(adapter_note);



            }

            //check anything in database If there have null
            private void kiemtra(final database database) {
                Log.d("Kiemtra", database.daoAccess().getAll_list_ghichu().size()+"");
                if (database.daoAccess().getAll_list_ghichu().size() ==0) {
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    image_empty.setVisibility(View.VISIBLE);
                    txt_empty.setVisibility(View.VISIBLE);
                    return;
                } else {
                    txt_empty.setVisibility(View.GONE);
                    image_empty.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


    }


    private void khaibao() {

        //id view
        button_setting_bar=(Button) findViewById(R.id.btn_setting_bar);
        spinner=(Spinner) findViewById(R.id.spinner);
        edit_title=(EditText) findViewById(R.id.editText_title);
        edit_content=(EditText) findViewById(R.id.editText_content);
        btn_tao=(Button) findViewById(R.id.btn_tao);
        listView=(ListView) findViewById(R.id.listview_item);
        main_content=(LinearLayout) findViewById(R.id.main_content);
        progressBar=(ProgressBar) findViewById(R.id.progress_bar);
        image_empty=(ImageView) findViewById(R.id.image_empty);
        txt_empty=(TextView) findViewById(R.id.txt_empty);
    }

    private void set_onclick_listview() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),them_ghichu.class);
                intent.putExtra("title",list_ghichus.get(position).getTitle());
                intent.putExtra("content",list_ghichus.get(position).getTxt_content());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

    }

    private void instance_balloon () {
        Balloon balloon = new Balloon.Builder(this)
                .setArrowSize(5)
                .setArrowOrientation(ArrowOrientation.LEFT)
                .setArrowVisible(true)
                .setWidthRatio(0.65f)
                .setHeight(65)
                .setTextSize(14f)
                .setArrowPosition(0.5f)
                .setCornerRadius(10f)
                .setAlpha(0.9f)
                .setText("Hello Student, Let's study now...")
                .setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setIconDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_foreground))
                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .build();

    }

}
