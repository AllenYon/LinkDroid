package cn.link.httpclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SyncResponseResult {
    Exception mException;

    int mStatusCode;

    String mResponseBody;

    public boolean hasException() {
        return mException != null;
    }

    public Exception getException() {
        return mException;
    }


    public int getStatusCode() {
        return mStatusCode;
    }


    public String getResponseBody() {
        return mResponseBody;
    }

    public JSONObject getJSONObject() throws JSONException {
        return new JSONObject(mResponseBody);
    }
    public JSONArray getJSONArray() throws JSONException {
        return new JSONArray(mResponseBody);
    }
}
