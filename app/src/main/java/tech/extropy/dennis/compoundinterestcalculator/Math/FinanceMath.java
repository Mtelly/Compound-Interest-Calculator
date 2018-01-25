package tech.extropy.dennis.compoundinterestcalculator.Math;

import java.io.Serializable;

/**
 * Created by dennis on 1/15/18.
 */

public class FinanceMath implements Serializable {

    private static final long serialVersionUID = 1L;
    private double principle;
    private double interestRate;
    private int time;
    private int compoundingPeriods;
    private double annualAddition;
    private int formulaType;

    public double continuousInterest(double principle, double interestRate, int time) {

        this.principle = principle;
        this.interestRate = interestRate;
        this.time = time;
        formulaType = 3;

        //A = P*e^(r*t)
        double amount = principle*Math.exp(interestRate*time);

        return amount;
    }

    public double simpleInterest(double principle, double interestRate, int time) {

        this.principle = principle;
        this.interestRate = interestRate;
        this.time = time;
        formulaType = 2;

        //A = P(1 + rt)
        double amount = principle*(1+interestRate*time);

        return amount;
    }

    public double annualCompoundInterest(double principle, double interestRate, int time, int compoundingPeriods){

        this.principle = principle;
        this.interestRate = interestRate;
        this.time = time;
        this.compoundingPeriods = compoundingPeriods;
        formulaType = 1;

        //A=P(1+(r/n))^(n*t)
        double total = Math.pow((1 + (interestRate/compoundingPeriods)), (compoundingPeriods*time));
        total = principle*total;

        return total;
    }

    /*This is compound interest with annual addition for the end
    * of a compounding period.*/
    public double compoundInterestAnnualAdditionEnd(int time,double interestRate,
                                                    double principle,double annualAddition,int compoundingPeriods){
        this.time = time;
        this.interestRate = interestRate;
        this.principle = principle;
        this.annualAddition = annualAddition;
        this.compoundingPeriods = compoundingPeriods;
        formulaType = 4;

        //[ P(1+r/n)^(nt) ] + [ PMT × (((1 + r/n)^(nt) - 1) / (r/n)) ]
        //[ P(1+r/n)^(nt) ]
        double PMT = annualAddition / compoundingPeriods;
        double interestForPrinciple = (principle*Math.pow((1 + (interestRate/compoundingPeriods)), compoundingPeriods * time));

        //[ PMT × (((1 + r/n)^(nt) - 1) / (r/n)) ]
        double first = interestRate / compoundingPeriods;
        first += 1;
        double second = Math.pow(first, compoundingPeriods * time);
        second -= 1;
        double third = (second)/(interestRate/compoundingPeriods);
        third = PMT * third;//100*155.2822794456672
        double futureValueOfASeries = third;
        double total = interestForPrinciple + futureValueOfASeries;

        return total;
    }

    /*This is compound interest with annual addition for the beginning
    * of a compounding period.*/
    public double compoundInterestAnnualAdditionBeginning(int time,double interestRate,
                                                          double principle,double annualAddition,int compoundingPeriods){
        this.time = time;
        this.interestRate = interestRate;
        this.principle = principle;
        this.annualAddition = annualAddition;
        this.compoundingPeriods = compoundingPeriods;
        formulaType = 0;

        //[ P(1+r/n)^(nt) ] + [ PMT × (((1 + r/n)^(nt) - 1) / (r/n)) ] × (1+r/n)
        //[ P(1+r/n)^(nt) ]
        double PMT = annualAddition / compoundingPeriods;
        double interestForPrinciple = (principle*Math.pow((1 + (interestRate/compoundingPeriods)), compoundingPeriods * time));

        //[ PMT × (((1 + r/n)^(nt) - 1) / (r/n)) ] × (1+r/n)
        double first = interestRate / compoundingPeriods;
        first += 1;
        double second = Math.pow(first, compoundingPeriods * time);
        second -= 1;
        double third = (second)/(interestRate/compoundingPeriods);
        third = PMT * third;//100*155.2822794456672
        double fourth = third * (1+ (interestRate/compoundingPeriods));
        double futureValueOfASeries = fourth;
        double total = interestForPrinciple + futureValueOfASeries;

        return total;
    }

    /*
    private double principle;
    private double interestRate;
    private int time;
    private int compoundingPeriods;
    private double annualAddition;
    */
    public String toString(){
        return "FinanceMath [principle="+principle+", interestRate"+interestRate+", time"+time
                +", compoundingPeriods"+", annualAdditions"+annualAddition+"]";
    }
}