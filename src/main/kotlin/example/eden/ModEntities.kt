package example.eden

import example.eden.entities.projectiles.Meteor
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject


object ModEntities {

    val ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Eden.ID)

    // the returned ObjectHolderDelegate can be used as a property delegate
    // this is automatically registered by the deferred registry at the correct times
    val CUSTOM_FIREBALL by ENTITY_REGISTRY.registerObject("custom_fireball") {
        EntityType.Builder.of(::Meteor, MobCategory.MISC).sized(3.0F, 0.5F).clientTrackingRange(100).setUpdateInterval(1).build("custom_fireball")
    }
}