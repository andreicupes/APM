package Controller;

import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GarbageCollector {
    public static List<Integer> getAddrFromTable(Collection<IValue> tableValues) {
        return tableValues.stream()
                .filter(v -> v.getType() instanceof RefType)
                .map(v -> { RefValue v1 = (RefValue) v; return v1.getAddr();})
                .collect(Collectors.toList());
    }

    public static Map<Integer, IValue> safeGarbageCollector (List<Integer> symTableAddr, Map<Integer, IValue> heap) {
        List<Integer> heapAddr = getAddrFromTable(heap.values());

        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
