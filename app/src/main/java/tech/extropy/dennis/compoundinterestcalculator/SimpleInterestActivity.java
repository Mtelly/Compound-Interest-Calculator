package tech.extropy.dennis.compoundinterestcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import tech.extropy.dennis.compoundinterestcalculator.Controller.Stack;
import tech.extropy.dennis.compoundinterestcalculator.Math.FinanceMath;

public class SimpleInterestActivity extends AppCompatActivity {
    TextView mYearGrow, mInterestRate, mCurrentPrinciple;
    EditText mYearGrowInput, mInterestRateInput, mCurrentPrincipleInput;
    Button mCalculate, mSave;
    TextView mTotal;
    FinanceMath finance;
    DecimalFormat df2;
    String strInput;
    int yearsToGrow;
    Intent intent;
    double interestRate, currentPrinciple, total;
    boolean checkValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_interest);
        mYearGrow = (TextView) findViewById(R.id.yearGrow);
        mInterestRate = (TextView) findViewById(R.id.interestRate);
        mCurrentPrinciple = (TextView) findViewById(R.id.currentPrinciple);
        mYearGrowInput = (EditText) findViewById(R.id.yearGrowInput);
        mInterestRateInput = (EditText) findViewById(R.id.interestRateInput);
        mCurrentPrincipleInput = (EditText) findViewById(R.id.currentPrincipleInput);
        mTotal = (TextView) findViewById(R.id.total);
        df2 = new DecimalFormat(".##");
        mCalculate = (Button) findViewById(R.id.calculate);
        finance = new FinanceMath();
        checkValidation = false;
        intent = getIntent();

        mTotal.setText("Total: $0.00");
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
                        Log.d("strInput :", "" + strInput);
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
                if(isEmpty(strInput)) {
                    mCurrentPrincipleInput.setError("Input must not be empty.");
                    checkValidation = true;
                } else {
                    currentPrinciple = Double.parseDouble(strInput);
                }

                if(checkValidation != true) {
                    Log.d("All vars   :", "currentPrinciple :" + currentPrinciple + "interestRate :" + interestRate + "yearsToGrow :" + yearsToGrow);
                    total = finance.continuousInterest(currentPrinciple, interestRate, yearsToGrow);
                    Log.d("Total :", "" + total);
                    mTotal.setText("Total: " + "$" + df2.format(total));
                }
            }});
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

    public boolean isEmpty(String strInput){
        if(strInput.isEmpty() || (0 == strInput.compareTo(" "))) {
            return true;
        } else {
            return false;
        }
    }
}