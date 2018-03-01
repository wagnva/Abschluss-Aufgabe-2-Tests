package base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wagnva on 24.02.18.
 */
public class Result<T> {

    private List<T> captured;
    private int ignoreEntries = 0;

    public Result(){
        captured = new ArrayList<>();
    }

    public void addResult(T addThis){
        if(ignoreEntries == 0)
            captured.add(addThis);
        else
            ignoreEntries--;
    }

    public List<T> getResults(){
        return captured;
    }

    public T getLast(){
        if(isEmpty()) return null;
        return captured.get(captured.size()-1);
    }

    public boolean isEmpty(){
        return captured.size() == 0;
    }

    public void reset(){
        captured = new ArrayList<>();
    }

    public void setIgnoreFirstEntries(int amountOfEntries){
        this.ignoreEntries = amountOfEntries;
    }
}
