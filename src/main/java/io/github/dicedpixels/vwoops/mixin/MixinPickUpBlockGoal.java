package io.github.dicedpixels.vwoops.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.dicedpixels.vwoops.config.VwoopsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.registry.tag.TagKey;

@Mixin(EndermanEntity.PickUpBlockGoal.class)
abstract class MixinPickUpBlockGoal {
	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private boolean vwoops$isBlockAllowed(BlockState blockState, TagKey<?> tagKey, Operation<Boolean> originalOperation) {
		return VwoopsConfig.isBlockAllowed(blockState.getBlock());
	}
}
