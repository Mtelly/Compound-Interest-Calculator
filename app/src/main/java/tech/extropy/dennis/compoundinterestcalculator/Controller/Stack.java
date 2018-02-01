package tech.extropy.dennis.compoundinterestcalculator.Controller;

import android.util.Log;

/**
 * Created by dennis on 1/31/18.
 */

public class Stack {
    private int[] stackArr;
    private int top;
    private int size;

    Stack(){
        this(10);
    }

    Stack(int size) {
        this.size = (size > 0) ? size : 10;
        top = -1;
        stackArr = new int[size];
    }

    public void push(int data){
        if(top == size - 1) {
            Log.d("ERROR :", "There shouldn't be more than 10." );
        } else {
            stackArr[++top] = data;
        }
    }

    public void pop(){
        if(top < 0) {
            Log.d("ERROR :","There shouldn't be more than 0.");
        } else {
            stackArr[top--] = 0;
        }
    }

}
