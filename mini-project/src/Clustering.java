import java.util.*;

public class Clustering {

    private static final Object clusteringCostLock = new Object();
    private static final Object finishedTasksLock = new Object();

    private HashSet<HashSet<Movie>> clustering;
    private Double clusteringCost;
    private Integer numberOfFinishedTasks;
    private boolean alreadyCalculated;

    public Clustering() {
        this.clustering = new HashSet<>();
        this.clusteringCost = 0.0;
        this.numberOfFinishedTasks = 0;
        this.alreadyCalculated = false;
    }

    public Clustering(HashSet<HashSet<Movie>> clustering) {
        this.clustering = clustering;
        this.clusteringCost = 0.0;
        this.numberOfFinishedTasks = 0;
        this.alreadyCalculated = false;
    }

    public HashSet<HashSet<Movie>> getClustering() {
        return this.clustering;
    }

    public void addMultipleClusters(HashSet<HashSet<Movie>> multiple) {
        for(HashSet<Movie> s : multiple) {
            addCluster(s);
        }
    }

    public void addCluster(HashSet<Movie> cluster) {
        if(checkClusterToAdd(cluster)) {
            this.clustering.add(cluster);
        }
    }

    public boolean removeCluster(HashSet<Movie> toRemove) {
        return this.clustering.remove(toRemove);
    }

    private boolean checkClusterToAdd(HashSet<Movie> newCluster) {
        for(HashSet<Movie> cluster : this.clustering) {
            if(!Collections.disjoint(cluster, newCluster)) {
                return false;
            }
        }
        return true;
    }

    public void printClusteringStatistics() {
        System.out.println("Number of Clusters - " + this.getClustering().size());
        System.out.println("Cost - " + this.clusteringCost());
    }

    public double clusteringCost() {
        if(this.alreadyCalculated) {
            return this.clusteringCost;
        }
        Thread[] threads = new Thread[this.clustering.size()];
        int i = 0;
        for(HashSet<Movie> cluster : this.clustering) {
            ClusterCost task = new ClusterCost(cluster);
            threads[i] = new Thread(task);
            i++;
        }

        for (Thread thread : threads) {
            thread.start();
        }

        // wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.getStackTrace();
            }
        }

        if(numberOfFinishedTasks != clustering.size()) {
            System.out.println("Did not calculate all clusters!");
            System.out.println("Suppose to be " + clustering.size() + " but calculated " + numberOfFinishedTasks);
        }

//        // wait for all threads to finish
//        // busy wait
//        int check = 0;
//        while(check < this.clustering.size()){
//            synchronized (finishedTasksLock) {
//                check = numberOfFinishedTasks;
//            }
//        }
        this.alreadyCalculated = true;
        return this.clusteringCost;
    }

    private double clusterCost(HashSet<Movie> cluster) {
        double res = 0.0;
        double multiplier = (double) cluster.size() - 1.0;
        multiplier = 1.0 / multiplier;
        for(Movie movie : cluster) {
            int id1 = movie.getId();
            for(Movie movie2 : cluster) {
                int id2 = movie2.getId();
                if(id1 != id2) {
                    double next = 1.0 / ProbabilitiesAndCorrelations.probability_two_movies(id1, id2);
                    next = Math.log(next);
                    next = next / Math.log(2.0);
                    res += next;
                }
            }
        }
        // because we added twice each couple of movies
        // we need to divide in 2
        res = res / 2.0;

        res = multiplier * res;
        return res;
    }

    private class ClusterCost implements Runnable {

        private HashSet<Movie> cluster;
        private double result;

        public ClusterCost(HashSet<Movie> cluster) {
            super();
            this.cluster = cluster;
        }

        public void run() {
            if(this.cluster.size() > 1) {
                double res = clusterCost(this.cluster);
                synchronized (clusteringCostLock) {
                    clusteringCost += res;
                    synchronized (finishedTasksLock) {
                        numberOfFinishedTasks++;
                    }
                }
            }
            else {  // cluster size == 1
                for(Movie movie : this.cluster) {
                    double res = 1.0 / movie.getWatchedProbability();
                    res = Math.log(res);
                    res = res / Math.log(2.0);
                    synchronized (clusteringCostLock) {
                        clusteringCost += res;
                        synchronized (finishedTasksLock) {
                            numberOfFinishedTasks++;
                        }
                    }
                }
            }
            return;
        }
    }
}
