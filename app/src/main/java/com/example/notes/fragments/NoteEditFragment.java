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

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

// Установить текущую категорию
        if (note != null) {
            int position = adapter.getPosition(note.getCategory());
            spinnerCategory.setSelection(position);
        }

        view.findViewById(R.id.btnSaveNote).setOnClickListener(v -> {
            if (note != null) {
                note.setTitle(titleEditText.getText().toString());
                note.setContent(contentEditText.getText().toString());
                note.setCategory(spinnerCategory.getSelectedItem().toString());
                note.setTimestamp(System.currentTimeMillis());
                notesDAO.updateNote(note);
            }
            getParentFragmentManager().popBackStack();
        });


        return view;
    }
}