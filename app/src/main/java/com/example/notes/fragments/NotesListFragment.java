package com.example.notes.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.adapters.NotesAdapter;
import com.example.notes.database.NotesDAO;
import com.example.notes.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

public class NotesListFragment extends Fragment {
    private NotesDAO notesDAO;
    private NotesAdapter adapter;
    private Spinner spinnerSort;
    private EditText editTextSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotes);
        FloatingActionButton fab = view.findViewById(R.id.fabAddNote);
        spinnerSort = view.findViewById(R.id.spinnerSort);
        editTextSearch = view.findViewById(R.id.editTextSearch);

        notesDAO = new NotesDAO(requireContext());
        adapter = new NotesAdapter(notesDAO.getAllNotes("timestamp DESC"), note -> {
            // Открываем фрагмент редактирования заметки
            Bundle bundle = new Bundle();
            bundle.putSerializable("note", note);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, NoteEditFragment.class, bundle)
                    .addToBackStack(null)
                    .commit();
        }, notesDAO);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new NoteAddFragment())
                    .addToBackStack(null)
                    .commit();
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                adapter.moveItem(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Не используем свайп
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Обработчик выбора в спиннере
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateNotes();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Обработчик ввода в поле поиска
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateNotes();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return view;
    }

    private void updateNotes() {
        String query = editTextSearch.getText().toString().toLowerCase();
        String sortOrder = getSortOrder(spinnerSort.getSelectedItemPosition());

        List<Note> filteredNotes = notesDAO.getAllNotes(sortOrder).stream()
                .filter(note -> note.getTitle().toLowerCase().contains(query) ||
                        note.getCategory().toLowerCase().contains(query)||
                        note.getContent() != null && note.getContent().toLowerCase().contains(query)
                )
                .collect(Collectors.toList());

        adapter.updateData(filteredNotes);
    }

    private String getSortOrder(int position) {
        switch (position) {
            case 1: return "category ASC";
            case 2: return "category ASC, timestamp DESC";
            default: return "timestamp DESC";
        }
    }
}
