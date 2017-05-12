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

import org.teknux.tinyclockin.model.ClockAction;
import org.teknux.tinyclockin.service.IService;

import java.util.List;


/**
 * @author Francois EYL
 */
public interface IStoreService extends IService  {

    public String getOrCreateToken(String userId);

    public boolean isTokenExist(String tokenId);

    public List<ClockAction> getActions(String userId);

    public ClockAction getLastAction(String userId);

    public ClockAction storeAction(String userId, ClockAction action);
}
