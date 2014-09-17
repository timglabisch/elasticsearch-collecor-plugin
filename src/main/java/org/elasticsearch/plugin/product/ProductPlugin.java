package org.elasticsearch.plugin.product;

import com.google.common.collect.Lists;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.plugins.AbstractPlugin;

import java.util.Collection;

public class ProductPlugin extends AbstractPlugin {

    @Override public String name() {
        return "product-plugin";
    }

    @Override public String description() {
        return "Example Plugin Description";
    }

    public Collection<Class<? extends Module>> modules() {
        Collection<Class<? extends Module>> modules = Lists.newArrayList();
        modules.add(ExampleRestModule.class);
        return modules;
    }
}