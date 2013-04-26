package cn.link.httpclient;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-3-1
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
public class SyncHttpClient extends AsyncHttpClient {


    ////////////////////////////
    //     GET HTTP REQUEST   //
    ////////////////////////////

    public SyncResponseResult get(String url) {
        return get(url, new RequestParams());
    }

    public SyncResponseResult get(String url, RequestParams params) {
        return get(url, null, params);
    }

    public SyncResponseResult get(String url, Header[] headers, RequestParams params) {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
        if (headers != null) request.setHeaders(headers);
        return sendRequest(httpClient, httpContext, request, null);
    }


    public SyncResponseResult post(String url) {
        return post(url, new RequestParams());
    }


    public SyncResponseResult post(String url, RequestParams params) {
        return post(url, null, params, null);
    }

    public SyncResponseResult post(String url, Header[] headers, RequestParams params,
                                   String contentType) {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if (params != null) request.setEntity(paramsToEntity(params));
        if (headers != null) request.setHeaders(headers);
        return sendRequest(httpClient, httpContext, request, contentType);
    }


    //
    // HTTP PUT Requests
    //

    /**
     * Perform a HTTP PUT request, without any parameters.
     *
     * @param url             the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
//    public void put(String url, AsyncHttpResponseHandler responseHandler) {
//        put(url, null, responseHandler);
//    }
//
//    /**
//     * Perform a HTTP PUT request with parameters.
//     *
//     * @param url             the URL to send the request to.
//     * @param params          additional PUT parameters or files to send with the request.
//     * @param responseHandler the response handler instance that should handle the response.
//     */
//    public void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        put(url, params, responseHandler);
//    }
//
//    /**
//     * Perform a HTTP PUT request and track the Android Context which initiated the request.
//     * And set one-time headers for the request
//     *
//     * @param context         the Android Context which initiated the request.
//     * @param url             the URL to send the request to.
//     * @param entity          a raw {@link HttpEntity} to send with the request, for example, use this to send string/json/xml payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
//     * @param contentType     the content type of the payload you are sending, for example application/json if sending a json payload.
//     * @param responseHandler the response handler instance that should handle the response.
//     */
//    public void put(String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
//        sendRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPut(url), entity), contentType, responseHandler);
//    }
//
//    /**
//     * Perform a HTTP PUT request and track the Android Context which initiated the request.
//     * And set one-time headers for the request
//     *
//     * @param context         the Android Context which initiated the request.
//     * @param url             the URL to send the request to.
//     * @param headers         set one-time headers for this request
//     * @param entity          a raw {@link HttpEntity} to send with the request, for example, use this to send string/json/xml payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
//     * @param contentType     the content type of the payload you are sending, for example application/json if sending a json payload.
//     * @param responseHandler the response handler instance that should handle the response.
//     */
//    public void put(String url, Header[] headers, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
//        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPut(url), entity);
//        if (headers != null) request.setHeaders(headers);
//        sendRequest(httpClient, httpContext, request, contentType, responseHandler);
//    }
//
//    //
//    // HTTP DELETE Requests
//    //
//
//    /**
//     * Perform a HTTP DELETE request.
//     *
//     * @param context         the Android Context which initiated the request.
//     * @param url             the URL to send the request to.
//     * @param responseHandler the response handler instance that should handle the response.
//     */
//    public void delete( String url, AsyncHttpResponseHandler responseHandler) {
//        final HttpDelete delete = new HttpDelete(url);
//        sendRequest(httpClient, httpContext, delete, null, responseHandler);
//    }
//
//    /**
//     * Perform a HTTP DELETE request.
//     *
//     * @param context         the Android Context which initiated the request.
//     * @param url             the URL to send the request to.
//     * @param headers         set one-time headers for this request
//     * @param responseHandler the response handler instance that should handle the response.
//     */
//    public void delete( String url, Header[] headers, AsyncHttpResponseHandler responseHandler) {
//        final HttpDelete delete = new HttpDelete(url);
//        if (headers != null) delete.setHeaders(headers);
//        sendRequest(httpClient, httpContext, delete, null, responseHandler);
//    }
    protected SyncResponseResult sendRequest(final DefaultHttpClient client,
                                             final HttpContext httpContext,
                                             final HttpUriRequest uriRequest,
                                             final String contentType) {
        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }
        SyncResponseResult result = null;
        try {
            result = new AsyncHttpTask(client, httpContext, uriRequest, null).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }
}
