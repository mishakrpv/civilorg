package com.mishakrpv.civilorg.classes;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/*
    One of supposed way to create selectable Classes for players
* */
public class BaseClassEffect extends StatusEffect {
    public BaseClassEffect(StatusEffectCategory category, int color) {
        super(StatusEffectCategory.BENEFICIAL, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {

        }
    }
}
