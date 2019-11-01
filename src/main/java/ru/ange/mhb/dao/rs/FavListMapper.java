//package ru.ange.mhb.dao.rs;
//
//import org.springframework.jdbc.core.RowMapper;
//import ru.ange.mhb.pojo.fav.FavList;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class FavListMapper implements RowMapper<FavList> {
//
//    @Override
//    public FavList mapRow(ResultSet rs, int i) throws SQLException {
//
//        return new FavList()
//                .setId( rs.getInt("id") )
//                .setName( rs.getString( "name" ) )
//                .setDate( rs.getDate( "date" ) )
//                .setCreateUserId( rs.getInt("createUserId" ) )
//                .setPubl( rs.getBoolean( "public" ) );
//    }
//}
