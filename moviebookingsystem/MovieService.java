package moviebookingsystem;

import java.util.HashMap;

public class MovieService {
    private HashMap<String, Movie> movies;
    private static MovieService instance;

    private MovieService() 
    {
        movies = new HashMap<>();
    }

    public synchronized static MovieService getInstance()
    {
        if(instance == null)
        {
            instance = new MovieService();
        }
        return instance;
    }

    public Movie addMovie(String name, Double duration)
    {
        Movie movie = new Movie(name, duration);
        movies.put(movie.getID(), movie);
        return movie;
    }
}
