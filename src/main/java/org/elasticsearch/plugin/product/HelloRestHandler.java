package org.elasticsearch.plugin.product;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import org.elasticsearch.common.inject.Inject;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.OK;

public class HelloRestHandler implements RestHandler {

    @Inject
    public HelloRestHandler(RestController restController) {
        restController.registerHandler(GET, "/_product", this);
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel) throws IOException {

        XContentBuilder builder = channel.newBuilder();
        builder
            .startObject()
            .field("tagline", "You Know, for Search")
            .endObject();

        channel.sendResponse(
            new BytesRestResponse(OK, builder)
        );

    }
}