package org.eclipse.jetty.client;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jetty.client.api.Connection;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.io.AbstractConnection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class HttpConnection extends AbstractConnection implements Connection
{
    private static final Logger LOG = Log.getLogger(HttpConnection.class);

    private final AtomicReference<HttpExchange> exchange = new AtomicReference<>();
    private final HttpClient client;
    private final HttpSender sender;
    private final HttpReceiver receiver;

    public HttpConnection(HttpClient client, EndPoint endPoint)
    {
        super(endPoint, client.getExecutor());
        this.client = client;
        this.sender = new HttpSender(this);
        this.receiver = new HttpReceiver(this);
    }

    public HttpClient getHttpClient()
    {
        return client;
    }

    @Override
    public void onOpen()
    {
        super.onOpen();
        fillInterested();
    }

    @Override
    protected boolean onReadTimeout()
    {
        HttpExchange exchange = this.exchange.get();
        if (exchange != null)
            exchange.idleTimeout();
        return true;
    }

    @Override
    public void send(Request request, Response.Listener listener)
    {
        normalizeRequest(request);
        HttpConversation conversation = client.conversationFor(request);
        HttpExchange exchange = new HttpExchange(conversation, sender, receiver, request, listener);
        if (this.exchange.compareAndSet(null, exchange))
        {
            conversation.add(exchange);
            LOG.debug("{}({})", request, conversation);
            exchange.send();
        }
        else
        {
            throw new UnsupportedOperationException("Pipelined requests not supported");
        }
    }

    private void normalizeRequest(Request request)
    {
        HttpVersion version = request.version();
        HttpFields headers = request.headers();
        ContentProvider content = request.content();

        // Make sure the path is there
        String path = request.path();
        if (path.matches("\\s*"))
            request.path("/");

        // Add content headers
        if (content != null)
        {
            long contentLength = content.length();
            if (contentLength >= 0)
            {
                if (!headers.containsKey(HttpHeader.CONTENT_LENGTH.asString()))
                    headers.put(HttpHeader.CONTENT_LENGTH, String.valueOf(contentLength));
            }
            else
            {
                if (!headers.containsKey(HttpHeader.TRANSFER_ENCODING.asString()))
                    headers.put(HttpHeader.TRANSFER_ENCODING, "chunked");
            }
        }

        // TODO: decoder headers

        // If we are HTTP 1.1, add the Host header
        if (version.getVersion() > 10)
        {
            if (!headers.containsKey(HttpHeader.HOST.asString()))
                headers.put(HttpHeader.HOST, request.host() + ":" + request.port());
        }
    }

    @Override
    public void onFillable()
    {
        HttpExchange exchange = this.exchange.get();
        if (exchange != null)
            exchange.receive();
        else
            throw new IllegalStateException();
    }
}