package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import static java.lang.Integer.*;

public class movie {
    private String id;
    private String name;
    private String price;
    private String [] bannedCountries;
    private String availableAmount;
    private String totalAmount;

    public movie(String name, String price, String [] bannedCountries, String totalAmount){
        this.id = "0";
        this.totalAmount = totalAmount;
        this.availableAmount = totalAmount;
        this.bannedCountries = bannedCountries;
        this.name = name;
        this.price = price;
    }
    public void RentThisMovie(){
        int availableAmount = valueOf(this.availableAmount);
        availableAmount--;
        this.availableAmount = "" + availableAmount;
    }
    public boolean IsThereAMovieLeft(){
        int availableAmount = valueOf(this.availableAmount);
        return availableAmount > 0;
    }
    public void setPrice(int price){
        this.price = "" + price;
    }
    public int getPrice(){
        return valueOf(price);
    }
    public void setId (int id){this.id = "" + id;}

    public String getName() {
        return name;
    }

    public int getId() {
        return valueOf(id);
    }
}
