package org.teknux.tinyclockin.service.store;

import org.teknux.tinyclockin.model.AuthToken;
import org.teknux.tinyclockin.util.MD5Util;


/**
 * @author Francois EYL
 */
public abstract class AbstractStoreService implements IStoreService {

    protected AuthToken newAuthToken(String email) {
        final AuthToken authToken = new AuthToken();
        authToken.setToken(MD5Util.toHexString(MD5Util.md5(email)));
        authToken.setEmail(email);
        return authToken;
    }
}
