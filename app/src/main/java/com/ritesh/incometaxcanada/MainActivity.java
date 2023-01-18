package com.ritesh.incometaxcanada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import java.text.NumberFormat;
import java.util.Currency;

public class MainActivity extends AppCompatActivity {

    private EditText personalincome;
    private Slider rrspslider;
    private EditText federaltax, provincialtax, totaltax, afterTaxIncome, rrspText, nextYearRrspLimit, taxableincome;
    private CalculateTax calculatingTax ;
    private Button calculateTax,reset;

    String Shared_Prefs = "sharedPrefs";
    String Taxable_Income = "taxableIncome";
    String RRSP_Contribution = "rrspContribution";

    private double v_TaxablelIncome, v_RRSPContribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rrspslider=findViewById(R.id.rrspSlider);
        personalincome=findViewById(R.id.personalIncome);
        taxableincome=findViewById(R.id.taxableIncome);
        provincialtax=findViewById(R.id.provincialTax);
        federaltax=findViewById(R.id.federalTax);
        totaltax=findViewById(R.id.totalTax);
        rrspText=findViewById(R.id.rrspTextView);
        afterTaxIncome=findViewById(R.id.afterTax);
        nextYearRrspLimit=findViewById(R.id.rrspRemainingText);
        calculateTax=findViewById(R.id.calculateTaxBtn);
        reset=findViewById(R.id.resetBtn);

        rrspslider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                currencyFormat.setCurrency(Currency.getInstance("CAD"));
                return currencyFormat.format(value);
            }
        });

        loadData();
        loadView();

        calculateTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                refreshView();
            }
        });

        rrspslider.addOnChangeListener((slider, value, fromUser) -> {
            saveData();
            refreshView();
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetView();
                saveData();
            }
        });

    }

    private void resetView() {

        v_TaxablelIncome=0;
        v_RRSPContribution=0;

        calculatingTax = new CalculateTax(v_TaxablelIncome, v_RRSPContribution);

        personalincome.setText(Double.toString(v_TaxablelIncome));
        rrspslider.setValue((float) v_RRSPContribution);
        rrspText.setText("CA$" + String.format("%,.2f", v_RRSPContribution));
        taxableincome.setText("CA$" + String.format("%,.2f", calculatingTax.gettaxableIncome()));
        federaltax.setText("CA$" + String.format("%,.2f", calculatingTax.getFederalTax()));
        provincialtax.setText("CA$" + String.format("%,.2f", calculatingTax.getProvincialTax()));
        totaltax.setText("CA$" + String.format("%,.2f", calculatingTax.getTotalTax()));
        afterTaxIncome.setText("CA$" + String.format("%,.2f", calculatingTax.getAfterTaxIncome()));
        nextYearRrspLimit.setText("CA$" + String.format("%,.2f", calculatingTax.getNextYearRrsp()));

    }

    protected void refreshView() {

        double newTotalIncome, newRRSP;

        if (personalincome.getText().toString().isEmpty())
            newTotalIncome = 0;
        else
            newTotalIncome = Double.parseDouble(personalincome.getText().toString());

        newRRSP = rrspslider.getValue();

        calculatingTax = new CalculateTax(newTotalIncome, newRRSP);

        rrspText.setText("CA$" + String.format("%,.2f", newRRSP));
        taxableincome.setText("CA$" + String.format("%,.2f", calculatingTax.gettaxableIncome()));
        federaltax.setText("CA$" + String.format("%,.2f", calculatingTax.getFederalTax()));
        provincialtax.setText("CA$" + String.format("%,.2f", calculatingTax.getProvincialTax()));
        totaltax.setText("CA$" + String.format("%,.2f", calculatingTax.getTotalTax()));
        afterTaxIncome.setText("CA$" + String.format("%,.2f", calculatingTax.getAfterTaxIncome()));
        nextYearRrspLimit.setText("CA$" + String.format("%,.2f", calculatingTax.getNextYearRrsp()));
    }

    protected void saveData() {

        SharedPreferences sharedPreferences = getSharedPreferences(Shared_Prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Taxable_Income, personalincome.getText().toString());
        editor.putFloat(RRSP_Contribution, rrspslider.getValue());

        editor.apply();
    }

    protected void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(Shared_Prefs, MODE_PRIVATE);

        if(sharedPreferences.getString(Taxable_Income, "0").isEmpty())
            v_TaxablelIncome = 0;
        else
            v_TaxablelIncome = Double.parseDouble(sharedPreferences.getString(Taxable_Income, "0"));
        v_RRSPContribution = sharedPreferences.getFloat(RRSP_Contribution, 0);
    }

    protected void loadView() {

        personalincome.setText(Double.toString(v_TaxablelIncome));
        rrspslider.setValue((float) v_RRSPContribution);

        calculatingTax = new CalculateTax(v_TaxablelIncome, v_RRSPContribution);

        rrspText.setText("CA$" + String.format("%,.2f", v_RRSPContribution));
        taxableincome.setText("CA$" + String.format("%,.2f", calculatingTax.gettaxableIncome()));
        federaltax.setText("CA$" + String.format("%,.2f", calculatingTax.getFederalTax()));
        provincialtax.setText("CA$" + String.format("%,.2f", calculatingTax.getProvincialTax()));
        totaltax.setText("CA$" + String.format("%,.2f", calculatingTax.getTotalTax()));
        afterTaxIncome.setText("CA$" + String.format("%,.2f", calculatingTax.getAfterTaxIncome()));
        nextYearRrspLimit.setText("CA$" + String.format("%,.2f", calculatingTax.getNextYearRrsp()));
    }

}
