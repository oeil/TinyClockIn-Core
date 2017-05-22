package org.teknux.tinyclockin.test.store;

import io.ebean.Ebean;
import io.ebean.dbmigration.DdlGenerator;
import io.ebeaninternal.server.core.DefaultServer;
import org.junit.After;
import org.junit.AfterClass;
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
        Ebean.getServer(null);
    }

    @After
    public void clean() {
        //forces tp close H2 database connections, since it is in-mem db, next connection will have empty db
        //Ebean.execute(Ebean.createCallableSql("SHUTDOWN"));

    }

    @Override
    protected IStoreService createStorageInstance() {
        return new EbeanStoreServiceImpl();
    }
}
