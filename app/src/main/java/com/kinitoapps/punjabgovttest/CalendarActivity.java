package com.kinitoapps.punjabgovttest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        final CompactCalendarView compactCalendarView = findViewById(R.id.compactcalendar_view);
        final TextView monthname = findViewById(R.id.month);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1539282600000L);
        monthname.setText(returnMonth(calendar.get(Calendar.MONTH))+" - "+calendar.get(Calendar.YEAR));
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        Event ev1 = new Event(Color.BLUE,1539282600000L);
        compactCalendarView.addEvent(ev1);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
//                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Toast.makeText(CalendarActivity.this, String.valueOf(dateClicked.getTime()), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                calendar.setTimeInMillis(firstDayOfNewMonth.getTime());
                monthname.setText(returnMonth(calendar.get(Calendar.MONTH))+" - "+calendar.get(Calendar.YEAR));


            }
        });


    }

    private String returnMonth(int month){
        if(month==0)
            return "January";
        else if(month == 1)
            return "February";
        else if(month == 2)
            return "March";
        else if(month == 3)
            return "April";
        else if(month == 4)
            return "May";
        else if(month == 5)
            return "June";
        else if(month == 6)
            return "July";
        else if(month == 7)
            return "August";
        else if(month == 8)
            return "September";
        else if(month == 9)
            return "October";
        else if(month == 10)
            return "November";
        else
            return "December";
    }
}
