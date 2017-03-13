package simone.it.appunti.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import simone.it.appunti.R;

import static simone.it.appunti.Activities.MainActivity.NOTE_DATE_KEY;
import static simone.it.appunti.Activities.MainActivity.NOTE_TEXT_KEY;
import static simone.it.appunti.Activities.MainActivity.NOTE_TITLE_KEY;

/**
 * Created by Simone on 13/03/2017.
 */

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {


    EditText titleET, dateET, textET, colorET;
    Calendar myCalendar;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteadd);
        titleET = (EditText) findViewById(R.id.titleET);
        dateET = (EditText) findViewById(R.id.dateET);
        textET = (EditText) findViewById(R.id.textET);
        colorET = (EditText) findViewById(R.id.colorET);
        dateET.setOnClickListener(this);
        colorET.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            if (getIntent() != null) {
                if (getIntent().getStringExtra(NOTE_TITLE_KEY) != null) {
                    titleET.setText(getIntent().getStringExtra(NOTE_TITLE_KEY));
                    textET.setText(getIntent().getStringExtra(NOTE_TEXT_KEY));
                    dateET.setText(getIntent().getStringExtra(MainActivity.NOTE_DATE_KEY));
                }
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                Intent intent = new Intent();
                intent.putExtra(NOTE_TITLE_KEY, titleET.getText().toString());
                intent.putExtra(NOTE_TEXT_KEY, textET.getText().toString());
                intent.putExtra(NOTE_DATE_KEY, dateET.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dateET) {
            myCalendar = Calendar.getInstance();
            new DatePickerDialog(AddNoteActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALIAN);

        dateET.setText(sdf.format(myCalendar.getTime()));
    }
}
