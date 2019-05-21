package com.jetec.wicloud.Post_GET;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GetArrayRequest extends Request<JSONArray> {

    private Response.Listener<JSONArray> listener;
    private Map<String, String> params;

    GetArrayRequest(String url, Map<String, String> params,
                    Response.Listener<JSONArray> reponseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public GetArrayRequest(String url, Response.Listener<JSONArray> reponseListener,
                           Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
    }

    protected Map<String, String> getParams() {
        return params;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}
