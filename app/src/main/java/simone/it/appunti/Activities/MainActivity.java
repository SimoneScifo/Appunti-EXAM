package simone.it.appunti.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import simone.it.appunti.Adapters.NoteAdapter;
import simone.it.appunti.DatabaseHandler.DatabaseHandler;
import simone.it.appunti.Models.Note;
import simone.it.appunti.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    public static final int REQUEST_ADD = 1001;
    public static final int REQUEST_DELETE = 1002;
    public static final int REQUEST_EDIT = 1003;
    public static final String NOTE_TITLE_KEY ="NOTE_TITLE_KEY" ;
    public static final String NOTE_TEXT_KEY = "NOTE_TEXT_KEY";
    private static final String NOTE_COLOR_KEY ="NOTE_COLOR_KEY" ;
    public static final String NOTE_DATE_KEY = "NOTE_DATE_KEY";

    //Costanti per modificare layout
    private static final String LAYOUT_MANAGER_KEY = "LAYOUT_MANAGER_KEY";
    private int STAGGERED_LAYOUT = 1;
    private int LINEAR_LAYOUT = 2;
    private int layoutManagerType = LINEAR_LAYOUT;
    private int orderManager = 0;
    private int DESC_ORDER = 4;
    private int ASC_ORDER = 5;



    EditText searchNoteET;
    FloatingActionButton btnAdd;


    Note Note = new Note();
    Note editNote = new Note ();

    RecyclerView.LayoutManager layoutManager;
    NoteAdapter adapter;
    RecyclerView noteRV;
    DatabaseHandler database;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noteRV = (RecyclerView) findViewById(R.id.notes_rv);
        searchNoteET = (EditText) findViewById(R.id.searchNoteET);
        layoutManager = getSavedLayoutManager();
        adapter = new NoteAdapter();
        noteRV.setAdapter(adapter);
        noteRV.setLayoutManager(layoutManager);
        database = new DatabaseHandler(this);
        adapter.setDataSet(database.getAllNotes());

        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        searchNoteET.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        ((Activity) v.getContext()).startActivityForResult((new Intent(v.getContext(), AddNoteActivity.class)), REQUEST_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            Note.setTitle(data.getStringExtra(NOTE_TITLE_KEY));
            Note.setText(data.getStringExtra(NOTE_TEXT_KEY));
            Note.setDate(data.getStringExtra(NOTE_DATE_KEY));
            Note.setColor(data.getStringExtra(NOTE_COLOR_KEY));
            noteRV.scrollToPosition(0);
            database.addNote(Note);
            adapter.addNote(Note);
            Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == REQUEST_DELETE && resultCode == RESULT_OK) {
            //remove record
            database.deleteNote(adapter.getNote(adapter.getPosition()));
            // remove from adapter
            adapter.deleteNote(adapter.getPosition());
            Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            editNote.setTitle(data.getStringExtra(NOTE_TITLE_KEY));
            editNote.setText(data.getStringExtra(NOTE_TEXT_KEY));
            editNote.setDate(data.getStringExtra(NOTE_DATE_KEY));
            editNote.setColor(data.getStringExtra(NOTE_COLOR_KEY));
            noteRV.scrollToPosition(0);

            adapter.updateNote(editNote, adapter.getPosition());
            database.updateNote(editNote);

            Toast.makeText(getApplicationContext(), "Edit success", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.setDataSet(database.getSearchNotes(s));
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(searchNoteET.getText().toString().equals("") || searchNoteET.getText().toString().equals(" ")){
            searchNoteET.setHint("Cerca");
            adapter.setDataSet(database.getAllNotes());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
                database.deleteNote(adapter.getNote(adapter.getPosition()));
                adapter.deleteNote(adapter.getPosition());
                break;

            case R.id.action_edit:

                editNote = adapter.getNote(adapter.getPosition());
                Intent intent = new Intent(this, AddNoteActivity.class);
                intent.putExtra(NOTE_TITLE_KEY, editNote.getTitle());
                intent.putExtra(NOTE_TEXT_KEY, editNote.getText());
                intent.putExtra(NOTE_DATE_KEY, editNote.getDate());
                intent.putExtra(NOTE_COLOR_KEY, editNote.getColor());
                startActivityForResult(intent, REQUEST_EDIT);
                break;

        }

        return super.onContextItemSelected(item);
    }
    private RecyclerView.LayoutManager getSavedLayoutManager() {
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int layoutManager = sharedPrefs.getInt(LAYOUT_MANAGER_KEY, -1);
        if (layoutManager == STAGGERED_LAYOUT) {
            setLayoutManagerType(layoutManager);
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }
        if (layoutManager == LINEAR_LAYOUT) {
            setLayoutManagerType(layoutManager);
            return new LinearLayoutManager(this);
        }
        return new LinearLayoutManager(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_layoutSTAG) {
            if (getLayoutManagerType() == STAGGERED_LAYOUT) {
                setLayoutManagerType(LINEAR_LAYOUT);
                noteRV.setLayoutManager(new LinearLayoutManager(this));
                item.setIcon(R.drawable.view_dashboard);


            } else {
                setLayoutManagerType(STAGGERED_LAYOUT);
                noteRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                item.setIcon(R.drawable.view_list);

            }

        }
        if(id ==R.id.action_order){
            if (orderManager == ASC_ORDER || orderManager == 0) {
                adapter.setDataSet(database.getOrderDescNotes());
                item.setIcon(R.drawable.down);
                this.orderManager = DESC_ORDER;
            }
            else if (orderManager == DESC_ORDER){
                adapter.setDataSet(database.getOrderAscNotes());
                item.setIcon(R.drawable.up);
                this.orderManager = ASC_ORDER;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public int getLayoutManagerType() {

        return layoutManagerType;
    }

    public void setLayoutManagerType(int layoutManagerType) {
        this.layoutManagerType = layoutManagerType;
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences layoutPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = layoutPreferences.edit();
        editor.putInt(LAYOUT_MANAGER_KEY, getLayoutManagerType());
        editor.apply();
    }
}