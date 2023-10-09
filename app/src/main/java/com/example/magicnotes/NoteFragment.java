package com.example.magicnotes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.magicnotes.Items.Note;
import com.example.magicnotes.Items.NoteDao;


public class NoteFragment extends Fragment {

    private NoteDao noteDao;
    private int noteId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);

        // Inicialización
        noteDao = AppDatabase.getDatabase(getContext()).noteDao();
        Bundle args = getArguments();
        if (args != null) {
            noteId = args.getInt("note_id", -1);
        }

        final EditText titleEditText = view.findViewById(R.id.title_edit_text);
        final EditText descriptionEditText = view.findViewById(R.id.description_edit_text);

        // Si noteId != -1, carga los datos
        if (noteId != -1) {
            noteDao.getNoteById(noteId).observe(getViewLifecycleOwner(), new Observer<Note>() {
                @Override
                public void onChanged(Note note) {
                    if (note != null) {
                        titleEditText.setText(note.getTitle());
                        descriptionEditText.setText(note.getDescription());
                    }
                }
            });
        }

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigateUp();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        saveNote(); // Guardar la nota cuando el fragmento se pausa
    }

    private void saveNote() {
        String title = ((EditText) getView().findViewById(R.id.title_edit_text)).getText().toString();
        String description = ((EditText) getView().findViewById(R.id.description_edit_text)).getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!title.isEmpty() || !description.trim().isEmpty()) {
                    if (noteId == -1) {  // Nueva nota
                        Note newNote = new Note(title, description, "");
                        noteDao.insert(newNote);
                    } else {  // Actualizar nota existente
                        Note updatedNote = new Note(noteId, Note.NoteType.TEXT, title, description, "", null, 0, false);
                        updatedNote.updateEditedTimestamp();
                        noteDao.update(updatedNote);
                    }
                }
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);
    }
}