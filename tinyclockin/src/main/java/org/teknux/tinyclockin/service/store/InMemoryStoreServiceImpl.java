/*
 * Copyright (C) 2016 TekNux.org
 *
 * This file is part of the TinyClockIn GPL Source Code.
 *
 * TinyClockIn Source Code is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * TinyClockIn Source Code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dropbitz Community Source Code.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package org.teknux.tinyclockin.service.store;

import org.teknux.tinyclockin.model.Audit;
import org.teknux.tinyclockin.model.AuthToken;
import org.teknux.tinyclockin.model.ClockAction;
import org.teknux.tinyclockin.service.IServiceManager;
import org.teknux.tinyclockin.service.ServiceException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Homemade In-Memory store implementation
 *
 * @author Francois EYL
 */
public class InMemoryStoreServiceImpl implements IStoreService {

    private AtomicInteger idsIndex = new AtomicInteger(0);

    private final Map<String, List<ClockAction>> actionStore = new ConcurrentHashMap<>();
    private final Set<AuthToken> tokenStore = new HashSet<>();
    private final List<Audit> audits = new ArrayList<>();
    private final Object tokenLock = new Object();

    @Override
    public AuthToken getOrCreateToken(final String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        final Optional<AuthToken> storedAuthToken = tokenStore.stream().filter(t -> t.getEmail().equals(email)).findFirst();
        if (storedAuthToken.isPresent()) {
            return storedAuthToken.get();
        }

        synchronized (tokenLock) {
            final AuthToken authToken = this.newAuthToken(email);
            tokenStore.add(authToken);
            return authToken;
        }
    }

    @Override
    public AuthToken findToken(String tokenId) {
        Optional<AuthToken> token = tokenStore.stream().filter(t -> t.getToken().equals(tokenId)).findFirst();
        return token.isPresent() ? token.get() : null;
    }

    @Override
    public boolean isTokenExist(final String tokenId) {
        return tokenStore.stream().filter(t -> t.getToken().equals(tokenId)).findFirst().isPresent();
    }

    @Override
    public List<ClockAction> getActions(final String email) {
        return actionStore.getOrDefault(email, new ArrayList<>());
    }

    @Override
    public ClockAction getLastAction(final String email) {
        return getActions(email).stream().reduce((first, second) -> second).orElse(null);
    }

    @Override
    public ClockAction storeAction(final String email, final ClockAction action) {
        action.setTimestamp(LocalDateTime.now());
        action.setId(idsIndex.incrementAndGet());

        final List<ClockAction> actions = actionStore.getOrDefault(email, new ArrayList<>());
        actions.add(action);

        actionStore.put(email, actions);
        return action;
    }

    @Override
    public Audit storeAudit(Audit audit) {
        if (audit == null) {
            return null;
        }

        audits.add(audit);
        return audit;
    }

    @Override
    public List<Audit> getAudits() {
        return audits;
    }

    @Override
    public void start(IServiceManager serviceManager) throws ServiceException {

    }

    @Override
    public void stop() throws ServiceException {

    }
}
