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

package org.teknux.tinyclockin.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;


/**
 * @author Francois EYL
 */
@Entity
@Table(name = "user")
public class AuthToken {

    @Id
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String token;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authToken", cascade = { CascadeType.ALL })
    private List<ClockAction> clockActions;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @XmlTransient
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public List<ClockAction> getClockActions() {
        return clockActions;
    }

    public void setClockActions(List<ClockAction> clockActions) {
        this.clockActions = clockActions;
    }
}
