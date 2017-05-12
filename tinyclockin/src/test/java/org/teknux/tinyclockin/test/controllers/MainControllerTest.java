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

package org.teknux.tinyclockin.test.controllers;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;


public class MainControllerTest extends AbstractControllerTest {

	public void testIndex() {
		final Response r = getWebTarget("/").request(MediaType.TEXT_HTML_TYPE).get();

		Assert.assertEquals(Status.OK.getStatusCode(), r.getStatus());
	}

	public void testAuth() {
		Form form = new Form();
		form.param("secureId", "bigup is not awesome");

		final Response r = getWebTarget("/authenticate").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		Assert.assertEquals(Status.SEE_OTHER.getStatusCode(), r.getStatus());
	}

	public void testNotFound() {
		final Response r = getWebTarget("/not/found").request().get();
		Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), r.getStatus());
	}
}
