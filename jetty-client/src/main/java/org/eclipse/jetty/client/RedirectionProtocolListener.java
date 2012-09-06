//
//  ========================================================================
//  Copyright (c) 1995-2012 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.client;

import java.net.URI;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;

public class RedirectionProtocolListener extends Response.Listener.Adapter
{
    private final HttpClient client;

    public RedirectionProtocolListener(HttpClient client)
    {
        this.client = client;
    }

    @Override
    public void onSuccess(Response response)
    {
        switch (response.status())
        {
            case 301: // GET or HEAD only allowed, keep the method
            {
                break;
            }
            case 302:
            case 303: // use GET for next request
            {
                String location = response.headers().get("location");
                Request redirect = client.newRequest(response.request().id(), URI.create(location));
                redirect.send(this);
            }
        }
    }

    @Override
    public void onFailure(Response response, Throwable failure)
    {
         // TODO
    }
}