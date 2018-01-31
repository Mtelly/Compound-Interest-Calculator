package tech.extropy.dennis.compoundinterestcalculator;

import android.content.Intent;
import android.preference.Preference;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    Button continueNext, load;
    int formulaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        radioGroup = (RadioGroup) findViewById(R.id.calculateGroup);
        continueNext = (Button) findViewById(R.id.continue_next);
        load = (Button) findViewById(R.id.load);

        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.compoundInterestWithAnnualAdd) {
                    formulaType = 0;
                } else if (checkedId == R.id.annual_comp_int){
                    formulaType = 1;
                } else if(checkedId == R.id.simple_interest){
                    formulaType = 2;
                } else if(checkedId == R.id.continuously_compound){
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

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formulaType = 999;
                startFormula(formulaType);
            }
        });
    }

    private void startFormula(int formulaType) {
        if(formulaType == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("type", formulaType);
            startActivity(intent);
        } else if(formulaType == 1){
            Intent intent = new Intent(this, AnnualCompoundInterest.class);
            intent.putExtra("type", formulaType);
            startActivity(intent);
        } else if(formulaType == 2){
            Intent intent = new Intent(this, SimpleInterestActivity.class);
            intent.putExtra("type", formulaType);
            startActivity(intent);
        } else if(formulaType == 3){
            Intent intent = new Intent(this, ContinuouslyCompoundedActivity.class);
            intent.putExtra("type", formulaType);
            startActivity(intent);
        } else if(formulaType == 999) {
            Intent intent = new Intent(this, ListDataActivity.class);
            intent.putExtra("type", formulaType);
            startActivity(intent);
        }
    }
}