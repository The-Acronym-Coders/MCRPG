package com.teamacronymcoders.epos.registry.builder;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.teamacronymcoders.epos.api.path.IPath;
import com.teamacronymcoders.epos.api.path.features.IPathFeature;
import com.teamacronymcoders.epos.api.path.features.PathFeatures;
import com.teamacronymcoders.epos.path.Path;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.util.text.IFormattableTextComponent;

import java.util.*;

public class PathBuilder<T extends IPath, P> extends AbstractBuilder<IPath, T, P, PathBuilder<T, P>> {

    public static <P> PathBuilder<Path, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback) {
        return new PathBuilder<>(owner, parent, name, callback, Path::new);
    }

    private final Function4<IFormattableTextComponent, IFormattableTextComponent, Integer, PathFeatures, T> factory;
    private NonNullSupplier<IFormattableTextComponent> name;
    private NonNullSupplier<IFormattableTextComponent> description;
    private NonNullSupplier<Integer> maxLevel;
    private final Int2ObjectMap<List<IPathFeature>> pathFeatures;

    public PathBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, Function4<IFormattableTextComponent, IFormattableTextComponent, Integer, PathFeatures, T> factory) {
        super(owner, parent, name, callback, IPath.class);
        this.factory = factory;
        this.pathFeatures = Int2ObjectMaps.emptyMap();
    }

    public PathBuilder<T,P> name(NonNullSupplier<IFormattableTextComponent> name) {
        this.name = name;
        return this;
    }

    public PathBuilder<T,P> description(NonNullSupplier<IFormattableTextComponent> description) {
        this.description = description;
        return this;
    }

    public PathBuilder<T,P> maxLevel(NonNullSupplier<Integer> maxLevel) {
        this.maxLevel = maxLevel;
        return this;
    }

    public PathBuilder<T,P> withPathFeatureForLevel(int level, IPathFeature pathFeature) {
        this.pathFeatures.computeIfAbsent(level, integer -> new ArrayList<>()).add(pathFeature);
        return this;
    }

    public PathBuilder<T,P> withPathFeatures(int level, IPathFeature... pathFeatures) {
        this.pathFeatures.computeIfAbsent(level, (integer) -> new ArrayList<>()).addAll(Arrays.asList(pathFeatures));
        return this;
    }

    @Override
    protected @NonnullType T createEntry() {
        return factory.apply(this.name.get(), this.description.get(), this.maxLevel.get(), new PathFeatures(this.pathFeatures));
    }
}
