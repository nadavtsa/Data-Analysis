import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class Algorithms {

    private static HashSet<HashSet<Movie>> improveAgeAlgorithm(HashSet<Movie> cluster, HashSet<HashSet<Movie>> ret) {
        if(cluster.isEmpty()) {
            return ret;
        }

        if(cluster.size() == 1) {
            ret.add(cluster);
            return ret;
        }

        HashSet<Movie> toAdd = new HashSet<>();
        Movie pivot = randomPivot(cluster);
        toAdd.add(pivot);

        for (Movie movie : cluster) {
            if(pivot.getId() != movie.getId()) {
                if(ProbabilitiesAndCorrelations.positivelyCorrelated(pivot, movie)) {
                    toAdd.add(movie);
                }
            }
        }

        for(Movie m : toAdd) {
            cluster.remove(m);
        }

        ret.add(toAdd);

        return improveAgeAlgorithm(cluster, ret);
    }

    public static Clustering dominantAgeAlgorithm(Clustering clustering, Collection<Movie> movies,
                                                  boolean isImprove) {
        HashSet<Movie> toRemove = new HashSet<>();
        Movie pivot = randomPivot(movies);

//        if(isImprove) {
//            pivot = mostWatchedPivot(movies);
//        }
        int pivotId = pivot.getId();
        movies.remove(pivot);
        HashSet<Movie> cluster = new HashSet<>();
        cluster.add(pivot);
        if(movies.isEmpty()) {
            clustering.addCluster(cluster);
            if(isImprove) {
                HashSet<HashSet<Movie>> beforeImprovement = new HashSet<>(clustering.getClustering());
                for(HashSet<Movie> set : beforeImprovement) {
                    clustering.removeCluster(set);
                    clustering.addMultipleClusters(improveAgeAlgorithm(set, new HashSet<HashSet<Movie>>()));
                }
            }
            return clustering;
        }

        for(Movie movie : movies) {
            if(movie.getId() != pivotId && (movie.dominantAge() == pivot.dominantAge())) {
                cluster.add(movie);
                toRemove.add(movie);
            }
        }

        for(Movie movie : toRemove) {
            movies.remove(movie);
        }

        clustering.addCluster(cluster);
        if(movies.isEmpty()) {
            if(isImprove) {
                HashSet<HashSet<Movie>> beforeImprovement = new HashSet<>(clustering.getClustering());
                for(HashSet<Movie> set : beforeImprovement) {
                    clustering.removeCluster(set);
                    clustering.addMultipleClusters(improveAgeAlgorithm(set, new HashSet<HashSet<Movie>>()));
                }
            }
            return clustering;
        }

        return dominantAgeAlgorithm(clustering, movies, isImprove);
    }

    public static Clustering dominantGenderAlgorithm(Clustering clustering, Collection<Movie> movies) {
        HashSet<Movie> toRemove = new HashSet<>();
        Movie pivot = randomPivot(movies);
        int pivotId = pivot.getId();
        movies.remove(pivot);
        HashSet<Movie> cluster = new HashSet<>();
        cluster.add(pivot);
        if(movies.isEmpty()) {
            clustering.addCluster(cluster);
            return clustering;
        }

        for(Movie movie : movies) {
            if(movie.getId() != pivotId && (movie.dominantGender().equals(pivot.dominantGender()))) {
                cluster.add(movie);
                toRemove.add(movie);
            }
        }

        for(Movie movie : toRemove) {
            movies.remove(movie);
        }

        clustering.addCluster(cluster);
        if(movies.isEmpty()) {
            return clustering;
        }

        return dominantGenderAlgorithm(clustering, movies);
    }

    public static Clustering dominantOccupationAlgorithm(Clustering clustering, Collection<Movie> movies) {
        HashSet<Movie> toRemove = new HashSet<>();
        Movie pivot = randomPivot(movies);
        int pivotId = pivot.getId();
        movies.remove(pivot);
        HashSet<Movie> cluster = new HashSet<>();
        cluster.add(pivot);
        if(movies.isEmpty()) {
            clustering.addCluster(cluster);
            return clustering;
        }

        for(Movie movie : movies) {
            if(movie.getId() != pivotId && (movie.dominantOccupation() == pivot.dominantOccupation())) {
                cluster.add(movie);
                toRemove.add(movie);
            }
        }

        for(Movie movie : toRemove) {
            movies.remove(movie);
        }

        clustering.addCluster(cluster);
        if(movies.isEmpty()) {
            return clustering;
        }

        return dominantOccupationAlgorithm(clustering, movies);
    }


    // this algorithm chooses a random pivot and cluster with it all
    // the movies that share a genre with the pivot
    // In most cases' provides results close to the pivot algorithm
    public static Clustering genreAlgorithm(Clustering clustering, Collection<Movie> movies) {
        HashSet<Movie> toRemove = new HashSet<>();
        Movie pivot = randomPivot(movies);
        movies.remove(pivot);
        int pivot_id = pivot.getId();
        HashSet<Movie> cluster = new HashSet<>();
        cluster.add(pivot);
        if(movies.isEmpty()) {
            clustering.addCluster(cluster);
            return clustering;
        }
        for(Movie movie : movies) {
            int next_movie_id = movie.getId();
            int result = pivot.numberOfMutualGenres(movie);
            if(pivot_id != next_movie_id && result > 0) {
                cluster.add(movie);
                toRemove.add(movie);
            }
        }

        for(Movie movie : toRemove) {
            movies.remove(movie);
        }

        clustering.addCluster(cluster);
        if(movies.isEmpty()) {
            return clustering;
        }
        return genreAlgorithm(clustering, movies);
    }

    public static Movie randomPivot(Collection<Movie> movies) {
        Object[] moviesArray = movies.toArray();
        int pivotIndex = ThreadLocalRandom.current().nextInt(0, moviesArray.length);
        return (Movie)moviesArray[pivotIndex];
    }

    public static Movie mostWatchedPivot(Collection<Movie> movies) {
        Movie pivot = null;
        double max = 0.0;
        for (Movie m : movies) {
            if(Double.compare(m.getWatchedProbability(), max) >= 0) {
                max = m.getWatchedProbability();
                pivot = m;
            }
        }
        return pivot;
    }


    public static Clustering basicAlgorithm(Clustering clustering,
                                            Collection<Movie> movies) {
        HashSet<Movie> toRemove = new HashSet<>();
        Movie pivot = randomPivot(movies);
        movies.remove(pivot);
        int pivot_id = pivot.getId();
        HashSet<Movie> cluster = new HashSet<>();
        cluster.add(pivot);
        if(movies.isEmpty()) {
            clustering.addCluster(cluster);
            return clustering;
        }
        for(Movie movie : movies) {
            int next_movie_id = movie.getId();
            if(pivot_id != next_movie_id && ProbabilitiesAndCorrelations.positivelyCorrelated(pivot, movie)) {
                cluster.add(movie);
                toRemove.add(movie);
            }
        }

        for(Movie movie : toRemove) {
            movies.remove(movie);
        }

        clustering.addCluster(cluster);
        if(movies.isEmpty()) {
            return clustering;
        }
        return basicAlgorithm(clustering, movies);
    }
}
