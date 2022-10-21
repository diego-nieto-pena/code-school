package com.patterns.structural.decorator;

public class ArqulianDecorator extends DataSourceDecorator {

    public ArqulianDecorator(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void writeData(String data) {
        System.out.println("To Arquillian process");
        super.writeData(data);
    }
    @Override
    public String readData() {
        System.out.println("From Arquillian process");
        return super.readData();
    }
}
