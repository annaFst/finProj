package com.example.bt.activities.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.bt.R;

import java.util.Objects;

public class RepeatDialog extends AppCompatDialogFragment {

    private RadioButton dailyBtn;
    private RadioButton weeklyBtn;
    private RadioButton monthlyBtn;
    private RadioButton noneBtn;
    private  repeatDialogListener listener;
    private boolean setDaily = false, setWeekly = false, setMonthly = false;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View newView = inflater.inflate(R.layout.repeat_dialog,null);

        build.setView(newView)
                .setTitle("Repeat")
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyChoice(noneBtn.isChecked(),dailyBtn.isChecked(),weeklyBtn.isChecked(),monthlyBtn.isChecked());
                    }
                });

        noneBtn = newView.findViewById(R.id.none);
        dailyBtn = newView.findViewById(R.id.daily);
        weeklyBtn = newView.findViewById(R.id.weekly);
        monthlyBtn = newView.findViewById(R.id.monthly);

        noneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyBtn.setChecked(false);
                weeklyBtn.setChecked(false);
                monthlyBtn.setChecked(false);
            }
        });
        dailyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noneBtn.setChecked(false);
                weeklyBtn.setChecked(false);
                monthlyBtn.setChecked(false);
            }
        });

        weeklyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noneBtn.setChecked(false);
                dailyBtn.setChecked(false);
                monthlyBtn.setChecked(false);
            }
        });

        monthlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noneBtn.setChecked(false);
                dailyBtn.setChecked(false);
                weeklyBtn.setChecked(false);
            }
        });



        return build.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (repeatDialogListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "have to implement repeatDialogListener ");

        }
    }

    public interface  repeatDialogListener{
        void applyChoice (boolean none , boolean daily, boolean weekly, boolean monyhly);
    }


}
