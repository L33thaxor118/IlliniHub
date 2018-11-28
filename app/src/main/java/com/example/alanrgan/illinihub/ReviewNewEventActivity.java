package com.example.alanrgan.illinihub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReviewNewEventActivity extends AppCompatActivity implements View.OnClickListener{
  //Intent keys
  public static final String LOCATION_EXTRA = "final_location";
  public static final String TITLE_EXTRA = "final_title";
  public static final String DESC_EXTRA = "final_description";
  public static final String START_TIME_EXTRA = "final_start_time";
  public static final String END_TIME_EXTRA = "final_end_time";
  public static final String TAGS_EXTRA = "final_tags";

  //Intent data
  private long selectedStartDate;
  private long selectedEndDate;
  private ArrayList<String> selectedTags;
  private double[] selectedLocation;
  private String selectedTitle;
  private String selectedDescription;

  EditText startDateTextEdit;
  EditText endDateTextEdit;
  EditText startTimeTextEdit;
  EditText endTimeTextEdit;
  Button nextButton;
  EditText titleTextEdit;
  EditText descriptionTextEdit;

  String beginStartDate;
  String beginStartTime;
  String beginEndDate;
  String beginEndTime;

  private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
  private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_review_new_event);
    Intent intent = getIntent();

    selectedTitle = intent.getStringExtra(SelectTagsActivity.TITLE_EXTRA);
    selectedDescription = intent.getStringExtra(SelectTagsActivity.DESC_EXTRA);
    selectedLocation = intent.getDoubleArrayExtra(SelectTagsActivity.LOCATION_EXTRA);
    selectedTags = intent.getStringArrayListExtra((SelectTagsActivity.TAGS_EXTRA));
    selectedStartDate = intent.getLongExtra(SelectTagsActivity.START_TIME_EXTRA, 0);
    selectedEndDate = intent.getLongExtra(SelectTagsActivity.END_TIME_EXTRA, 0);

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
    titleTextEdit = (EditText) findViewById(R.id.title);
    descriptionTextEdit = (EditText) findViewById(R.id.description);

    titleTextEdit.setText(selectedTitle);
    descriptionTextEdit.setText(selectedDescription);
    Date startDate = new Date(selectedStartDate);
    Date endDate = new Date(selectedEndDate);

    beginStartDate = new String(startDate.getDay() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getYear());
    beginStartTime = new String(startDate.getHours() + ":" + startDate.getMinutes());
    beginEndDate = new String(endDate.getDay() + "-" + (endDate.getMonth() + 1) + "-" + endDate.getYear());
    beginEndTime = new String(endDate.getHours() + ":" + endDate.getMinutes());

    startDateTextEdit.setText(beginStartDate);
    endDateTextEdit.setText(beginEndDate);
    startTimeTextEdit.setText(beginStartTime);
    endTimeTextEdit.setText(beginEndTime);

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
      done();
    }
  }

  public void done(){
    Intent intent = new Intent();
    if (!titleTextEdit.getText().toString().equals(selectedTitle)){
      intent.putExtra(TITLE_EXTRA, titleTextEdit.getText().toString());
    } else intent.putExtra(TITLE_EXTRA, selectedTitle);

    if (!descriptionTextEdit.getText().toString().equals(selectedDescription)){
      intent.putExtra(DESC_EXTRA, descriptionTextEdit.getText().toString());
    } else intent.putExtra(DESC_EXTRA, selectedDescription);

    if (beginStartDate.equals(startDateTextEdit.getText().toString()) & beginStartTime.equals(startTimeTextEdit.getText().toString())){
      intent.putExtra(START_TIME_EXTRA, selectedStartDate);
    } else intent.putExtra(START_TIME_EXTRA, new Date(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute).getTime());

    if (beginEndDate.equals(endDateTextEdit.getText().toString()) & beginEndTime.equals(endTimeTextEdit.getText().toString())){
      intent.putExtra(END_TIME_EXTRA, selectedEndDate);
    } else intent.putExtra(END_TIME_EXTRA, new Date(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute).getTime());

    intent.putExtra(LOCATION_EXTRA, selectedLocation);
    intent.putExtra(TAGS_EXTRA, selectedTags);
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 9) {
      if (resultCode == RESULT_OK) {

      }
    }
  }

}
