package com.example.nt17;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.IconForm;

import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class adapter_note extends BaseAdapter implements AdapterView.OnItemClickListener {

    private List<list_ghichu> list_ghichus;
    private Context context;
    private list_ghichu list_ghichu;
    private String name_database;
    private database database;
    private adapter_note adapter_note;
    private LinearLayout linearLayout;

    public adapter_note(List<com.example.nt17.list_ghichu> list_ghichus, Context context,String name_database,adapter_note adapter_note) {
        this.list_ghichus = list_ghichus;
        this.context = context;
        this.name_database=name_database;
        this.adapter_note=adapter_note;

    }





    @Override
    public int getCount() {
        return list_ghichus.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewholder viewholder;
        if(convertView == null) {

            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.adapter_list_item,null);

            viewholder=new viewholder();

            viewholder.txt_content=(TextView) convertView.findViewById(R.id.txt_content_adapter);
            viewholder.txt_title=(TextView) convertView.findViewById(R.id.txt_title_adapter);
            viewholder.btn_setting=(Button) convertView.findViewById(R.id.btn_settings);

            convertView.setTag(viewholder);

        } else {
            viewholder=(viewholder) convertView.getTag();
        }
        list_ghichu=list_ghichus.get(position);

        viewholder.txt_title.setText(list_ghichu.getTitle());
        viewholder.txt_content.setText(list_ghichu.getTxt_content());


        if(name_database !=null) {
            database = Room.databaseBuilder(context, com.example.nt17.database.class, name_database)
                    .allowMainThreadQueries()
                    .build();

        }

        viewholder.btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,viewholder.btn_setting);
                popupMenu.getMenuInflater().inflate(R.menu.menu_settings,popupMenu.getMenu());

                //set onclick with popup menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_delete:
                                database.daoAccess().delete_ghichu(list_ghichus.get(position));
                                update_data(database.daoAccess().getAll_list_ghichu());
                        }
                        return true;
                    }

                    private void update_data(List<list_ghichu> list_ghichu1) {
                        list_ghichus.clear();
                        list_ghichus.addAll(list_ghichu1);
                        notifyDataSetChanged();
                    }
                });
                popupMenu.show();

            }
        });





        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        linearLayout=(LinearLayout) view.findViewById(R.id.layout_adapter);
        linearLayout.setBackgroundColor(Color.parseColor("#fff"));
        Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();
    }


    public class viewholder {
        public Button btn_setting;
        public TextView txt_title,txt_content;

    }


}
