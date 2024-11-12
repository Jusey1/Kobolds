package net.salju.kobolds.client.renderer;

import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@OnlyIn(Dist.CLIENT)
public class AbstractKoboldState extends HumanoidRenderState {
	public boolean isZomboConverting = false;
	public boolean isAggressive = false;
	public boolean isBlocking = false;
	public boolean isCharging = false;
	public boolean isDiamond = false;
	public boolean isLeftHanded = false;
	public boolean isPopper = false;
	public ResourceLocation texture;
	public String getZomboType = "base";

	public ItemStack getOffhandItem() {
		return this.mainArm == HumanoidArm.RIGHT ? this.leftHandItem : this.rightHandItem;
	}
}