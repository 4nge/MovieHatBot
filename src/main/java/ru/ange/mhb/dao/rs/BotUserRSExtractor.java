package ru.ange.mhb.dao.rs;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.pojo.user.BotUserExtended;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class BotUserRSExtractor implements ResultSetExtractor<BotUserExtended> {

    private static final FavMovieMapper FM_MAPPER = new FavMovieMapper();

    @Override
    public BotUserExtended extractData(ResultSet rs) throws SQLException, DataAccessException {

        BotUserExtended botUser = null;

        for (int i = 0; rs.next(); i ++) {

            if (botUser == null) {
                botUser = getBotUserFromRS( rs );
            }

            FavList favList = getFavListFromRS( rs);
            if (botUser.getLastFavList() == null || botUser.getLastFavList().getId() != favList.getId()) {
                botUser.addToFavLists( favList );
            }

            FavMovie favMovie = FM_MAPPER.mapRow( rs, i );
            botUser.getLastFavList().getFavMovies().add( favMovie );

        }

        return botUser;
    }

    private BotUserExtended getBotUserFromRS(ResultSet rs) throws SQLException {
        int usId = rs.getInt("us_id" );
        int usTelUserId = rs.getInt("us_telUserId" );
        String usAlias = rs.getString("us_alias" );
        String usLanguage = rs.getString("us_language" );
        boolean usAdult = rs.getBoolean( "us_adult" );
        return new BotUserExtended( usId, usTelUserId, usAlias, usLanguage, usAdult );
    }

    private FavList getFavListFromRS(ResultSet rs) throws SQLException {
        int flId = rs.getInt("fl_id" );
        String flName = rs.getString("fl_name" );
        Date flDate = rs.getDate("fl_date" );
        int flCreateUserId = rs.getInt("fl_createUserId" );
        return new FavList( flId, flName, flDate, flCreateUserId, false );
    }

}


