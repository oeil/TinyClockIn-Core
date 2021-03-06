package org.teknux.tinyclockin.service.store;

import io.ebean.Ebean;
import io.ebean.EbeanServerFactory;
import io.ebean.config.DbMigrationConfig;
import io.ebean.config.ServerConfig;
import org.avaje.datasource.DataSourceConfig;
import org.teknux.tinyclockin.model.Audit;
import org.teknux.tinyclockin.model.AuthToken;
import org.teknux.tinyclockin.model.ClockAction;
import org.teknux.tinyclockin.model.query.QAudit;
import org.teknux.tinyclockin.model.query.QAuthToken;
import org.teknux.tinyclockin.model.query.QClockAction;
import org.teknux.tinyclockin.service.IServiceManager;
import org.teknux.tinyclockin.service.ServiceException;
import org.teknux.tinyclockin.service.configuration.IConfigurationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * The ebean store implementation.
 *
 * @author Francois EYL
 */
public class EbeanStoreServiceImpl implements IStoreService {

    @Override
    public void start(IServiceManager serviceManager) throws ServiceException {
        final String dbFilePath = serviceManager.getService(IConfigurationService.class).getConfiguration().getDatabaseFilePath();

        //read ebean config & create server & migrate database when necessary
        final DataSourceConfig dataSourceConfig = DatasourceConfigFactory.get().setMemoryDb(false).setDbFilePath(dbFilePath).build();
        final ServerConfig serverConfig = EbeanStoreServiceImpl.ServerConfigFactory.build(dataSourceConfig, true);
        serverConfig.setH2ProductionMode(true); //specifically tells ebean to avoid in-memory H2 db

        //manually register Entities Package for the runnable Fat JAR/WAR (ebean search classpath issue)
        List<String> packages = new ArrayList<>();
        packages.add(ClockAction.class.getPackage().getName());
        serverConfig.setPackages(packages);

        //builds the server and runs db migration
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

    @Override
    public Audit storeAudit(Audit audit) {
        if (audit == null) {
            return null;
        }

        Ebean.save(audit);
        return audit;
    }

    @Override
    public List<Audit> getAudits() {
        return new QAudit().findList();
    }

    /**
     * @author Francois EYL
     */
    public static final class DatasourceConfigFactory {

        private static final String DRIVER = "org.h2.Driver";
        private static final String URL_MEM = "jdbc:h2:mem:tests;DB_CLOSE_DELAY=0";
        private static final String URL_FILE = "jdbc:h2:%s;AUTO_SERVER=TRUE";
        private static final String DEFAULT_USER = "sa";
        private static final String DEFAULT_PWD = "";
        private static final String HEARTBEAT_SQL = "select 1";

        private String dbFilePath;
        private boolean inMemory;

        private DatasourceConfigFactory() {
            dbFilePath = "~/database";
            inMemory = true;
        }

        public static DatasourceConfigFactory get() {
            return new DatasourceConfigFactory();
        }

        public DataSourceConfig build() {
            final DataSourceConfig datasource = new DataSourceConfig();
            datasource.setDriver(DRIVER);
            datasource.setUsername(DEFAULT_USER);
            datasource.setPassword(DEFAULT_PWD);
            datasource.setHeartbeatSql(HEARTBEAT_SQL);
            datasource.setUrl(inMemory ? URL_MEM : String.format(URL_FILE, dbFilePath));
            return datasource;
        }

        public DatasourceConfigFactory setDbFilePath(String path) {
            this.dbFilePath = path;
            return this;
        }

        public DatasourceConfigFactory setMemoryDb(boolean isMemory) {
            this.inMemory = isMemory;
            return this;
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
