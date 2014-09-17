package org.elasticsearch.plugin.product.customquery;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.multibindings.Multibinder;
import org.elasticsearch.index.query.MatchAllQueryParser;
import org.elasticsearch.index.query.QueryParser;
import org.elasticsearch.index.query.functionscore.ScoreFunctionParser;
import org.elasticsearch.index.query.functionscore.ScoreFunctionParserMapper;

/**
 * Created by tim on 17/09/14.
 */
public class CustomQueryModule extends AbstractModule {

    public CustomQueryModule() {
    }

    public void registerParser(Class<? extends ScoreFunctionParser> parser) {

    }

    @Override
    protected void configure() {

        Multibinder<QueryParser> qpBinders = Multibinder.newSetBinder(binder(), QueryParser.class);
        qpBinders.addBinding().to(ProductSearchQueryParser.class).asEagerSingleton();

    }
}
