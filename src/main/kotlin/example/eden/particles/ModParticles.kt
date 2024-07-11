package example.eden.particles

import example.eden.Eden
import example.eden.particles.ModParticles.RUNE
import net.minecraft.client.Minecraft
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import team.lodestar.lodestone.systems.particle.world.behaviors.DirectionalParticleBehavior
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType.Factory
import thedarkcolour.kotlinforforge.forge.registerObject


object ModParticles {
    val PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Eden.ID)

    val RUNE by PARTICLES.registerObject("rune", ::LodestoneWorldParticleType)
}

@EventBusSubscriber(modid = Eden.ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object RegisterParticles{
    @SubscribeEvent
    fun registerParticleFactory(event: RegisterParticleProvidersEvent?) {
        Minecraft.getInstance().particleEngine.register(RUNE, ::Factory)
    }
}