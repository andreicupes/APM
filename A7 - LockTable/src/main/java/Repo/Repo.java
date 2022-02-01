package Repo;

import Model.PrgState;
import Model.adt.IList;
import Model.adt.MyList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class Repo implements IRepo {

    private IList<PrgState> myPrgStates;
    private String logFilePath;

    public Repo(String _logFilePath) {
        myPrgStates = new MyList<PrgState>();
        this.logFilePath = _logFilePath;
    }

    @Override
    public String toString() {
        return myPrgStates.toString();
    }

    @Override
    public void logPrgStateExec(PrgState state) throws Exception {
        PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));

        logFile.write(state.toFile());
        logFile.flush();
        logFile.close();
    }

    @Override
    public void addPrg(PrgState newPrg) {
        myPrgStates.add(newPrg);
    }

    @Override
    public IList<PrgState> getPrgList() {
        return this.myPrgStates;
    }

    @Override
    public void setPrgList(IList<PrgState> newPrgList) {
        myPrgStates = newPrgList;
    }
}
