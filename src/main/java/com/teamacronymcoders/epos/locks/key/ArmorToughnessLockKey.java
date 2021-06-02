package com.teamacronymcoders.epos.locks.key;

import com.google.common.collect.Multimap;
import com.teamacronymcoders.epos.api.locks.key.GenericLockKey;
import com.teamacronymcoders.epos.api.locks.key.IFuzzyLockKey;
import com.teamacronymcoders.epos.api.locks.key.ILockKey;
import com.teamacronymcoders.epos.locks.FuzzyLockKeyTypes;
import com.teamacronymcoders.epos.util.AttributeUtils;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Used for locking armor items based on their armor toughness.
 */
public class ArmorToughnessLockKey implements IFuzzyLockKey {

    private static final GenericLockKey NOT_FUZZY = new GenericLockKey(FuzzyLockKeyTypes.ARMOR_TOUGHNESS);

    private final double toughness;

    public ArmorToughnessLockKey(double toughness) {
        if (toughness < 0) {
            throw new IllegalArgumentException("Armor toughness value must be at least zero.");
        }
        this.toughness = toughness;
    }

    @Override
    public boolean fuzzyEquals(@Nonnull IFuzzyLockKey o) {
        return o == this || o instanceof ArmorToughnessLockKey && toughness >= ((ArmorToughnessLockKey) o).toughness;
    }

    @Override
    @Nonnull
    public ILockKey getNotFuzzy() {
        return NOT_FUZZY;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof ArmorToughnessLockKey && toughness == ((ArmorToughnessLockKey) o).toughness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toughness);
    }

    @Nullable
    public static ArmorToughnessLockKey fromObject(@Nonnull Object object) {
        if (object instanceof ItemStack) {
            ItemStack stack = (ItemStack) object;
            if (stack.isEmpty()) {
                return null;
            }
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                ArmorItem armorItem = (ArmorItem) item;
                Multimap<Attribute, AttributeModifier> attributeModifiers = armorItem.getAttributeModifiers(armorItem.getEquipmentSlot(), stack);
                double toughness = AttributeUtils.calculateValueFromModifiers(attributeModifiers, Attributes.ARMOR_TOUGHNESS, armorItem.getToughness());
                //Ensure that the value is actually positive
                if (toughness >= 0) {
                    return new ArmorToughnessLockKey(toughness);
                }
            }
        }
        return null;
    }
}