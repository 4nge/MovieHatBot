package ru.ange.mhb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import ru.ange.mhb.pojo.fav.FavList;

import javax.sql.DataSource;

@Component
public class FavListDAO {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private NamedParameterJdbcTemplate npjdbc;

    private static final String ADD_FAV_LIST = "" +
            "INSERT INTO moviehatbot.FavLists VALUES (" +
            "  default, :name, :date, :userId " +
            ")";

    private static final String ADD_FAV_USER_TO_FAVLIST = "" +
            "INSERT INTO moviehatbot.UsersToFavLists VALUES (" +
            "  default, :userId, :favListId " +
            ")";

    public FavList addFavList(FavList favList) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", favList.getName());
        params.addValue("date", favList.getDate());
        params.addValue("userId", favList.getCreateUserId());
        params.addValue("publ", favList.isPubl());

        DataSourceTransactionManager dstm = new DataSourceTransactionManager(dataSource);
        TransactionTemplate tt = new TransactionTemplate(dstm);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        int favListId = tt.execute(status -> {
            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            npjdbc.update(ADD_FAV_LIST, params, holder);

            int favListId1 = holder.getKey().intValue();
            params.addValue("favListId", favListId1);

            npjdbc.update(ADD_FAV_USER_TO_FAVLIST, params);
            return favListId1;
        });
        return favList.setId(favListId);
    }
}
