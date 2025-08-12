package items;

import com.example.DivingPack;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.equipment.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item DIVING_WATCH = registerItem("diving_watch", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(DivingPack.MOD_ID,"diving_watch")))));
    public static final Item DIVING_FINS = registerItem("diving_fins", new Item(new Item.Settings().armor(ArmorMaterials.IRON, EquipmentType.BOOTS).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(DivingPack.MOD_ID,"diving_fins")))));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(DivingPack.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(DIVING_WATCH);

        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(DIVING_FINS);
        });
    }
}
