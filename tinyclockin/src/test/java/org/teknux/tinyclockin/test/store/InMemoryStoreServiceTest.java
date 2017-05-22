package org.teknux.tinyclockin.test.store;

import org.junit.Assert;
import org.junit.Test;
import org.teknux.tinyclockin.model.AuthToken;
import org.teknux.tinyclockin.model.ClockAction;
import org.teknux.tinyclockin.service.store.IStoreService;
import org.teknux.tinyclockin.service.store.InMemoryStoreServiceImpl;

import java.util.ArrayList;
import java.util.List;


/**
 * Runs the unit test against the in-memory store service implementation.
 *
 * @author Francois EYL
 */
public class InMemoryStoreServiceTest extends AbsStoreServiceTest {

    @Override
    protected IStoreService createStorageInstance() {
        return new InMemoryStoreServiceImpl();
    }
}
