package tech.extropy.dennis.compoundinterestcalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import tech.extropy.dennis.compoundinterestcalculator.Model.DatabaseHelper;
import tech.extropy.dennis.compoundinterestcalculator.Controller.Stack;

/**
 * Created by dennis on 1/26/18.
 */

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;

    private ListView mListView;
    final Context context1 = this;
    String classType;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("All Interest Files");
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);
        intent = getIntent();

        populateListView();
    }

    @Override
    public void onBackPressed(){
        Stack newStack = new Stack();

        int[] stackArr = intent.getIntArrayExtra("intArr");
        newStack.setStackArr(stackArr);
        int top = intent.getIntExtra("top",9999);
        newStack.setTop(top);
        newStack.pop();

        int formulaType = intent.getIntExtra("type",9999);
        Log.d("newStack.peek() :",""+newStack.peek());
        if(newStack.peek() == 100){
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("type", formulaType);
            intent.putExtra("intArr",stackArr);
            intent.putExtra("top",newStack.getTop());
            startActivity(intent);
            finish();
        } else if(newStack.peek() == 6) {
            //formulaType = 0;
            Intent intent = new Intent(this, CompoundInterestAnnualAddition.class);
            intent.putExtra("type", formulaType);
            intent.putExtra("intArr",stackArr);
            intent.putExtra("top",newStack.getTop());
            startActivity(intent);
            finish();
        } else if(newStack.peek() == 1) {
            //formulaType = 0;
            Intent intent = new Intent(this, AnnualCompoundInterest.class);
            intent.putExtra("type", formulaType);
            intent.putExtra("intArr",stackArr);
            intent.putExtra("top",newStack.getTop());
            startActivity(intent);
            finish();
        } else if(newStack.peek() == 2) {
        //formulaType = 0;
        Intent intent = new Intent(this, SimpleInterestActivity.class);
        intent.putExtra("type", formulaType);
        intent.putExtra("intArr",stackArr);
        intent.putExtra("top",newStack.getTop());
        startActivity(intent);
        finish();
        } else if(newStack.peek() == 3) {
        //formulaType = 0;
        Intent intent = new Intent(this, ContinuouslyCompoundedActivity.class);
        intent.putExtra("type", formulaType);
        intent.putExtra("intArr",stackArr);
        intent.putExtra("top",newStack.getTop());
        startActivity(intent);
        finish();
    }
    }

    private void populateListView() {

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }

//TEST
        //get the data and append to a list
        Cursor data2 = mDatabaseHelper.getData();
        while (data2.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
           Log.d("class_type54789",""+data2.getString(8));//It should display class type!
        }
//END TEST

        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();

                Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name

                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                displayWindow(name, itemID);
            }
        });
    }

    public void displayWindow(final String name, int itemID) {
// get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context1);
        View promptsView = li.inflate(R.layout.custom2, null);
        final String name2 = name;
        final int itemID2 = itemID;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(itemID2 > -1) {
                                    Stack newStack = new Stack();

                                    int[] stackArr = intent.getIntArrayExtra("intArr");
                                    newStack.setStackArr(stackArr);
                                    int top = intent.getIntExtra("top",9999);
                                    newStack.setTop(top);
                                    newStack.push(10);

                                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                                    editScreenIntent.putExtra("type", 5);
                                    editScreenIntent.putExtra("top", newStack.getTop());
                                    editScreenIntent.putExtra("intArr", stackArr);
                                    editScreenIntent.putExtra("id",itemID2);
                                    editScreenIntent.putExtra("name",name2);
                                    editScreenIntent.putExtra("type",10);
                                    startActivity(editScreenIntent);
                                    finish();
                                }
                                else {
                                    toastMessage("No ID associated with that name");
                                }
                            }
                        })
                .setNegativeButton("Load",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Stack newStack = new Stack();
                                int[] stackArr = intent.getIntArrayExtra("intArr");
                                newStack.setStackArr(stackArr);
                                int top = intent.getIntExtra("top",9999);
                                newStack.setTop(top);
                                int formulaType = intent.getIntExtra("type",9999);

                                columnHandler(name, intent);

                                if(classType.compareTo("CompoundInterestAnnualAddition") == 0) {
                                    Intent intent = new Intent(ListDataActivity.this, CompoundInterestAnnualAddition.class);
                                    newStack.push(6);
                                    intent.putExtra("type", formulaType);
                                    intent.putExtra("intArr",stackArr);
                                    intent.putExtra("top",newStack.getTop());
                                    intent.putExtra("classLoaded",newStack.peek());
                                    columnHandler(name, intent);
                                    startActivity(intent);
                                    finish();
                                } else if (classType.compareTo("AnnualCompoundInterest") == 0)
                                {
                                    Intent intent = new Intent(ListDataActivity.this, AnnualCompoundInterest.class);
                                    newStack.push(1);
                                    intent.putExtra("type", formulaType);
                                    intent.putExtra("intArr",stackArr);
                                    intent.putExtra("top",newStack.getTop());
                                    intent.putExtra("classLoaded",newStack.peek());
                                    columnHandler(name, intent);
                                    startActivity(intent);
                                    finish();
                                } else if (classType.compareTo("SimpleInterestActivity") == 0)
                                {
                                    Intent intent = new Intent(ListDataActivity.this, SimpleInterestActivity.class);
                                    newStack.push(2);
                                    intent.putExtra("type", formulaType);
                                    intent.putExtra("intArr",stackArr);
                                    intent.putExtra("top",newStack.getTop());
                                    intent.putExtra("classLoaded",newStack.peek());
                                    columnHandler(name, intent);
                                    startActivity(intent);
                                    finish();
                                } else if (classType.compareTo("ContinuouslyCompoundedActivity") == 0)
                                {
                                    Intent intent = new Intent(ListDataActivity.this, ContinuouslyCompoundedActivity.class);
                                    newStack.push(3);
                                    intent.putExtra("type", formulaType);
                                    intent.putExtra("intArr",stackArr);
                                    intent.putExtra("top",newStack.getTop());
                                    intent.putExtra("classLoaded",newStack.peek());
                                    columnHandler(name, intent);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void columnHandler(String name, Intent intent){
        Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name
        int itemID = -1;
        int selectedPosition = 0;

        while(data.moveToNext()){
            intent.putExtra("listPosition",data.getString(0));
            selectedPosition = Integer.parseInt(data.getString(0));
        }

        Cursor data2 = mDatabaseHelper.getData();
        while (data2.moveToNext()) {
            if(selectedPosition == Integer.parseInt(data2.getString(0))) {
                intent.putExtra("listPosition", data2.getInt(0));
                Log.d("listPosition",""+data2.getInt(0));
                intent.putExtra("interest_table", data2.getString(1));
                Log.d("interest_table",""+data2.getString(1));
                intent.putExtra("years_to_grow", data2.getInt(2));
                Log.d("years_to_grow",""+data2.getInt(2));
                intent.putExtra("interest_rate", data2.getDouble(3));
                Log.d("interest_rate",""+data2.getDouble(3));
                intent.putExtra("current_principle", data2.getDouble(4));
                Log.d("current_principle",""+data2.getDouble(4));
                intent.putExtra("annual_addition", data2.getDouble(5));
                Log.d("annual_addition",""+data2.getDouble(5));
                intent.putExtra("NumOfTimeCompAnnually", data2.getInt(6));
                Log.d("NumOfTimeCompAnnually",""+data2.getInt(6));
                intent.putExtra("make_add_end_or_start", data2.getInt(7));
                Log.d("make_add_end_or_start",""+data2.getInt(7));

                intent.putExtra("class_type", data2.getString(8));
                classType = data2.getString(8);
                Log.d("class_type",""+data2.getString(8));
            }
        }
    }

    public void getClassType(String name){
        Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name
        int itemID = -1;
        int selectedPosition = 0;
        while(data.moveToNext()){
            intent.putExtra("listPosition",data.getString(0));
            selectedPosition = Integer.parseInt(data.getString(0));
        }
        Cursor data2 = mDatabaseHelper.getData();
        while (data2.moveToNext()) {
            if(selectedPosition == Integer.parseInt(data2.getString(0))) {
                intent.putExtra("class_type", data2.getString(8));
                classType = data2.getString(8);
                Log.d("class_type",""+data2.getString(8));
            }
        }
    }
    /*"CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " + COL3 + " INTEGER, " + COL4 + " REAL, " + COL5 + " REAL, " + COL6 + " REAL,"+
                COL7 + " INT, "+COL8+" INT, "+COL9+" TEXT)";*/

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}