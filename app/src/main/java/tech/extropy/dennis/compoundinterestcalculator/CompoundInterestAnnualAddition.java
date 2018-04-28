package tech.extropy.dennis.compoundinterestcalculator;

import tech.extropy.dennis.compoundinterestcalculator.Controller.Stack;
import tech.extropy.dennis.compoundinterestcalculator.Math.FinanceMath;

import tech.extropy.dennis.compoundinterestcalculator.Model.DatabaseHelper;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

import static java.lang.Double.parseDouble;

public class CompoundInterestAnnualAddition extends AppCompatActivity {//extends Activity { //
    private static final String TAG = "MyActivity";
    TextView mYearsGrow, mInterestRate, mCurrentPrinciple, mAnnualAddition,
            mNumberOfTimesCompounded, mTotal;
    EditText mYearsGrowInput, mInterestRateInput, mCurrentPrincipleInput,
            mAnnualAdditionInput, mNumberOfTimesCompoundedInput, mResult;
    Button mCalculate, mSave;
    RadioGroup mRadioGroup;
    FinanceMath finance;
    int yearsToGrow;
    double interestRate, currentPrinciple, annualAddition, total;
    int numberOfTimesCompounded, startOrEnd;
    String strInput;

    private DecimalFormat df2;
    private NumberFormat nf1;
    private Currency c1;

    boolean checkValidation;
    Intent intent;
    Bundle bd;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Compound Interest With Annual Additions");
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        mYearsGrow = (TextView) findViewById(R.id.yearGrow);
        mInterestRate = (TextView) findViewById(R.id.interestRate);
        mCurrentPrinciple = (TextView) findViewById(R.id.currentPrinciple);
        mAnnualAddition = (TextView) findViewById(R.id.annualAddition);
        mNumberOfTimesCompounded = (TextView) findViewById(R.id.numberOfTimesCompounded);
        mYearsGrowInput = (EditText) findViewById(R.id.yearGrowInput);
        mInterestRateInput = (EditText) findViewById(R.id.interestRateInput);
        mCurrentPrincipleInput = (EditText) findViewById(R.id.currentPrincipleInput);
        mAnnualAdditionInput = (EditText) findViewById(R.id.annualAdditionInput);
        mNumberOfTimesCompoundedInput = (EditText) findViewById(R.id.numberOfTimesCompoundedInput);
        mResult = (EditText) findViewById(R.id.editTextResult);
        mCalculate = (Button) findViewById(R.id.calculate);
        mSave = (Button) findViewById(R.id.save);
        mTotal = (TextView) findViewById(R.id.total);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        df2 = new DecimalFormat(".##");
        finance = new FinanceMath();

        nf1 = NumberFormat.getInstance();
        nf1.setMaximumFractionDigits(2);
        c1 = Currency.getInstance("USD");
        nf1.setCurrency(c1);

        intent = getIntent();
        checkValidation = false;
        mDatabaseHelper = new DatabaseHelper(this);


        mTotal.setText("Total: $0.00");
        final Context context = getApplicationContext();
        final Context context1 = this;
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        if(mYearsGrowInput.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.endRadioButton) {
                    startOrEnd = 1;
                } else {
                    startOrEnd = 2;
                }
            }
        });

        mNumberOfTimesCompoundedInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    checkValidation = false;
                    strInput = mNumberOfTimesCompoundedInput.getText().toString();
                    if(isEmpty(strInput)) {
                        mNumberOfTimesCompoundedInput.setError("Input must not be empty.");
                        checkValidation = true;
                    } else {
                        numberOfTimesCompounded = Integer.parseInt(strInput);
                    }
                }
                return false;
            }
        });

//TODO: put stack array intents from ListDataActivity
        //User opened a saved file.
        if(intent.getIntExtra("classLoaded",9999) == 6){
            unpackSavedData();
        }

        mCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                        checkValidation = false;
                        strInput = mYearsGrowInput.getText().toString();
                        if(isEmpty(strInput)){
                            mYearsGrowInput.setError("Input must not be empty.");
                            checkValidation = true;
                        } else {
                            yearsToGrow = Integer.parseInt(strInput);
                        }
                        strInput = mInterestRateInput.getText().toString();
                        if(isEmpty(strInput)){
                            mInterestRateInput.setError("Input must not be empty.");
                            checkValidation = true;
                        } else {
                            interestRate = parseDouble(strInput);
                            interestRate = interestRate * .01;
                        }
                        strInput = mCurrentPrincipleInput.getText().toString();
                        if(isEmpty(strInput)){
                            mCurrentPrincipleInput.setError("Input must not be empty.");
                            checkValidation = true;
                        } else {
                            currentPrinciple = parseDouble(strInput);
                        }
                        strInput = mAnnualAdditionInput.getText().toString();
                        if(isEmpty(strInput)){
                            mAnnualAdditionInput.setError("Input must not be empty.");
                            checkValidation = true;
                        } else {
                            //annualAddition = Integer.parseInt(strInput);
                            annualAddition = Double.parseDouble(strInput);
                        }
                        strInput = mNumberOfTimesCompoundedInput.getText().toString();
                        if(isEmpty(mNumberOfTimesCompoundedInput.getText().toString())){
                            mNumberOfTimesCompoundedInput.setError("Input must not be empty.");
                            checkValidation = true;
                        } else {
                            numberOfTimesCompounded = Integer.parseInt(strInput);
                        }

                if(checkValidation != true) {
                    if (startOrEnd == 1) {
                        Log.d("CALCULATE", " BUTTON");
                        Log.d("yearsToGrow",""+yearsToGrow);
                        Log.d("interestRate",""+interestRate);
                        Log.d("currentPrinciple",""+currentPrinciple);
                        Log.d("annualAddition",""+annualAddition);
                        Log.d("numberOfTimesCompounded",""+numberOfTimesCompounded);
                        total = finance.compoundInterestAnnualAdditionEnd(yearsToGrow, interestRate, currentPrinciple, annualAddition, numberOfTimesCompounded);
                    } else {
                        total = finance.compoundInterestAnnualAdditionBeginning(yearsToGrow, interestRate, currentPrinciple, annualAddition, numberOfTimesCompounded);
                    }

                    Log.d("Test", "Total amount: " + df2.format(total));
                    String totalDisplay = "Total: " + "$" + nf1.format(total);
                    mTotal.setText(totalDisplay);
                }
            }
        });

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

                                        //File name is asked here.
                                        String fileName = userInput.getText().toString();

                                        //(fileName, yearsToGrow, currentPrinciple, annualAddition, numberOfTimesCompounded)
                                        if (userInput.length() != 0) {
                                            AddData(fileName, yearsToGrow, interestRate, currentPrinciple,
                                                    annualAddition, numberOfTimesCompounded, startOrEnd);
                                            userInput.setText("");

                                            //View list of data
                                            Intent intent = new Intent(CompoundInterestAnnualAddition.this, ListDataActivity.class);
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
        mYearsGrowInput.setText(String.valueOf(yearsToGrow),TextView.BufferType.EDITABLE);
        double interestRate = intent.getDoubleExtra("interest_rate",9999);
        //interestRate = interestRate * 100;
        mInterestRateInput.setText(String.valueOf(interestRate*100),TextView.BufferType.EDITABLE);
        double currentPrinciple = intent.getDoubleExtra("current_principle",9999);
        mCurrentPrincipleInput.setText(String.valueOf(currentPrinciple),TextView.BufferType.EDITABLE);
        double annualAddition = intent.getDoubleExtra("annual_addition",9999);
        mAnnualAdditionInput.setText(String.valueOf(annualAddition),TextView.BufferType.EDITABLE);
        int numOfTimeCompAnnually = intent.getIntExtra("NumOfTimeCompAnnually",9999);
        mNumberOfTimesCompoundedInput.setText(String.valueOf(numOfTimeCompAnnually),TextView.BufferType.EDITABLE);
        int endOrStartInt = intent.getIntExtra("make_add_end_or_start",9999);
        Log.d("endOrStartInt",""+endOrStartInt);

        if(endOrStartInt == 2)
        {
            //Start chosen
            mRadioGroup.check(R.id.startRadioButton);
            total = finance.compoundInterestAnnualAdditionBeginning(yearsToGrow, interestRate, currentPrinciple, annualAddition, numOfTimeCompAnnually);
        } else {
            //End chosen
            mRadioGroup.check(R.id.endRadioButton);
            total = finance.compoundInterestAnnualAdditionEnd(yearsToGrow, interestRate, currentPrinciple, annualAddition, numOfTimeCompAnnually);
            Log.d("total end :",""+total);
        }

        mTotal.setText("Total: " + "$" + df2.format(total));
    }

    @Override
    public void onBackPressed() {
        Stack newStack = new Stack();
        int formulaType = intent.getIntExtra("type", 9999);
        int[] stackArr = intent.getIntArrayExtra("intArr");
        newStack.setStackArr(stackArr);
        int top = intent.getIntExtra("top",9999);
        newStack.setTop(top);

        Log.d("onBackPressed() :","");
        newStack.printAll();

        newStack.pop();

        if(newStack.peek() == 4)
        {
            Intent nextIntent = new Intent(CompoundInterestAnnualAddition.this, ListDataActivity.class);
            nextIntent.putExtra("type", formulaType);
            nextIntent.putExtra("intArr", stackArr);
            nextIntent.putExtra("top", newStack.getTop());
            startActivity(nextIntent);
            finish();
        } else if(newStack.peek() == 100) {
            Intent nextIntent = new Intent(CompoundInterestAnnualAddition.this, MainMenuActivity.class);
            nextIntent.putExtra("type", formulaType);
            nextIntent.putExtra("intArr", stackArr);
            nextIntent.putExtra("top", newStack.getTop());
            startActivity(nextIntent);
            finish();
        }
    }

    public void AddData(String fileName,int yearsToGrow,double interestRate,
                        double currentPrinciple,double annualAddition,int numberOfTimesCompounded, int startOrEnd) {

        boolean insertData = mDatabaseHelper.addData(fileName, interestRate,yearsToGrow,
                currentPrinciple, annualAddition, numberOfTimesCompounded, startOrEnd, "CompoundInterestAnnualAddition");

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

    public boolean isEmpty(String strInput){

        if(strInput.isEmpty() || (0 == strInput.compareTo(" "))) {
            return true;
        } else {
            return false;
        }

    }

}