package Model.value;

import Model.types.IType;
import Model.types.IntType;
import Model.types.StringType;

public class StringValue implements IValue{

    private String value;

    public StringValue() {
        this.value = "";
    }

    public StringValue(String i) {
        this.value = i;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StringValue) {
            StringValue o_value = (StringValue) o;
            return o_value.getValue().equals(this.value);
        }
        return false;
    }

    public boolean equals(StringValue s) {
        return this.value.equals(s.getValue());
    }

    @Override
    public String toString() {
        return "'" + this.value + "'";
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(this.value);
    }

}
