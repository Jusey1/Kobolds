package net.salju.kobolds.client.model;

import net.minecraft.world.item.Item;
import net.salju.kobolds.client.renderer.AbstractKoboldState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.item.TridentItem;

public class ZomboldModel<T extends AbstractKoboldState> extends KoboldModel<T> {
	public ZomboldModel(ModelPart root) {
		super(root);
	}

    @Override
    protected void setupAttackAnimation(T target) {
        float progress = 1.0F - target.attackTime;
        progress = progress * progress;
        progress = progress * progress;
        progress = 1.0F - progress;
        float f2 = Mth.sin(progress * (float) Math.PI);
        leftArm.xRot = (float) ((double) leftArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
        rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
    }

    @Override
    protected void defaultPose(T target) {
        super.defaultPose(target);
        this.rightArm.xRot = -1.5708F;
        this.leftArm.xRot = -1.5708F;
        this.rightArm.xRot += Mth.cos(target.ageInTicks * 0.08F) * 0.08F;
        this.leftArm.xRot -= Mth.cos(target.ageInTicks * 0.08F) * 0.08F;
        this.rightArm.zRot += Mth.cos(target.ageInTicks * 0.08F) * 0.08F + 0.05F;
        this.leftArm.zRot -= Mth.cos(target.ageInTicks * 0.08F) * 0.08F + 0.05F;
    }

    @Override
    protected void poseArms(T target, Item item, ModelPart mainArm, ModelPart offArm) {
        if (target.isAggressive) {
            if (item instanceof TridentItem) {
                mainArm.xRot = 2.8798F;
                mainArm.zRot = -0.1309F;
                offArm.xRot = -1.5708F;
            } else {
                mainArm.xRot = -2.0944F;
                offArm.xRot = -2.0944F;
            }
        }
    }
}