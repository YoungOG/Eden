package example.eden.entities.projectiles

import com.mojang.blaze3d.vertex.PoseStack
import example.eden.Eden
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry
import team.lodestar.lodestone.systems.easing.Easing
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder
import team.lodestar.lodestone.systems.particle.data.GenericParticleData
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData

class MeteorRenderer(context: EntityRendererProvider.Context) : EntityRenderer<Meteor>(context) {
    val TEXTURE = ResourceLocation(Eden.ID, "textures/entity/eye/eye.png")

    override fun render(
        entity: Meteor,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        // Entity's current velocity vector
        val entityVelocity = entity.deltaMovement
        // Minimum velocity squared to consider the entity as moving
        val MIN_VELOCITY_SQ = 0.0001
        // Direction of the trail (normalized)
        val trailDirection = if (entityVelocity.lengthSqr() > MIN_VELOCITY_SQ)
            entityVelocity.normalize() else entity.lookAngle
        val TRAIL_SEGMENTS = 2
        val TRAIL_LENGTH = 60.0
        val SPAWN_INTERVAL = 2
        val MAIN_PARTICLE_LIFETIME = 60
        val MAIN_PARTICLE_INITIAL_SCALE = 3.0f
        val MAIN_PARTICLE_FINAL_SCALE = 0.5f
        val MAIN_PARTICLE_INITIAL_ALPHA = 0.8f
        val MAIN_PARTICLE_FINAL_ALPHA = 0.2f
        val MAIN_PARTICLE_SPIN_RATE = 0.2f
        val PARTICLE_VELOCITY_MULTIPLIER = 0.1
        val MAIN_PARTICLE_START_COLOR = Vec3(1.0, 0.7, 0.1)
        val MAIN_PARTICLE_END_COLOR = Vec3(0.8, 0.2, 0.0)

        // Spawn a new particle every SPAWN_INTERVAL ticks to create a continuous trail
        if (entity.tickCount % SPAWN_INTERVAL == 0) {
            // Calculate the position along the trail based on the current tick
            val trailProgress = (entity.tickCount % TRAIL_SEGMENTS) / TRAIL_SEGMENTS.toDouble()
            val particlePosition = entity.position().add(trailDirection.scale(trailProgress * TRAIL_LENGTH))

            // Create and configure the main trail particle
            WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setColorData(
                    ColorParticleData.create(
                        MAIN_PARTICLE_START_COLOR.x.toFloat(), MAIN_PARTICLE_START_COLOR.y.toFloat(),
                        MAIN_PARTICLE_START_COLOR.z.toFloat(),
                        MAIN_PARTICLE_END_COLOR.x.toFloat(), MAIN_PARTICLE_END_COLOR.y.toFloat(),
                        MAIN_PARTICLE_END_COLOR.z.toFloat()
                    )
                        .setEasing(Easing.QUAD_OUT)
                        .build()
                )
                .setLifetime(MAIN_PARTICLE_LIFETIME)
                .setSpinData(SpinParticleData.create(MAIN_PARTICLE_SPIN_RATE).build())
                .enableNoClip()
                .setScaleData(
                    GenericParticleData.create(MAIN_PARTICLE_INITIAL_SCALE, MAIN_PARTICLE_FINAL_SCALE)
                        .setEasing(Easing.QUAD_IN)
                        .build()
                )
                .setTransparencyData(
                    GenericParticleData.create(MAIN_PARTICLE_INITIAL_ALPHA, MAIN_PARTICLE_FINAL_ALPHA)
                        .setEasing(Easing.LINEAR)
                        .build()
                )
                .addTickActor { particle ->
                    val currentEntityVelocity = entity.deltaMovement
                    particle.setParticleSpeed(
                        trailDirection.x * PARTICLE_VELOCITY_MULTIPLIER + currentEntityVelocity.x,
                        trailDirection.y * PARTICLE_VELOCITY_MULTIPLIER + currentEntityVelocity.y,
                        trailDirection.z * PARTICLE_VELOCITY_MULTIPLIER + currentEntityVelocity.z
                    )
                    if (particle.age % SPAWN_INTERVAL == 0) {
                        spawnTrailParticle(entity.level(), particle.x, particle.y, particle.z)
                    }
                }
                .spawn(entity.level(), particlePosition.x, particlePosition.y, particlePosition.z)
        }

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight)
    }

    // Function to spawn smaller trail particles
    fun spawnTrailParticle(level: Level, x: Double, y: Double, z: Double) {
        // Lifetime of the smaller trail particles in ticks
        val SMALL_PARTICLE_LIFETIME = 20
        // Colors for the smaller trail particles (start and end)
        val SMALL_PARTICLE_START_COLOR = Vec3(1.0, 0.5, 0.0)
        val SMALL_PARTICLE_END_COLOR = Vec3(0.6, 0.1, 0.0)
        // Scale of the smaller particles (start and end)
        val SMALL_PARTICLE_START_SCALE = 1.0f
        val SMALL_PARTICLE_END_SCALE = 0.2f
        // Transparency of the smaller particles (start and end)
        val SMALL_PARTICLE_START_ALPHA = 0.6f
        val SMALL_PARTICLE_END_ALPHA = 0.0f

        WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
            // Set the color transition of the smaller trail particles
            .setColorData(
                ColorParticleData.create(
                    SMALL_PARTICLE_START_COLOR.x.toFloat(), SMALL_PARTICLE_START_COLOR.y.toFloat(), SMALL_PARTICLE_START_COLOR.z.toFloat(),
                    SMALL_PARTICLE_END_COLOR.x.toFloat(), SMALL_PARTICLE_END_COLOR.y.toFloat(), SMALL_PARTICLE_END_COLOR.z.toFloat()
                )
                    .setEasing(Easing.QUAD_OUT)
                    .build()
            )
            .setLifetime(SMALL_PARTICLE_LIFETIME)
            // Set the scale of the smaller particles over their lifetime
            .setScaleData(
                GenericParticleData.create(SMALL_PARTICLE_START_SCALE, SMALL_PARTICLE_END_SCALE)
                    .setEasing(Easing.QUAD_OUT)
                    .build()
            )
            // Set the transparency of the smaller particles over their lifetime
            .setTransparencyData(
                GenericParticleData.create(SMALL_PARTICLE_START_ALPHA, SMALL_PARTICLE_END_ALPHA)
                    .setEasing(Easing.QUAD_IN)
                    .build()
            )
            .enableNoClip() // Allow smaller particles to pass through blocks
            // Spawn the smaller trail particle
            .spawn(level, x, y, z)
    }


    override fun shouldRender(
        pLivingEntity: Meteor,
        pCamera: Frustum,
        pCamX: Double,
        pCamY: Double,
        pCamZ: Double
    ): Boolean {
        return true
    }

    override fun getTextureLocation(entity: Meteor): ResourceLocation {
        return TEXTURE
    }
}