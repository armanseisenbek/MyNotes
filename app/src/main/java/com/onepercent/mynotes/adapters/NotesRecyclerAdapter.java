package com.onepercent.mynotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onepercent.mynotes.R;
import com.onepercent.mynotes.models.Note;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private ArrayList<Note> mNotes;
    private OnNoteClickListener mOnNoteClickListener;
    private OnNoteLongClickListener mOnNoteLongClickListener;

    public NotesRecyclerAdapter(ArrayList<Note> notes, OnNoteClickListener onNoteClickListener,
                                OnNoteLongClickListener onNoteLongClickListener) {
        this.mNotes = notes;
        this.mOnNoteClickListener = onNoteClickListener;
        this.mOnNoteLongClickListener = onNoteLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_note_list_item, parent, false);
        return new ViewHolder(view, mOnNoteClickListener, mOnNoteLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mNotes.get(position).getTitle());
        holder.content.setText(mNotes.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView title, content;
        OnNoteClickListener mOnNoteClickListener;
        OnNoteLongClickListener mOnNoteLongClickListener;

        public ViewHolder(@NonNull View itemView, OnNoteClickListener onNoteClickListener,
                          OnNoteLongClickListener onNoteLongClickListener) {
            super(itemView);

            title = itemView.findViewById(R.id.note_title);
            content = itemView.findViewById(R.id.note_content);
            mOnNoteClickListener = onNoteClickListener;
            mOnNoteLongClickListener = onNoteLongClickListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnNoteClickListener.onNoteClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnNoteLongClickListener.onNoteLongClick(getAdapterPosition(), v);
            return true;
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(int position, View view);
    }

    public interface OnNoteLongClickListener {
        void onNoteLongClick(int position, View view);
    }
}
