package com.onepercent.mynotes.async;

import android.os.AsyncTask;

import com.onepercent.mynotes.models.Note;
import com.onepercent.mynotes.persistence.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public InsertAsyncTask(NoteDao dao) {
        this.mNoteDao = dao;
    }


    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return null;
    }
}
