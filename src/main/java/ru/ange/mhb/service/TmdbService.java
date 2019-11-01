//package ru.ange.mhb.service;
//
//import info.movito.themoviedbapi.TmdbApi;
//import info.movito.themoviedbapi.TmdbMovies;
//import info.movito.themoviedbapi.model.MovieDb;
//import info.movito.themoviedbapi.model.MovieImages;
//import info.movito.themoviedbapi.model.core.MovieResultsPage;
//import info.movito.themoviedbapi.model.people.Person;
//import info.movito.themoviedbapi.model.people.PersonCrew;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import ru.ange.mhb.pojo.movie.MovieFullInfo;
//
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//
//
//
//@Service
//public class TmdbService {
//
//    private static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
//    private static final String POSTER_SIZE = "w500";
//
//    @Autowired
//    private TmdbApi tmdbApi;
//
//    public MovieResultsPage searchMovies(String query, int page) {
//        return tmdbApi.getSearch().searchMovie( query, 0, "ru",false, page );
//    }
//
//    public MovieFullInfo getMovie(int movieId) {
//
//        MovieDb movieDb = tmdbApi.getMovies().getMovie(movieId, "ru", TmdbMovies.MovieMethod.credits,
//                TmdbMovies.MovieMethod.images, TmdbMovies.MovieMethod.popular);
//
//        // TODO create builder
//        MovieFullInfo movie = new MovieFullInfo(movieDb.getTitle(), getYear(movieDb.getReleaseDate()),
//                movieDb.getOverview(), getPosterPath(movieId), movieDb.getUserRating() );
//
//        for ( PersonCrew person : movieDb.getCrew() ) {
//            if ( person.getJob().equals("Director") ) {
//                movie.setDirector( person.getName() );
//                break;
//            }
//        }
//
//        return movie;
//    }
//
//    private String getPosterSizeDir() {
//        List<String> posterSizes = tmdbApi.getConfiguration().getPosterSizes();
//        if ( posterSizes.contains( POSTER_SIZE ) ) {
//            return POSTER_SIZE;
//        } else {
//            posterSizes.remove("original"); // too big
//            return posterSizes.get(posterSizes.size());
//        }
//    }
//
//    private String getPosterPath(int movieId) {
//        MovieImages mims = tmdbApi.getMovies().getImages(movieId, "ru");
//        if (mims != null && mims.getPosters().size() > 0) {
//            String posterPath = getPosterSizeDir() + mims.getPosters().get(0).getFilePath();
//            String fullPath = tmdbApi.getConfiguration().getBaseUrl() + posterPath;
//            return fullPath;
//        } else {
//            return null;
//        }
//    }
//
//    private int getYear(String dateStr) {
//        try {
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(DF.parse(dateStr));
//            int year = cal.get(Calendar.YEAR);
//            return year;
//        } catch (ParseException | NullPointerException e) {
//            return 0;
//        }
//    }
//}
