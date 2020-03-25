import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.File;


public class Main {

    public static void main(String[] args) {
        Utils.parseAndCreateUsers();
        Utils.parseAndCreateMovies();
        HashMap<Integer, User> users = Utils.users;
        HashMap<Integer, Movie> movies = Utils.movies;
        HashMap<Integer, Vector<Pair<Integer, Integer>>> ratings = Utils.parseRatings();

        for(User user : users.values()) {
            Utils.setUserRatings(user, ratings.get(user.getId()));
        }

        Utils.calculatePms();
//
//        double sum = 0.0;
//        for(Movie movie : movies.values()) {
//            sum = sum + movie.getPm();
//        }

        System.out.println(users.size());
        System.out.println(movies.size());

        Cluster cluster = pivotAlgorithm(movies.get(80), movies.values());

        HashSet<HashSet<Movie>> check = cluster.getCluster();
        double sum = 0.0;
        for(HashSet<Movie> set: check) {
            System.out.println(set.size());
            sum = sum + Utils.subClusterCost(set);
        }
        System.out.println(sum);
    }

    public static Cluster pivotAlgorithm(Movie pivot, Collection<Movie> movies) {
        Cluster ret = new Cluster();
        HashSet<Movie> positiveCorrelations = new HashSet<>();
        HashSet<Movie> negativeCorrelations = new HashSet<>();
        positiveCorrelations.add(pivot);
        for(Movie movie : movies) {
            if ((pivot.getId() != movie.getId()) & Utils.isPositivelyCorrelated(pivot, movie)) {
                positiveCorrelations.add(movie);
            }
            else if (pivot.getId() != movie.getId()) {
                negativeCorrelations.add(movie);
            }
        }
        ret.addSubCluster(positiveCorrelations);
        ret.addSubCluster(negativeCorrelations);
        return ret;
    }
}
