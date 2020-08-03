import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class MovieProbability implements Runnable {

    private Movie movie;

    public MovieProbability(Movie movie) {
        super();
        this.movie = movie;
    }

    public void run() {
        int movieId = this.movie.getId();
        Double prob = this.movie.getWatchedProbability();
        if(Double.compare(prob, 0.0) == 0) {
            ProbabilitiesAndCorrelations.probability_one_movie(movieId);
        }
        if(!ProbabilitiesAndCorrelations.probabilities.containsKey(movieId)) {
            ProbabilitiesAndCorrelations.probabilities.put(movieId, this.movie.getWatchedProbability());
        }
        for(Movie other : Parsing.movies.values()) {
            int otherId = other.getId();
            if(otherId != movieId) {
                double prob2 = ProbabilitiesAndCorrelations.probability_two_movies(movieId, otherId);
                if(!ProbabilitiesAndCorrelations.twoProbs.containsKey(movieId)) {
                    HashMap<Integer, Double> mapToAdd = new HashMap<>();
                    mapToAdd.put(otherId, prob2);
                    ProbabilitiesAndCorrelations.twoProbs.put(movieId, mapToAdd);
                }
                else {
                    if(!ProbabilitiesAndCorrelations.twoProbs.get(movieId).containsKey(otherId)) {
                        ProbabilitiesAndCorrelations.twoProbs.get(movieId).put(otherId, prob2);
                    }
                    else {
                        System.out.println("This should not happen");
                    }
                }

                if(ProbabilitiesAndCorrelations.positivelyCorrelated(this.movie, other)) {
                    if(!ProbabilitiesAndCorrelations.positiveCorrelations.containsKey(movieId)) {
                        ProbabilitiesAndCorrelations.positiveCorrelations.put(movieId, new HashSet<>(otherId));
                    }
                    else {
                        ProbabilitiesAndCorrelations.positiveCorrelations.get(movieId).add(otherId);
                    }
                }
            }
        }
        System.out.println("finished task - " + movieId);
    }
}
