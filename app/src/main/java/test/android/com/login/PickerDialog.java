package test.android.com.login;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by u189299 on 13/09/2016.
 */
public class PickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText txtDate;
    char s;

    public PickerDialog (){

        this.txtDate = FormObj.getInstance().getTextForPicker();
        this.s = FormObj.getInstance().getTypeOfPicker();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

            DatePickerDialog dialog;
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (s == 'm'){

                dialog =  new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog, this, year,month, day);
                dialog.getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);

            }else{

                dialog =  new DatePickerDialog(getActivity(), this, year,month, day);


            }


            return dialog;


    }

    public  void onDateSet (DatePicker view, int year, int month, int day){

        if(s == 'm'){

            String date = refactor((month+1))+"-"+year;
            txtDate.setText(date);

        }else{

            String date = refactor(day)+"-"+refactor((month+1))+"-"+year;
            txtDate.setText(date);
        }

    }

    private String refactor(int d) {

        switch (d){

            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return "0"+d;

            default:
                return String.valueOf(d);
        }
    }
}
