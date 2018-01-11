package bgu.spl181.net.impl.Blockbuster.gsonimpl;

import static java.lang.Integer.*;

/**
 * This class represent a movie object with its information.
 * data member :
 *  	id := the movie id in the database.
 *  	name := the movie name that will be requested by the clients.
 *  	price := the cost to rent this movie.
 *  	bannedCountries := the banned countries that this movie cannot be broadcast into.
 * 	 	availableAmount := how many copies left to the server to rent to clients.
 * 		totalAmount := general amount of copies of this movie.
 */
public class movie {
    private String id;
    private String name;
    private String price;
    private String [] bannedCountries;
    private String availableAmount;
    private String totalAmount;

    /**
     * default constructor.
     * @param name
     * @param price
     * @param bannedCountries
     * @param totalAmount
     */
    public movie(String name, String price, String [] bannedCountries, String totalAmount){
        this.id = "0";
        this.totalAmount = totalAmount;
        this.availableAmount = totalAmount;
        this.bannedCountries = bannedCountries;
        this.name = name;
        this.price = price;
    }
    
    /**
     * rent this movie and reduce the current available amount of copies.
     */
    public void RentThisMovie(){
        int availableAmount = valueOf(this.availableAmount);
        availableAmount--;
        this.availableAmount = "" + availableAmount;
    }
    
    /**
     * return this movie and add 1 to the current available amount of copies.
     */
    public void ReturnThisMovie(){
        int availableAmount = valueOf(this.availableAmount);
        availableAmount++;
        this.availableAmount = "" + availableAmount;
    }
    
    /**
     * @return a boolean that indicate if this movie can be rent by more clients.
     */
    public boolean IsThereAMovieLeft(){
        int availableAmount = valueOf(this.availableAmount);
        return availableAmount > 0;
    }
    
    /**
     * set the price for this movie.
     * @param price
     */
    public void setPrice(int price){
        this.price = "" + price;
    }
    
    /**
     * getter for the price movie.
     * @return price movie
     */
    public int getPrice(){
        return valueOf(price);
    }
    
    /**
     * setter for this movie ID
     * @param id
     */
    public void setId (int id){this.id = "" + id;}

    /**
     * getter for the movie name
     * @return this movie name
     */
    public String getName() {
        return name;
    }

    /**
     * getter for the movie ID
     * @return this movie ID
     */
    public int getId() {
        return valueOf(id);
    }

    @Override
    public String toString() {
        String Countries = "";
        if(bannedCountries.length !=0){
            for (String name:bannedCountries) {
            	if ( name!= null ) {
                    if(Countries.length() == 0)
                        Countries = " "+'"' +name+ '"';
                    else
                        Countries = Countries + " " + '"' +name+ '"';
            	}
            }
        }
        return broadcastToString() + Countries ;
    }
    
    public String broadcastToString() {
    	return '"'+name+'"'+ " " +availableAmount + " " + price; //<"movie name"> < No. copies left > <price>
    }
    
    /**
     * @param country
     * @return true if country is in the forbidden countries list.
     */
    public boolean doesThisCountryForbidden(String country) {
    	boolean ans = false;
    	for (String tempCountry : bannedCountries) {
    		if (tempCountry!= null && tempCountry.equals(country)) {
    			ans = true;
    			break;
    		}
    	}
		return ans;
    }
    
    /**
     * @return true if someone renting this movie
     */
    public boolean isSomeoneRetning() {
    	boolean ans = false;
    	if (Integer.valueOf(this.availableAmount) < Integer.valueOf(this.totalAmount))
    		ans = true;
    	return ans;
    }

    /**
     * End of File.
     */
}
