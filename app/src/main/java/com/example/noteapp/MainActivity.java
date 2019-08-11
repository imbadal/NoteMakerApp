package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> {

            adapter.setNotes(notes);
//                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Removed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListner(new NoteAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra("key", note);
                intent.putExtra(AddEditNoteActivity.ID, note.getId());
                intent.putExtra(AddEditNoteActivity.NOTIFICATION_ID, note.getNotificationid());
                intent.putExtra(AddEditNoteActivity.TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.DESC, note.getDesc());
                intent.putExtra(AddEditNoteActivity.TIME, note.getTimeStamp());
                intent.putExtra(AddEditNoteActivity.PRIORITY, note.getPriority());
                intent.putExtra(AddEditNoteActivity.NOTIFY, note.isNotify());
                intent.putExtra(AddEditNoteActivity.NOTIFY_DATE, note.getNotify_date());
                intent.putExtra(AddEditNoteActivity.NOTIFY_TIME, note.getNotify_time());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {

            long notification_id = data.getLongExtra(AddEditNoteActivity.NOTIFICATION_ID, 0);
            if (notification_id == 0)
                notification_id = System.currentTimeMillis();

            String title = data.getStringExtra(AddEditNoteActivity.TITLE);
            String desc = data.getStringExtra(AddEditNoteActivity.DESC);
            String timeStamp = data.getStringExtra(AddEditNoteActivity.TIME);
            String priority = data.getStringExtra(AddEditNoteActivity.PRIORITY);
            boolean notify = data.getBooleanExtra(AddEditNoteActivity.NOTIFY, false);
            String notify_date = data.getStringExtra(AddEditNoteActivity.NOTIFY_DATE);
            String notify_time = data.getStringExtra(AddEditNoteActivity.NOTIFY_TIME);

            Note note = new Note(notification_id, title, desc, timeStamp, priority, notify, notify_date, notify_time);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.TITLE);
            String desc = data.getStringExtra(AddEditNoteActivity.DESC);
            String timeStamp = data.getStringExtra(AddEditNoteActivity.TIME);
            String priority = data.getStringExtra(AddEditNoteActivity.PRIORITY);
            boolean notify = data.getBooleanExtra(AddEditNoteActivity.NOTIFY, false);
            long notification_id = data.getLongExtra(AddEditNoteActivity.NOTIFICATION_ID, 0);
            if (notification_id == 0)
                notification_id = System.currentTimeMillis();

            String notify_date = data.getStringExtra(AddEditNoteActivity.NOTIFY_DATE);
            String notify_time = data.getStringExtra(AddEditNoteActivity.NOTIFY_TIME);

            Note note = new Note(notification_id, title, desc, timeStamp, priority, notify, notify_date, notify_time);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Failed to Update...", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


