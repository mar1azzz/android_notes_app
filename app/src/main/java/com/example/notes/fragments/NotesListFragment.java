package com.example.notes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notes.R;
import com.example.notes.adapters.NotesAdapter;
import com.example.notes.database.NotesDAO;
import com.example.notes.database.NotesDatabaseHelper;
import com.example.notes.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

// Fragment for displaying all notes
public class NotesListFragment extends Fragment {

    private NotesDAO notesDAO;
    private NotesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotes);
        FloatingActionButton fab = view.findViewById(R.id.fabAddNote);

        notesDAO = new NotesDAO(requireContext());
        List<Note> notes = notesDAO.getAllNotes("timestamp DESC");

        adapter = new NotesAdapter(notes, note -> {
            // Navigate to edit note fragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("note", note);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, NoteEditFragment.class, bundle)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            // Navigate to add note fragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new NoteAddFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}