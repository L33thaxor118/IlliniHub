package com.example.alanrgan.illinihub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {
  //Intent keys
  public static final String LOCATION_EXTRA = "create_event_location";
  public static final String TITLE_EXTRA = "create_event_title";
  public static final String DESC_EXTRA = "create_event_description";
  public static final String START_TIME_EXTRA = "create_event_start_time";
  public static final String END_TIME_EXTRA = "create_event_end_time";
  public static final String TAGS_EXTRA = "create_event_tags";

  //Intent data
  private long selectedStartDate;
  private long selectedEndDate;
  private ArrayList<String> selectedTags;
  private double[] selectedLocation;
  private String selectedTitle;
  private String selectedDescription;

  private Date startDate;
  private Date endDate;

  EditText startDateTextEdit;
  EditText endDateTextEdit;
  EditText startTimeTextEdit;
  EditText endTimeTextEdit;
  Button nextButton;
  private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
  private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTitle("New Event General Info");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_event);
    nextButton = (Button)findViewById(R.id.nextButton);
    startDateTextEdit = (EditText) findViewById(R.id.startDate);
    endDateTextEdit = (EditText) findViewById(R.id.endDate);
    startTimeTextEdit = (EditText) findViewById(R.id.startTime);
    endTimeTextEdit = (EditText) findViewById(R.id.endTime);
    startDateTextEdit.setOnClickListener(this);
    endDateTextEdit.setOnClickListener(this);
    startTimeTextEdit.setOnClickListener(this);
    endTimeTextEdit.setOnClickListener(this);
    nextButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v == startDateTextEdit) {
      final Calendar c = Calendar.getInstance();
      mStartYear = c.get(Calendar.YEAR);
      mStartMonth = c.get(Calendar.MONTH);
      mStartDay = c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
          startDateTextEdit.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
          mStartDay = dayOfMonth;
          mStartMonth = monthOfYear;
          mStartYear = year;
        }
      }, mStartYear, mStartMonth, mStartDay);
      datePickerDialog.show();
    }
    if (v == startTimeTextEdit) {
      final Calendar c = Calendar.getInstance();
      mStartHour = c.get(Calendar.HOUR_OF_DAY);
      mStartMinute = c.get(Calendar.MINUTE);

      TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
          startTimeTextEdit.setText(hourOfDay + ":" + minute);
          mStartHour = hourOfDay;
          mStartMinute = minute;
        }
      }, mStartHour, mStartMinute, false);
      timePickerDialog.show();
    }
    if (v == endDateTextEdit) {
      final Calendar c = Calendar.getInstance();
      mEndYear = c.get(Calendar.YEAR);
      mEndMonth = c.get(Calendar.MONTH);
      mEndDay = c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
          endDateTextEdit.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
          mEndDay = dayOfMonth;
          mEndMonth = monthOfYear;
          mEndYear = year;
        }
      }, mEndYear, mEndMonth, mEndDay);
      datePickerDialog.show();
    }
    if (v == endTimeTextEdit) {
      final Calendar c = Calendar.getInstance();
      mEndHour = c.get(Calendar.HOUR_OF_DAY);
      mEndMinute = c.get(Calendar.MINUTE);

      TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
          endTimeTextEdit.setText(hourOfDay + ":" + minute);
          mEndHour = hourOfDay;
          mEndMinute = minute;
        }
      }, mEndHour, mEndMinute, false);
      timePickerDialog.show();
    }
    if (v == nextButton){
      EditText title = (EditText) findViewById(R.id.title);
      EditText description = (EditText) findViewById(R.id.description);
      startDate = new Date(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute);
      endDate = new Date(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute);
      Intent intent = new Intent(this, SelectLocationActivity.class);
      intent.putExtra(TITLE_EXTRA, title.getText().toString());
      intent.putExtra(DESC_EXTRA, description.getText().toString());
      intent.putExtra(START_TIME_EXTRA, startDate.getTime());
      intent.putExtra(END_TIME_EXTRA, endDate.getTime());
      Log.i("9136319 first startdate", String.valueOf(startDate.getTime()));
      Log.i("9136319 first enddate", String.valueOf(endDate.getTime()));
      startActivityForResult(intent,2);
    }
    }


  public void done(){
    Intent intent = new Intent();
    intent.putExtra(TITLE_EXTRA, selectedTitle);
    intent.putExtra(DESC_EXTRA, selectedDescription);
    intent.putExtra(START_TIME_EXTRA, selectedStartDate);
    intent.putExtra(END_TIME_EXTRA, selectedEndDate);
    intent.putExtra(LOCATION_EXTRA, selectedLocation);
    intent.putExtra(TAGS_EXTRA, selectedTags);
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 2) {
      if (resultCode == RESULT_OK) {
        selectedLocation = data.getDoubleArrayExtra(SelectLocationActivity.LOCATION_EXTRA);
        selectedTitle = data.getStringExtra(SelectLocationActivity.TITLE_EXTRA);
        selectedDescription = data.getStringExtra(SelectLocationActivity.DESC_EXTRA);
        selectedStartDate = data.getLongExtra(SelectLocationActivity.START_TIME_EXTRA, 0);
        selectedEndDate = data.getLongExtra(SelectLocationActivity.END_TIME_EXTRA, 0);
        selectedTags = data.getStringArrayListExtra(SelectLocationActivity.TAGS_EXTRA);
        done();
      }
    }
  }

  }
