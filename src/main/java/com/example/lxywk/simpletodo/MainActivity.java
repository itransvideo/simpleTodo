package com.example.lxywk.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 20;

    ArrayList<String> item;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    int CurrentId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readItems();
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        //item = new ArrayList<>();
        itemsAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,item);
        lvItems.setAdapter(itemsAdapter);
        //item.add("first item");
        //item.add("second item");
        setupListViewListener();
    }
    public void onAddItem(View V){
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }
    public void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> Adapter,View item1, int pos, long id){
                item.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                String data = item.get((int)id).toString();
                CurrentId = (int)id;
                i.putExtra("data", data);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String name = data.getExtras().getString("name");
            int code = data.getExtras().getInt("code", 0);
            item.set(CurrentId,name);
            // Toast the name to display temporarily on screen
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            writeItems();
        }
    }
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFiles = new File(filesDir,"todo.txt");
        try{
            item = new ArrayList<String>(FileUtils.readLines(todoFiles));
        }catch(IOException e){
            item = new ArrayList<String>();
            Toast.makeText(this, "open file error", Toast.LENGTH_SHORT).show();
        }
    }
    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFiles = new File(filesDir,"todo.txt");
        try{
            FileUtils.writeLines(todoFiles,item);
        }catch(IOException e){
            e.printStackTrace();
            String err = e.getStackTrace().toString();
            Toast.makeText(this, err, Toast.LENGTH_LONG).show();
        }
    }
}
