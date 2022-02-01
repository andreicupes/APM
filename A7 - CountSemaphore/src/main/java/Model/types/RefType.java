package Model.types;

import Model.value.IValue;
import Model.value.RefValue;

public class RefType implements IType{
    private IType inner;

    public RefType(IType inner){this.inner=inner;}
    public IType getInner() {return  this.inner;}

    public boolean equals(Object another){
        if(another instanceof RefType)
            return inner.equals(((RefType) another).getInner());
        else
            return false;
    }

    public String toString() {return "Ref("+inner.toString()+")";}
    public IValue defaultValue(){ return new RefValue(0,inner);}

    @Override
    public IType deepCopy() {
        return new RefType(inner);
    }
}
