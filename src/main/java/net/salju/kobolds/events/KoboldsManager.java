package net.salju.kobolds.events;

import net.salju.kobolds.Kobolds;
import net.salju.kobolds.compat.Supplementaries;
import net.salju.kobolds.entity.AbstractKoboldEntity;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.List;
import java.util.Objects;

public class KoboldsManager {
	public static AttributeSupplier.Builder createAttributes(double h, double d, double a, double s) {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, h).add(Attributes.ATTACK_DAMAGE, d).add(Attributes.ARMOR, a).add(Attributes.MOVEMENT_SPEED, s);
	}

	public static Holder<Enchantment> getEnchantment(RegistryAccess target, String id, String name) {
		return target.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(id, name)));
	}

    public static List<ItemStack> getTradeItems(AbstractKoboldEntity kobold, String table) {
        return Objects.requireNonNull(kobold.level().getServer()).reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Kobolds.MODID, table))).getRandomItems((new LootParams.Builder((ServerLevel) kobold.level())).withParameter(LootContextParams.THIS_ENTITY, kobold).create(LootContextParamSets.EMPTY));
    }

    public static void addPirateGoals(AbstractKoboldEntity kobold) {
        if (ModList.get().isLoaded("supplementaries")) {
            Supplementaries.addCannonGoals(kobold);
            Supplementaries.addBoatGoals(kobold);
        }
    }

    public static KoboldEvent.DragonEvent onGetDragonEvent(AbstractKoboldEntity kobold) {
        KoboldEvent.DragonEvent event = new KoboldEvent.DragonEvent(kobold);
        NeoForge.EVENT_BUS.post(event);
        return event;
    }
}