package me.lucko.luckperms.hytale.calculator;

import me.lucko.luckperms.common.cacheddata.CacheMetadata;
import me.lucko.luckperms.common.calculator.CalculatorFactory;
import me.lucko.luckperms.common.calculator.PermissionCalculator;
import me.lucko.luckperms.common.calculator.processor.*;
import me.lucko.luckperms.common.config.ConfigKeys;
import me.lucko.luckperms.hytale.LPHytalePlugin;
import net.luckperms.api.query.QueryOptions;

import java.util.ArrayList;
import java.util.List;

public class HytaleCalculatorFactory implements CalculatorFactory {

    private final LPHytalePlugin hytalePlugin;

    public HytaleCalculatorFactory(LPHytalePlugin hytalePlugin) {
        this.hytalePlugin = hytalePlugin;
    }

    @Override
    public PermissionCalculator build(QueryOptions queryOptions, CacheMetadata metadata) {
        List<PermissionProcessor> processors = new ArrayList<>(4);

        processors.add(new DirectProcessor());

        if (this.hytalePlugin.getConfiguration().get(ConfigKeys.APPLYING_REGEX)) {
            processors.add(new RegexProcessor());
        }

        if (this.hytalePlugin.getConfiguration().get(ConfigKeys.APPLYING_WILDCARDS)) {
            processors.add(new WildcardProcessor());
        }

        if (this.hytalePlugin.getConfiguration().get(ConfigKeys.APPLYING_WILDCARDS_SPONGE)) {
            processors.add(new SpongeWildcardProcessor());
        }

        return new PermissionCalculator(hytalePlugin, metadata, processors);
    }

}
