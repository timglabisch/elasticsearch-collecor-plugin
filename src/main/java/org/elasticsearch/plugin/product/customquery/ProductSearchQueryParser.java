package org.elasticsearch.plugin.product.customquery;


import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermFilter;
import org.apache.lucene.search.*;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.search.join.FixedBitSetCachingWrapperFilter;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinCollector;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.query.QueryParser;
import org.elasticsearch.index.query.QueryParsingException;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductSearchQueryParser implements QueryParser {

    public static final String NAME = "product_search";

    @Override
    public String[] names() {
        return new String[]{NAME, Strings.toCamelCase(NAME)};
    }

    @Override
    public Query parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        /*
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("_type", "product")), BooleanClause.Occur.MUST);
        query.setBoost(2);
        return query;
        */



        BooleanQuery productQuery = new BooleanQuery();

        BooleanQuery productVariantQuery = new BooleanQuery();
        productVariantQuery.add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST);

        // todo: may use a filter?
        productVariantQuery.add(new TermQuery(new Term("type", "productVariant".toLowerCase())), BooleanClause.Occur.MUST);

        Filter productParentFilter = new FixedBitSetCachingWrapperFilter(
                new TermFilter(new Term("type", "product"))
        );

        ToParentBlockJoinQuery skuJoinQuery = new ToParentBlockJoinQuery(
                productVariantQuery,
                productParentFilter,
                ScoreMode.Max
        );

        BooleanQuery query = new BooleanQuery();
        query.add(productQuery, BooleanClause.Occur.MUST);
        query.add(skuJoinQuery, BooleanClause.Occur.MUST);

        ToParentBlockJoinCollector joinCollector = new ToParentBlockJoinCollector(Sort.RELEVANCE, 10, true, false);

        this.getIndexSearcher().search(query, joinCollector);

        TopGroups hits = joinCollector.getTopGroups(
                skuJoinQuery,
                Sort.RELEVANCE,
                0,   // offset
                2,  // maxDocsPerGroup
                0,   // withinGroupOffset
                true // fillSortFields
        );

        List<Document> documents = new ArrayList<Document>();


        if(hits == null)
        {
            return null;
        }

        for (int i = 0; i < hits.groups.length; i++) {

            String productId = "";
            //List<Document> productVariantSearchResults = new ArrayList<Document>();

            for (int j = 0; j < hits.groups[i].scoreDocs.length; j++) {
                Document hitDoc = getIndexSearcher().doc(hits.groups[i].scoreDocs[j].doc);

                productId = hitDoc.get("_productId");

                /*
                productVariantSearchResults.add(new ProductVariantSearchResult(
                        hitDoc.get("sku"),
                        false // todo TRACK SALES
                ));
                */


                documents.add(hitDoc);
            }

            //result.addProductSearchResult(new ProductSearchResult(productId, productVariantSearchResults));

        }

       // return result;



    }
}
