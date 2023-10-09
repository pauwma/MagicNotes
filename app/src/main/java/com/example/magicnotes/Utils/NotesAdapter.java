package com.example.magicnotes.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.magicnotes.Items.Note;
import com.example.magicnotes.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    Context context;
    private List<Note> notes;
    private List<Note> selectedNotes = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnNoteLongClickListener onNoteLongClickListener;
    private OnSelectionListener onSelectionListener;
    public interface OnSelectionListener {
        void onSelectionStarted();
        void onSelectionEnded();
    }
    public void setOnSelectionListener(OnSelectionListener listener) {
        this.onSelectionListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public interface OnNoteLongClickListener {
        void onNoteLongClick(View view, Note note);
    }

    public void setOnNoteLongClickListener(OnNoteLongClickListener listener) {
        this.onNoteLongClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }

    public List<Note> getSelectedNotes() {
        return selectedNotes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_home, parent, false);
        return new NoteViewHolder(view, onItemClickListener, onNoteLongClickListener, notes);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.descriptionTextView.setText(note.getDescription());
        holder.bind(note, selectedNotes.contains(note));
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        OnNoteLongClickListener onNoteLongClickListener;

        public void bind(Note note, boolean isSelected) {
            MaterialCardView bottomCard = itemView.findViewById(R.id.bottom_card);
            if (isSelected) {
                bottomCard.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.primary));
                bottomCard.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.primary));
                bottomCard.setStrokeWidth(3);
            } else {
                bottomCard.setStrokeColor(ContextCompat.getColor(itemView.getContext(), R.color.outline));
                bottomCard.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.outline));
                bottomCard.setStrokeWidth((int) 1.2f);
            }
        }

        public NoteViewHolder(@NonNull View itemView, final OnItemClickListener listener, OnNoteLongClickListener onNoteLongClickListener, final List<Note> notes) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.note_description_text_view);
            this.onNoteLongClickListener = onNoteLongClickListener;

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (selectedNotes.isEmpty()) {
                        onSelectionListener.onSelectionStarted();
                    }
                    Note note = notes.  get(position);
                    if (selectedNotes.contains(note)) {
                        selectedNotes.remove(note);
                    } else {
                        selectedNotes.add(note);
                    }
                    notifyItemChanged(position);
                    if (selectedNotes.isEmpty()) {
                        onSelectionListener.onSelectionEnded();
                    }
                }
                return true;
            });
            itemView.setOnClickListener(v -> {
                if (!selectedNotes.isEmpty()) {
                    itemView.performLongClick();
                } else {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(notes.get(position));
                        }
                    }
                }
            });

        }
    }
}