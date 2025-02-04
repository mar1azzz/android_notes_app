package com.example.notes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.notes.R;
import com.example.notes.database.NotesDAO;
import com.example.notes.models.Note;

public class NoteAddFragment extends Fragment {

    private NotesDAO notesDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        notesDAO = new NotesDAO(requireContext());

        view.findViewById(R.id.btnSaveNote).setOnClickListener(v -> {
            String title = ((EditText) view.findViewById(R.id.editTextNoteTitle)).getText().toString();
            String content = ((EditText) view.findViewById(R.id.editTextNoteContent)).getText().toString();

            notesDAO.addNote(title, content, "Uncategorized", System.currentTimeMillis());

            // Navigate back to notes list
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
