package ws.feras.notesapp;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends Activity {
    static ArrayList<String> dataList;
    static ArrayList<Integer> idsList;
    static ArrayAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();
        idsList = new ArrayList<>();
        if (getDataFromDataBase() == false)
        {
            dataList.add("Click To Edit Me :) ");
            idsList.add(-1);
            dataList.add("Long Click To Delete Me :) ");
            idsList.add(-1);
        }


        final ListView myList = (ListView) findViewById(R.id.listView);



         myAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, dataList);
        myList.setAdapter(myAdapter);

    myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete")
                    .setMessage("Do You Want To Delete it?")
                    .setIcon(android.R.drawable.ic_input_delete)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i1) {
                            dataList.remove(i);
                            int currentItemsID = idsList.get(i);
                            if (currentItemsID != -1)
                                deleteFromDataBase(currentItemsID);
                            idsList.remove(i);
                            myAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("No", null).show();

            return true;
        }
    });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goToEdit = new Intent(getBaseContext(), AddNewNote.class);
                goToEdit.putExtra("position", i);
                startActivity(goToEdit);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuAddBtn)
        {
            Intent addNewIntent = new Intent(getBaseContext(), AddNewNote.class);
            addNewIntent.putExtra("position", -1);
            startActivity(addNewIntent);
        }
        return super.onOptionsItemSelected(item);
    }

     public boolean getDataFromDataBase(){
        idsList.clear();
         dataList.clear();
        try {
             SQLiteDatabase myDB = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS StoreNotes('id' INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR)");

            Cursor getData = myDB.rawQuery("SELECT * FROM StoreNotes", null);

            int titlesCol = getData.getColumnIndex("title");
            int idsCol = getData.getColumnIndex("id");

            getData.moveToFirst();

            if (getData.getCount() == 0) {
            return false;
            } else {
                while (getData != null) {
                    dataList.add(getData.getString(titlesCol));
                    idsList.add(getData.getInt(idsCol));
                    getData.moveToNext();
                }
                myAdapter.notifyDataSetChanged();
            }
            }catch(Exception e)
            {
                e.printStackTrace();
            }

        return true;

    }
    public void deleteFromDataBase(int idNo)
    {
        SQLiteDatabase myDB = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS StoreNotes(id INT(9) PRIMARY KEY,title VARCHAR)");

        myDB.execSQL("DELETE FROM StoreNotes WHERE id='"+ idNo  +"'");
    }

}
