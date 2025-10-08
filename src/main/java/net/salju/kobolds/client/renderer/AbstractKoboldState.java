package net.salju.kobolds.client.renderer;

import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AbstractKoboldState extends HumanoidRenderState {
	public ItemStack rightStack = ItemStack.EMPTY;
	public ItemStack leftStack = ItemStack.EMPTY;
	public boolean isZomboConverting;
	public boolean isAggressive;
	public boolean isBlocking;
	public boolean isCharging;
	public boolean isDiamond;
	public boolean isLeftHanded;
	public ResourceLocation texture;
	public String getZomboType = "base";

	public ItemStack getMainhandItem() {
		return this.mainArm.equals(HumanoidArm.RIGHT) ? this.rightStack : this.leftStack;
	}

	public ItemStack getOffhandItem() {
		return this.mainArm.equals(HumanoidArm.RIGHT) ? this.leftStack : this.rightStack;
	}
}