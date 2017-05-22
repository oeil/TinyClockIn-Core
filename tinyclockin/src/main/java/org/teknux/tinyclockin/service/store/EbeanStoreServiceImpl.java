package org.teknux.tinyclockin.service.store;

import io.ebean.Ebean;
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
public class EbeanStoreServiceImpl extends AbstractStoreService {

    @Override
    public void start(IServiceManager serviceManager) throws ServiceException {
        //read ebean config & create server
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

        AuthToken token = new QAuthToken()
                .email
                .eq(email)
                .findUnique();

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
}
