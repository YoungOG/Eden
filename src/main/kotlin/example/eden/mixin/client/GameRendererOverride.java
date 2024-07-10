package example.eden.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GameRenderer.class)
public abstract class GameRendererOverride {

    /**
     * @author Corosus
     * @reason render particle clouds further
     *
     * UNUSED ATM
     */
    @Overwrite
    public float getDepthFar() {
        System.out.println("mixing?");
        //CULog.dbg("getDepthFar override");
        return Minecraft.getInstance().gameRenderer.getRenderDistance() * 4F;
    }
}