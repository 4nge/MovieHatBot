package ru.ange.mhb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import ru.ange.mhb.dao.BotUserDAO;
import ru.ange.mhb.dao.FavMovieDAO;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.pojo.user.BotUserExtended;

import java.util.List;

@Service
public class DbService {

    @Autowired
    private BotUserDAO botUserDAO;

    @Autowired
    private FavMovieDAO favMovieDAO;


    public BotUserExtended getBotUserByTelUserId(int telUserId) {
        try {
            return botUserDAO.getBotUserByTelUserId(telUserId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public FavMovie addFavMovie(FavMovie favMovie) {
        try {
            return favMovieDAO.addFavMovie( favMovie );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public FavMovie getFavMovie(int movieId) {
        try {
            return favMovieDAO.getFavMovie( movieId );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public FavMovie updateFavMovie(FavMovie favMovie) {
        try {
            return favMovieDAO.updateFavMovie( favMovie );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


//    public BotUserBasic addBotUser(BotUserBasic botUser) {
//        return botUserDAO.addBotUser( botUser );
//    }

//    public BotUserExtended getBotUser(int id) {
//        try {
//            return botUserDAO.getBotUser(id);
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }

//    public FavList addFavList(FavList favList) {
//        return favListDAO.addFavList( new FavList()
//                .setName( favList.getName() )
//                .setDate( favList.getDate() )
//                .setCreateUserId( favList.getCreateUserId() ) );
//    }
//
//    public List<FavList> getUserFavLists(BotUserBasic botUser) {
//        try {
//            return favListDAO.getUserFavLists( botUser );
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }
//
//    public FavMovie getFavMovieByTmdbId(BotUserBasic botUser, int tmdbId) {
//        try {
//            //return favMovieDAO.getFavMovieByTmdbId( botUser, tmdbId );
//            return null;
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }
//
//    public List<FavMovie> getFavMovies(int[] favListsId, int tmdbId) {
//        try {
//            return favMovieDAO.getFavMovies( favListsId, tmdbId );
//        } catch (EmptyResultDataAccessException | InvalidDataAccessApiUsageException e) {
//            return null;
//        }
//    }

}
