package edu.rit.se.tutorme.util;

/**
 * Class to represent a location.
 *
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class Location {

    /**
     * Original postal address;
     */
    private String postal;

    /**
     * Postal city.
     */
    private String city;

    /**
     * State (if applicable)
     */
    private String state;

    /**
     * Country code.
     */
    private String country;

    /**
     * Default constructor for the Location class.
     */
    public Location() {
        this.postal = "";
        this.city = "";
        this.state = "";
        this.country = "";
    }

    /**
     * Constructor for the Location class.
     * @param postal
     * @param city
     * @param state
     * @param country
     */
    public Location(String postal,
                    String city,
                    String state,
                    String country) {
        this.postal = postal;
        this.city = city;
        this.state = state;
        this.country = country;
    }
}
