package com.teamacronymcoders.epos.path.feature.point;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamacronymcoders.epos.api.character.ICharacterStats;
import com.teamacronymcoders.epos.api.character.info.PointInfo;
import com.teamacronymcoders.epos.api.path.features.IPathFeature;
import com.teamacronymcoders.epos.path.feature.AbstractPathFeature;
import com.teamacronymcoders.epos.util.EposCodecs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;

public class PointPathFeature extends AbstractPathFeature {

    public static final Codec<PointPathFeature> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            EposCodecs.FORMATTABLE_TEXT_COMPONENT.fieldOf("name").forGetter(PointPathFeature::getName),
            EposCodecs.FORMATTABLE_TEXT_COMPONENT.fieldOf("description").forGetter(PointPathFeature::getDescription),
            EposPointTypes.CODEC.optionalFieldOf("type", EposPointTypes.PATH).forGetter(PointPathFeature::getType),
            Codec.INT.optionalFieldOf("amount", 0).forGetter(PointPathFeature::getAmount)
        ).apply(instance, PointPathFeature::new)
    );

    private final EposPointTypes type;
    private final int amount;

    public PointPathFeature(IFormattableTextComponent name, IFormattableTextComponent description, EposPointTypes type, int amount) {
        super(name, description);
        this.type = type;
        this.amount = amount;
    }

    public EposPointTypes getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void applyTo(LivingEntity character, ICharacterStats stats) {
        if (character instanceof PlayerEntity) {
            PointInfo info = stats.getCharacterInfo().getPointInfo();
            switch (type) {
                case PATH: info.addPathPoints(this.amount);
                case SKILL: info.addSkillPoints(this.amount);
                case FEAT: info.addFeatPoints(this.amount);
            }
        }
    }

    @Override
    public void removeFrom(LivingEntity character, ICharacterStats stats) {
        if (character instanceof PlayerEntity) {
            PointInfo info = stats.getCharacterInfo().getPointInfo();
            switch (type) {
                case PATH: info.removePathPoints(this.amount);
                case SKILL: info.removeSkillPoints(this.amount);
                case FEAT: info.removeFeatPoints(this.amount);
            }
        }
    }

    @Override
    public Codec<? extends IPathFeature> getCodec() {
        return CODEC;
    }
}
