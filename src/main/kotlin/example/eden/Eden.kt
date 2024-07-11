package example.eden

import example.eden.Eden.LOGGER
import example.eden.entities.projectiles.MeteorRenderer
import example.eden.particles.ModParticles
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent
import team.lodestar.lodestone.systems.particle.world.behaviors.components.LodestoneBehaviorComponent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@EventBusSubscriber
@Mod(Eden.ID)
object Eden {

    const val ID = "eden"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOGGER.log(Level.INFO, "Hello world!")

        // Register the KDeferredRegister to the mod-specific event bus
//        ModBlocks.REGISTRY.register(MOD_BUS)
        ModEntities.ENTITY_REGISTRY.register(MOD_BUS)
        ModParticles.PARTICLES.register(MOD_BUS)

        val obj = runForDist(
            clientTarget = {
//                MOD_BUS.addListener(::onClientSetup)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
                "test"
            })

        MinecraftForge.EVENT_BUS.register(this)

        println(obj)
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
//    private fun onClientSetup(event: FMLClientSetupEvent) {
//        LOGGER.log(Level.INFO, "Initializing client...")
//
//        EntityRenderers.register(ModEntities.CUSTOM_FIREBALL) {
//            ThrownItemRenderer(it, 10.0F, true)
//        }
//    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }

    @SubscribeEvent
    fun onUseStick(event: PlayerInteractEvent) {

//        if (event.level.isClientSide)
//            return
//
//        Items.STICK
        val item = event.entity.mainHandItem.item
        val entity = event.entity

        if (item == Items.STICK) {

            val ball = ModEntities.CUSTOM_FIREBALL.create(event.level) ?: throw Exception("WTF IS THIS")

            ball.remainingFireTicks = -1
            ball.xPower = event.entity.lookAngle.x * 1.15
            ball.yPower = event.entity.lookAngle.y * 1.15
            ball.zPower = event.entity.lookAngle.z * 1.15
//        ball.yPower = -2.0
//        ball.zPower = 0.1

            ball.moveTo(event.entity.x, event.entity.y - 2, event.entity.z)
            ball.deltaMovement = event.entity.lookAngle

            event.level.addFreshEntity(ball)
        } else if (item == Items.APPLE) {
            WorldParticleBuilder.create(ModParticles.RUNE)
                .setColorData(ColorParticleData.create(1.0f, 0.0f, 0.0f).build())
                .setLifetime(100)
                .setSpinData(SpinParticleData.create(0.02f).build())
//                .disableCull()
//                .enableNoClip()
                .setBehavior(DirectionalBehaviorComponent(Vec3(0.0, -1.0, 0.0)))
                .setScaleData(
                    GenericParticleData.create(5.0f)
                        .build()
                )
                .setRenderType(LodestoneWorldParticleRenderType.LUMITRANSPARENT)
                .setTransparencyData(
                    GenericParticleData.create(1.0f)
                        .build()
                )
                .spawn(entity.level(), entity.x, entity.y + 0.01, entity.z)
        }
    }
}

@EventBusSubscriber(modid = Eden.ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ClientStuff {

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")

        EntityRenderers.register(ModEntities.CUSTOM_FIREBALL) {
            MeteorRenderer(it)
        }


    }
}