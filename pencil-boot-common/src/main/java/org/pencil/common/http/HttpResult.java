package org.pencil.common.http;

import lombok.Data;

import org.apache.http.Header;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pencil
 * @date 25/04/28 10:19
 */
@Data
public class HttpResult {

    private int statusCode;

    private Map<String, String> headers;

    private String body;

    public HttpResult(int statusCode, Header[] headers, String body) {
        this.statusCode = statusCode;
        this.headers = new HashMap<>();
        for (Header header : headers) {
            this.headers.put(header.getName(), header.getValue());
        }
        this.body = body;
    }

}
