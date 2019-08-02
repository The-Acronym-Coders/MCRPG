package com.teamacronymcoders.eposmajorum.pathfeature.feat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamacronymcoders.eposmajorum.api.EposAPI;
import com.teamacronymcoders.eposmajorum.api.json.JsonUtils;
import com.teamacronymcoders.eposmajorum.api.pathfeature.IPathFeature;
import com.teamacronymcoders.eposmajorum.api.pathfeature.IPathFeatureProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class FeatPointFeatureProvider implements IPathFeatureProvider {
    private final ResourceLocation registryName = new ResourceLocation(EposAPI.ID, "feat_points");

    @Override
    public IPathFeature provide(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        int featPoints = JsonUtils.getInt(jsonObject, "points");
        if (featPoints > 0) {
            return new FeatPointFeature(featPoints);
        } else {
            throw new JsonParseException("Couldn't provide valid number of feat points: " + featPoints);
        }
    }

    @Nonnull
    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }
}
