import java.util.HashMap;
import java.util.HashSet;

public class ProbabilitiesAndCorrelations {

    public static HashMap<Integer, Double> probabilities = new HashMap<>();
    public static HashMap<Integer, HashMap<Integer, Double>> twoProbs = new HashMap<>();

    public static HashMap<Integer, HashSet<Integer>> positiveCorrelations = new HashMap<>();

    public static double probability_one_movie(int movie_id) {
        double multiplier = 1.0 / (Constants.numOfUsers + 1.0);
        double sum = 2.0 / Constants.numOfMovies;
        for(User user : Parsing.users.values()) {
            if(user.watched_movie(movie_id)) {
                sum += (2.0 / user.getNumOfRatings());
            }
        }
        sum = sum * multiplier;
        Parsing.movies.get(movie_id).setWatchedProbability(sum);
        return sum;
    }

    public static double probability_two_movies(int id_1, int id_2) {
        double multiplier = 1.0 / (Constants.numOfUsers + 1.0);
        double sum = 2.0 / (Constants.numOfMovies * (Constants.numOfMovies - 1.0));
        for(User user : Parsing.users.values()) {
            if(user.watched_movie(id_1) && user.watched_movie((id_2))) {
                int numOfRatings = user.getNumOfRatings();
                sum += (2.0 / (numOfRatings * (numOfRatings - 1)));
            }
        }
        sum = sum * multiplier;
        return sum;
    }

    public static boolean positivelyCorrelated(Movie movie1, Movie movie2) {
        int id1 = movie1.getId();
        int id2 = movie2.getId();
        if(movie1.isCheckedCorrelation(id2)) {
            return movie1.isPositivelyCorrelated(id2);
        }
        if(movie2.isPositivelyCorrelated(id1)) {
            return movie2.isPositivelyCorrelated(id1);
        }

        if(Double.compare(movie1.watched_probability, 0.0) == 0) {
            probability_one_movie(id1);
        }


        if(Double.compare(movie2.watched_probability, 0.0) == 0) {
            probability_one_movie(id2);
        }

        // if we are here we know both movies probabilities are set
        // hence no need to calculate them again
        double movie1Prob = movie1.getWatchedProbability();
        double movie2Prob = movie2.getWatchedProbability();

        double bothProb = probability_two_movies(movie1.getId(), movie2.getId());
        movie1.addCheckedCorrelation(id2);
        movie2.addCheckedCorrelation(id1);
        return Double.compare(bothProb, (movie1Prob * movie2Prob)) >= 0;
    }
}
