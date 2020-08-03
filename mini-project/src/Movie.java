import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class Movie {

    private int id;
    private String title;
    private String[] genres;
    private double pm;
    private Vector<Integer> positiveCorrelations;
    private Vector<Integer> alreadyCheckedCorrelations;
    public Double watched_probability;
    private int correlationId;
    private HashMap<String, Integer> genders;
    private HashMap<Integer, Integer> occupations;
    private HashMap<Integer, Integer> ages;



    public Movie(String[] initData) {
        this.id = Integer.parseInt(initData[0]);
        this.title = initData[1];
        this.genres = initData[2].split("\\|");
        this.pm = 0;
        this.positiveCorrelations = new Vector<>();
        this.watched_probability = 0.0;
        this.correlationId = 0;
        this.alreadyCheckedCorrelations = new Vector<>();
        genders = new HashMap<>();
        occupations = new HashMap<>();
        ages = new HashMap<>();
    }

    public void addGender(String gender) {
        int curr = 0;
        if(this.genders.containsKey(gender)) {
            curr = this.genders.get(gender);
        }
        curr++;
        this.genders.remove(gender);
        this.genders.put(gender, curr);
    }

    public void addOccupation(int occupation) {
        int curr = 0;
        if(this.occupations.containsKey(occupation)) {
            curr = this.occupations.get(occupation);
        }
        curr++;
        this.occupations.remove(occupation);
        this.occupations.put(occupation, curr);
    }

    public void addAge(int age) {
        int curr = 0;
        if(this.ages.containsKey(age)) {
            curr = this.ages.get(age);
        }
        curr++;
        this.ages.remove(age);
        this.ages.put(age, curr);
    }

    public String dominantGender() {
        String dominant = "M";
        for(String gender : genders.keySet()) {
            if(!genders.containsKey(dominant)) {
                dominant = gender;
            }
            else {
                if(genders.get(gender) > genders.get(dominant)) {
                    dominant = gender;
                }
            }
        }
        return dominant;
    }

    public int dominantAge() {
        int dominant = 0;
        for(int age : ages.keySet()) {
            if(!ages.containsKey(dominant)) {
                dominant = age;
            }
            else {
                if(ages.get(age) > ages.get(dominant)) {
                    dominant = age;
                }
            }
        }
        return dominant;
    }

    public int dominantOccupation() {
        int dominant = 0;
        for(int occupation : occupations.keySet()) {
            if(!occupations.containsKey(dominant)) {
                dominant = occupation;
            }
            else {
                if(occupations.get(occupation) > occupations.get(dominant)) {
                    dominant = occupation;
                }
            }
        }
        return dominant;
    }

    public boolean watchedByGender(String gender) {
        return genders.containsKey(gender);
    }

    public boolean watchedByOccupation(int occupation) {
        return occupations.containsKey(occupation);
    }

    public boolean watchedByAge(int age) {
        return ages.containsKey(age);
    }

    public int getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(int correlationId) {
        this.correlationId = correlationId;
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

    public synchronized void addPositiveCorrelation(int movieId) {
        if(!positiveCorrelations.contains(movieId)) {
            this.positiveCorrelations.add(movieId);
        }
    }

    public synchronized boolean isPositivelyCorrelated(int movieId) {
        return this.positiveCorrelations.contains(movieId);
    }

    public synchronized void addCheckedCorrelation(int movieId) {
        if(!alreadyCheckedCorrelations.contains(movieId)) {
            this.alreadyCheckedCorrelations.add(movieId);
        }
    }

    public synchronized boolean isCheckedCorrelation(int movieId) {
        return this.alreadyCheckedCorrelations.contains(movieId);
    }

    public void setWatchedProbability(double probability) {
        this.watched_probability = probability;
    }

    public Double getWatchedProbability() {
        return this.watched_probability;
    }

    public int numberOfMutualGenres(Movie other) {
        int ret = 0;
        String[] otherGenres = other.getGenres();
        for(int i = 0; i < otherGenres.length; i++) {
            for(int j = 0; j < this.genres.length; j++) {
                if(otherGenres[i].equals(this.genres[j])) {
                    ret++;
                }
            }
        }
        return ret;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Movie other = (Movie)o;

        return  this.getId() == other.getId() &&
                this.getTitle().equals(other.getTitle());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, pm);
        result = 31 * result + Arrays.hashCode(genres);
        return result;
    }
}
