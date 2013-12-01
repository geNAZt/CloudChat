package net.cubespace.CloudChat.Database.Logging;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.cubespace.CloudChat.CloudChatPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fabian on 01.12.13.
 */
public class Database {
    private CloudChatPlugin plugin;
    private ConnectionSource connectionSource;
    private HashMap<Class<?>, Dao> daos = new HashMap<>();

    public Database(CloudChatPlugin plugin) {
        this.plugin = plugin;

        try {
            connectionSource = new JdbcPooledConnectionSource(
                    plugin.getDatabaseConfig().Url.replace("{DIR}", plugin.getDataFolder().getAbsolutePath() + File.separator),
                    plugin.getDatabaseConfig().Username,
                    plugin.getDatabaseConfig().Password);

            daos.put(ChatMessage.class, DaoManager.createDao(connectionSource, ChatMessage.class));

            //Create the tables if not existing
            for(Map.Entry<Class<?>, Dao> dao : daos.entrySet()) {
                TableUtils.createTableIfNotExists(connectionSource, dao.getKey());
            }
        } catch(SQLException e) {
            throw new RuntimeException("Could not connect to logging Database", e);
        }
    }

    public synchronized Dao getDAO(Class<?> entityClass) {
        return daos.get(entityClass);
    }

    public synchronized ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
