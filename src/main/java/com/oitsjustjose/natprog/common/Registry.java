package com.oitsjustjose.natprog.common;

import com.google.common.collect.Lists;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.common.blocks.PebbleBlock;
import com.oitsjustjose.natprog.common.blocks.TwigBlock;
import com.oitsjustjose.natprog.common.data.damageitem.DamageItemRecipeSerializer;
import com.oitsjustjose.natprog.common.data.damageitem.DamageItemRecipeType;
import com.oitsjustjose.natprog.common.items.DynamicItemTier;
import com.oitsjustjose.natprog.common.items.HatchetItem;
import com.oitsjustjose.natprog.common.items.PebbleItem;
import com.oitsjustjose.natprog.common.items.SawItem;
import com.oitsjustjose.natprog.common.utils.Constants;
import com.oitsjustjose.natprog.common.utils.NatProgGroup;
import com.oitsjustjose.natprog.common.world.feature.PebbleFeature;
import com.oitsjustjose.natprog.common.world.feature.TwigFeature;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class Registry {
    public final DeferredRegister<Block> BlockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public final DeferredRegister<Item> ItemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public final DeferredRegister<RecipeType<?>> RecipeTypeRegistry = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Constants.MODID);
    public final DeferredRegister<RecipeSerializer<?>> SerializerRegistry = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);
    public final DeferredRegister<Feature<?>> FeatureRegistry = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MODID);

    public Holder<PlacedFeature> PlacedPebbleFeature;
    public Holder<PlacedFeature> PlacedTwigFeature;

    public static Tier flintTier = new DynamicItemTier().setMaxUses(16).setEfficiency(1.5F).setAttackDamage(1.0F).setHarvestLvl(0).setEnchantability(0).setRepairMats(Items.FLINT);
    public static Tier boneTier = new DynamicItemTier().setMaxUses(128).setEfficiency(2.0F).setAttackDamage(2.0F).setHarvestLvl(1).setEnchantability(0).setRepairMats(Items.BONE);
    public static Tier copperTier = new DynamicItemTier().setMaxUses(192).setEfficiency(1.65F).setAttackDamage(1.5F).setHarvestLvl(0).setEnchantability(0).setRepairMat(ItemTags.create(new ResourceLocation("forge:ingots/copper")));
    public static Tier bronzeTier = new DynamicItemTier().setMaxUses(442).setEfficiency(2.5F).setAttackDamage(2.5F).setHarvestLvl(2).setEnchantability(0).setRepairMat(ItemTags.create(new ResourceLocation("forge:ingots/bronze")));
    public static Tier steelTier = new DynamicItemTier().setMaxUses(914).setEfficiency(3.5F).setAttackDamage(3.5F).setHarvestLvl(3).setEnchantability(0).setRepairMat(ItemTags.create(new ResourceLocation("forge:ingots/steel")));
    public List<ResourceLocation> AllPebbles = Lists.newArrayList(new ResourceLocation("minecraft", "stone"), new ResourceLocation("minecraft", "andesite"), new ResourceLocation("minecraft", "diorite"), new ResourceLocation("minecraft", "granite"), new ResourceLocation("minecraft", "sandstone"), new ResourceLocation("minecraft", "red_sandstone"), new ResourceLocation("minecraft", "tuff"), new ResourceLocation("minecraft", "deepslate"), new ResourceLocation("minecraft", "dripstone_block"), new ResourceLocation("minecraft", "netherrack"), new ResourceLocation("minecraft", "end_stone"), new ResourceLocation("quark", "marble"), new ResourceLocation("quark", "limestone"), new ResourceLocation("quark", "jasper"), new ResourceLocation("quark", "slate"), new ResourceLocation("quark", "basalt"), new ResourceLocation("create", "asurine"), new ResourceLocation("create", "crimsite"), new ResourceLocation("create", "limestone"), new ResourceLocation("create", "ochrum"), new ResourceLocation("create", "scorchia"), new ResourceLocation("create", "scoria"), new ResourceLocation("create", "veridium"));

    private final List<RegistryObject<Block>> NeedItemBlocks = Lists.newArrayList();

    public Registry() {
        RegisterBlocks();
        BlockRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        RegisterItems();
        ItemRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());

        RegisterRecipeStuff();
        RecipeTypeRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());

        RegisterWorldGen();
        SerializerRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        FeatureRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public void RegisterAll(FMLJavaModLoadingContext ctx) {
//        BlockRegistry.register(ctx.getModEventBus());
//        ItemRegistry.register(ctx.getModEventBus());
//        RecipeTypeRegistry.register(ctx.getModEventBus());
//        SerializerRegistry.register(ctx.getModEventBus());
//        FeatureRegistry.register(ctx.getModEventBus());
    }

    private void RegisterBlocks() {
        BlockRegistry.register("twigs", TwigBlock::new);

        // Register all pebbles for each mod
        AllPebbles.forEach(rl -> {
            Block block = ForgeRegistries.BLOCKS.getValue(rl);
            String generated = (rl.getNamespace().equals("minecraft") ? "" : rl.getNamespace() + "_") + rl.getPath() + "_pebble";
            NeedItemBlocks.add(BlockRegistry.register(generated, () -> new PebbleBlock(block)));
        });

        // Register Cobblestones
        Block.Properties properties = Block.Properties.of(Material.STONE).strength(2.0F, 6.0F).sound(SoundType.STONE);
        NeedItemBlocks.add(BlockRegistry.register("cobbled_andesite", () -> new Block(properties)));
        NeedItemBlocks.add(BlockRegistry.register("cobbled_diorite", () -> new Block(properties)));
        NeedItemBlocks.add(BlockRegistry.register("cobbled_granite", () -> new Block(properties)));
        NeedItemBlocks.add(BlockRegistry.register("cobbled_sandstone", () -> new Block(properties)));
        NeedItemBlocks.add(BlockRegistry.register("cobbled_red_sandstone", () -> new Block(properties)));
        NeedItemBlocks.add(BlockRegistry.register("cobbled_tuff", () -> new Block(properties)));
        NeedItemBlocks.add(BlockRegistry.register("cobbled_dripstone_block", () -> new Block(properties)));
        NeedItemBlocks.add(BlockRegistry.register("cobbled_netherrack", () -> new Block(properties)));
        NeedItemBlocks.add(BlockRegistry.register("cobbled_end_stone", () -> new Block(properties)));
        NatProg.getInstance().LOGGER.info(NeedItemBlocks);
    }

    private void RegisterItems() {
        NatProgGroup tab = NatProgGroup.getInstance();
        // Register Item Block References
        NeedItemBlocks.forEach(x -> {
            Block block = x.get();
            ResourceLocation rn = x.getId();
            Item.Properties props = new Item.Properties();

            if (block instanceof PebbleBlock pebble) {
                // If there *is* a parent block, add it to a creative tab
                if (pebble.getParentBlock() != null) {
                    ItemRegistry.register(rn.getPath(), () -> new PebbleItem(block, props.tab(tab)));
                } else {
                    ItemRegistry.register(rn.getPath(), () -> new PebbleItem(block, props));
                }
            } else {
                ItemRegistry.register(rn.getPath(), () -> new BlockItem(block, props.tab(tab)));
            }
        });

        Item.Properties props = new Item.Properties().tab(tab);

        // Register all other items
        ItemRegistry.register("flint_hatchet", HatchetItem::new);
        ItemRegistry.register("bone_pickaxe", () -> new PickaxeItem(boneTier, 1, -2.8F, props.stacksTo(1)));
        ItemRegistry.register("bone_knife", () -> new SwordItem(boneTier, 1, -1.4F, props.stacksTo(1)));
        ItemRegistry.register("bone_shard", () -> new Item(props));
        // Saws all in bulk, y'know
        ItemRegistry.register("flint_saw", () -> new SawItem(flintTier));
        ItemRegistry.register("iron_saw", () -> new SawItem(Tiers.IRON));
        ItemRegistry.register("gold_saw", () -> new SawItem(Tiers.GOLD));
        ItemRegistry.register("diamond_saw", () -> new SawItem(Tiers.DIAMOND));
        ItemRegistry.register("netherite_saw", () -> new SawItem(Tiers.NETHERITE, true));
        ItemRegistry.register("copper_saw", () -> new SawItem(copperTier));
        ItemRegistry.register("bronze_saw", () -> new SawItem(bronzeTier));
        ItemRegistry.register("steel_saw", () -> new SawItem(steelTier));
    }

    public void RegisterRecipeStuff() {
        SerializerRegistry.register("damage_tools", DamageItemRecipeSerializer::new);
        RecipeTypeRegistry.register("damage_tools", DamageItemRecipeType::new);
    }

    public void RegisterWorldGen() {
        // Pebbles
        Feature<NoneFeatureConfiguration> pebbles = new PebbleFeature(NoneFeatureConfiguration.CODEC);
        FeatureRegistry.register("pebbles", () -> pebbles);
        PlacedPebbleFeature = PlacementUtils.register("pebbles", FeatureUtils.register("pebbles", pebbles), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320)));
        // Twigs
        Feature<NoneFeatureConfiguration> twigs = new TwigFeature(NoneFeatureConfiguration.CODEC);
        FeatureRegistry.register("twigs", () -> twigs);
        PlacedTwigFeature = PlacementUtils.register("twigs", FeatureUtils.register("twigs", twigs), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320)));
    }
}
