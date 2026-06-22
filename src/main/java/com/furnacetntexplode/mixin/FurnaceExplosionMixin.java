package com.furnacetntexplode.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FurnaceExplosionMixin {

	@Unique
    private static void CreateExplosion(World world, BlockPos pos) {
		Explosion explosion = world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 6, World.ExplosionSourceType.TNT);
		explosion.collectBlocksAndDamageEntities();
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private static void ExplosionTick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
		if(blockEntity != null) {
			if(blockEntity.getStack(0).isOf(Items.TNT) || blockEntity.getStack(0).isOf(Items.TNT_MINECART))
			{
				//System.out.println("TNT is burning...");

				boolean isBurning = state.get(Properties.LIT);
				if(isBurning) //if burning and fuel slot is not empty
				{
					//System.out.println("Creating explosion...");
					CreateExplosion(world, pos);
				}
			}
		}
	}
}