package tech.extropy.dennis.compoundinterestcalculator.Controller;

import android.util.Log;

/**
 * Created by dennis on 1/31/18.
 */

public class Stack {
    private int[] stackArr;
    private int top;
    private int size;

    public Stack(){
        this(10);
    }

    public Stack(int size) {
        this.size = (size > 0) ? size : 10;
        top = -1;
        stackArr = new int[size];
    }

    public void push(int data){
        if(top == size - 1) {
            Log.d("ERROR :", "Stack overflow exception." );
        } else {
            Log.d("data :", ""+data);
            stackArr[++top] = data;
            Log.d("top :",""+top);
        }
    }

    public int pop(){
        if(top < 0) {
            Log.d("ERROR :","Stack underflow exception.");
        } else {
            Log.d("top :", "" + stackArr[top]);
            return stackArr[top--];
        }
        return -100;
    }

    public void peek(){
        if(top < 0) {
            Log.d("ERROR :","Stack underflow exception.");
        } else {
            Log.d("TOP :", ""+stackArr[top]);
        }
    }

    public void setTop(int top){
        this.top = top;
    }

    public int getTop(){
        return top;
    }

    public void setStackArr(int[] stackArr){
        this.stackArr = stackArr;
    }

    public int[] getStackArr(){
        return stackArr;
    }
}