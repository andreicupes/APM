package Model.value;

import Model.types.IType;
import Model.types.RefType;

public class RefValue implements IValue{

    private int address;
    private IType locationType;

    public RefValue(int addr, IType locType){
        this.address=addr;
        this.locationType = locType;
    }

    public int getAddr(){return address;}

    public void setAddr(int newAddress) {
        this.address = newAddress;
    }

    public IType getLocType() {
        return this.locationType;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RefValue) {
            RefValue oVal = (RefValue) o;
            return oVal.getAddr() == this.address;
        } else return false;
    }

    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    @Override
    public IValue deepCopy() {
        return new RefValue(this.address,this.locationType);
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType.toString() + ")";
    }
}
