package test.android.com.login;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Created by Nicolas Scordamaglia on 13/09/2016.
 */
public class DateCallBack implements DatePickerDialog.OnDateSetListener {

    Context context;


    public DateCallBack(Context context) {
        this.context = context;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        if(monthOfYear<9){

            int month = monthOfYear + 1;
            FormObj.getInstance().getRq().onTaskComplete("0"+month+"-"+year,"datepicker");

        }else{

            int month = monthOfYear + 1;
            FormObj.getInstance().getRq().onTaskComplete(month+"-"+year,"datepicker");
        }


    }
}
