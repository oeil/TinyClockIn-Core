package org.teknux.tinyclockin.test.store;

import org.teknux.tinyclockin.service.store.IStoreService;
import org.teknux.tinyclockin.service.store.InMemoryStoreServiceImpl;


/**
 * Runs the unit test against the in-memory store service implementation.
 *
 * @author Francois EYL
 */
public class InMemoryStoreServiceTest extends AbsStoreServiceTest {

    @Override
    protected IStoreService createStoreServiceInstance() {
        return new InMemoryStoreServiceImpl();
    }
}
