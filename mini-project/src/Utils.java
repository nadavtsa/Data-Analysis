import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Utils {

    public static HashMap<Integer, User> parseAndCreateUsers() {
        String path = "/home/nadav/Desktop/mini-project/src/ml-1m/users.dat";
        HashMap<Integer, User> ret = new HashMap<Integer, User>();
        try {
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
        return ret;
    }

    public static HashMap<Integer, Movie> parseAndCreateMovies() {
        String path = "/home/nadav/Desktop/mini-project/src/ml-1m/movies.dat";
        HashMap<Integer, Movie> ret = new HashMap<Integer, Movie>();
        try {
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
        return ret;
    }


    public static Vector<Rating> parseAndCreateRatings() {
        String path = "/home/nadav/Desktop/mini-project/src/ml-1m/ratings.dat";
        Vector<Rating> ret = new Vector<Rating>();
        try {
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
        String path = "/home/nadav/Desktop/mini-project/src/ml-1m/ratings.dat";
        HashMap<Integer, Vector<Pair<Integer, Integer>>> ret = new HashMap<>();
        try {
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

    private static double calculatePmFormula(double k, int movieId1, int movieId2,
                                             Collection<User> users, boolean isCorrelation) {
        double numOfUsers = users.size();
        double multiplier = 2.0 / (numOfUsers + 1.0);
        double pm = multiplier * (1.0 / k);
        double sum = 0.0;
        for(User user: users) {
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

    public static void calculatePms(Collection<User> users, Collection<Movie> movies) {
        // calculate pm's
        for(Movie movie : movies) {
            movie.setPM(calculatePmFormula(movies.size(), movie.getId(), -1, users, false));
        }
    }

    public boolean isPositivelyCorrelated(Movie movie1, Movie movie2, int numOfMovies,
                                          Collection<User> users) {
        double independentMovies = movie1.getPm() * movie2.getPm();
        double correlation = calculatePmFormula(numOfMovies, movie1.getId(), movie2.getId(),
                users, true) -
                                     calculatePmFormula(numOfMovies, movie1.getId(), movie2.getId(),
                                             users, false);
        if(independentMovies <= correlation) {
            return true;
        }
        return false;
    }
}
