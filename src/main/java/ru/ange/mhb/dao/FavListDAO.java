//package ru.ange.mhb.dao;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.stereotype.Component;
//import ru.ange.mhb.dao.rs.FavListMapper;
//import ru.ange.mhb.pojo.user.BotUserBasic;
//import ru.ange.mhb.pojo.fav.FavList;
//import ru.ange.mhb.pojo.movie.Movie;
//
//import java.util.List;
//
//@Component
//public class FavListDAO {
//
//    private static final RowMapper ML_MAPPER = new FavListMapper();
//
//    @Autowired
//    private NamedParameterJdbcTemplate npjdbc;
//
//    private static final String ADD_MOVIE_LIST =
//            "insert into moviehatbot.FavLists values (" +
//            "    default, :name, :date, :userId " +
//            ") ";
//
//    private static final String ADD_MOVIE_LIST_TO_USER =
//            "insert into moviehatbot.UsersToFavLists values (" +
//                    "    default, :userId, :favListId " +
//                    ")";
//
//    public FavList addFavList(FavList favList) throws DataAccessException {
//
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("name", favList.getName() );
//        params.addValue("date", favList.getDate() );
//        params.addValue("userId", favList.getCreateUserId() );
//
//        // TODO do in one transaction
//        GeneratedKeyHolder holder = new GeneratedKeyHolder();
//        npjdbc.update( ADD_MOVIE_LIST, params, holder );
//
//        favList.setId( holder.getKey().intValue() );
//
//        params.addValue("favListId", favList.getId() );
//
//        npjdbc.update( ADD_MOVIE_LIST_TO_USER, params, holder );
//
//        return favList;
//    }
//
//
//
//    private static final String GET_MOVIE_LIST = "" +
//            "select " +
//            "   ml.id, " +
//            "   ml.name, " +
//            "   ml.date, " +
//            "   ml.createUserId, " +
//            "   case " +
//            "     when (count(utml.id) > 1) then true " +
//            "     else false " +
//            "   end as public " +
//            "from " +
//            "   moviehatbot.FavLists as ml " +
//            "   left join moviehatbot.UsersToFavLists as utml on utml.favListId = ml.id " +
//            "group by " +
//            "   ml.id, " +
//            "   ml.name, " +
//            "   ml.date, " +
//            "   ml.createUserId " +
//            "   ";
//
//    public List<FavList> getUserFavLists(BotUserBasic botUser) {
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("createUserId", botUser.getId() );
//        return npjdbc.query( GET_MOVIE_LIST, params, ML_MAPPER );
//    }
//
//    public void addToFavList(int favListId, Movie movie) {
//
//    }
//}
