import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Main {

    public static void main(String[] args) {
        Parsing.parseAndCreateUsers();
        Parsing.parseAndCreateMovies();
        Parsing.parseRatings();

        Collection<Movie> movies = Parsing.movies.values();

        HashSet<Movie> basicMovies = new HashSet<>();
        HashSet<Movie> ageMovies = new HashSet<>();
        HashSet<Movie> ageMoviesImprove = new HashSet<>();

        int count = 0;
        for(Movie m : movies) {
            if(count%29 == 1) {
                basicMovies.add(m);
                ageMovies.add(m);
                ageMoviesImprove.add(m);
            }
            count++;
        }

        System.out.println("Number of movies - " + basicMovies.size() + "\n");

        Clustering basic =  Algorithms.basicAlgorithm(new Clustering(), basicMovies);
        Clustering age = Algorithms.dominantAgeAlgorithm(new Clustering(), ageMovies, false);
        Clustering ageImprove = Algorithms.dominantAgeAlgorithm(new Clustering(), ageMoviesImprove, true);

        System.out.println("BASIC");
        basic.printClusteringStatistics();
        System.out.println("\nAGE");
        age.printClusteringStatistics();
        System.out.println("\nAGE IMPROVE");
        ageImprove.printClusteringStatistics();

    }

//    public static void saveProbabilitiesAndCorrelations() {
//        Thread[] threads = new Thread[(int)Utils.numOfMovies];
//        int i = 0;
//        // set threads tasks (runnable)
//        for(Movie movie: Utils.movies.values()) {
//            MovieProbability task = new MovieProbability(movie);
//            threads[i] = new Thread(task);
//            i++;
//        }
//
//        for(Movie movie : Utils.movies.values()) {
//            Utils.positiveCorrelations.put(movie.getId(), new HashSet<>());
//            Utils.twoProbs.put(movie.getId(), new HashMap<>());
//        }
//
//        // run threads
//        for(int j = 0; j < threads.length; j++) {
//            threads[j].start();
//        }
//
//        // join threads
//        for(int k = 0; k < threads.length; k++) {
//            try {
//                threads[k].join();
//            } catch (InterruptedException e) {
//                System.out.println("Thread " + threads[k].getName() + " was interrupted");
//            }
//        }
//
//        System.out.println("FINISHED!!");
//
//        // write hash-maps to files
//        try {
//            ObjectOutputStream outputProbabilities = new ObjectOutputStream(new FileOutputStream("load_files/probabilities.bin"));
//            ObjectOutputStream outputTwoProbabilities = new ObjectOutputStream(new FileOutputStream("load_files/twoProbabilities.bin"));
//            ObjectOutputStream outputPositiveCorrelations = new ObjectOutputStream(new FileOutputStream("load_files/correlations.bin"));
//
//            outputProbabilities.writeObject(Utils.probabilities);
//            outputTwoProbabilities.writeObject(Utils.twoProbs);
//            outputPositiveCorrelations.writeObject(Utils.positiveCorrelations);
//
//        } catch (FileNotFoundException e) {
//            System.out.println(e.getMessage());
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}

//        int count = 1;
//        int numOfMovies = 0;
//        for(Map.Entry e : Utils.movies.entrySet()) {
//            if (count % 20 == 1) {
//                numOfMovies++;
//                copy_movies_basic.put((Integer) e.getKey(), (Movie) e.getValue());
//                copy_movies_genre.put((Integer) e.getKey(), (Movie) e.getValue());
//                copy_movies_occupation.put((Integer) e.getKey(), (Movie) e.getValue());
//                copy_movies_gender.put((Integer) e.getKey(), (Movie) e.getValue());
//                copy_movies_age.put((Integer) e.getKey(), (Movie) e.getValue());
//            }
//            count++;
//        }
//
//        System.out.println("Number of movies - " + numOfMovies + "\n");
//
//        Clustering basicClustering = new Clustering();
//        basicAlgorithm(basicClustering, copy_movies_basic);
//
//        int basicCorrelations = 0;
//        for(HashSet<Movie> cluster : basicClustering.getClustering()) {
//            for(Movie movie1 : cluster) {
//                for(Movie movie2 : cluster) {
//                    if(Utils.positiveCorrelation(movie1, movie2)) {
//                        basicCorrelations++;
//                    }
//                }
//            }
//        }
//
//        System.out.println("Basic clusters - " + basicClustering.getClustering().size());
//        System.out.println("Basic cost - " + basicClustering.clusteringCost());
//        System.out.println(("Basic correlations - " + basicCorrelations/2) + "\n");
//
//        Clustering genreClustering = new Clustering();
//        genreAlgorithm(genreClustering, copy_movies_genre);
//
//        int genreCorrelations = 0;
//        for(HashSet<Movie> cluster : genreClustering.getClustering()) {
//            for(Movie movie1 : cluster) {
//                for(Movie movie2 : cluster) {
//                    if(Utils.positiveCorrelation(movie1, movie2)) {
//                        genreCorrelations++;
//                    }
//                }
//            }
//        }
//
//        System.out.println("Genre clusters - " + genreClustering.getClustering().size());
//        System.out.println("Genre cost - " + genreClustering.clusteringCost());
//        System.out.println(("Genre correlations - " + genreCorrelations/2) + "\n");
//
//        Clustering occupationClustering = new Clustering();
//        dominantOccupationAlgorithm(occupationClustering, copy_movies_occupation);
//
//        int occupationCorrelations = 0;
//        for(HashSet<Movie> cluster : occupationClustering.getClustering()) {
//            for(Movie movie1 : cluster) {
//                for(Movie movie2 : cluster) {
//                    if(Utils.positiveCorrelation(movie1, movie2)) {
//                        occupationCorrelations++;
//                    }
//                }
//            }
//        }
//
//        System.out.println("Occupation clusters - " + occupationClustering.getClustering().size());
//        System.out.println("Occupation cost - " + occupationClustering.clusteringCost());
//        System.out.println(("Occupation correlations - " + occupationCorrelations/2) + "\n");
//
//
//        Clustering genderClustering = new Clustering();
//        dominantGenderAlgorithm(genderClustering, copy_movies_gender);
//
//        int genderCorrelations = 0;
//        for(HashSet<Movie> cluster : genderClustering.getClustering()) {
//            for(Movie movie1 : cluster) {
//                for(Movie movie2 : cluster) {
//                    if(Utils.positiveCorrelation(movie1, movie2)) {
//                        genderCorrelations++;
//                    }
//                }
//            }
//        }
//
//        System.out.println("Gender clusters - " + genderClustering.getClustering().size());
//        System.out.println("Gender cost - " + genderClustering.clusteringCost());
//        System.out.println("Gender correlations - " + genderCorrelations/2 + "\n");
//
//        Clustering ageClustering = new Clustering();
//        dominantAgeAlgorithm(ageClustering, copy_movies_age);
//
//        int ageCorrelations = 0;
//        for(HashSet<Movie> cluster : ageClustering.getClustering()) {
//            for(Movie movie1 : cluster) {
//                for(Movie movie2 : cluster) {
//                    if(Utils.positiveCorrelation(movie1, movie2)) {
//                        ageCorrelations++;
//                    }
//                }
//            }
//        }
//
//        System.out.println("Age clusters - " + ageClustering.getClustering().size());
//        System.out.println("Age cost - " + ageClustering.clusteringCost());
//        System.out.println("Age correlations - " + ageCorrelations/2 + "\n");

