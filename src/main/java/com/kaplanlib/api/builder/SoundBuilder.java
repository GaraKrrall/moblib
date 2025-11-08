package com.kaplanlib.api.builder;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static com.kaplanlib.api.builder.BlockBuilder.MOD_ID;

public class SoundBuilder {
    public static SoundEvent buildSound(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
