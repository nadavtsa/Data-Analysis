import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class Movie {

    private int id;
    private String title;
    private String[] genres;
    private double pm;
    private Vector<Integer> positiveCorrelations;


    public Movie(String[] initData) {
        this.id = Integer.parseInt(initData[0]);
        this.title = initData[1];
        this.genres = initData[2].split("\\|");
        this.pm = 0;
        this.positiveCorrelations = new Vector<>();
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String[] getGenres() {
        return this.genres;
    }

    public double getPm() {
        return this.pm;
    }

    public boolean isOfGenre(String genre) {
        return Arrays.asList(this.genres).contains(genre);
    }

    public void setPM(double pm) {
       this.pm = pm;
    }

    public Vector<Integer> getPositiveCorrelations() {
        return this.positiveCorrelations;
    }

    public void addPositiveCorrelation(int movieId) {
        this.positiveCorrelations.add(movieId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id &&
                Double.compare(movie.pm, pm) == 0 &&
                Objects.equals(title, movie.title) &&
                Arrays.equals(genres, movie.genres);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, pm);
        result = 31 * result + Arrays.hashCode(genres);
        return result;
    }
}
