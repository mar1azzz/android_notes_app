package com.example.notes.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.database.NotesDAO;
import com.example.notes.models.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private final OnNoteClickListener onNoteClickListener;
    private final NotesDAO notesDAO;

    public NotesAdapter(List<Note> notes, OnNoteClickListener onNoteClickListener, NotesDAO notesDAO) {
        this.notes = notes;
        this.onNoteClickListener = onNoteClickListener;
        this.notesDAO = notesDAO;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);

        // Обработчик долгого нажатия
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(note);
                return true;
            }
            return false;
        });

        // Установка данных в `textViewCategory` и окрашивание фона
        holder.textViewCategory.setText(note.getCategory());
        GradientDrawable bg = (GradientDrawable) holder.textViewCategory.getBackground();
        bg.setColor(getCategoryColor(note.getCategory()));


        // Обработчик удаления заметки
        holder.imageViewDelete.setOnClickListener(v -> {
            notesDAO.deleteNote(note.getId());
            updateData(notesDAO.getAllNotes("timestamp DESC"));
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewContent, textViewCategory;
        ImageView imageViewDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewNoteTitle);
            textViewContent = itemView.findViewById(R.id.textViewNoteContent);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onNoteClickListener.onNoteClick(notes.get(position));
                }
            });
        }

        public void bind(Note note) {
            textViewTitle.setText(note.getTitle());
            textViewContent.setText(note.getContent());
        }
    }

    // Интерфейсы для обработки кликов
    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(Note note);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public void updateData(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    // Метод для получения цвета по категории
    private int getCategoryColor(String category) {
        switch (category) {
            case "Здоровье": return Color.parseColor("#E91E63"); // Розовый
            case "Дом": return Color.parseColor("#9C27B0"); // Фиолетовый
            case "Хобби": return Color.parseColor("#FF4081"); // Светло-розовый
            case "Семья": return Color.parseColor("#673AB7"); // Темно-фиолетовый
            case "Работа": return Color.parseColor("#A94064"); //Темно-розовый
            case "Учёба": return Color.parseColor("#F88379"); //Кораловый
            default: return Color.GRAY; // Серый по умолчанию
        }
    }
}
