package org.teknux.tinyclockin.test.store;

import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import org.avaje.datasource.DataSourceConfig;
import org.junit.Before;
import org.teknux.tinyclockin.service.store.EbeanStoreServiceImpl;
import org.teknux.tinyclockin.service.store.IStoreService;


/**
 * @author Francois EYL
 */
public class EbeanStoreServiceTest extends AbsStoreServiceTest {

    @Before
    public void setup() {
        //force to generate and apply DDL to in-memory database
        final DataSourceConfig dataSourceConfig = EbeanStoreServiceImpl.DatasourceConfigFactory.create(true);
        final ServerConfig serverConfig = EbeanStoreServiceImpl.ServerConfigFactory.build(dataSourceConfig, false);
        serverConfig.setH2ProductionMode(false);
        EbeanServerFactory.create(serverConfig);
    }

    @Override
    protected IStoreService createStoreServiceInstance() {
        return new EbeanStoreServiceImpl();
    }
}
