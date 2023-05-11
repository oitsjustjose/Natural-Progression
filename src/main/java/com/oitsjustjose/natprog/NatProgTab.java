package com.oitsjustjose.natprog;

import com.oitsjustjose.natprog.common.blocks.PebbleBlock;
import com.oitsjustjose.natprog.common.items.PebbleItem;
import com.oitsjustjose.natprog.common.items.SawItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NatProgTab {
    private static CreativeModeTab TAB;

    @SubscribeEvent
    public static void registerEvent(CreativeModeTabEvent.Register evt) {
        TAB = evt.registerCreativeModeTab(new ResourceLocation(Constants.MOD_ID, "items"), builder -> builder.icon(() -> new ItemStack(NatProg.getInstance().REGISTRY.ironSaw.get())).title(Component.translatable("itemGroup." + Constants.MOD_ID + ".name")).build());
    }

    @SubscribeEvent
    public static void registerEvent(CreativeModeTabEvent.BuildContents evt) {
        if (evt.getTab() != TAB) return;

        // Grab all items from NP
        var npItems = ForgeRegistries.ITEMS.getKeys().stream().filter(x -> x.getNamespace().equals(Constants.MOD_ID));
        // Sort them in a way that makes sense
        npItems.sorted((a, b) -> {
            var x = ForgeRegistries.ITEMS.getValue(a);
            var y = ForgeRegistries.ITEMS.getValue(b);

            // Sort saws first, then alphabetically
            if (x instanceof SawItem && y instanceof SawItem) return a.compareTo(b);
            if (x instanceof SawItem) return -1;
            if (y instanceof SawItem) return 1;

            // Other tools next, alphabetically
            if (x instanceof TieredItem && y instanceof TieredItem) return a.compareTo(b);
            if (x instanceof TieredItem) return -1;
            if (y instanceof TieredItem) return 1;

            // Then pebbles, alphabetically
            if (x instanceof PebbleItem && y instanceof PebbleItem) return a.compareTo(b);
            if (x instanceof PebbleItem) return -1;
            if (y instanceof PebbleItem) return 1;

            // Then alphabetically
            return a.compareTo(b);
        }).forEach(key -> { // Add to tab
            var item = ForgeRegistries.ITEMS.getValue(key);
            if (item instanceof PebbleItem pebble) {
                if (pebble.getBlock() instanceof PebbleBlock block) {
                    if (block.getParentBlock() != null) {
                        evt.accept(new ItemStack(item));
                    }
                }
            } else {
                evt.accept(new ItemStack(item));
            }
        });
    }
}
