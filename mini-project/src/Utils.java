import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Utils {

    public static final int numOfMovies = 3883;
    public static final int numOfUsers = 6040;

    public static HashMap<Integer, User> users;
    public static HashMap<Integer, Movie> movies;


    public static void parseAndCreateUsers() {
        HashMap<Integer, User> ret = new HashMap<Integer, User>();
        try {
            String path = new File(".").getAbsolutePath() + "/src/users.dat";
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String nextLine = "";
            while ((nextLine = br.readLine()) != null) {
                String[] nextInit = nextLine.split("::");
                User userToAdd = new User(nextInit);
                ret.put(userToAdd.getId(), userToAdd);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        users = ret;
    }

    public static void parseAndCreateMovies() {
        HashMap<Integer, Movie> ret = new HashMap<Integer, Movie>();
        try {
            String path = new File(".").getAbsolutePath() + "/src/movies.dat";
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String nextLine = "";
            while ((nextLine = br.readLine()) != null) {
                String[] nextInit = nextLine.split("::");
                Movie movieToAdd = new Movie(nextInit);
                ret.put(movieToAdd.getId(), movieToAdd);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        movies = ret;
    }


    public static Vector<Rating> parseAndCreateRatings() {
        Vector<Rating> ret = new Vector<Rating>();
        try {
            String path = new File(".").getAbsolutePath() + "/src/ratings.dat";
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String nextLine = "";
            while ((nextLine = br.readLine()) != null) {
                String[] nextInit = nextLine.split("::");
                Rating ratingToAdd = new Rating(nextInit);
                ret.add(ratingToAdd);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    // This function parse the ratings.dat file and returns a HashMap
    // where each key is an Id of a user and each value is a Vector of all the user's ratings
    public static HashMap<Integer, Vector<Pair<Integer, Integer>>> parseRatings() {
        HashMap<Integer, Vector<Pair<Integer, Integer>>> ret = new HashMap<>();
        try {
            String path = new File(".").getAbsolutePath() + "/src/ratings.dat";
            BufferedReader br = new BufferedReader(new FileReader(path));
            String nextLine = "";
            while ((nextLine = br.readLine()) != null) {
                String[] nextInit = nextLine.split("::");
                int userId = Integer.parseInt(nextInit[0]);
                int movieId = Integer.parseInt(nextInit[1]);
                int rating = Integer.parseInt(nextInit[2]);
                if(ret.containsKey(userId)) {
                    ret.get(userId).add(new Pair<>(movieId, rating));
                }
                else {
                    Vector<Pair<Integer, Integer>> toAdd = new Vector<>();
                    toAdd.add(new Pair<>(movieId, rating));
                    ret.put(userId, toAdd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void setUserRatings(User user, Vector<Pair<Integer, Integer>> userRatings) {
        for(Pair<Integer, Integer> pair : userRatings) {
            user.addRating(pair.getFirst(), pair.getSecond());
        }
    }

    private static double calculatePmFormula(double k, int movieId1, int movieId2, boolean isCorrelation) {
        double numOfUsers = users.size();
        double multiplier = 2.0 / (numOfUsers + 1.0);
        double pm = multiplier * (1.0 / k);
        double sum = 0.0;
        for(User user: users.values()) {
            HashMap<Integer, Double> userRatings = user.getRatings();
            if (movieId2 == -1 && userRatings.containsKey(movieId1)) {
                sum = sum + (1.0 / (double)user.getNumOfRatings());
            }
            else if (userRatings.containsKey(movieId1) &&
                    userRatings.containsKey(movieId2)) {
                if(isCorrelation) {
                    sum = sum + (1.0 / ((double)user.getNumOfRatings() - 1.0));
                }
                else {
                    sum = sum + (1.0 / (double)user.getNumOfRatings());
                }

            }
        }
        return pm + (multiplier * sum);
    }

    public static void calculatePms() {
        // calculate pm's
        for(Movie movie : movies.values()) {
            movie.setPM(calculatePmFormula(movies.size(), movie.getId(), -1, false));
        }
    }

    public static double calculateCorrelation(int movieId1, int movieId2) {
        return calculatePmFormula(numOfMovies - 1, movieId1, movieId2, true) -
                calculatePmFormula(numOfMovies, movieId1, movieId2, false);
    }

    public static boolean isPositivelyCorrelated(Movie movie1, Movie movie2) {
        double independentMovies = movie1.getPm() * movie2.getPm();
        double correlation = calculateCorrelation(movie1.getId(), movie2.getId());
        if(independentMovies <= correlation) {
            return true;
        }
        return false;
    }

    public static double clusterCost(Cluster cluster) {
        double ret = 0.0;
        for(HashSet<Movie> subCluster : cluster.getCluster()) {
            ret = ret + subClusterCost(subCluster);
        }
        return ret;
    }

    public static double subClusterCost(HashSet<Movie> subCluster) {
        double ret = 0.0;
        double size = subCluster.size();
        double multiplier = 1.0 / (size - 1.0);
        for(Movie movie1 : subCluster) {
            for(Movie movie2 : subCluster) {
                if(!movie1.equals(movie2)) {
                    double log_base_2 = Math.log(1.0 / calculateCorrelation(movie1.getId(),
                            movie2.getId())) / Math.log(2.0);
                    ret = ret + multiplier * log_base_2;
                }
            }
        }
        return ret;
    }
}
