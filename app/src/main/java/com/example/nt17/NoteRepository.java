package com.example.nt17;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository  {

    private DaoAccess daoAccess;
    private LiveData<List<list_ghichu>> listLiveData;
    private list_ghichu listGhichuLiveData;

    NoteRepository(Application application,String name_database) {
        database database_room= com.example.nt17.database.getDatabase(application,name_database);
        daoAccess=database_room.daoAccess();
        listLiveData=daoAccess.getAlphabetaNote();
    }

    LiveData<List<list_ghichu>> getAllNote () {
        return listLiveData;
    }

    void inset_note(final list_ghichu list_ghichu) {
        database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                daoAccess.insert_data_note(list_ghichu);
            }
        });
    }

    void update_note(list_ghichu list_ghichu) {
        daoAccess.update_ghichu(list_ghichu);
    }

    list_ghichu getGhiChu(String content) {
        listGhichuLiveData=daoAccess.getGhiChu_detail(content);
        return listGhichuLiveData;
    }

}
