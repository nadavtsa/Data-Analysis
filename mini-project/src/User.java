import java.util.HashMap;

public class User {

    private int id;
    private int ageRange;
    private String gender;
    private int occupation;
    private String zipCode;
    private HashMap<Integer, Double> ratings;
    private int numOfRatings;

    public User(String[] initData) {
        this.id = Integer.parseInt(initData[0]);
        this.gender = initData[1];
        this.ageRange = Integer.parseInt(initData[2]);
        this.occupation = Integer.parseInt(initData[3]);
        this.zipCode = initData[4];
        this.ratings = new HashMap<>();
        this.numOfRatings = 0;
    }

    public int getOccupation() {
        return this.occupation;
    }

    public int getId() {
        return this.id;
    }

    public int getAgeRange() {
        return this.ageRange;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public String getGender() {
        return this.gender;
    }

    public int getNumOfRatings() {
        return this.numOfRatings;
    }

    public HashMap<Integer, Double> getRatings() {
        return this.ratings;
    }

    public void addRating(int movieId, double rating) {
        this.ratings.put(movieId, rating);
        this.numOfRatings++;
    }



}
