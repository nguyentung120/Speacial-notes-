package com.example.nt17;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class list_ghichu {
    @ColumnInfo(name = "txt_content")
    private String txt_content;

    @ColumnInfo(name="txt_ghichu")
    private String txt_ghi_chu;

    @ColumnInfo(name = "title")
    private String title;

    @PrimaryKey (autoGenerate = true)
    private int id;



    @Ignore
    public list_ghichu(int id, String txt_content,  String title, String txt_ghi_chu) {
        this.txt_content = txt_content;
        this.txt_ghi_chu = txt_ghi_chu;
        this.title = title;
        this.id=id;
    }

    @Ignore
    public list_ghichu(String txt_content, String title) {
        this.txt_content = txt_content;
        this.title = title;
    }






    public list_ghichu() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxt_content() {
        return txt_content;
    }

    public void setTxt_content(String txt_content) {
        this.txt_content = txt_content;
    }

    public String getTxt_ghi_chu() {
        return txt_ghi_chu;
    }

    public void setTxt_ghi_chu(String txt_ghi_chu) {
        this.txt_ghi_chu = txt_ghi_chu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    

}
