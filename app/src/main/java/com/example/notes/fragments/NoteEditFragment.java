package com.example.notes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.notes.R;
import com.example.notes.database.NotesDAO;
import com.example.notes.models.Note;
import android.widget.EditText;

public class NoteEditFragment extends Fragment {

    private NotesDAO notesDAO;
    private Note note;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);

        notesDAO = new NotesDAO(requireContext());

        if (getArguments() != null) {
            note = (Note) getArguments().getSerializable("note");
        }

        EditText titleEditText = view.findViewById(R.id.editTextNoteTitle);
        EditText contentEditText = view.findViewById(R.id.editTextNoteContent);

        if (note != null) {
            titleEditText.setText(note.getTitle());
            contentEditText.setText(note.getContent());
        }

        view.findViewById(R.id.btnSaveNote).setOnClickListener(v -> {
            if (note != null) {
                note.setTitle(titleEditText.getText().toString());
                note.setContent(contentEditText.getText().toString());
                note.setTimestamp(System.currentTimeMillis());
                notesDAO.updateNote(note);
            }

            // Navigate back to notes list
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}