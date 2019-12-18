package com.oitsjustjose.naturalprogression.common.event;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.items.NaturalProgressionItems;
import com.oitsjustjose.naturalprogression.common.utils.Constants;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.StandaloneLootEntry;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.ItemHandlerHelper;

public class BoneEvent
{
    private HashMap<UUID, Long> playersLastRightClicked = new HashMap<>();

    public static final Field tablePools;
    public static final Field poolEntries;
    public static final Field entryItem;

    static
    {
        tablePools = ObfuscationReflectionHelper.findField(LootTable.class, "field_186466_c" /* pools */);
        tablePools.setAccessible(true);
        poolEntries = ObfuscationReflectionHelper.findField(LootPool.class, "field_186453_a" /* lootEntries */);
        poolEntries.setAccessible(true);
        entryItem = ObfuscationReflectionHelper.findField(ItemLootEntry.class, "field_186368_a" /* item */);
        entryItem.setAccessible(true);
    }

    private static LootPool createLootPool(String name, RandomValueRange numRolls, RandomValueRange bonusRolls,
            LootEntry.Builder<?> entryBuilder, ILootCondition.IBuilder conditionBuilder,
            ILootFunction.IBuilder functionBuilder)
    {
        LootPool.Builder builder = LootPool.builder();
        builder.name(name);
        builder.rolls(numRolls);
        builder.bonusRolls(bonusRolls.getMin(), bonusRolls.getMax());
        builder.addEntry(entryBuilder);
        builder.acceptCondition(conditionBuilder);
        builder.acceptFunction(functionBuilder);

        return builder.build();
    }

    private static void addItemToTable(LootTable table, Item item, int weight, float numRolls, float probability,
            int minQuantity, int maxQuantity, String name)
    {
        RandomChance.IBuilder conditionBuilder = RandomChance.builder(probability);
        SetCount.IBuilder functionBuilder = SetCount.func_215932_a(new RandomValueRange(minQuantity, maxQuantity));

        StandaloneLootEntry.Builder<?> entryBuilder = ItemLootEntry.builder(item);
        entryBuilder.weight(weight);
        entryBuilder.quality(1);
        entryBuilder.acceptCondition(conditionBuilder);
        entryBuilder.acceptFunction(functionBuilder);

        LootPool newPool = createLootPool(name, new RandomValueRange(numRolls), new RandomValueRange(0), entryBuilder,
                conditionBuilder, functionBuilder);

        table.addPool(newPool);
    }

    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event)
    {
        if (playersLastRightClicked.containsKey(event.getPlayer().getUniqueID()))
        {
            if ((System.currentTimeMillis() - playersLastRightClicked.get(event.getPlayer().getUniqueID())) < 200)
            {
                return;
            }
        }

        Hand boneHand = event.getPlayer().getHeldItemMainhand().getItem() == Items.BONE ? Hand.MAIN_HAND
                : event.getPlayer().getHeldItemOffhand().getItem() == Items.BONE ? Hand.OFF_HAND : null;
        Hand flintHand = event.getPlayer().getHeldItemMainhand().getItem() == Items.FLINT ? Hand.MAIN_HAND
                : event.getPlayer().getHeldItemOffhand().getItem() == Items.FLINT ? Hand.OFF_HAND : null;

        if (boneHand == null || flintHand == null)
        {
            return;
        }

        event.getPlayer().swingArm(boneHand);

        if (event.getPlayer().getRNG().nextInt(100) <= CommonConfig.BONE_SHARD_CHANCE.get())
        {
            if (event.getWorld().isRemote)
            {
                event.getPlayer().playSound(SoundEvents.ENTITY_SKELETON_AMBIENT, 1F, 1F);
            }
            else
            {
                int count = event.getPlayer().getRNG().nextInt(2) + 1;
                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(),
                        new ItemStack(NaturalProgressionItems.boneShard, count));
                event.getPlayer().getHeldItem(boneHand).shrink(1);
            }
        }
        else
        {
            event.getPlayer().playSound(SoundEvents.ENTITY_SKELETON_HURT, 1F, 0.7F);
        }

        playersLastRightClicked.put(event.getPlayer().getUniqueID(), System.currentTimeMillis());
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event)
    {
        if (CommonConfig.BONE_DROP_CHANCE.get() == 0)
        {
            return;
        }

        LootTable table = event.getTable();

        CommonConfig.BONE_DROP_MOBS.get().forEach((string) -> {
            if (event.getName().equals(new ResourceLocation(string)))
            {
                float chance = (float) CommonConfig.BONE_DROP_CHANCE.get() / 100F;
                NaturalProgression.getInstance().LOGGER.info("Injecting bone drops into {} with {}% chance", string,
                        CommonConfig.BONE_DROP_CHANCE.get());
                addItemToTable(table, Items.BONE, 10, 1F, chance, 1, 1, Constants.MODID + ":extra_bone_drop");
            }
        });

    }
}
