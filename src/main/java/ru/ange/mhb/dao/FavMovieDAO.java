package ru.ange.mhb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.ange.mhb.dao.rs.FavMovieMapper;
import ru.ange.mhb.pojo.fav.FavMovie;

import java.util.List;

@Component
public class FavMovieDAO {

    private static final FavMovieMapper RM = new FavMovieMapper();

    @Autowired
    private NamedParameterJdbcTemplate npjdbc;

    private static final String ADD_FAV_MOVIE = "" +
            "INSERT INTO moviehatbot.FavMovies VALUES (" +
            "  default, :fm_name, :fm_tmdbId, :fm_favListId, :fm_addDate, :fm_addUserId, :fm_watched, :fm_rating " +
            ")";

    public FavMovie addFavMovie(FavMovie favMovie) throws DataAccessException {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        npjdbc.update(ADD_FAV_MOVIE, favMovieToParams(favMovie), holder);
        return favMovie.setId( holder.getKey().intValue() );
    }

    private static final String GET_FAV_MOVIE = "" +
            "select  " +
            "  fm.id        as fm_id, " +
            "  fm.name      as fm_name, " +
            "  fm.tmdbId    as fm_tmdbId, " +
            "  fm.favListId as fm_favListId, " +
            "  fm.addDate   as fm_addDate, " +
            "  fm.addUserId as fm_addUserId, " +
            "  fm.watched   as fm_watched, " +
            "  fm.rating    as fm_rating " +
            "from " +
            "  moviehatbot.FavMovies as fm " +
            "where " +
            "  fm.id = :fm_id";

    public FavMovie getFavMovie(int movieId) throws DataAccessException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("fm_id", movieId);
        return npjdbc.queryForObject(GET_FAV_MOVIE, params, RM);
    }

    private static final String UPDATE_FAV_MOVIE = "" +
            "update moviehatbot.FavMovies set " +
            "  name      = :fm_name, " +
            "  tmdbId    = :fm_tmdbId, " +
            "  favListId = :fm_favListId, " +
            "  addDate   = :fm_addDate, " +
            "  addUserId = :fm_addUserId, " +
            "  watched   = :fm_watched, " +
            "  rating    = :fm_rating " +
            "where " +
            "  id = :fm_id";

    public FavMovie updateFavMovie(FavMovie favMovie) {
        npjdbc.update(UPDATE_FAV_MOVIE, favMovieToParams(favMovie) );
        return favMovie;
    }

    private MapSqlParameterSource favMovieToParams(FavMovie favMovie) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("fm_id", favMovie.getId());
        params.addValue("fm_tmdbId", favMovie.getTmdbId());
        params.addValue("fm_name", favMovie.getName());
        params.addValue("fm_favListId", favMovie.getFavListId());
        params.addValue("fm_addDate", favMovie.getAddDate());
        params.addValue("fm_addUserId", favMovie.getAddUserId());
        params.addValue("fm_watched", favMovie.isWatched());
        params.addValue("fm_rating", favMovie.getRating());
        return params;
    }
}
