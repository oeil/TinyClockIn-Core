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

import org.teknux.tinyclockin.config.app.LocalDateTimeAdapter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;


/**
 * @author Francois EYL
 */
@Entity
@Table(name = "action")
public class ClockAction {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "workstation", nullable = false)
    private int workstation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AuthToken authToken;

    public ClockAction() {
    }

    public ClockAction(int type, String description, int workstation) {
        this();
        this.type = type;
        this.description = description;
        this.workstation = workstation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWorkstation() {
        return workstation;
    }

    public void setWorkstation(int workstation) {
        this.workstation = workstation;
    }

    @XmlTransient
    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
