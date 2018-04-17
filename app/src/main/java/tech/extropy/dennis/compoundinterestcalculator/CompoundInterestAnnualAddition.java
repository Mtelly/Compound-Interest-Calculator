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

public class CompoundInterestAnnualAddition extends Activity { //extends AppCompatActivity {
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
    boolean checkValidation;
    Intent intent;
    Bundle bd;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                        TestPrintDatabase();
                        //deleteDatabase();
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
                            interestRate = Double.parseDouble(strInput);
                            interestRate = interestRate * .01;
                        }
                        strInput = mCurrentPrincipleInput.getText().toString();
                        if(isEmpty(strInput)){
                            mCurrentPrincipleInput.setError("Input must not be empty.");
                            checkValidation = true;
                        } else {
                            currentPrinciple = Double.parseDouble(strInput);
                        }
                        strInput = mAnnualAdditionInput.getText().toString();
                        if(isEmpty(strInput)){
                            mAnnualAdditionInput.setError("Input must not be empty.");
                            checkValidation = true;
                        } else {
                            annualAddition = Integer.parseInt(strInput);
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
                        total = finance.compoundInterestAnnualAdditionEnd(yearsToGrow, interestRate, currentPrinciple, annualAddition, numberOfTimesCompounded);
                    } else {
                        total = finance.compoundInterestAnnualAdditionBeginning(yearsToGrow, interestRate, currentPrinciple, annualAddition, numberOfTimesCompounded);
                    }

                    Log.d("Test", "Total amount: " + df2.format(total));
                    mTotal.setText("Total: " + "$" + df2.format(total));
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

    public void deleteDatabase() {
        mDatabaseHelper.deleteDatabase(this);
        mDatabaseHelper.closeDatabase();
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

    /*This is test method to be deleted.*/
    public void TestPrintDatabase(){
        //TESTING
        Cursor cursorTest = mDatabaseHelper.getData();
        String[] columnNames = cursorTest.getColumnNames();
        for(String x: columnNames) {
            Log.d("columnName :", x);
        }
    }
}