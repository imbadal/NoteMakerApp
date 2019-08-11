package com.example.noteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();
    OnItemClickListner listner;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDesc.setText(currentNote.getDesc());

        String date_time = getDateTime(currentNote.getTimeStamp());
        holder.textViewTime.setText(date_time);
        holder.textViewPriority.setText(currentNote.getPriority());
    }

    private String getDateTime(String timeStamp) {

        long currentTimeMills = System.currentTimeMillis();

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        String dateString = formatter.format(new Date(Long.parseLong(timeStamp)));

        Date date = new Date(Long.valueOf(timeStamp));
        DateFormat formatter_d = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter_d.format(date);

        if (currentTimeMills - Long.valueOf(timeStamp) < (long) 86400000) {
            SimpleDateFormat formatter_ = new SimpleDateFormat("dd");
            String date_ = formatter_.format(new Date(Long.parseLong(timeStamp)));
            String date_c = formatter_.format(new Date(currentTimeMills));
            if (date_.equals(date_c))
                return "Today" + ", " + dateFormatted;
            else
                return "Yesterday" + ", " + dateFormatted;
        }


        return dateString + ", " + dateFormatted;
    }

    public void setNotes(List<Note> notes) {

        this.notes = notes;
        notifyDataSetChanged();

    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, textViewDesc, textViewPriority, textViewTime;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDesc = itemView.findViewById(R.id.text_view_desc);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listner != null && position != RecyclerView.NO_POSITION) {
                        listner.onItemClick(notes.get(position));
                    }
                }
            });

        }
    }

    public interface OnItemClickListner {
        void onItemClick(Note note);
    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        this.listner = listner;
    }

}
