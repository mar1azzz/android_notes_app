package com.example.notes.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notes.R;
import com.example.notes.database.NotesDAO;

import jp.wasabeef.richeditor.RichEditor;

public class NoteAddFragment extends Fragment {

    private NotesDAO notesDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        notesDAO = new NotesDAO(requireContext());

        // 1. Заголовок
        EditText etTitle = view.findViewById(R.id.editTextNoteTitle);

        // 2. Spinner категорий
        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // 3. RichEditor
        RichEditor editor = view.findViewById(R.id.richEditor);
        editor.setEditorHeight(200);
        editor.setPlaceholder("Текст заметки...");

        // 4. Кнопки форматирования
        Button btnBold      = view.findViewById(R.id.btnBold);
        Button btnItalic    = view.findViewById(R.id.btnItalic);
        Button btnUnderline = view.findViewById(R.id.btnUnderline);
        Button btnAlignLeft   = view.findViewById(R.id.btnAlignLeft);
        Button btnAlignCenter = view.findViewById(R.id.btnAlignCenter);
        Button btnAlignRight  = view.findViewById(R.id.btnAlignRight);

        btnBold.setOnClickListener(v -> editor.setBold());
        btnItalic.setOnClickListener(v -> editor.setItalic());
        btnUnderline.setOnClickListener(v -> editor.setUnderline());
        btnAlignLeft.setOnClickListener(v -> editor.setAlignLeft());
        btnAlignCenter.setOnClickListener(v -> editor.setAlignCenter());
        btnAlignRight.setOnClickListener(v -> editor.setAlignRight());

        // 5. Кнопка сохранения
        view.findViewById(R.id.btnSaveNote).setOnClickListener(v -> {
            String title    = etTitle.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            String html     = editor.getHtml();               // форматированный HTML
            if (html == null) html = "";                     // на случай пустоты

            // Сохраняем в БД
            notesDAO.addNote(title, html, category, System.currentTimeMillis());
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}

/*package com.example.notes.fragments;

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
}*/
