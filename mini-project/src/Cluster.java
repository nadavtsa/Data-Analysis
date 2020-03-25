import java.util.*;

public class Cluster {

    private HashSet<HashSet<Movie>> cluster;

    public Cluster() {
        this.cluster = new HashSet<>();
    }

    public HashSet<HashSet<Movie>> getCluster() {
        return this.cluster;
    }

    public void addSubCluster(HashSet<Movie> subCluster) {
        if(checkSubClusterToAdd(subCluster)) {
            this.cluster.add(subCluster);
        }
    }

    private boolean checkSubClusterToAdd(HashSet<Movie> newSubCluster) {
        for(HashSet<Movie> subCluster : this.cluster) {
            if(!Collections.disjoint(subCluster, newSubCluster)) {
                return false;
            }
        }
        return true;
    }
}
