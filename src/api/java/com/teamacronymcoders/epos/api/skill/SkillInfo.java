package com.teamacronymcoders.epos.api.skill;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class SkillInfo implements INBTSerializable<CompoundNBT>, Comparable<SkillInfo> {
    private final String registryName;
    private final ISkill skill;
    private boolean active;
    private int level;
    private int experience;
    private CompoundNBT data;

    public SkillInfo(ISkill skill) {
        this.skill = skill;
        this.registryName = skill.toString();
        this.data = new CompoundNBT();
    }

    @Override
    public int compareTo(SkillInfo o) {
        return 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("Active", active);
        nbt.putInt("Level", level);
        nbt.putInt("Experience", experience);
        nbt.put("Data", data);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.active = nbt.getBoolean("Active");
        this.level = nbt.getInt("Level");
        this.experience = nbt.getInt("Experience");
        this.data = nbt.getCompound("Data");
    }
}
