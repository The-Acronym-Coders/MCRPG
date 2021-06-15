package com.teamacronymcoders.epos.path.feature;

import com.teamacronymcoders.epos.api.path.features.IPathFeature;
import net.minecraft.util.text.IFormattableTextComponent;

public abstract class AbstractPathFeature implements IPathFeature {

    private final IFormattableTextComponent name;
    private final IFormattableTextComponent description;

    public AbstractPathFeature(IFormattableTextComponent name, IFormattableTextComponent description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public IFormattableTextComponent getName() {
        return this.name;
    }

    @Override
    public IFormattableTextComponent getDescription() {
        return this.description;
    }
}
