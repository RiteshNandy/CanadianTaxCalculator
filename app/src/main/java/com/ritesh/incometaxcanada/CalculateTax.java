package com.ritesh.incometaxcanada;

import androidx.annotation.NonNull;

public class CalculateTax {
    private static final double RRSP_2022 = 29210.0;
    private static final double RRSP_2023 = 30780.0;

    private double income;
    private double rrspContribution;
    private double taxableincome;

    public CalculateTax(double income, double rrspContribution) {
        this.income = income;
        this.rrspContribution = rrspContribution;
    }

    public CalculateTax() {
        this(0, 0);
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getRrspContribution() {
        return rrspContribution;
    }

    public void setRrspContribution(double rrspContribution) {
        this.rrspContribution = rrspContribution;
    }

    public double gettaxableIncome() {
        taxableincome= income-rrspContribution;
        return taxableincome;
    }

    public double getFederalTax() {
        double federalTax = 0;
        double taxableincome_federal = taxableincome;

        double federalTaxBracketAmount[] = {50197, 50195, 55233, 66083};
        double federalTaxBracketPercent[] = {15, 20.5, 26, 29};

        double maxFederalTax = 33;

        int i = 0;
        while (taxableincome_federal > 0 && i < federalTaxBracketAmount.length) {

            if(taxableincome_federal > federalTaxBracketAmount[i]){
                federalTax += federalTaxBracketAmount[i] * (federalTaxBracketPercent[i] / 100);
                taxableincome_federal -= federalTaxBracketAmount[i];
            }
            else {
                federalTax += taxableincome_federal * (federalTaxBracketPercent[i] / 100);
                taxableincome_federal -= taxableincome_federal;
            }

            i+=1;
        }

        if (taxableincome_federal > 0) {
            federalTax += taxableincome_federal * (maxFederalTax / 100);
        }

        return federalTax;
    }

    public double getProvincialTax() {

        double provincialTax = 0;
        double taxableincome_provincial = taxableincome;

        double provincialTaxBracketAmount[] = {46226, 46228, 57546, 70000};

        double provincialTaxBracketPercent[] = {5.05, 9.15, 11.16, 12.16};

        double maxProvincialTax = 13.16;

        int i = 0;
        while (taxableincome_provincial > 0 && i < provincialTaxBracketAmount.length) {

            if(taxableincome_provincial > provincialTaxBracketAmount[i]){
                provincialTax += provincialTaxBracketAmount[i] * (provincialTaxBracketPercent[i] / 100);
                taxableincome_provincial -= provincialTaxBracketAmount[i];
            }
            else {
                provincialTax += taxableincome_provincial * (provincialTaxBracketPercent[i] / 100);
                taxableincome_provincial -= taxableincome_provincial;
            }

            i+=1;
        }

        if (taxableincome_provincial > 0) {
            provincialTax += taxableincome_provincial * (maxProvincialTax / 100);
        }

        return provincialTax;

    }

    public double getTotalTax() {
        return getFederalTax() + getProvincialTax();
    }

    public  double getAfterTaxIncome() {
        return this.income - getTotalTax();
    }

    public double getNextYearRrsp() {

        double unusedRrrp = RRSP_2022 - rrspContribution;
        double nextYearRrsp = Math.min(RRSP_2023,  (unusedRrrp + (income * 18 / 100)));

        return nextYearRrsp;
    }
}

