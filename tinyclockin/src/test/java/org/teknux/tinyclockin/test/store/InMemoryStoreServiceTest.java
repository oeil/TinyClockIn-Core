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
 * @author Francois EYL
 */
public class InMemoryStoreServiceTest {

    private InMemoryStoreServiceImpl createStorageInstance() {
        return new InMemoryStoreServiceImpl();
    }

    @Test
    public void getOrCreateToken() {
        final IStoreService storeService = createStorageInstance();

        Assert.assertNull(storeService.getOrCreateToken(null));
        Assert.assertNull(storeService.getOrCreateToken(""));
        Assert.assertNull(storeService.getOrCreateToken(" "));

        final String userId1 = "test.user1@test.org";
        final AuthToken user1AuthToken = storeService.getOrCreateToken(userId1);
        Assert.assertNotNull(user1AuthToken.getToken());
        Assert.assertFalse(user1AuthToken.getToken().trim().isEmpty());
        Assert.assertEquals(user1AuthToken, storeService.getOrCreateToken(userId1));

        final String userId2 = "test.user2@test.org";
        final AuthToken user2AuthToken = storeService.getOrCreateToken(userId2);
        Assert.assertNotNull(user2AuthToken.getToken());
        Assert.assertFalse(user2AuthToken.getToken().trim().isEmpty());
        Assert.assertEquals(user2AuthToken, storeService.getOrCreateToken(userId2));

        Assert.assertNotEquals(user1AuthToken, user2AuthToken);
        Assert.assertNotEquals(user1AuthToken.getToken(), user2AuthToken.getToken());
    }

    @Test
    public void findToken() {
        final IStoreService storeService = createStorageInstance();

        final String userId1 = "test.user1@test.org";
        final AuthToken userAuthToken1 = storeService.getOrCreateToken(userId1);

        final String userId2 = "test.user2@test.org";
        final AuthToken userAuthToken2 = storeService.getOrCreateToken(userId2);

        Assert.assertNull(storeService.findToken(null));
        Assert.assertNull(storeService.findToken(""));
        Assert.assertNull(storeService.findToken(" "));

        Assert.assertEquals(userAuthToken1, storeService.findToken(userAuthToken1.getToken()));
        Assert.assertEquals(userAuthToken2, storeService.findToken(userAuthToken2.getToken()));
        Assert.assertNull(storeService.findToken("UNKNOWN_TOKEN_ID"));
    }

    @Test
    public void isTokenExist() {
        final IStoreService storeService = createStorageInstance();

        final String userId1 = "test.user1@test.org";
        final AuthToken userAuthToken1 = storeService.getOrCreateToken(userId1);

        final String userId2 = "test.user2@test.org";
        final AuthToken userAuthToken2 = storeService.getOrCreateToken(userId2);

        Assert.assertFalse(storeService.isTokenExist(null));
        Assert.assertFalse(storeService.isTokenExist(""));
        Assert.assertFalse(storeService.isTokenExist(" "));

        Assert.assertTrue(storeService.isTokenExist(userAuthToken1.getToken()));
        Assert.assertTrue(storeService.isTokenExist(userAuthToken2.getToken()));
        Assert.assertFalse(storeService.isTokenExist("UNKNOWN_TOKEN_ID"));
    }

    @Test
    public void storeAction() {
        final IStoreService storeService = createStorageInstance();

        final String userId1 = "test.user1@test.org";
        final AuthToken userAuthToken1 = storeService.getOrCreateToken(userId1);


        final String userId2 = "test.user2@test.org";
        final AuthToken userAuthToken2 = storeService.getOrCreateToken(userId2);

        final ClockAction user1Action1 = storeService.storeAction(userId1, new ClockAction(1, "in", 42));
        Assert.assertNotNull(user1Action1.getId());
        Assert.assertNotNull(user1Action1.getTimestamp());
        Assert.assertEquals(1, user1Action1.getType());
        Assert.assertEquals("in", user1Action1.getDescription());
        Assert.assertEquals(42, user1Action1.getWorkstation());
        Assert.assertEquals(user1Action1, storeService.getLastAction(userId1));

        final ClockAction user1Action2 = storeService.storeAction(userId1, new ClockAction(0, "out", 142));
        Assert.assertNotNull(user1Action2.getId());
        Assert.assertNotNull(user1Action2.getTimestamp());
        Assert.assertEquals(0, user1Action2.getType());
        Assert.assertEquals("out", user1Action2.getDescription());
        Assert.assertEquals(142, user1Action2.getWorkstation());
        Assert.assertEquals(user1Action2, storeService.getLastAction(userId1));


        final ClockAction user2Action1 = storeService.storeAction(userId2, new ClockAction(1, "in", 2042));
        Assert.assertEquals(user2Action1, storeService.getLastAction(userId2));
        Assert.assertEquals(user1Action2, storeService.getLastAction(userId1));

        final ClockAction user2Action2 = storeService.storeAction(userId2, new ClockAction(0, "out", 2042));
        Assert.assertEquals(user2Action2, storeService.getLastAction(userId2));
        Assert.assertEquals(user1Action2, storeService.getLastAction(userId1));

        final ClockAction user2Action3 = storeService.storeAction(userId2, new ClockAction(1, "in", 2042));
        Assert.assertEquals(user2Action3, storeService.getLastAction(userId2));
        Assert.assertEquals(user1Action2, storeService.getLastAction(userId1));
    }

    @Test
    public void getLastAction() {
        final IStoreService storeService = createStorageInstance();

        final String userId1 = "test.user1@test.org";
        storeService.getOrCreateToken(userId1);
        final ClockAction user1Action1 = storeService.storeAction(userId1, new ClockAction(1, "in", 42));
        Assert.assertEquals(user1Action1, storeService.getLastAction(userId1));
        final ClockAction user1Action2 = storeService.storeAction(userId1, new ClockAction(0, "out", 142));
        Assert.assertEquals(user1Action2, storeService.getLastAction(userId1));

        final String userId2 = "test.user2@test.org";
        storeService.getOrCreateToken(userId2);
        final ClockAction user2Action1 = storeService.storeAction(userId2, new ClockAction(1, "in", 2042));
        Assert.assertEquals(user2Action1, storeService.getLastAction(userId2));
        Assert.assertEquals(user1Action2, storeService.getLastAction(userId1));

        final ClockAction user2Action2 = storeService.storeAction(userId2, new ClockAction(0, "out", 2042));
        Assert.assertEquals(user2Action2, storeService.getLastAction(userId2));
        Assert.assertEquals(user1Action2, storeService.getLastAction(userId1));

        final ClockAction user2Action3 = storeService.storeAction(userId2, new ClockAction(1, "in", 2042));
        Assert.assertEquals(user2Action3, storeService.getLastAction(userId2));
        Assert.assertEquals(user1Action2, storeService.getLastAction(userId1));
    }

    @Test
    public void getActions() {
        final IStoreService storeService = createStorageInstance();

        Assert.assertNotNull(storeService.getActions("test.unknown@test.org"));
        Assert.assertTrue(storeService.getActions("test.unknown@test.org").isEmpty());

        final String userId1 = "test.user1@test.org";
        storeService.getOrCreateToken(userId1);
        final List<ClockAction> user1Actions = new ArrayList<>();
        final ClockAction user1Action1 = storeService.storeAction(userId1, new ClockAction(1, "in", 42));
        user1Actions.add(user1Action1);
        final ClockAction user1Action2 = storeService.storeAction(userId1, new ClockAction(0, "out", 142));
        user1Actions.add(user1Action2);

        final String userId2 = "test.user2@test.org";
        storeService.getOrCreateToken(userId2);
        final List<ClockAction> user2Actions = new ArrayList<>();
        final ClockAction user2Action1 = storeService.storeAction(userId2, new ClockAction(1, "in", 2042));
        user2Actions.add(user2Action1);
        final ClockAction user2Action2 = storeService.storeAction(userId2, new ClockAction(0, "out", 2042));
        user2Actions.add(user2Action2);
        final ClockAction user2Action3 = storeService.storeAction(userId2, new ClockAction(1, "in", 2042));
        user2Actions.add(user2Action3);

        final List<ClockAction> user1ActionsFound = storeService.getActions(userId1);
        final List<ClockAction> user2ActionsFound = storeService.getActions(userId2);

        Assert.assertNotNull(user1ActionsFound);
        Assert.assertEquals(user1Action1, user1ActionsFound.get(0));
        Assert.assertEquals(user1Action2, user1ActionsFound.get(1));

        Assert.assertNotNull(user2ActionsFound);
        Assert.assertEquals(user2Action1, user2ActionsFound.get(0));
        Assert.assertEquals(user2Action2, user2ActionsFound.get(1));
        Assert.assertEquals(user2Action3, user2ActionsFound.get(2));
    }
}
