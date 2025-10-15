package net.salju.kobolds.client.model;

import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import java.util.function.Function;
import java.util.Set;

public class KoboldArmorModel extends HumanoidModel {
	public static ArmorModelSet<LayerDefinition> KOBOLD_ARMOR_LAYER = KoboldArmorModel.createArmorSet().map(mesh -> LayerDefinition.create(mesh, 64, 32));

	public KoboldArmorModel(ModelPart part) {
		super(part);
	}

	public static LayerDefinition createHeadLayer() {
		return KOBOLD_ARMOR_LAYER.head();
	}

	public static LayerDefinition createBodyLayer() {
		return KOBOLD_ARMOR_LAYER.chest();
	}

	public static LayerDefinition createLegsLayer() {
		return KOBOLD_ARMOR_LAYER.legs();
	}

	public static LayerDefinition createBootsLayer() {
		return KOBOLD_ARMOR_LAYER.feet();
	}

	public static ArmorModelSet<MeshDefinition> createArmorSet() {
		return createArmorSet(KoboldArmorModel::createBaseArmor);
	}

	public static ArmorModelSet<MeshDefinition> createArmorSet(Function<CubeDeformation, MeshDefinition> base) {
		MeshDefinition head = base.apply(new CubeDeformation(0.0F));
		head.getRoot().retainPartsAndChildren(Set.of("head"));
		MeshDefinition body = base.apply(new CubeDeformation(0.1F));
		body.getRoot().retainExactParts(Set.of("body", "left_arm", "right_arm"));
		MeshDefinition legs = base.apply(new CubeDeformation(0.05F));
		legs.getRoot().retainExactParts(Set.of("left_leg", "right_leg"));
		MeshDefinition boots = base.apply(new CubeDeformation(0.1F));
		boots.getRoot().retainExactParts(Set.of("left_leg", "right_leg"));
		return new ArmorModelSet(head, body, legs, boots);
	}

	public static MeshDefinition createBaseArmor(CubeDeformation cube) {
		MeshDefinition mesh = HumanoidModel.createMesh(cube, 0.0F);
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, cube.extend(-0.3F)), PartPose.offset(0.0F, 4.0F, -0.5F));
		root.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, cube.extend(-0.25F)), PartPose.offset(0.0F, 4.0F, -0.5F));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 11.0F, 4.0F, cube.extend(0.05F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, cube.extend(-0.35F)).mirror(false), PartPose.offset(1.5F, 14.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, cube.extend(-0.35F)), PartPose.offset(-1.5F, 14.0F, 0.0F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(0.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, cube.extend(-0.35F)).mirror(false), PartPose.offset(2.5F, 5.0F, 0.0F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, cube.extend(-0.35F)), PartPose.offset(-2.5F, 5.0F, 0.0F));
		return mesh;
	}
}