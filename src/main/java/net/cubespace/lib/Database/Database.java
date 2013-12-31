package net.cubespace.lib.Database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

public class Database {
    private CubespacePlugin plugin;
    private ConnectionSource connectionSource;
    private final HashMap<Class<?>, Dao> daos = new HashMap<>();

    /**
     * Create a new AsyncDatabaseLogger Connection
     *
     * @param plugin Plugin for which this Connection is
     * @param url The URL to connect to
     * @param username The Username to pick
     * @param password The Password to use
     */
    public Database(CubespacePlugin plugin, String url, String username, String password) {
        this.plugin = plugin;

        plugin.getPluginLogger().info("Connecting to Database " + url);

        try {
            //Create the Pooled Connection
            connectionSource = new JdbcPooledConnectionSource(
                    url.replace("{DIR}", plugin.getDataFolder().getAbsolutePath() + File.separator),
                    username,
                    password);

            plugin.getPluginLogger().info("Connected to the Database " + url);
        } catch(SQLException e) {
            plugin.getPluginLogger().error("Could not connect to Database", e);
            throw new RuntimeException("Could not connect to Database", e);
        }
    }

    /**
     * Store the DAO for the Entity
     *
     * @param dao The constructed DAO to store
     * @param entityClass The Entity for which the DAO is
     */
    public void registerDAO(Dao dao, Class<?> entityClass) {
        //For the Logger
        plugin.getPluginLogger().debug("Register new DAO for " + entityClass.getName());

        //Register the DAO
        synchronized (daos) {
            daos.put(entityClass, dao);
        }

        //Check if this DAO needs to make a Table
        plugin.getPluginLogger().debug("Checking for Table " + entityClass.getName());
        try {
            TableUtils.createTableIfNotExists(connectionSource, entityClass);
        } catch (SQLException e) {
            plugin.getPluginLogger().warn("Could not create Table for " + entityClass.getName());
        }
    }

    /**
     * Get the correct DAO for the EntityClass. If no DAO was registered it returns null/
     *
     * @param entityClass
     * @return
     */
    public Dao getDAO(Class<?> entityClass) {
        plugin.getPluginLogger().debug("Getting DAO for " + entityClass.getName());

        return daos.get(entityClass);
    }

    /**
     * Get the ConnectionSource, its most needed to create new DAOs
     *
     * @return
     */
    public ConnectionSource getConnectionSource() {
        plugin.getPluginLogger().debug("Giving the connectionSource");

        return connectionSource;
    }
}
