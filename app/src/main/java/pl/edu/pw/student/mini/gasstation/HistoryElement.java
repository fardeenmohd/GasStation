package pl.edu.pw.student.mini.gasstation;

/**
 * Created by Filip Matracki on 1/2/2017.
 */

public class HistoryElement {

    private String date;
    private String name;
    private String price;
    public HistoryElement(){

    }
    public HistoryElement(String date_, String name_, String price_){
        date = date_;
        name = name_;
        price = price_;
    }

    public String getDate(){
        return date;
    }
    public String getName(){
        return name;
    }
    public String getPrice(){
        return price;
    }
    @Override
    public String toString(){
        return "Date: " + date + "Name: " + name + "Price: " + price;
    }
}
