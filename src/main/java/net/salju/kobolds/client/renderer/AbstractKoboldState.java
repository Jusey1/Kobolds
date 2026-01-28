package net.salju.kobolds.client.renderer;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

public class AbstractKoboldState extends HumanoidRenderState {
	public boolean isZomboConverting;
	public boolean isAggressive;
	public boolean isBlocking;
	public boolean isCharging;
	public boolean isDiamond;
	public boolean isLeftHanded;
    public boolean hasOffhandItem;
    public int dragonColor;
	public Identifier texture;
	public String getZomboType = "base";
}