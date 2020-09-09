package com.example.nt17;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ViewModelnote extends AndroidViewModel {
    private NoteRepository noteRepository;
    private LiveData<List<list_ghichu>> listLiveData;

    public ViewModelnote(@NonNull Application application,@NonNull String name_database) {
        super(application);
        noteRepository=new NoteRepository(application,name_database);
        listLiveData=noteRepository.getAllNote();
    }

    LiveData<List<list_ghichu>> GetAllNote () {
        return listLiveData;
    }
    void insert_note(list_ghichu list_ghichu) {
        noteRepository.inset_note(list_ghichu);
    }
    void update_note(list_ghichu list_ghichu) {
        noteRepository.update_note(list_ghichu);
    }
    list_ghichu getNote_detail(String content) {
        return noteRepository.getGhiChu(content);
    }

}
