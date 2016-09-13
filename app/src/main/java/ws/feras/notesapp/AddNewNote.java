package ws.feras.notesapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

public class AddNewNote extends Activity {
    int currentPos = -1;
    EditText editInputText;
    SharedPreferences getSavedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
       getActionBar().setDisplayHomeAsUpEnabled(true);

         editInputText  = (EditText) findViewById(R.id.editText);
        getSavedData = this.getSharedPreferences(getPackageName(),MODE_PRIVATE);

        Intent checkIntent = getIntent();
         currentPos = checkIntent.getIntExtra("position", -1);

        if (currentPos != -1)
            {
             editInputText.setText(MainActivity.dataList.get(currentPos));
              }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            if (editInputText.getText().length() != 0) {
                if (currentPos == -1) {
                    insertNewData(editInputText.getText().toString());

                } else {
                    updateData(editInputText.getText().toString(), MainActivity.idsList.get(currentPos));
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertNewData(String s){

        try {
            SQLiteDatabase myDB = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS StoreNotes('id' INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR)");

            myDB.execSQL("INSERT INTO StoreNotes (title) VALUES ('"+ s +  "')");

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateData(String s, int index){

        try {
            SQLiteDatabase myDB = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS StoreNotes('id' INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR)");

            myDB.execSQL("UPDATE StoreNotes SET title='"+ s +  "' WHERE id=" +index+"");

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
