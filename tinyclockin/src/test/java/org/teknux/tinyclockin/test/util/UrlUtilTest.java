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

package org.teknux.tinyclockin.test.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;
import org.teknux.tinyclockin.util.UrlUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class UrlUtilTest {

    private ServletContext servletContext  = mock(ServletContext.class);

    @Test(expected = NullPointerException.class) 
    public void test01Error() {
        UrlUtil.getAbsoluteUrl(null, null);
    }
    
    @Test
    public void test02Root() {
        when(servletContext.getContextPath()).thenReturn("");
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));

        when(servletContext.getContextPath()).thenReturn("/");
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
    }

    @Test
    public void test03NotRoot() {
        when(servletContext.getContextPath()).thenReturn("notroot");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
        
        when(servletContext.getContextPath()).thenReturn("/notroot");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
        
        when(servletContext.getContextPath()).thenReturn("notroot/");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
        
        when(servletContext.getContextPath()).thenReturn("/notroot/");
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, null));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, ""));
        Assert.assertEquals("/notroot", UrlUtil.getAbsoluteUrl(servletContext, "/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "url/"));
        Assert.assertEquals("/notroot/url", UrlUtil.getAbsoluteUrl(servletContext, "/url/"));
    }
}
