package com.example.productivityapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddTask extends AppCompatDialogFragment {

    EditText taskInp;
    AddTaskListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_task_dialogue, null);

        builder.setView(view)
                .setTitle("Add Task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = taskInp.getText().toString();
                        listener.applyTexts(task);
                    }
                });

        taskInp = view.findViewById(R.id.taskInp);

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddTaskListener) context;
        }

        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }

    }

    public interface AddTaskListener{
        void applyTexts(String task);

    }


}
