package ru.ange.mhb.dao.rs;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.mhb.pojo.fav.FavMovie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class FavMovieMapper implements RowMapper<FavMovie> {

    @Override
    public FavMovie mapRow(ResultSet rs, int i) throws SQLException {
        String fmTitle = rs.getString("fm_title");
        int fmTmdbId = rs.getInt("fm_tmdbId");

        if (fmTitle != null && fmTmdbId != 0)
            return new FavMovie(fmTitle, fmTmdbId)
                    .setId(rs.getInt("fm_id"))
                    .setFavListId(rs.getInt("fm_favListId"))
                    .setAddDate(rs.getDate("fm_addDate"))
                    .setAddUserId(rs.getInt( "fm_addUserId"))
                    .setWatched(rs.getDate("fm_watched") != null)
                    .setWatchedDate(rs.getDate("fm_watched"))
                    .setRating(rs.getInt("fm_rating"));
        else
            return null;
    }

}
