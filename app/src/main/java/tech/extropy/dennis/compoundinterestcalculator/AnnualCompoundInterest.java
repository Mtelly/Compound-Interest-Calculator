package tech.extropy.dennis.compoundinterestcalculator;

import android.content.Context;
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
import android.widget.Toast;

import java.text.DecimalFormat;

import tech.extropy.dennis.compoundinterestcalculator.Math.FinanceMath;

public class AnnualCompoundInterest extends AppCompatActivity {
    TextView mYearGrow;
    TextView mInterestRate;
    TextView mCurrentPrinciple;
    TextView mNumberOfTimesCompounded;
    EditText mYearGrowInput;
    EditText mInterestRateInput;
    EditText mCurrentPrincipleInput;
    EditText mNumberOfTimesCompoundedInput;
    Button mCalculate;
    TextView mTotal;
    FinanceMath finance;
    DecimalFormat df2;
    String strInput;
    int numberOfTimesCompoundedCompute;
    int yearsToGrow;
    double interestRate;
    double currentPrinciple;
    double total;

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
        finance = new FinanceMath();

        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        if(mYearGrowInput.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        mNumberOfTimesCompoundedInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    strInput = mNumberOfTimesCompoundedInput.getText().toString();
                    numberOfTimesCompoundedCompute = Integer.parseInt(strInput);
                }
                return false;
            }
        });

        mCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                strInput = mYearGrowInput.getText().toString();
                yearsToGrow = Integer.parseInt(strInput);
                strInput = mInterestRateInput.getText().toString();
                interestRate = Double.parseDouble(strInput);
                interestRate = interestRate *.01;
                strInput = mCurrentPrincipleInput.getText().toString();
                currentPrinciple = Double.parseDouble(strInput);
                strInput = mNumberOfTimesCompoundedInput.getText().toString();

                total = finance.annualCompoundInterest(currentPrinciple, interestRate, yearsToGrow, numberOfTimesCompoundedCompute);
                Log.d("Total :", ""+total);
                mTotal.setText("Total: "+"$"+df2.format(total));
            }});
    }
}
