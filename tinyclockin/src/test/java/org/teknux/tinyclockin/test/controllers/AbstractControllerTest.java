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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.teknux.tinyclockin.Application;
import org.teknux.tinyclockin.config.app.Configuration;


public abstract class AbstractControllerTest {

	@ClassRule
	public static final StartApplicationRule RULE = new StartApplicationRule();

	public String getBaseUrl() {
		return String.format("http://localhost:%d", RULE.getConfiguration().getPort());
	}

	public Client createClient() {
		return ClientBuilder.newClient();
	}

	public WebTarget getWebTarget(final String relativeUrl) {
		return createClient().target(getBaseUrl() + relativeUrl);
	}

	private static class StartApplicationRule implements TestRule {

		private Application application;

		@Override
		public Statement apply(Statement base, Description description) {
			return new Statement() {

				@Override
				public void evaluate() throws Throwable {
					startIfRequired();
					try {
						base.evaluate();
					} finally {
						if (application.isStarted()) {
							application.stop();
						}
					}
				}
			};
		}

		protected void startIfRequired() {
			if (application == null) {
				application = new Application(null);
				application.start();
			}
		}

		public Application getApplication() {
			return application;
		}

		public Configuration getConfiguration() {
			return Application.getConfiguration();
		}
	}
}
