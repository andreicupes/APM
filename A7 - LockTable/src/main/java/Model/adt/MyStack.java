package Model.adt;

import Exceptions.ADTException;
import Model.stmt.CompStmt;
import Model.stmt.IStmt;

import java.util.Stack;

public class MyStack<T> implements IStack<T> {
    private Stack<T> stack;

    public MyStack() {
        stack = new Stack<T>();
    }

    public MyStack(Stack<T> _stack) {
        stack = (Stack<T>) _stack.clone();
    }

    @Override
    public T pop() throws Exception {
        if (this.isEmpty()) {
            throw new ADTException("Stack is empty!");
        }
        return this.stack.pop();
    }

    @Override
    public void push(T v) {
         this.stack.push(v);
    }

    @Override
    public boolean isEmpty() {
        return this.stack.empty();
    }

//    public String toString() {
//        return stack.toString();
//    }

    @Override
    public String toFile() {
        MyStack<T> copyStack = (MyStack<T>) this.deepCopy();

        String result = "";
        try {
            while (!copyStack.isEmpty()) {
                T elem = copyStack.pop();
                if (elem instanceof IStmt)
                    result += inOrderTraversal((IStmt) elem) + "\n";
            }
        } catch (Exception e) { }

        return  "\n"+result+ "\n";
    }

    public String inOrderTraversal(IStmt stmt) {
        if (stmt instanceof CompStmt) {
            String returnString = "";

            CompStmt newStmt = (CompStmt) stmt;
            IStmt left = newStmt.getFirst();
            IStmt right = newStmt.getSecond();

            if (left instanceof CompStmt) {
                returnString += inOrderTraversal(left);
            } else if (left != null) returnString +=   left.toString()+'\n';

            if (right instanceof CompStmt) {
                returnString += inOrderTraversal(right);
            } else if (right!= null) returnString +=  right.toString()+'\n';

            return returnString;

        } else return "\n"+ stmt.toString()+"\n";
    }

    @Override
    public String toString() {
        StringBuilder stringRep = new StringBuilder();

        Stack<T> printStack = new Stack<>();
        printStack.addAll(this.stack);

        for (T elem : printStack) {
            if (elem instanceof IStmt){
                stringRep.append(inOrderTraversal((IStmt) elem));
            }
            else {
                stringRep.append(elem.toString());
            }
        }
        return stringRep.toString();
    }

    @Override
    public IStack<T> deepCopy() {
        return new MyStack<T>(this.stack);
    }
}
