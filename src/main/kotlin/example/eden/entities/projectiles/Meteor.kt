package example.eden.entities.projectiles

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.LargeFireball
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.ImposterProtoChunk
import net.minecraft.world.level.chunk.LevelChunkSection

class Meteor(entityType: EntityType<out LargeFireball>, level: Level) : LargeFireball(entityType, level) {

    override fun shouldBurn(): Boolean {
        return false
    }
}