package pl.dev0on.bingo.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class Card {

    private UUID player;
    private List<BingoItem> items;
    private int points;
    private static final List<Material> possibleMaterials = Stream.of("minecraft:diamond_block", "minecraft:gold_block", "minecraft:copper_block", "minecraft:iron_block", "minecraft:amethyst_block", "minecraft:raw_gold_block", "minecraft:lapis_block", "minecraft:cobweb", "minecraft:grass", "minecraft:sea_pickle", "minecraft:dead_bush", "minecraft:orange_wool", "minecraft:magenta_wool", "minecraft:light_blue_wool", "minecraft:yellow_wool", "minecraft:lime_wool", "minecraft:pink_wool", "minecraft:gray_wool", "minecraft:light_gray_wool", "minecraft:cyan_wool", "minecraft:purple_wool", "minecraft:blue_wool", "minecraft:brown_wool", "minecraft:green_wool", "minecraft:red_wool", "minecraft:black_wool", "minecraft:dandelion", "minecraft:poppy", "minecraft:blue_orchid", "minecraft:allium", "minecraft:azure_bluet", "minecraft:red_tulip", "minecraft:orange_tulip", "minecraft:white_tulip", "minecraft:pink_tulip", "minecraft:oxeye_daisy", "minecraft:cornflower", "minecraft:lily_of_the_valley", "minecraft:brown_mushroom", "minecraft:red_mushroom", "minecraft:sugar_cane", "minecraft:kelp", "minecraft:big_dripleaf", "minecraft:small_dripleaf", "minecraft:bamboo", "minecraft:bricks", "minecraft:bookshelf", "minecraft:decorated_pot", "minecraft:obsidian", "minecraft:mossy_cobblestone", "minecraft:snow_block", "minecraft:cactus", "minecraft:jukebox", "minecraft:jack_o_lantern", "minecraft:enchanting_table", "minecraft:melon", "minecraft:white_terracotta", "minecraft:orange_terracotta", "minecraft:magenta_terracotta", "minecraft:light_blue_terracotta", "minecraft:yellow_terracotta", "minecraft:lime_terracotta", "minecraft:pink_terracotta", "minecraft:gray_terracotta", "minecraft:light_gray_terracotta", "minecraft:cyan_terracotta", "minecraft:purple_terracotta", "minecraft:blue_terracotta", "minecraft:brown_terracotta", "minecraft:green_terracotta", "minecraft:red_terracotta", "minecraft:black_terracotta", "minecraft:hay_block", "minecraft:white_carpet", "minecraft:orange_carpet", "minecraft:magenta_carpet", "minecraft:light_blue_carpet", "minecraft:yellow_carpet", "minecraft:lime_carpet", "minecraft:pink_carpet", "minecraft:gray_carpet", "minecraft:light_gray_carpet", "minecraft:cyan_carpet", "minecraft:purple_carpet", "minecraft:blue_carpet", "minecraft:brown_carpet", "minecraft:green_carpet", "minecraft:red_carpet", "minecraft:black_carpet", "minecraft:terracotta", "minecraft:sunflower", "minecraft:lilac", "minecraft:rose_bush", "minecraft:peony", "minecraft:turtle_egg", "minecraft:white_stained_glass", "minecraft:orange_stained_glass", "minecraft:magenta_stained_glass", "minecraft:light_blue_stained_glass", "minecraft:yellow_stained_glass", "minecraft:lime_stained_glass", "minecraft:pink_stained_glass", "minecraft:gray_stained_glass", "minecraft:light_gray_stained_glass", "minecraft:cyan_stained_glass", "minecraft:purple_stained_glass", "minecraft:blue_stained_glass", "minecraft:brown_stained_glass", "minecraft:green_stained_glass", "minecraft:red_stained_glass", "minecraft:black_stained_glass", "minecraft:white_stained_glass_pane", "minecraft:orange_stained_glass_pane", "minecraft:magenta_stained_glass_pane", "minecraft:light_blue_stained_glass_pane", "minecraft:yellow_stained_glass_pane", "minecraft:lime_stained_glass_pane", "minecraft:pink_stained_glass_pane", "minecraft:gray_stained_glass_pane", "minecraft:light_gray_stained_glass_pane", "minecraft:cyan_stained_glass_pane", "minecraft:purple_stained_glass_pane", "minecraft:blue_stained_glass_pane", "minecraft:brown_stained_glass_pane", "minecraft:green_stained_glass_pane", "minecraft:red_stained_glass_pane", "minecraft:black_stained_glass_pane", "minecraft:bone_block", "minecraft:white_glazed_terracotta", "minecraft:orange_glazed_terracotta", "minecraft:magenta_glazed_terracotta", "minecraft:light_blue_glazed_terracotta", "minecraft:yellow_glazed_terracotta", "minecraft:lime_glazed_terracotta", "minecraft:pink_glazed_terracotta", "minecraft:gray_glazed_terracotta", "minecraft:light_gray_glazed_terracotta", "minecraft:cyan_glazed_terracotta", "minecraft:purple_glazed_terracotta", "minecraft:blue_glazed_terracotta", "minecraft:brown_glazed_terracotta", "minecraft:green_glazed_terracotta", "minecraft:red_glazed_terracotta", "minecraft:black_glazed_terracotta", "minecraft:white_concrete", "minecraft:orange_concrete", "minecraft:magenta_concrete", "minecraft:light_blue_concrete", "minecraft:yellow_concrete", "minecraft:lime_concrete", "minecraft:pink_concrete", "minecraft:gray_concrete", "minecraft:light_gray_concrete", "minecraft:cyan_concrete", "minecraft:purple_concrete", "minecraft:blue_concrete", "minecraft:brown_concrete", "minecraft:green_concrete", "minecraft:red_concrete", "minecraft:black_concrete", "minecraft:white_concrete_powder", "minecraft:orange_concrete_powder", "minecraft:magenta_concrete_powder", "minecraft:light_blue_concrete_powder", "minecraft:yellow_concrete_powder", "minecraft:lime_concrete_powder", "minecraft:pink_concrete_powder", "minecraft:gray_concrete_powder", "minecraft:light_gray_concrete_powder", "minecraft:cyan_concrete_powder", "minecraft:purple_concrete_powder", "minecraft:blue_concrete_powder", "minecraft:brown_concrete_powder", "minecraft:green_concrete_powder", "minecraft:red_concrete_powder", "minecraft:black_concrete_powder", "minecraft:redstone", "minecraft:redstone_torch", "minecraft:redstone_block", "minecraft:piston", "minecraft:sticky_piston", "minecraft:slime_block", "minecraft:dispenser", "minecraft:dropper", "minecraft:lectern", "minecraft:target", "minecraft:lever", "minecraft:lightning_rod", "minecraft:comparator", "minecraft:repeater", "minecraft:observer", "minecraft:hopper", "minecraft:tripwire_hook", "minecraft:daylight_detector", "minecraft:tnt", "minecraft:note_block", "minecraft:redstone_lamp", "minecraft:light_weighted_pressure_plate", "minecraft:heavy_weighted_pressure_plate", "minecraft:iron_trapdoor", "minecraft:iron_door", "minecraft:powered_rail", "minecraft:detector_rail", "minecraft:rail", "minecraft:activator_rail", "minecraft:minecart", "minecraft:chest_minecart", "minecraft:furnace_minecart", "minecraft:tnt_minecart", "minecraft:hopper_minecart", "minecraft:carrot_on_a_stick", "minecraft:scute", "minecraft:flint_and_steel", "minecraft:apple", "minecraft:bow", "minecraft:arrow", "minecraft:coal", "minecraft:charcoal", "minecraft:diamond", "minecraft:emerald", "minecraft:lapis_lazuli", "minecraft:quartz", "minecraft:amethyst_shard", "minecraft:raw_iron", "minecraft:iron_ingot", "minecraft:raw_copper", "minecraft:copper_ingot", "minecraft:raw_gold", "minecraft:gold_ingot", "minecraft:golden_sword", "minecraft:golden_shovel", "minecraft:golden_pickaxe", "minecraft:golden_axe", "minecraft:golden_hoe", "minecraft:iron_sword", "minecraft:iron_shovel", "minecraft:iron_pickaxe", "minecraft:iron_axe", "minecraft:iron_hoe", "minecraft:diamond_sword", "minecraft:diamond_shovel", "minecraft:diamond_pickaxe", "minecraft:diamond_axe", "minecraft:diamond_hoe", "minecraft:mushroom_stew", "minecraft:string", "minecraft:feather", "minecraft:gunpowder", "minecraft:wheat", "minecraft:bread", "minecraft:leather_helmet", "minecraft:leather_chestplate", "minecraft:leather_leggings", "minecraft:leather_boots", "minecraft:iron_helmet", "minecraft:iron_chestplate", "minecraft:iron_leggings", "minecraft:iron_boots", "minecraft:diamond_helmet", "minecraft:diamond_chestplate", "minecraft:diamond_leggings", "minecraft:diamond_boots", "minecraft:golden_helmet", "minecraft:golden_chestplate", "minecraft:golden_leggings", "minecraft:golden_boots", "minecraft:painting", "minecraft:oak_sign", "minecraft:spruce_sign", "minecraft:golden_apple", "minecraft:birch_sign", "minecraft:jungle_sign", "minecraft:acacia_sign", "minecraft:cherry_sign", "minecraft:dark_oak_sign", "minecraft:mangrove_sign", "minecraft:bamboo_sign", "minecraft:oak_hanging_sign", "minecraft:spruce_hanging_sign", "minecraft:birch_hanging_sign", "minecraft:jungle_hanging_sign", "minecraft:acacia_hanging_sign", "minecraft:cherry_hanging_sign", "minecraft:dark_oak_hanging_sign", "minecraft:mangrove_hanging_sign", "minecraft:bamboo_hanging_sign", "minecraft:water_bucket", "minecraft:lava_bucket", "minecraft:snowball", "minecraft:leather", "minecraft:milk_bucket", "minecraft:pufferfish_bucket", "minecraft:salmon_bucket", "minecraft:cod_bucket", "minecraft:tropical_fish_bucket", "minecraft:axolotl_bucket", "minecraft:brick", "minecraft:clay_ball", "minecraft:dried_kelp_block", "minecraft:paper", "minecraft:book", "minecraft:slime_ball", "minecraft:egg", "minecraft:compass", "minecraft:fishing_rod", "minecraft:clock", "minecraft:spyglass", "minecraft:glowstone_dust", "minecraft:cod", "minecraft:salmon", "minecraft:tropical_fish", "minecraft:pufferfish", "minecraft:cooked_cod", "minecraft:cooked_salmon", "minecraft:ink_sac", "minecraft:glow_ink_sac", "minecraft:cocoa_beans", "minecraft:white_dye", "minecraft:orange_dye", "minecraft:magenta_dye", "minecraft:light_blue_dye", "minecraft:yellow_dye", "minecraft:lime_dye", "minecraft:pink_dye", "minecraft:gray_dye", "minecraft:light_gray_dye", "minecraft:cyan_dye", "minecraft:purple_dye", "minecraft:blue_dye", "minecraft:brown_dye", "minecraft:green_dye", "minecraft:red_dye", "minecraft:black_dye", "minecraft:bone_meal", "minecraft:bone", "minecraft:sugar", "minecraft:cake", "minecraft:white_bed", "minecraft:orange_bed", "minecraft:magenta_bed", "minecraft:light_blue_bed", "minecraft:yellow_bed", "minecraft:lime_bed", "minecraft:pink_bed", "minecraft:gray_bed", "minecraft:light_gray_bed", "minecraft:cyan_bed", "minecraft:purple_bed", "minecraft:blue_bed", "minecraft:brown_bed", "minecraft:green_bed", "minecraft:red_bed", "minecraft:black_bed", "minecraft:cookie", "minecraft:shears", "minecraft:melon_slice", "minecraft:dried_kelp", "minecraft:pumpkin_seeds", "minecraft:melon_seeds", "minecraft:beef", "minecraft:cooked_beef", "minecraft:chicken", "minecraft:cooked_chicken", "minecraft:rotten_flesh", "minecraft:ender_pearl", "minecraft:blaze_rod", "minecraft:spider_eye", "minecraft:cauldron", "minecraft:writable_book", "minecraft:item_frame", "minecraft:glow_item_frame", "minecraft:flower_pot", "minecraft:carrot", "minecraft:potato", "minecraft:baked_potato", "minecraft:golden_carrot", "minecraft:map", "minecraft:pumpkin_pie", "minecraft:rabbit", "minecraft:cooked_rabbit", "minecraft:rabbit_stew", "minecraft:rabbit_foot", "minecraft:rabbit_hide", "minecraft:armor_stand", "minecraft:leather_horse_armor", "minecraft:lead", "minecraft:mutton", "minecraft:cooked_mutton", "minecraft:beetroot_seeds", "minecraft:beetroot", "minecraft:beetroot_soup", "minecraft:shield", "minecraft:crossbow", "minecraft:loom", "minecraft:smoker", "minecraft:blast_furnace", "minecraft:cartography_table", "minecraft:fletching_table", "minecraft:smithing_table", "minecraft:stonecutter", "minecraft:lantern", "minecraft:brush")
            .map(item -> Material.matchMaterial(item))
            .toList();

    public Card(UUID player) {
        this.player = player;
        this.items = new ArrayList<>();
        initItems();
    }

    public Card(Player player) {
        this(player.getUniqueId());
    }

    public void initItems() {
        for (int i = 0; i < 25; i++) {
            final AtomicReference<Material> mat = new AtomicReference<>(
                    possibleMaterials.get(new Random().nextInt(possibleMaterials.size()))
            );
            if (!this.items.isEmpty()) {
                while (this.items.stream().anyMatch(item -> item.material.equals(mat.get()))) {
                    mat.set(possibleMaterials.get(new Random().nextInt(possibleMaterials.size())));
                }
            }

            this.items.add(new BingoItem(mat.get(), false));
        }
    }

    public List<BingoItem> getItems() {
        return items;
    }

    public UUID getPlayer() {
        return player;
    }

    public int getPoints() {
        return this.points;
    }

    public boolean solveItem(ItemStack item) {
        var bingoItem = this.items.stream().filter(bItem -> bItem.material.equals(item.getType())).findFirst().orElse(null);
        if (bingoItem == null || bingoItem.done)
            return false;
        bingoItem.done = true;
        this.points += 3;
        return true;
    }

    public boolean checkForBingo() {
        for (int i = 0; i < 5; i++) {
            boolean height = true;
            boolean width = true;
            for (int j = 0; j < 5; j++) {
                if (!this.items.get(i * 5 + j).done) height = false;
                if (!this.items.get(j * 5 + i).done) width = false;
            }
            if (width || height) return true;
        }
        return false;
    }

    public class BingoItem {
        private Material material;
        private boolean done;

        public BingoItem(Material material, boolean done) {
            this.material = material;
            this.done = done;
        }

        public ItemStack getItem() {
            ItemStack item = new ItemStack(this.material);
            setGlowing(item, done);
            setLore(item, this.done ? List.of(Component.text("SKOŃCZONO").color(TextColor.color(Color.RED.asRGB()))) : List.of());

            return item;
        }

        private void setLore(ItemStack item, List<Component> components) {
            ItemMeta im = item.getItemMeta();

            im.lore(components);
            item.setItemMeta(im);
        }

        private void setGlowing(ItemStack is, boolean shouldGlow) {
            ItemMeta im = is.getItemMeta();

            if (!shouldGlow) {
                im.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
                im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                is.setItemMeta(im);
                return;
            }

            im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            is.setItemMeta(im);
            return;
        }
    }
}
