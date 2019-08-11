package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ToDoActivity extends AppCompatActivity {

    BottomSheetDialog dialog;
    EditText todoText;
    private NoteViewModel noteViewModel;
    boolean isText = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        dialog = new BottomSheetDialog(ToDoActivity.this);
        dialog.setContentView(R.layout.dialog_sheet);
        findViewById(R.id.add_todo).setOnClickListener(v -> {
            dialog.show();
        });

        findViewById(R.id.add_todo).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(ToDoActivity.this, MainActivity.class));
                return true;
            }
        });

        todoText = dialog.findViewById(R.id.edit_text_title);
        dialog.findViewById(R.id.save_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Note note = new Note(1,
                        todoText.getText().toString(),
                        "",
                        String.valueOf((System.currentTimeMillis())),
                        "",
                        false,
                        "",
                        "");
                noteViewModel.insert(note);
                todoText.setText("");
                dialog.dismiss();
            }
        });

        todoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isText = true;
                    todoText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_24dp, 0);
                } else {
                    isText = false;
                    todoText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_deactive_24dp, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        todoText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (todoText.getRight() - todoText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here

                    if (isText)
                        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            return false;
        });

    }
}
