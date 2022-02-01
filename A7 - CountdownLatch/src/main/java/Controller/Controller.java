package Controller;

import Exceptions.ExecutionException;
import Model.PrgState;
import Model.adt.IList;
import Model.adt.IStack;
import Model.adt.MyList;
import Model.stmt.IStmt;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;
import Repo.IRepo;
import Repo.Repo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {

    private Repo repo;
    private ExecutorService executor;

    public Controller(IRepo _r) {
        this.repo = (Repo)_r;
    }

    public void addProgram(PrgState newPrg) {
        this.repo.addPrg(newPrg);
    }


    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }

    public IRepo getRepo() { return this.repo; }

    public void oneStepForAllPrg(List<PrgState> prgList) throws Exception{

        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(p::oneStep))
                .collect(Collectors.toList());

        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try { return future.get(); }
                    catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        prgList.addAll(newPrgList);
        for(PrgState prg : prgList) {
            repo.logPrgStateExec(prg);
        }

        IList<PrgState> copyPrgList = new MyList<>(prgList);
        repo.setPrgList(copyPrgList);
    }

    public void allStep() throws Exception{
        executor = Executors.newFixedThreadPool(2);

        IList<PrgState> list = repo.getPrgList();
        List<PrgState> prgList = removeCompletedPrg(list.getAll());

        while (prgList.size() > 0) {

            for (PrgState prg : prgList) {
                prg.getHeapTable().setContent(GarbageCollector.safeGarbageCollector(
                        GarbageCollector.getAddrFromTable(prg.getSymTable().values()),
                        prg.getHeapTable().getContent()));
            }

            oneStepForAllPrg(prgList);

            list = repo.getPrgList();
            prgList = removeCompletedPrg(list.getAll());
        }
        executor.shutdownNow();

        IList<PrgState> copyPrgList = new MyList<>(prgList);
        repo.setPrgList(copyPrgList);
    }

    public void oneStepGUI() throws Exception{
        executor = Executors.newFixedThreadPool(2);
        IList<PrgState> list = repo.getPrgList();
        List<PrgState> prgList = list.getAll();

        if (prgList.size() > 0) {
            for (PrgState prg : prgList) {
                prg.getHeapTable().setContent(GarbageCollector.safeGarbageCollector(
                        GarbageCollector.getAddrFromTable(prg.getSymTable().values()),
                        prg.getHeapTable().getContent()));
            }
            oneStepForAllPrg(prgList);
        } else {
            executor.shutdownNow();
            IList<PrgState> copyPrgList = new MyList<>(prgList);
            repo.setPrgList(copyPrgList);

            throw new Exception("Program finished successfully");
        }

        executor.shutdownNow();
        IList<PrgState> copyPrgList = new MyList<>(prgList);
        repo.setPrgList(copyPrgList);
    }

    public String toString() {
        try {
            return this.repo.getPrgList().get(0).getStack().toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }
}