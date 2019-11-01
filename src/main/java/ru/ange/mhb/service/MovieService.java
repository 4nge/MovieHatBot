package ru.ange.mhb.service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ange.mhb.pojo.movie.*;

import ru.ange.mhb.service.WikipediaService.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MovieService {

    private static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    private static final String POSTER_SIZE = "w500";
    private static final int FI_MAX_ACTORS = 3;
    private static final int DI_MAX_ACTORS = 15;
    private static final int MAX_PRODUCERS = 3;
    private static final int MAX_SCREENWRITERS = 3;
    private static final int MAX_GENRES = 3;

    private static final String JOB_COMPOSER = "Original Music Composer";
    private static final String JOB_PRODUCER = "Producer";
    private static final String JOB_DIRECTOR = "Director";
    private static final String JOB_SCREENPL = "Screenplay";
    private static final String JOB_PHOTODIR = "Director of Photography";

    @Autowired
    private GoogleSearchService gsService;

    @Autowired
    private KinoPoiskService kpService;

    @Autowired
    private WikipediaService wikiService;

    @Autowired
    private TmdbApi tmdbApi;


    public MoviesPage searchMovies(String query, int pageIdx, String language, boolean adult) {
        MovieResultsPage mrp = tmdbApi.getSearch().searchMovie(query, 0,
                language, adult, pageIdx);

        MoviesPage moviesPage = new MoviesPage( pageIdx, mrp.getTotalPages(), mrp.getTotalResults() );
        for (MovieDb movieDb : mrp.getResults()) {
            int tmdbId = movieDb.getId();
            String title = movieDb.getTitle();
            int year = getYear( movieDb.getReleaseDate() );
            moviesPage.addMovie( new SearchMovie( title, year, tmdbId ) );
        }

        return moviesPage;
    }

    private List<Genre> extractGenres(MovieDb movieDb) {
        List<Genre> genres = new ArrayList();
        List<info.movito.themoviedbapi.model.Genre> tmdbGenres = movieDb.getGenres();
        for (int i = 0, idx = tmdbGenres.size() > MAX_GENRES ? MAX_GENRES : tmdbGenres.size(); i < idx; i ++) {
            info.movito.themoviedbapi.model.Genre tmdbGenre = tmdbGenres.get(i);
            genres.add( new Genre(tmdbGenre.getId(), tmdbGenre.getName()));
        }
        return genres;
    }

    private List<MovieCrewPerson> extractActors(MovieDb movieDb, int actorsCount) {
        List<MovieCrewPerson> actors = new ArrayList<>();
        List<PersonCast> casts = movieDb.getCredits().getCast();
        for (int i = 0, idx = casts.size() > actorsCount ? actorsCount : casts.size() ; i < idx; i ++) {
            PersonCast pc = casts.get(i);
            actors.add(getPerson( pc.getId(), pc.getName(), WikipediaService.Profession.ACTOR));
        }
        return actors;
    }

    private MovieCrewPerson extractPerson(MovieDb movieDb, String job, Profession profession) {
        for (PersonCrew person : movieDb.getCrew()) {
            if (person.getJob().equals(job)) {
                return getPerson(person.getId(), person.getName(), profession);
            }
        }
        return null;
    }

    private KpInfo getKpInfo(String movieName) {
        List<String> links =  gsService.getKpLinks(movieName);
        if ( links != null && links.size() > 0 ) {
            String movieKpLink = links.get(0);
            return kpService.getKPInfo(movieKpLink);
        }
        return null;
    }

    private MovieCrewPerson extractComposer(MovieDb movieDb) {
        for (PersonCrew person : movieDb.getCrew()) {
            if (person.getJob().equals(JOB_COMPOSER)) {
                return getPerson(person.getId(), person.getName(), Profession.COMPOSER);
            }
        }
        return null;
    }

    private List<MovieCrewPerson> extractPersons(MovieDb movieDb, int count, String jobName, Profession profession) {
        List<MovieCrewPerson> persons = new ArrayList<MovieCrewPerson>();
        for (PersonCrew person : movieDb.getCrew()) {
            if (person.getJob().equals(jobName)) {
                persons.add(getPerson(person.getId(), person.getName(), profession));
                if (persons.size() >= count)
                    return persons;
            }
        }
        return persons;
    }

    private MovieFullInfo buildMovieFullInfo(MovieDb movieDb) {
        return new MovieFullInfo(movieDb.getTitle(), getYear(movieDb.getReleaseDate()), movieDb.getId())
                .setDesc(movieDb.getOverview())
                .setPoster(getPoster(movieDb.getId()))
                .setRating(movieDb.getUserRating())
                .setGenres(extractGenres(movieDb))
                .setDirector(extractPerson(movieDb, JOB_DIRECTOR, Profession.DIRECTOR))
                .setKpInfo(getKpInfo(movieDb.getTitle()))
                .setReleaseDate(getDate(movieDb.getReleaseDate()));
    }


    public MovieFullInfo getMovie(int movieId) {
        MovieDb movieDb = tmdbApi.getMovies().getMovie( movieId, "ru",
                TmdbMovies.MovieMethod.credits,
                TmdbMovies.MovieMethod.images,
                TmdbMovies.MovieMethod.similar
        );

        MovieFullInfo movie = buildMovieFullInfo(movieDb)
                .setActors(extractActors(movieDb, FI_MAX_ACTORS));

        return movie;
    }


    public MovieFullInfo getMovieDetails(int movieId) {
        MovieDb movieDb = tmdbApi.getMovies().getMovie( movieId, "ru",
                TmdbMovies.MovieMethod.credits,
                TmdbMovies.MovieMethod.images,
                TmdbMovies.MovieMethod.keywords,
                TmdbMovies.MovieMethod.release_dates,
                TmdbMovies.MovieMethod.similar,
                TmdbMovies.MovieMethod.alternative_titles
        );

        MovieFullInfo movie = buildMovieFullInfo(movieDb)
                .setActors(extractActors(movieDb, DI_MAX_ACTORS))
                .setBurget(movieDb.getBudget())
                .setTagline(movieDb.getTagline())
                .setRevenue(movieDb.getRevenue())
                .setOriginalTitle(movieDb.getOriginalTitle())
                .setDuration(movieDb.getRuntime())
                .setComposer(extractPerson(movieDb, JOB_COMPOSER, Profession.COMPOSER))
                .setProducers(extractPersons(movieDb, MAX_PRODUCERS, JOB_PRODUCER, Profession.PRODUCER))
                .setScreenwriters(extractPersons(movieDb, MAX_SCREENWRITERS, JOB_SCREENPL, Profession.COMPOSER))
                .setPhotographer(extractPerson(movieDb, JOB_PHOTODIR, Profession.PHOTOGRAPHER));

        return movie;
    }

    private String getPosterSizeDir() {
        List<String> posterSizes = tmdbApi.getConfiguration().getPosterSizes();
        if ( posterSizes.contains( POSTER_SIZE ) ) {
            return POSTER_SIZE;
        } else {
            posterSizes.remove("original"); // too big
            return posterSizes.get(posterSizes.size());
        }
    }

    private Poster getPoster(int movieId) {
        MovieImages mims = tmdbApi.getMovies().getImages(movieId, "ru");
        if (mims != null && mims.getPosters().size() > 0) {
            String name = mims.getPosters().get(0).getFilePath().replace("/", "");
            String path = tmdbApi.getConfiguration().getBaseUrl() + getPosterSizeDir() + "/";
            return new Poster(path, name);
        } else {
            return null;
        }
    }


    private Date getDate(String dateStr) {
        try {
            return DF.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    private int getYear(String dateStr) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDate(dateStr));
            int year = cal.get(Calendar.YEAR);
            return year;
        } catch (NullPointerException e) {
            return 0;
        }
    }

//    private MovieCrewPerson getActor(int id, String name) {
//        return getPerson( id, name, WikipediaService.Profession.ACTOR );
//    }
//
//    private MovieCrewPerson getDirector(int id, String name) {
//        return getPerson( id, name, WikipediaService.Profession.DIRECTOR );
//    }
//
//    private MovieCrewPerson getProducer(int id, String name) {
//        return getPerson( id, name, WikipediaService.Profession.PRODUCER );
//    }
//
//    private MovieCrewPerson getComposer(int id, String name) {
//        return getPerson( id, name, WikipediaService.Profession.PRODUCER );
//    }

    private MovieCrewPerson getPerson(int id, String name, WikipediaService.Profession profession) {

        MovieCrewPerson person = new MovieCrewPerson( id, name );
        PersonPeople pp = tmdbApi.getPeople().getPersonInfo( id, TmdbPeople.TMDB_METHOD_PERSON );
        boolean tmdbContainsLocalName = false;

        for (String aka : pp.getAka()) {
            boolean isCyrillic = aka.matches(".*\\p{InCyrillic}.*");
            if (isCyrillic) {
                tmdbContainsLocalName = true;
                person.setLocalizedName( aka );
                break;
            }
        }

        if (!tmdbContainsLocalName) {
            String ln = wikiService.getRussianPersonName( name, profession );
            person.setLocalizedName( ln );
        }

        return person;
    }
}
