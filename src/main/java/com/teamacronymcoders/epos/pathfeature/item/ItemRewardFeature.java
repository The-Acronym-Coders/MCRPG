package com.teamacronymcoders.epos.pathfeature.item;

import com.teamacronymcoders.epos.api.characterstats.ICharacterStats;
import com.teamacronymcoders.epos.pathfeature.PathFeature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemRewardFeature extends PathFeature {
    private final String identifier;
    private final ItemStack stack;

    public ItemRewardFeature(String identifier, ItemStack stack) {
        super(new TranslationTextComponent("pathfeature.epos.itemreward.name", stack.getDisplayName()),
                new TranslationTextComponent("pathfeature.epos.itemreward.description", stack.getDisplayName()));
        this.identifier = identifier;
        this.stack = stack;
    }

    @Override
    public void applyTo(LivingEntity character, ICharacterStats characterStats) {
        World world = character.world;
        CompoundNBT entityNBT = character.getEntityData();
        if (!entityNBT.contains("item_rewards")) {
            CompoundNBT newNBT = new CompoundNBT();
            character.getEntityData().put("item_rewards", newNBT);
        }
        if (!entityNBT.getCompound("item_rewards").contains(identifier)) {
            entityNBT.getCompound("item_rewards").putBoolean(identifier, true);
            ItemEntity entity = new ItemEntity(world, character.posX, character.posY, character.posZ);
            entity.setItem(stack);
            character.world.addEntity(entity);
        }
    }

    @Override
    public void removeFrom(LivingEntity character, ICharacterStats characterStats) {
    }
}
