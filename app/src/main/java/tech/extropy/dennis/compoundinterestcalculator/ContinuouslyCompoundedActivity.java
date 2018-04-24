package tech.extropy.dennis.compoundinterestcalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import tech.extropy.dennis.compoundinterestcalculator.Controller.Stack;
import tech.extropy.dennis.compoundinterestcalculator.Math.FinanceMath;
import tech.extropy.dennis.compoundinterestcalculator.Model.DatabaseHelper;

public class ContinuouslyCompoundedActivity extends AppCompatActivity {
    TextView mYearGrow;
    TextView mInterestRate;
    TextView mCurrentPrinciple;
    EditText mYearGrowInput;
    EditText mInterestRateInput;
    EditText mCurrentPrincipleInput;
    Button mCalculate;
    Button mSave;
    TextView mTotal;
    FinanceMath finance;
    DecimalFormat df2;
    String strInput;
    int numberOfTimesCompoundedCompute;
    int yearsToGrow;
    Intent intent;
    double interestRate;
    double currentPrinciple;
    double total;
    boolean checkValidation;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_interest);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Continuously Compounded Activity");
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        mYearGrow = (TextView) findViewById(R.id.yearGrow);
        mInterestRate = (TextView) findViewById(R.id.interestRate);
        mCurrentPrinciple = (TextView) findViewById(R.id.currentPrinciple);
        mYearGrowInput = (EditText) findViewById(R.id.yearGrowInput);
        mInterestRateInput = (EditText) findViewById(R.id.interestRateInput);
        mCurrentPrincipleInput = (EditText) findViewById(R.id.currentPrincipleInput);
        mTotal = (TextView) findViewById(R.id.total);
        df2 = new DecimalFormat(".##");
        mCalculate = (Button) findViewById(R.id.calculate);
        mSave = (Button) findViewById(R.id.save);
        finance = new FinanceMath();
        checkValidation = false;
        intent = getIntent();
        mDatabaseHelper = new DatabaseHelper(this);

        mTotal.setText("Total: $0.00");
        final Context context1 = this;

        if(mYearGrowInput.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        mCurrentPrincipleInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    checkValidation = false;
                    strInput = mCurrentPrincipleInput.getText().toString();
                    if(isEmpty(strInput)){
                        mCurrentPrincipleInput.setError("Input must not be empty.");
                        checkValidation = true;
                    } else {
                        currentPrinciple = Double.parseDouble(strInput);
                    }
                }
                return false;
            }
        });

        if(intent.getIntExtra("classLoaded",9999) == 3){
            unpackSavedData();
        }

        mCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkValidation = false;
                strInput = mYearGrowInput.getText().toString();

                if(isEmpty(strInput)) {
                    mYearGrowInput.setError("Input must not be empty.");
                    checkValidation = true;
                } else {
                    yearsToGrow = Integer.parseInt(strInput);
                }

                strInput = mInterestRateInput.getText().toString();

                if(isEmpty(strInput)) {
                    mInterestRateInput.setError("Input must not be empty.");
                    checkValidation = true;
                } else {
                    interestRate = Double.parseDouble(strInput);
                    interestRate = interestRate *.01;
                }

                strInput = mCurrentPrincipleInput.getText().toString();

                if(isEmpty(mCurrentPrincipleInput.getText().toString())) {
                    mCurrentPrincipleInput.setError("Input must not be empty.");
                    checkValidation = true;
                } else {
                    currentPrinciple = Integer.parseInt(strInput);
                }


                if(checkValidation != true) {
                    total = finance.continuousInterest(currentPrinciple, interestRate, yearsToGrow);
                    mTotal.setText("Total: " + "$" + df2.format(total));
                }
            }});

// add button listener
        mSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context1);
                View promptsView = li.inflate(R.layout.custom, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        Stack newStack = new Stack();
                                        //Copy stack from previous activity to the next.
                                        int[] stackArr = intent.getIntArrayExtra("intArr");
                                        newStack.setStackArr(stackArr);
                                        int top = intent.getIntExtra("top",9999);
                                        newStack.setTop(top);
                                        newStack.push(4);

                                        newStack.printAll();

                                        String fileName = userInput.getText().toString();
                                        if (userInput.length() != 0) {
                                            AddData(fileName, yearsToGrow, interestRate, currentPrinciple);
                                            userInput.setText("");

                                            //View list of data
                                            Intent intent = new Intent(ContinuouslyCompoundedActivity.this, ListDataActivity.class);
                                            intent.putExtra("type", 4);
                                            intent.putExtra("top", newStack.getTop());
                                            intent.putExtra("intArr", newStack.getStackArr());
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            toastMessage("You must put something in the text field!");
                                        }

                                        //mResult.setText(test);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
    }

    public void unpackSavedData()
    {
        int listPosition = intent.getIntExtra("listPosition", 9999);
        String interestTable = intent.getStringExtra("interest_table");

        int yearsToGrow = intent.getIntExtra("years_to_grow",9999);
        mYearGrowInput.setText(String.valueOf(yearsToGrow),TextView.BufferType.EDITABLE);
        double interestRate = intent.getDoubleExtra("interest_rate",9999);
        //interestRate = interestRate * 100;
        mInterestRateInput.setText(String.valueOf(interestRate*100),TextView.BufferType.EDITABLE);
        double currentPrinciple = intent.getDoubleExtra("current_principle",9999);
        mCurrentPrincipleInput.setText(String.valueOf(currentPrinciple),TextView.BufferType.EDITABLE);

        total = finance.simpleInterest(currentPrinciple, interestRate, yearsToGrow);
        mTotal.setText("Total: " + "$" + df2.format(total));
    }

    @Override
    public void onBackPressed(){
        Stack newStack = new Stack();
        int formulaType = intent.getIntExtra("type", 9999);
        int[] stackArr = intent.getIntArrayExtra("intArr");
        newStack.setStackArr(stackArr);
        int top = intent.getIntExtra("top",9999);
        newStack.setTop(top);
        newStack.pop();

        if(newStack.peek() == 4)
        {
            Intent nextIntent = new Intent(ContinuouslyCompoundedActivity.this, ListDataActivity.class);
            nextIntent.putExtra("type", formulaType);
            nextIntent.putExtra("intArr", stackArr);
            nextIntent.putExtra("top", newStack.getTop());
            startActivity(nextIntent);
            finish();
        } else if(newStack.peek() == 100) {
            Intent nextIntent = new Intent(ContinuouslyCompoundedActivity.this, MainMenuActivity.class);
            nextIntent.putExtra("type", formulaType);
            nextIntent.putExtra("intArr", stackArr);
            nextIntent.putExtra("top", newStack.getTop());
            startActivity(nextIntent);
            finish();
        }
    }

    public void AddData(String fileName, int yearsToGrow,
                        double interestRate, double currentPrinciple){
        boolean insertData = mDatabaseHelper.addData(fileName, yearsToGrow, interestRate, currentPrinciple, "ContinuouslyCompoundedActivity");

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public boolean isEmpty(String strInput) {
        if(strInput.isEmpty() || (0 == strInput.compareTo(" "))) {
            return true;
        } else {
            return false;
        }
    }


}
