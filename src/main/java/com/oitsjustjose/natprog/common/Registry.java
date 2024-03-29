package com.oitsjustjose.natprog.common;

import com.google.common.collect.Lists;
import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.NatProg;
import com.oitsjustjose.natprog.NatProgGroup;
import com.oitsjustjose.natprog.common.blocks.PebbleBlock;
import com.oitsjustjose.natprog.common.blocks.TwigBlock;
import com.oitsjustjose.natprog.common.data.damageitem.DamageItemRecipeSerializer;
import com.oitsjustjose.natprog.common.data.damageitem.DamageItemRecipeType;
import com.oitsjustjose.natprog.common.items.DynamicItemTier;
import com.oitsjustjose.natprog.common.items.PebbleItem;
import com.oitsjustjose.natprog.common.items.SawItem;
import com.oitsjustjose.natprog.common.world.feature.PebbleFeature;
import com.oitsjustjose.natprog.common.world.feature.TwigFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;

public class Registry {
    // region DeferredRegisters
    public final DeferredRegister<Block> BlockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public final DeferredRegister<Item> ItemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public final DeferredRegister<RecipeType<?>> RecipeTypeRegistry = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Constants.MOD_ID);
    public final DeferredRegister<RecipeSerializer<?>> SerializerRegistry = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);
    public final DeferredRegister<Feature<?>> FeatureRegistry = DeferredRegister.create(net.minecraft.core.Registry.FEATURE_REGISTRY, Constants.MOD_ID);
    public final DeferredRegister<ConfiguredFeature<?, ?>> ConfiguredFeatureRegistry = DeferredRegister.create(net.minecraft.core.Registry.CONFIGURED_FEATURE_REGISTRY, Constants.MOD_ID);
    public final DeferredRegister<PlacedFeature> PlacedFeatureRegistry = DeferredRegister.create(net.minecraft.core.Registry.PLACED_FEATURE_REGISTRY, Constants.MOD_ID);
    // endregion DeferredRegisters

    // region TIERS
    public static Tier flintTier = new DynamicItemTier().setMaxUses(16).setEfficiency(1.5F).setAttackDamage(1.0F).setHarvestLvl(0).setEnchantability(0).setRepairMats(Items.FLINT);
    public static Tier boneTier = new DynamicItemTier().setMaxUses(128).setEfficiency(2.0F).setAttackDamage(2.0F).setHarvestLvl(1).setEnchantability(0).setRepairMats(Items.BONE);
    public static Tier copperTier = new DynamicItemTier().setMaxUses(192).setEfficiency(1.65F).setAttackDamage(1.5F).setHarvestLvl(0).setEnchantability(0).setRepairMat(ItemTags.create(new ResourceLocation("forge:ingots/copper")));
    public static Tier bronzeTier = new DynamicItemTier().setMaxUses(442).setEfficiency(2.5F).setAttackDamage(2.5F).setHarvestLvl(2).setEnchantability(0).setRepairMat(ItemTags.create(new ResourceLocation("forge:ingots/bronze")));
    public static Tier steelTier = new DynamicItemTier().setMaxUses(914).setEfficiency(3.5F).setAttackDamage(3.5F).setHarvestLvl(3).setEnchantability(0).setRepairMat(ItemTags.create(new ResourceLocation("forge:ingots/steel")));
    // endregion TIERS

    // region ITEMS
    public RegistryObject<Item> flintHatchet;
    public RegistryObject<Item> bonePickaxe;
    public RegistryObject<Item> boneKnife;
    public RegistryObject<Item> boneShard;
    public RegistryObject<Item> ironSaw;
    public RegistryObject<Item> flintSaw;
    public RegistryObject<Item> goldSaw;
    public RegistryObject<Item> diamondSaw;
    public RegistryObject<Item> netheriteSaw;
    public RegistryObject<Item> copperSaw;
    public RegistryObject<Item> bronzeSaw;
    public RegistryObject<Item> steelSaw;
    // endregion ITEMS

    // region BLOCKS
    public RegistryObject<Block> twigBlock;
    private final List<RegistryObject<? extends Block>> NeedItemBlocks = Lists.newArrayList();
    public final HashMap<ResourceLocation, RegistryObject<? extends Block>> Mapper = new HashMap<>();

    // endregion BLOCKS

    public Registry() {
        RegisterBlocks();
        RegisterItems();
        RegisterRecipeStuff();
        RegisterWorldGen();
    }

    public void RegisterAll(FMLJavaModLoadingContext ctx) {
        BlockRegistry.register(ctx.getModEventBus());
        ItemRegistry.register(ctx.getModEventBus());
        RecipeTypeRegistry.register(ctx.getModEventBus());
        SerializerRegistry.register(ctx.getModEventBus());
        FeatureRegistry.register(ctx.getModEventBus());
        ConfiguredFeatureRegistry.register(ctx.getModEventBus());
        PlacedFeatureRegistry.register(ctx.getModEventBus());
    }

    private void RegisterBlocks() {
        this.twigBlock = BlockRegistry.register("twigs", TwigBlock::new);

        // Register all pebbles for each mod
        Constants.PebbleMaterials.forEach(rl -> {
            var generated = (rl.getNamespace().equals("minecraft") ? "" : rl.getNamespace() + "_") + rl.getPath() + "_pebble";
            var block = BlockRegistry.register(generated, () -> new PebbleBlock(rl)); // no-use-var
            this.Mapper.put(rl, block);
            NeedItemBlocks.add(block);
        });

        // Register Cobblestones
        var properties = Block.Properties.of(Material.STONE).strength(2.0F, 6.0F).sound(SoundType.STONE);
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
        var tab = NatProgGroup.getInstance();
        // Register Item Block References
        NeedItemBlocks.forEach(x -> {
            var props = new Item.Properties();
            var rn = x.getId();

            ItemRegistry.register(rn.getPath(), () -> {
                // WAIT to resolve x.get() until the registry has been called (i.e. within the supp)
                Block block = x.get();
                if (block instanceof PebbleBlock pebble) {
                    return new PebbleItem(block, pebble.getParentBlock() == null ? props : props.tab(tab));
                } else {
                    return new BlockItem(block, props.tab(tab));
                }
            });
        });

        var props = new Item.Properties().tab(tab);

        // Register all other items
        this.flintHatchet = ItemRegistry.register("flint_hatchet", () -> new AxeItem(flintTier, 1.8F, 0F, props));
        this.bonePickaxe = ItemRegistry.register("bone_pickaxe", () -> new PickaxeItem(boneTier, 1, -2.8F, props));
        this.boneKnife = ItemRegistry.register("bone_knife", () -> new SwordItem(boneTier, 1, -1.4F, props));
        this.boneShard = ItemRegistry.register("bone_shard", () -> new Item(props));
        // Saws all in bulk, y'know
        this.flintSaw = ItemRegistry.register("flint_saw", () -> new SawItem(flintTier));
        this.ironSaw = ItemRegistry.register("iron_saw", () -> new SawItem(Tiers.IRON));
        this.goldSaw = ItemRegistry.register("gold_saw", () -> new SawItem(Tiers.GOLD));
        this.diamondSaw = ItemRegistry.register("diamond_saw", () -> new SawItem(Tiers.DIAMOND));
        this.netheriteSaw = ItemRegistry.register("netherite_saw", () -> new SawItem(Tiers.NETHERITE, true));
        this.copperSaw = ItemRegistry.register("copper_saw", () -> new SawItem(copperTier));
        this.bronzeSaw = ItemRegistry.register("bronze_saw", () -> new SawItem(bronzeTier));
        this.steelSaw = ItemRegistry.register("steel_saw", () -> new SawItem(steelTier));
    }

    public void RegisterRecipeStuff() {
        SerializerRegistry.register("damage_tools", DamageItemRecipeSerializer::new);
        RecipeTypeRegistry.register("damage_tools", DamageItemRecipeType::new);
    }

    public void RegisterWorldGen() { // no-use-var
        List<PlacementModifier> placement = Lists.newArrayList(HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320)));

        RegistryObject<Feature<NoneFeatureConfiguration>> pebbles = FeatureRegistry.register("pebbles", () -> new PebbleFeature(NoneFeatureConfiguration.CODEC));
        RegistryObject<ConfiguredFeature<?, ?>> configuredPebbles = ConfiguredFeatureRegistry.register("pebbles_configured", () -> new ConfiguredFeature<>(pebbles.get(), NoneFeatureConfiguration.INSTANCE));
        PlacedFeatureRegistry.register("pebbles_placed", () -> {
            if (configuredPebbles.getHolder().isEmpty()) {
                throw new RuntimeException("Failed to get configured feature natprog:pebbles_configured from holder, something has seriously broken. Please report this to oitsjustjose via https://discord.oitsjustjose.com");
            }
            return new PlacedFeature(configuredPebbles.getHolder().get(), placement);
        });

        RegistryObject<Feature<NoneFeatureConfiguration>> twigs = FeatureRegistry.register("twigs", () -> new TwigFeature(NoneFeatureConfiguration.CODEC));
        RegistryObject<ConfiguredFeature<?, ?>> configuredTwigs = ConfiguredFeatureRegistry.register("twigs_configured", () -> new ConfiguredFeature<>(twigs.get(), NoneFeatureConfiguration.INSTANCE));
        PlacedFeatureRegistry.register("twigs_placed", () -> {
            if (configuredTwigs.getHolder().isEmpty()) {
                throw new RuntimeException("Failed to get configured feature natprog:twigs_configured from holder, something has seriously broken. Please report this to oitsjustjose via https://discord.oitsjustjose.com");
            }
            return new PlacedFeature(configuredTwigs.getHolder().get(), placement);
        });
    }
}
