package org.elasticsearch.plugin.product.customquery;


import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.query.QueryParser;
import org.elasticsearch.index.query.QueryParsingException;

import java.io.IOException;

public class ProductSearchQueryParser implements QueryParser {

    public static final String NAME = "product_search";

    @Override
    public String[] names() {
        return new String[]{NAME, Strings.toCamelCase(NAME)};
    }

    @Override
    public Query parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        MatchAllDocsQuery query = new MatchAllDocsQuery();
        query.setBoost(2);
        return query;
    }
}
