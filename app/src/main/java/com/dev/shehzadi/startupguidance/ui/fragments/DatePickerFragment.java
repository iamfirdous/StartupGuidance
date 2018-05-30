package com.dev.shehzadi.startupguidance.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.dev.shehzadi.startupguidance.R;

import java.util.Calendar;

/**
 * Created by shehzadi on 3/22/2018.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText editText;

    private int day, month, year;

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

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

        this.day = view.getDayOfMonth();
        this.month = view.getMonth();
        this.year = view.getYear();

        editText.setText(this.day+"-"+(this.month+1)+"-"+this.year);
    }
}