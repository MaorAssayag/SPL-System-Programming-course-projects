package bgu.spl181.net.impl.Blockbuster.gsonimpl;

public class movie {
    private int id;
    private String name;
    private int price;
    private String [] bannedCountries;
    private int availableAmount;
    private int totalAmount;

    public movie(String name, int price, String [] bannedCountries, int totalAmount){
        this.id = 0;
        this.totalAmount = totalAmount;
        this.availableAmount = totalAmount;
        this.bannedCountries = bannedCountries;
        this.name = name;
        this.price = price;
    }
    public void RentThisMovie(){
        availableAmount--;
    }
    public boolean IsThereAMovieLeft(){
        return availableAmount > 0;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public int getPrice(){
        return price;
    }
    public void setId (int id){this.id = id;}

    public String getName() {
        return name;
    }

}
