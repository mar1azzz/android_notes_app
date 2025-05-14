package com.example.notes.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
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

import java.util.Collections;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private final OnNoteClickListener onNoteClickListener;
    private final NotesDAO notesDAO;
    private OnItemLongClickListener onItemLongClickListener;

    public NotesAdapter(List<Note> notes,
                        OnNoteClickListener onNoteClickListener,
                        NotesDAO notesDAO) {
        this.notes = notes;
        this.onNoteClickListener = onNoteClickListener;
        this.notesDAO = notesDAO;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        // 1. Заголовок
        holder.textViewTitle.setText(note.getTitle());

        // 2. Содержимое (HTML → Spanned)
        String html = note.getContent();
        if (html != null) {
            holder.textViewContent.setText(
                    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            );
        } else {
            holder.textViewContent.setText("");
        }

        // 3. Категория + фон
        holder.textViewCategory.setText(note.getCategory());
        GradientDrawable bg = (GradientDrawable) holder.textViewCategory.getBackground();
        bg.setColor(getCategoryColor(note.getCategory()));

        // 4. Удаление по иконке
        holder.imageViewDelete.setOnClickListener(v -> {
            notesDAO.deleteNote(note.getId());
            updateData(notesDAO.getAllNotes("timestamp DESC"));
        });

        // 5. Долгое нажатие для удаления (можно оставить, если нужно)
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(note);
                return true;
            }
            return false;
        });

        // 6. Обычный клик для редактирования
        holder.itemView.setOnClickListener(v ->
                onNoteClickListener.onNoteClick(note)
        );
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void updateData(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    public void moveItem(int fromPosition, int toPosition) {
        Collections.swap(notes, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    // Получение цвета по категории
    private int getCategoryColor(String category) {
        switch (category) {
            case "Здоровье": return Color.parseColor("#E91E63");
            case "Дом":      return Color.parseColor("#9C27B0");
            case "Хобби":    return Color.parseColor("#FF4081");
            case "Семья":    return Color.parseColor("#673AB7");
            case "Работа":   return Color.parseColor("#A94064");
            case "Учёба":    return Color.parseColor("#F88379");
            default:         return Color.GRAY;
        }
    }

    // ViewHolder
    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewContent;
        TextView textViewCategory;
        ImageView imageViewDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle    = itemView.findViewById(R.id.textViewNoteTitle);
            textViewContent  = itemView.findViewById(R.id.textViewNoteContent);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            imageViewDelete  = itemView.findViewById(R.id.imageViewDelete);
        }
    }

    // Интерфейсы для кликов
    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Note note);
    }
}
