public class Rating {

    private int userId;
    private int movieId;
    private double rating;
    private String timeStamp;

    public Rating(String[] initData) {
        this.userId = Integer.parseInt(initData[0]);
        this.movieId = Integer.parseInt(initData[1]);
        this.rating = Integer.parseInt(initData[2]);
        this.timeStamp = initData[3];
    }

    public int getUserId() {
        return this.userId;
    }

    public int getMovieId() {
        return this.movieId;
    }

    public double getRating() {
        return this.rating;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }
}
