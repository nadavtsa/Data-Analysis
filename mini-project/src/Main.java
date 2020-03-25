import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.io.File;


public class Main {

    public static void main(String[] args) {
       HashMap<Integer, User> users = Utils.parseAndCreateUsers();
       HashMap<Integer, Movie> movies = Utils.parseAndCreateMovies();
       HashMap<Integer, Vector<Pair<Integer, Integer>>> ratings = Utils.parseRatings();

       for(User user : users.values()) {
           Utils.setUserRatings(user, ratings.get(user.getId()));
       }


       Utils.calculatePms(users.values(), movies.values());

       double sum = 0.0;
       for(Movie movie : movies.values()) {
           sum = sum + movie.getPm();
       }

        System.out.println(users.size());
        System.out.println(movies.size());
    }
}
