package com.kinitoapps.punjabgovttest;

import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {
    String URL_DATES = "http://ngo-link.com/android_api/userdatelist.php";
    String URL_CALENDAR_JOBS = "http://ngo-link.com/android_api/calendar_joblist.php";

    CompactCalendarView compactCalendarView;
    SQLiteHandler db;
    ImageView right,left;
    HashMap<String,String> hashMap;
    JobsAdapter jobsAdapter;
    RecyclerView recyclerView;
    List<Jobs> jobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        hashMap = new HashMap<>();
        db = new SQLiteHandler(getApplicationContext());
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Job Calendar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        jobsList = new ArrayList<>();
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        hashMap.put("course",db.getUserDetails().get("course"));
        hashMap.put("field",db.getUserDetails().get("field"));
        hashMap.put("percentage",db.getUserDetails().get("percentage"));

        compactCalendarView = findViewById(R.id.compactcalendar_view);
        final TextView monthname = findViewById(R.id.month);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        final TextView date = findViewById(R.id.date);
        int month =calendar.get(Calendar.MONTH)+1;
        date.setText(calendar.get(Calendar.DAY_OF_MONTH)+"-"+month+"-"+calendar.get(Calendar.YEAR));
        monthname.setText(returnMonth(calendar.get(Calendar.MONTH))+" - "+calendar.get(Calendar.YEAR));

        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        recyclerView = findViewById(R.id.recyclerViewCalendar);
        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                recyclerView.dispatchNestedFling(velocityX, velocityY, false);
                return false;
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        jobsAdapter = new JobsAdapter(this,jobsList);
        recyclerView.setAdapter(jobsAdapter);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
//                List<Event> events = compactCalendarView.getEvents(dateClicked);
                calendar.setTime(dateClicked);
                jobsList.clear();
                jobsAdapter.notifyDataSetChanged();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
                String stringdate = df.format(dateClicked);
                DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
                date.setText(df2.format(dateClicked));
                populateRecyclerView(stringdate);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                calendar.setTime(firstDayOfNewMonth);
                monthname.setText(returnMonth(calendar.get(Calendar.MONTH))+" - "+calendar.get(Calendar.YEAR));
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollRight();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollLeft();
            }
        });
        setCalendarEvents();
        hashMap.put("date",calendar.get(Calendar.YEAR)+"-"+month+"-"+calendar.get(Calendar.DAY_OF_MONTH));
        populateRecyclerView(calendar.get(Calendar.YEAR)+"-"+month+"-"+calendar.get(Calendar.DAY_OF_MONTH));
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

    private void setCalendarEvents() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

//        pDialog.setMessage("Logging in ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_DATES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    if(!jObj.getBoolean("error")) {
                        JSONArray array = jObj.getJSONArray("array");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            String date = jsonObject1.getString("date");
//                            LocalDateTime localDateTime = LocalDateTime.parse(date,
//                                    DateTimeFormatter.ofPattern("yyyy-MM-dd") );
//                            long millis = localDateTime
//                                    .atZone(ZoneId.of("Asia/Calcutta"))
//                                    .toInstant().toEpochMilli();
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            cal.setTime(sdf.parse(date));
                            Event ev1 = new Event(Color.BLUE,cal.getTimeInMillis());
                            compactCalendarView.addEvent(ev1);

                        }
                    }
                    else{
                        String error = jObj.getString("error_msg");
//                        Toast.makeText(JobsActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    // Inserting row in users table


                    // Launch main activity

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                return hashMap;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void populateRecyclerView(final String date) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

//        pDialog.setMessage("Logging in ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_CALENDAR_JOBS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    if(!jObj.getBoolean("error")) {
                        JSONArray array = jObj.getJSONArray("array");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            jobsList.add(new Jobs(jsonObject1.getString("job"), jsonObject1.getString("org"),
                                    jsonObject1.getString("logolink"), jsonObject1.getString("id")));
                        }

                        jobsAdapter = new JobsAdapter(CalendarActivity.this, jobsList);
                        recyclerView.setAdapter(jobsAdapter);
                    }
                    else{
                        String error = jObj.getString("error_msg");
                        Toast.makeText(CalendarActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    // Inserting row in users table


                    // Launch main activity

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                hashMap.put("date",date);
                return hashMap;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
