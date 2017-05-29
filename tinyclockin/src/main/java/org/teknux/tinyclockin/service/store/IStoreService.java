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
import org.teknux.tinyclockin.service.IService;
import org.teknux.tinyclockin.util.MD5Util;

import java.util.List;


/**
 * Defines the api contract for the application store
 *
 * @author Francois EYL
 */
public interface IStoreService extends IService {

    AuthToken getOrCreateToken(String email);

    AuthToken findToken(String tokenId);

    boolean isTokenExist(String tokenId);

    List<ClockAction> getActions(String email);

    ClockAction getLastAction(String email);

    ClockAction storeAction(String email, ClockAction action);

    Audit storeAudit(Audit audit);

    List<Audit> getAudits();

    default AuthToken newAuthToken(String email) {
        final AuthToken authToken = new AuthToken();
        authToken.setToken(MD5Util.toHexString(MD5Util.md5(email)));
        authToken.setEmail(email);
        return authToken;
    }
}
