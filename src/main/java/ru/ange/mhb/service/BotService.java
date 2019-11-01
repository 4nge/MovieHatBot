package ru.ange.mhb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ange.mhb.pojo.user.BotUserBasic;
import ru.ange.mhb.pojo.movie.*;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.pojo.user.BotUserExtended;
import ru.ange.mhb.utils.Constants;

import java.util.Date;
import java.util.List;

@Service
public class BotService {

    @Autowired
    private DbService dbService;

    @Autowired
    private MovieService movieService;



    public BotUserExtended getBotUserByTelUserId(int telUserId) {
        return dbService.getBotUserByTelUserId( telUserId );
    }



    public MoviesPage searchMovies(String query, int pageIdx, String language, boolean adult) {
        return movieService.searchMovies( query, pageIdx, language, adult );
    }

    public MovieFullInfo getMovie(int movieId) {
        return movieService.getMovie(movieId);
    }

    public MovieFullInfo getMovieDetails(int movieId) {
        return movieService.getMovieDetails(movieId);
    }

    public FavMovie addFavMovie(FavMovie favMovie) {
        return dbService.addFavMovie( favMovie );
    }

    public FavMovie getFavMovie(int movieId) {
        return dbService.getFavMovie( movieId );
    }

    public FavMovie updateFavMovie(FavMovie favMovie) {
        return dbService.updateFavMovie( favMovie );
    }


//    public BotUserBasic addBotUser(BotUserBasic botUser) {
//
//        BotUserBasic newBotUser = dbService.addBotUser( botUser );
//        FavList defFavList = new FavList()
//                .setName( Constants.DEF_MOVIE_LIST_NAME )
//                .setDate( new Date())
//                .setCreateUserId( botUser.getId() )
//                .setPubl( false );
//
//        dbService.addFavList( defFavList );
//
//        return newBotUser;
//    }
//
//    public BotUserBasic getBotUser(int id) {
//        return dbService.getBotUser( id );
//    }
//
//    public BotUserBasic getBotUserByTelUserId(int telUserId) {
//        return dbService.getBotUserByTelUserId( telUserId );
//    }
//
//
//
//    public List<FavList> getUserFavLists(BotUserBasic botUser) {
//        return dbService.getUserFavLists( botUser );
//    }
//
//    public List<FavMovie> getFavMovies(BotUserBasic botUser, Movie movie) {
//        List<FavList> favLists = getUserFavLists( botUser );
//        int[] favListsId = new int[favLists.size()];
//        for (int i = 0; i < favLists.size(); i++) {
//            favListsId[i] = favLists.get( i ).getId();
//        }
//        return dbService.getFavMovies( favListsId, movie.getTmdbId() );
//    }
}
