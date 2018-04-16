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
            stackArr[++top] = data;
        }
    }

    public int pop(){
        if(top < 0) {
            Log.d("ERROR :","Stack underflow exception.");
        } else {
            return stackArr[top--];
        }
        return -100;
    }

    public int peek(){
        if(top < 0) {
            Log.d("ERROR :","Stack underflow exception.");
        } else {
            Log.d("TOP :", ""+stackArr[top]);
        }
        return stackArr[top];
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

    public void printAll(){
        Log.d("Top :",""+top);
        Log.d("Size",""+size);
        int x = 0;
        Log.d("###################","#");
        Log.d("printAll()","");
        Log.d("###################","#");
        for(int arr:stackArr){
            Log.d("arr :"," "+x+": "+arr);
            x++;
        }
        Log.d("###################","#");
    }
}