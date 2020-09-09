package com.example.nt17;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


//Using Room database to create database
@Dao //Data access Object (Xử lý object data)
public interface DaoAccess {


        @Insert
        void insert_data_note(list_ghichu listGhichu);

        @Query("SELECT * from list_ghichu")
        List<list_ghichu> getAll_list_ghichu();

        @Query(" SELECT * from list_ghichu Where id =:id ")
        list_ghichu get_list_ghichu(int id);

        @Update(onConflict = REPLACE)
        void update_ghichu(list_ghichu listGhichu);

        @Delete
        void delete_ghichu(list_ghichu listGhichu);

        @Query("SELECT * from list_ghichu")
        LiveData<List<list_ghichu>> getAlphabetaNote();

        @Query("SELECT * FROM list_ghichu Where txt_content=:content")
        list_ghichu getGhiChu_detail(String content);
}

