package com.onepercent.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.onepercent.mynotes.adapters.NotesRecyclerAdapter;
import com.onepercent.mynotes.models.Note;
import com.onepercent.mynotes.persistence.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity
        implements View.OnClickListener, NotesRecyclerAdapter.OnNoteClickListener,
        NotesRecyclerAdapter.OnNoteLongClickListener, MenuItem.OnMenuItemClickListener {

    // ui
    private RecyclerView mRecyclerView;
    private Menu optionsMenu;
    private MenuItem mDeleteButton;
    private MenuItem mCancelDeleteButton;
    private View mView;

    // vars
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNoteRecyclerAdapter;
    private NoteRepository mNoteRepository;

    private boolean inDeleteMode = false;
    private int mNotePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        mRecyclerView = findViewById(R.id.notes_recycler_view);
        mNoteRepository = new NoteRepository(this);

        findViewById(R.id.fab).setOnClickListener(this);

        setSupportActionBar((Toolbar) findViewById(R.id.notes_toolbar));

        initRecyclerView();
        retrieveNotes();
    }

    public void initRecyclerView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mNoteRecyclerAdapter = new NotesRecyclerAdapter(mNotes, this, this);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab: {
                if (inDeleteMode) deleteMode(false);
                Intent intent = new Intent(this, NoteActivity.class);

                ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(
                        view, 0, 0, view.getWidth(), view.getHeight());

                startActivity(intent, opts.toBundle());
            }
        }
    }

    @Override
    public void onNoteClick(int position, View view) {
        if (inDeleteMode) deleteMode(false);
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));

        ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(
                view, 0, 0, view.getWidth(), view.getHeight());

        startActivity(intent, opts.toBundle());
    }

    @Override
    public void onNoteLongClick(final int position, View view) {
        if (inDeleteMode){
            deleteMode(false);
        }
        else {
            mView = view;
            mNotePosition = position;
            deleteMode(true);
        }
    }

    private void retrieveNotes() {
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (mNotes.size() > 0) {
                    mNotes.clear();
                }
                mNotes.addAll(notes);
                mNoteRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void deleteNote(Note note) {
        mNotes.remove(note);
        mNoteRecyclerAdapter.notifyDataSetChanged();
        mNoteRepository.deleteNote(note);
    }

    private void deleteMode(boolean isVisible){
        mDeleteButton.setVisible(isVisible);
        mCancelDeleteButton.setVisible(isVisible);

        inDeleteMode = isVisible;

        CardView card = mView.findViewById(R.id.card_view);
        if (isVisible) {
            card.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
        }else {
            card.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_edit_menu, menu);
        optionsMenu = menu;

        mDeleteButton = optionsMenu.findItem(R.id.delete_note);
        mCancelDeleteButton = optionsMenu.findItem(R.id.cancel_delete_note);

        mDeleteButton.setOnMenuItemClickListener(this);
        mCancelDeleteButton.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (inDeleteMode) {
            deleteMode(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_note:{
                deleteNote(mNotes.get(mNotePosition));
                deleteMode(false);
                return true;
            }
            case R.id.cancel_delete_note:{
                deleteMode(false);
                return true;
            }
            default:
                return false;
        }
    }
}