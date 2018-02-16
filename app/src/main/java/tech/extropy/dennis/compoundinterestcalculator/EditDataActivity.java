package tech.extropy.dennis.compoundinterestcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tech.extropy.dennis.compoundinterestcalculator.Model.DatabaseHelper;

/**
 * Created by dennis on 1/27/18.
 */

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnSave, btnDelete;
    private EditText editable_item;

    DatabaseHelper mDatabaseHelper;

    private String selectedName;
    private int selectedID;
    Intent receivedIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        editable_item = (EditText) findViewById(R.id.editable_item);
        mDatabaseHelper = new DatabaseHelper(this);

        //get the intent extra from the ListDataActivity
        receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedName = receivedIntent.getStringExtra("name");

        //set the text to show the current selected name
        editable_item.setText(selectedName);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editable_item.getText().toString();
                if(!item.equals("")){
                    mDatabaseHelper.updateName(item,selectedID,selectedName);
                }else{
                    toastMessage("You must enter a name");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteName(selectedID,selectedName);
                editable_item.setText("");
                toastMessage("removed from database");
            }
        });
    }

    @Override
    public void onBackPressed() {

        int newActivity = receivedIntent.getIntExtra("type",9999);
        int formulaType;

        if(newActivity == 999) {
            formulaType = 999;
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("type", formulaType);
            startActivity(intent);
            Log.d("onBackPressed() :","FINISH()");
            finish();
        } else if(newActivity == 0) {
            formulaType = 0;
            Intent intent = new Intent(this, CompoundInterestAnnualAddition.class);
            intent.putExtra("type", formulaType);
            startActivity(intent);
            finish();
        } else if(newActivity == 10) {
            formulaType = 10;
            Intent intent = new Intent(this, ListDataActivity.class);
            intent.putExtra("type", formulaType);
            startActivity(intent);
            finish();
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}