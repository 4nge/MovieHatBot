package ru.ange.mhb.dao.rs;

import org.springframework.jdbc.core.RowMapper;
import ru.ange.mhb.pojo.fav.FavMovie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class FavMovieMapper implements RowMapper<FavMovie> {

    @Override
    public FavMovie mapRow(ResultSet rs, int i) throws SQLException {
        return new FavMovie( rs.getString("fm_name"), rs.getInt("fm_tmdbId") )
                .setId( rs.getInt("fm_id") )
                .setFavListId( rs.getInt("fm_favListId") )
                .setAddDate( rs.getDate("fm_addDate" ) )
                .setAddUserId( rs.getInt( "fm_addUserId" ) )
                .setWatched( rs.getBoolean("fm_watched") );
    }

}
