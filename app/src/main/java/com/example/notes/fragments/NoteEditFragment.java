package com.example.notes.fragments;

import android.os.Bundle;
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
import com.example.notes.models.Note;

import jp.wasabeef.richeditor.RichEditor;

public class NoteEditFragment extends Fragment {

    private NotesDAO notesDAO;
    private Note note;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);

        notesDAO = new NotesDAO(requireContext());

        // 1. Получаем переданную заметку
        if (getArguments() != null) {
            note = (Note) getArguments().getSerializable("note");
        }

        // 2. Заготовки UI
        EditText etTitle      = view.findViewById(R.id.editTextNoteTitle);
        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // 3. RichEditor
        RichEditor editor = view.findViewById(R.id.richEditor);
        editor.setEditorHeight(200);
        editor.setPlaceholder("Текст заметки...");

        // 4. Кнопки форматирования
        view.findViewById(R.id.btnBold).setOnClickListener(v -> editor.setBold());
        view.findViewById(R.id.btnItalic).setOnClickListener(v -> editor.setItalic());
        view.findViewById(R.id.btnUnderline).setOnClickListener(v -> editor.setUnderline());
        view.findViewById(R.id.btnAlignLeft).setOnClickListener(v -> editor.setAlignLeft());
        view.findViewById(R.id.btnAlignCenter).setOnClickListener(v -> editor.setAlignCenter());
        view.findViewById(R.id.btnAlignRight).setOnClickListener(v -> editor.setAlignRight());

        // 5. Заполняем поля данными из note
        if (note != null) {
            etTitle.setText(note.getTitle());
            int pos = adapter.getPosition(note.getCategory());
            spinnerCategory.setSelection(pos);
            // Загрузка HTML в редактор
            editor.setHtml(note.getContent());
        }

        // 6. Сохранение изменений
        Button btnSave = view.findViewById(R.id.btnSaveNote);
        btnSave.setOnClickListener(v -> {
            if (note != null) {
                note.setTitle(etTitle.getText().toString());
                note.setCategory(spinnerCategory.getSelectedItem().toString());
                // Забираем отформатированный HTML
                String html = editor.getHtml();
                note.setContent(html != null ? html : "");
                note.setTimestamp(System.currentTimeMillis());
                notesDAO.updateNote(note);
            }
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
