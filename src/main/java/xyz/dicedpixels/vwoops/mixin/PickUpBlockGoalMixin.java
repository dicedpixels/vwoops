package xyz.dicedpixels.vwoops.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.TagKey;

import xyz.dicedpixels.vwoops.Vwoops;

@Mixin(targets = "net.minecraft.entity.mob.EndermanEntity$PickUpBlockGoal")
public class PickUpBlockGoalMixin {
    // spotless:off
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    // spotless:on
    private boolean vwoops$isBlockAllowed(BlockState blockState, TagKey<?> tagKey, Operation<Boolean> original) {
        return Vwoops.allowedBlocks().contains(blockState.getBlock());
    }
}
