package com.dev.firdous.startupguidance.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.dev.firdous.startupguidance.R;

import java.util.Calendar;

/**
 * Created by Firdous on 3/22/2018.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText etDOB;

    private int day, month, year;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), R.style.AppTheme_DatePicker, this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        String tag = getTag();

        etDOB = getActivity().findViewById(R.id.editText_dob_signup);

        this.day = view.getDayOfMonth();
        this.month = view.getMonth();
        this.year = view.getYear();

        etDOB.setText(this.day+"-"+(this.month+1)+"-"+this.year);
    }
}