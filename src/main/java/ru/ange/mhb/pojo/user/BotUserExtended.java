package ru.ange.mhb.pojo.user;

import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.fav.FavMovie;

import java.util.ArrayList;
import java.util.List;

public class BotUserExtended extends BotUserBasic {

    private List<FavList> favLists;

    public BotUserExtended(int id, int telUserId, String alias, String language, boolean adult) {
        super(id, telUserId, alias, language, adult);
        favLists = new ArrayList<>();
    }

    public List<FavList> getFavLists() {
        return favLists;
    }

    public FavList getFavListById(int id) {
        for (FavList favList : favLists) {
            if (favList.getId() == id) {
                return favList;
            }
        }
        return null;
    }

    public BotUserExtended setFavLists(List<FavList> favLists) {
        this.favLists = favLists;
        return this;
    }

    public BotUserExtended addToFavLists(FavList favList) {
        this.favLists.add( favList );
        return this;
    }

    public FavList getLastFavList() {
        if (this.favLists.size() > 0 ) {
            return this.favLists.get( this.favLists.size() -1 );
        }
        return null;
    }

    public FavMovie getFavMovieById(int id) {
        for (FavList favList : favLists) {
            for (FavMovie favMovie : favList.getFavMovies()) {
                if (favMovie.getId() == id) {
                    return favMovie;
                }
            }
        }
        return null;
    }

    public void updateFavMovie(FavMovie favMovie) {
        for (FavList favList : favLists) {
            for (int i = 0; i < favList.getFavMovies().size(); i++) {
                if (favList.getFavMovies().get(i).getId() == favMovie.getId()) {
                    favList.getFavMovies().set(i, favMovie);
                    return;
                }
            }
        }
    }
}
