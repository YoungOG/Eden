package example.eden.entities.projectiles

import com.mojang.blaze3d.vertex.PoseStack
import example.eden.Eden
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
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
        val direction = entity.deltaMovement

        for (i in 0..10) {
            val x = entity.x + (direction.x / 10.0 * i)
            val y = entity.y + (direction.y / 10.0 * i)
            val z = entity.z + (direction.z / 10.0 * i)

            //Orange-ish
            WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setColorData(
                    ColorParticleData.create(1.0f, 0.7f, 0.1f, 0.8f, 0.2f, 0.0f)
                        .setEasing(Easing.QUAD_OUT)
                        .build()
                )
                .setLifetime(40)
                .setSpinData(SpinParticleData.create(0.5f).build())
                .enableNoClip()
                .setScaleData(
                    GenericParticleData.create(3.0f, 0.5f)
                        .setEasing(Easing.QUAD_IN)
                        .build()
                )
                .setTransparencyData(
                    GenericParticleData.create(0.9f, 0.0f)
                        .setEasing(Easing.LINEAR)
                        .build()
                ).spawn(entity.level(), x, y, z)

            WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setColorData(
                    ColorParticleData.create(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
//                        .setEasing(Easing.QUAD_OUT)
                        .build()
                )
                .setLifetime(140)
//                .setSpinData(SpinParticleData.create(0.5f).build())
                .enableNoClip()
                .setScaleData(
                    GenericParticleData.create(3.0f, 0.5f)
//                        .setEasing(Easing.QUAD_IN)
                        .build()
                )
//                .setTransparencyData(
//                    GenericParticleData.create(0.9f, 0.0f)
//                        .setEasing(Easing.LINEAR)
//                        .build()
//                )
                .spawn(entity.level(), x, y, z)

            /*            WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setColorData(ColorParticleData.create(1.0f, 203.0f / 255.0f, 0.0f, 1.0f, 60.0f / 255.0f, 0.0f).setEasing(Easing.CIRC_OUT).build())
                .setLifetime(20)
                .setSpinData(SpinParticleData.create(5.0f).build())
                .enableNoClip()
                .setScaleData(GenericParticleData.create(4.0f).setEasing(Easing.SINE_OUT).build())
                .setTransparencyData(GenericParticleData.create(0.2f).build())
                .spawn(entity.level(), entity.x, entity.y, entity.z)*/

//            WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
//                .setColorData(
//                    ColorParticleData.create(1.0f, 0.7f, 0.1f, 0.8f, 0.2f, 0.0f)
//                        .setEasing(Easing.QUAD_OUT)
//                        .build()
//                )
//                .setLifetime(40)
//                .setSpinData(SpinParticleData.create(0.5f).build())
//                .enableNoClip()
//                .setScaleData(
//                    GenericParticleData.create(3.0f, 0.5f)
//                        .setEasing(Easing.QUAD_IN)
//                        .build()
//                )
//                .setTransparencyData(
//                    GenericParticleData.create(0.9f, 0.0f)
//                        .setEasing(Easing.LINEAR)
//                        .build()
//                )
//                .addTickActor { particle ->
//                    // Set constant motion for the meteor
////                    particle.setParticleSpeed(entity.deltaMovement.x * 1.5, entity.deltaMovement.y * 1.5, entity.deltaMovement.z * 1.5)
//
//                    // Spawn trail particles
//                    if (particle.age % 2 == 0) {
//                        WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
//                            .setColorData(
//                                ColorParticleData.create(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
//                                    .build()
//                            )
//                            .setLifetime(100)
//                            .setScaleData(
//                                GenericParticleData.create(1.5f, 0.2f)
//                                    .setEasing(Easing.QUAD_OUT)
//                                    .build()
//                            )
//                            .setTransparencyData(
//                                GenericParticleData.create(0.7f, 0.0f)
//                                    .setEasing(Easing.QUAD_IN)
//                                    .build()
//                            )
//                            .enableNoClip()
////                            .addTickActor { trailParticle ->
////                                // Add some randomness to trail particle movement
////                                val randomFactor = 0.05
////                                trailParticle.setParticleSpeed(
////                                    (Math.random() - 0.5) * randomFactor,
////                                    (Math.random() - 0.5) * randomFactor,
////                                    (Math.random() - 0.5) * randomFactor
////                                )
////                            }
//                            .spawn(entity.level(), particle.x, particle.y, particle.z)
//                    }
//                }
//                .spawn(entity.level(), entity.x, entity.y, entity.z)


            super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight)
        }
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