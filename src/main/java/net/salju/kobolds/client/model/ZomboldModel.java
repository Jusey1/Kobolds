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
    protected void setupAttackAnimation(T kobold) {
        if (kobold.attackTime > 0.0F) {
            float progress = 1.0F - kobold.attackTime;
            progress = progress * progress;
            progress = progress * progress;
            progress = 1.0F - progress;
            float f2 = Mth.sin(progress * (float) Math.PI);
            leftArm.xRot = (float) ((double) leftArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
            rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
        }
    }

    @Override
    protected void defaultPose(T kobold) {
        super.defaultPose(kobold);
        this.rightArm.xRot = -1.5708F;
        this.leftArm.xRot = -1.5708F;
        this.rightArm.xRot += Mth.cos(kobold.ageInTicks * 0.08F) * 0.08F;
        this.leftArm.xRot -= Mth.cos(kobold.ageInTicks * 0.08F) * 0.08F;
        this.rightArm.zRot += Mth.cos(kobold.ageInTicks * 0.08F) * 0.08F + 0.05F;
        this.leftArm.zRot -= Mth.cos(kobold.ageInTicks * 0.08F) * 0.08F + 0.05F;
    }

    @Override
    protected void poseArms(T kobold, Item target, ModelPart mainArm, ModelPart offArm) {
        if (kobold.isAggressive) {
            if (target instanceof TridentItem) {
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