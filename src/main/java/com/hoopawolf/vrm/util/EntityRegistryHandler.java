package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.client.renderer.PesArrowRenderer;
import com.hoopawolf.vrm.client.renderer.SlothPetRenderer;
import com.hoopawolf.vrm.entities.SlothPetEntity;
import com.hoopawolf.vrm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class EntityRegistryHandler
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    //ENTITIES
    public static final RegistryObject<EntityType<SlothPetEntity>> SLOTH_PET_ENTITY = ENTITIES.register("slothpet", () -> EntityType.Builder.create(SlothPetEntity::new, EntityClassification.CREATURE)
            .size(0.4F, 0.8F)
            .setShouldReceiveVelocityUpdates(false)
            .build("slothpet"));

    //PROJECTILE
    public static final RegistryObject<EntityType<PesArrowEntity>> PES_ARROW_ENTITY = ENTITIES.register("pesarrow", () -> EntityType.Builder.<PesArrowEntity>create(PesArrowEntity::new, EntityClassification.MISC)
            .size(0.5F, 0.5F)
            .build("pesarrow"));

    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenderer()
    {
        RenderingRegistry.registerEntityRenderingHandler(SLOTH_PET_ENTITY.get(), SlothPetRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(PES_ARROW_ENTITY.get(), PesArrowRenderer::new);
    }

    public static void registerEntityAttributes()
    {
        GlobalEntityTypeAttributes.put(SLOTH_PET_ENTITY.get(), SlothPetEntity.func_234321_m_().create());
    }
}