package com.example.notes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        view.findViewById(R.id.btnSaveNote).setOnClickListener(v -> {
            String title = ((EditText) view.findViewById(R.id.editTextNoteTitle)).getText().toString();
            String content = ((EditText) view.findViewById(R.id.editTextNoteContent)).getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            notesDAO.addNote(title, content, category, System.currentTimeMillis());
            getParentFragmentManager().popBackStack();
        });


        return view;
    }
}
