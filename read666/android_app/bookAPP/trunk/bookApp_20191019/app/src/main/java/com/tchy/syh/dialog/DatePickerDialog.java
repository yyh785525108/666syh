package com.tchy.syh.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.tchy.syh.R;
import com.tchy.syh.custom.CustomCheckedBtn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.bus.Messenger;

public class DatePickerDialog extends DialogFragment {

    List<CustomCheckedBtn> checkBoxList = new ArrayList<>();
    public String date = "";

    public DatePickerDialog() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getDialog().getWindow();

        View view = inflater.inflate(R.layout.datepicker_dialog, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        final DatePicker datepicker = view.findViewById(R.id.datepicker);
        String dateParams = getArguments().getString("date");
        if(!dateParams.contains("-")){
            dateParams="1970-01-01";
        }
        datepicker.updateDate(Integer.parseInt(dateParams.split("-")[0]), Integer.parseInt(dateParams.split("-")[1]) - 1, Integer.parseInt(dateParams.split("-")[2]));
        final Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            String monthStr="";
            int month=(datepicker.getMonth() + 1);
            if(month<10){
                monthStr+="0";
            }
            monthStr+=month;
            date = datepicker.getYear() + "-" +monthStr+ "-" + datepicker.getDayOfMonth();
            Calendar calendar = Calendar.getInstance();
            calendar.set(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth());
            Messenger.getDefault().send(new DateBean(date,calendar.getTime()), "dateDialog");
            dismiss();
        });
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        params.windowAnimations = R.style.bottomSheet_animation;

        getDialog().getWindow().setAttributes(params);
        getDialog().setCanceledOnTouchOutside(false);


//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        return view;
    }

    public class DateBean {
        public String dateStr;
        public Date date;

        public DateBean(String dateStr, Date date) {
            this.date = date;
            this.dateStr = dateStr;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface onDateChangeListener {
        public void dataChange(String s);
    }
}
