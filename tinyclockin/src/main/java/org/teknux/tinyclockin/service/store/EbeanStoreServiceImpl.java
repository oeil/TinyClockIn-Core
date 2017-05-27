package org.teknux.tinyclockin.service.store;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.DbMigrationConfig;
import io.ebean.config.ServerConfig;
import org.avaje.datasource.DataSourceConfig;
import org.teknux.tinyclockin.model.AuthToken;
import org.teknux.tinyclockin.model.ClockAction;
import org.teknux.tinyclockin.model.query.QAuthToken;
import org.teknux.tinyclockin.model.query.QClockAction;
import org.teknux.tinyclockin.service.IServiceManager;
import org.teknux.tinyclockin.service.ServiceException;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Francois EYL
 */
public class EbeanStoreServiceImpl implements IStoreService {

    @Override
    public void start(IServiceManager serviceManager) throws ServiceException {
        //read ebean config & create server & migrate database when necessary
        final DataSourceConfig dataSourceConfig = EbeanStoreServiceImpl.DatasourceConfigFactory.create(false);
        final ServerConfig serverConfig = EbeanStoreServiceImpl.ServerConfigFactory.build(dataSourceConfig, true);
        serverConfig.setH2ProductionMode(true);

        EbeanServerFactory.create(serverConfig);
    }

    @Override
    public void stop() throws ServiceException {
        //gracefully stops comm with server when needed
    }

    @Override
    public AuthToken getOrCreateToken(final String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        AuthToken token = new QAuthToken().email.eq(email).findUnique();

        if (token != null) {
            return token;
        } else {
            //create & store and return
            token = this.newAuthToken(email);
            Ebean.save(token);
            return token;
        }

    }

    @Override
    public AuthToken findToken(final String tokenId) {
        if (tokenId == null || tokenId.isEmpty()) {
            return null;
        }

        return new QAuthToken().token.eq(tokenId).findUnique();
    }

    @Override
    public boolean isTokenExist(final String tokenId) {
        if (tokenId == null || tokenId.isEmpty()) {
            return false;
        }

        return new QAuthToken().token.eq(tokenId).findCount() > 0;
    }

    @Override
    public List<ClockAction> getActions(final String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        return new QClockAction().authToken.email.ieq(email).findList();
    }

    @Override
    public ClockAction getLastAction(final String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        return new QClockAction().setMaxRows(1).authToken.email.ieq(email).orderBy().timestamp.desc().findUnique();
    }

    @Override
    public ClockAction storeAction(final String email, final ClockAction action) {
        if (email == null || email.trim().isEmpty() || action == null) {
            return null;
        }

        action.setTimestamp(LocalDateTime.now());
        final AuthToken token = new QAuthToken().email.ieq(email).findUnique();
        token.getClockActions().add(action);
        Ebean.save(token);

        return action;
    }

    /**
     * @author Francois EYL
     */
    public static final class DatasourceConfigFactory {

        private static final String DRIVER = "org.h2.Driver";
        private static final String URL_MEM = "jdbc:h2:mem:tests;DB_CLOSE_DELAY=0";
        private static final String URL_FILE = "jdbc:h2:~/database;AUTO_SERVER=TRUE";
        private static final String DEFAULT_USER = "sa";
        private static final String DEFAULT_PWD = "";
        private static final String HEARTBEAT_SQL = "select 1";

        private DatasourceConfigFactory() {
        }

        public static DataSourceConfig create(final boolean inMemory) {
            final DataSourceConfig datasource = new DataSourceConfig();
            datasource.setDriver(DRIVER);
            datasource.setUsername(DEFAULT_USER);
            datasource.setPassword(DEFAULT_PWD);
            datasource.setHeartbeatSql(HEARTBEAT_SQL);
            datasource.setUrl(inMemory ? URL_MEM : URL_FILE);
            return datasource;
        }
    }

    public static final class ServerConfigFactory {

        private ServerConfigFactory() {
        }

        public static ServerConfig build(final DataSourceConfig dataSourceConfig, final boolean runMigration) {
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.setName("db");

            serverConfig.loadFromProperties();
            serverConfig.setDdlRun(false);
            serverConfig.setDdlGenerate(false);
            serverConfig.setDdlCreateOnly(false);

            DbMigrationConfig dbMigrationConfig = serverConfig.getMigrationConfig();
            dbMigrationConfig.setRunMigration(runMigration);
            serverConfig.setMigrationConfig(dbMigrationConfig);

            serverConfig.setDefaultServer(true);
            serverConfig.setRegister(true);
            serverConfig.setDataSourceConfig(dataSourceConfig);

            return serverConfig;
        }
    }
}
