package com.example.patterns.object_pool;

class CustomObject {

    private int intVar;
    private String stringVar;

    public CustomObject() {}

    public CustomObject(int intValue, String stringValue) {
        this.intVar = intValue;
        this.stringVar = stringValue;
    }

    public int getIntVar() {
        return intVar;
    }

    public void setIntVar(int intVar) {
        this.intVar = intVar;
    }

    public String getStringVar() {
        return stringVar;
    }

    public void setStringVar(String stringVar) {
        this.stringVar = stringVar;
    }

    @Override
    public String toString() {
        return "CustomObject("+hashCode()+"){" +
                "intVar=" + intVar +
                ", stringVar='" + stringVar + '\'' +
                '}';
    }
}
