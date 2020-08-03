import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Parsing {

    public static HashMap<Integer, User> users = new HashMap<>();
    public static HashMap<Integer, Movie> movies = new HashMap<>();

    public static void parseAndCreateUsers() {
        try {
            String path = new File(".").getAbsolutePath() + "/src/users.txt";
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String nextLine = "";
            while ((nextLine = br.readLine()) != null) {
                String[] nextInit = nextLine.split("::");
                User userToAdd = new User(nextInit);
                users.put(userToAdd.getId(), userToAdd);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseAndCreateMovies() {
        // int nextId = 1;
        try {
            String path = new File(".").getAbsolutePath() + "/src/movies.txt";
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String nextLine = "";
            while ((nextLine = br.readLine()) != null) {
                String[] nextInit = nextLine.split("::");
                Movie movieToAdd = new Movie(nextInit);
                movies.put(movieToAdd.getId(), movieToAdd);
            }
            br.close();
            fr.close();
            // setCorrelationsIds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseRatings() {
        try {
            String path = new File(".").getAbsolutePath() + "/src/ratings.txt";
            BufferedReader br = new BufferedReader(new FileReader(path));
            String nextLine = "";
            while((nextLine = br.readLine()) != null) {
                String[] next = nextLine.split("::");
                int user_id = Integer.parseInt(next[0]);
                int movie_id = Integer.parseInt(next[1]);
                double rating = Double.parseDouble(next[2]);
                User user = users.get(user_id);
                user.addRating(movie_id, rating);
                Movie movie = movies.get(movie_id);
                movie.addAge(user.getAgeRange());
                movie.addGender(user.getGender());
                movie.addOccupation(user.getOccupation());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
