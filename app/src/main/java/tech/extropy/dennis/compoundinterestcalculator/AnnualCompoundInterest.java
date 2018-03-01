package tech.extropy.dennis.compoundinterestcalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import tech.extropy.dennis.compoundinterestcalculator.Controller.Stack;
import tech.extropy.dennis.compoundinterestcalculator.Math.FinanceMath;
import tech.extropy.dennis.compoundinterestcalculator.Model.DatabaseHelper;

public class AnnualCompoundInterest extends AppCompatActivity {
    TextView mYearGrow, mInterestRate, mCurrentPrinciple, mNumberOfTimesCompounded, mTotal;
    EditText mYearGrowInput, mInterestRateInput, mCurrentPrincipleInput, mNumberOfTimesCompoundedInput;
    Button mCalculate;
    FinanceMath finance;
    DecimalFormat df2;
    String strInput;
    int numberOfTimesCompoundedCompute;
    int yearsToGrow;
    double interestRate;
    double currentPrinciple;
    double total;
    boolean checkValidation;
    Intent intent;
    Button mSave;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.annual_compound);
        mYearGrow = (TextView) findViewById(R.id.yearGrow);
        mInterestRate = (TextView) findViewById(R.id.interestRate);
        mCurrentPrinciple = (TextView) findViewById(R.id.currentPrinciple);
        mNumberOfTimesCompounded = (TextView) findViewById(R.id.numberOfTimesCompounded);
        mYearGrowInput = (EditText) findViewById(R.id.yearGrowInput);
        mInterestRateInput = (EditText) findViewById(R.id.interestRateInput);
        mCurrentPrincipleInput = (EditText) findViewById(R.id.currentPrincipleInput);
        mNumberOfTimesCompoundedInput = (EditText) findViewById(R.id.numberOfTimesCompoundedInput);
        mTotal = (TextView) findViewById(R.id.total);
        df2 = new DecimalFormat(".##");
        mCalculate = (Button) findViewById(R.id.calculate);
        mSave = (Button) findViewById(R.id.save);
        finance = new FinanceMath();
        checkValidation = false;
        intent = getIntent();
        mDatabaseHelper = new DatabaseHelper(this);

        mTotal.setText("Total: $0.00");
        Context context = getApplicationContext();
        final Context context1 = this;
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        if(mYearGrowInput.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        mNumberOfTimesCompoundedInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    checkValidation = false;
                    strInput = mNumberOfTimesCompoundedInput.getText().toString();
                    if(isEmpty(strInput)){
                        mNumberOfTimesCompoundedInput.setError("Input must not be empty.");
                        checkValidation = true;
                    } else {
                        numberOfTimesCompoundedCompute = Integer.parseInt(strInput);
                    }
                }
                return false;
            }
        });

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
                    interestRate = interestRate * .01;
                }
                strInput = mCurrentPrincipleInput.getText().toString();
                if(isEmpty(strInput)){
                    mCurrentPrincipleInput.setError("Input must not be empty.");
                    checkValidation = true;
                } else {
                    currentPrinciple = Double.parseDouble(strInput);
                }
                strInput = mNumberOfTimesCompoundedInput.getText().toString();
                if(isEmpty(mNumberOfTimesCompoundedInput.getText().toString())){
                    mNumberOfTimesCompoundedInput.setError("Input must not be empty.");
                    checkValidation = true;
                } else {
                    numberOfTimesCompoundedCompute = Integer.parseInt(strInput);
                }

                //strInput = mNumberOfTimesCompoundedInput.getText().toString();
                if(checkValidation != true) {
                    total = finance.annualCompoundInterest(currentPrinciple, interestRate, yearsToGrow, numberOfTimesCompoundedCompute);
                    Log.d("Total :", "" + total);
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

                                        // get user input and set it to result
                                        // edit text
                                        //intent = getIntent();
                                        int formulaType = intent.getIntExtra("type", 9999);

                                        //Log.d("formulaType234987 :",""+formulaType);

                                        //newStack.push(4);
                                        //Copy stack from previous activity to the next.
                                        int[] stackArr = intent.getIntArrayExtra("intArr");
                                        newStack.setStackArr(stackArr);
                                        int top = intent.getIntExtra("top",9999);
                                        newStack.setTop(top);
                                        newStack.push(4);

                                        newStack.printAll();

                                        String newEntry = userInput.getText().toString();
                                        if (userInput.length() != 0) {
                                            AddData(newEntry);
                                            userInput.setText("");

                                            //View list of data
                                            Intent intent = new Intent(AnnualCompoundInterest.this, ListDataActivity.class);
                                            intent.putExtra("type", 5);
                                            intent.putExtra("top", newStack.getTop());
                                            intent.putExtra("intArr", stackArr);

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

    @Override
    public void onBackPressed(){
        Stack newStack = new Stack();

        int formulaType = intent.getIntExtra("type", 9999);
        int[] stackArr = intent.getIntArrayExtra("intArr");
        newStack.setStackArr(stackArr);
        int top = intent.getIntExtra("top",9999);
        newStack.setTop(top);
        newStack.pop();

        Intent nextIntent = new Intent(AnnualCompoundInterest.this, MainMenuActivity.class);
        nextIntent.putExtra("type", formulaType);
        nextIntent.putExtra("intArr",stackArr);
        nextIntent.putExtra("top",newStack.getTop());
        startActivity(nextIntent);
        finish();
    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

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
