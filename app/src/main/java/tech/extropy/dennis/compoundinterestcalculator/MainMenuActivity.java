package tech.extropy.dennis.compoundinterestcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import tech.extropy.dennis.compoundinterestcalculator.Controller.Stack;

public class MainMenuActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    Button continueNext, viewListData;
    int formulaType;
    Stack myStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        radioGroup = (RadioGroup) findViewById(R.id.calculateGroup);
        continueNext = (Button) findViewById(R.id.continue_next);
        viewListData = (Button) findViewById(R.id.load);
        myStack = new Stack(10);

        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.compoundInterestWithAnnualAdd) {
                    formulaType = 6;
                } else if (checkedId == R.id.annual_comp_int) {
                    formulaType = 1;
                } else if(checkedId == R.id.simple_interest) {
                    formulaType = 2;
                } else if(checkedId == R.id.continuously_compound) {
                    formulaType = 3;
                } else {
                    formulaType = 4;
                }
            }
        });

        continueNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFormula(formulaType);
            }
        });

        viewListData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formulaType = 4;
                startFormula(formulaType);
            }
        });
    }

    private void startFormula(int formulaType) {
        if(formulaType == 6) {
            myStack.push(100);
            myStack.push(6);
            Intent intent = new Intent(this, CompoundInterestAnnualAddition.class);
            intent.putExtra("type", formulaType);
            intent.putExtra("intArr",myStack.getStackArr());
            intent.putExtra("top",myStack.getTop());
            startActivity(intent);
            finish();
        } else if(formulaType == 1){
            myStack.push(100);
            myStack.push(1);
            Intent intent = new Intent(this, AnnualCompoundInterest.class);
            intent.putExtra("type", formulaType);
            intent.putExtra("intArr",myStack.getStackArr());
            intent.putExtra("top",myStack.getTop());
            startActivity(intent);
            finish();
        } else if(formulaType == 2){
            myStack.push(100);
            myStack.push(2);
            Intent intent = new Intent(this, SimpleInterestActivity.class);
            intent.putExtra("type", formulaType);
            intent.putExtra("intArr",myStack.getStackArr());
            intent.putExtra("top",myStack.getTop());
            startActivity(intent);
            finish();
        } else if(formulaType == 3){
            myStack.push(100);
            myStack.push(3);
            Intent intent = new Intent(this, ContinuouslyCompoundedActivity.class);
            intent.putExtra("type", formulaType);
            intent.putExtra("intArr",myStack.getStackArr());
            intent.putExtra("top",myStack.getTop());
            startActivity(intent);
            finish();
        } else if(formulaType == 4) {
            myStack.push(100);
            myStack.push(4);
            Intent intent = new Intent(this, ListDataActivity.class);
            intent.putExtra("type", formulaType);
            intent.putExtra("intArr",myStack.getStackArr());
            intent.putExtra("top",myStack.getTop());
            startActivity(intent);
            finish();
        }
    }
}