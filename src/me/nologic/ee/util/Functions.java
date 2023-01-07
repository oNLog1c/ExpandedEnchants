package me.nologic.ee.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ShapedRecipe;

import me.nologic.ee.CustomEnchantsManager;
import me.nologic.ee.ExpandedEnchants;

public class Functions {
	
	public static void GetBlocksInBox(List<Block> list, Location origin, int radius, Material type) {
		for (int x = -radius; x < radius; x++)
		{
			for (int y = -radius; y < radius; y++)
			{
				for (int z = -radius; z < radius; z++)
				{
					Block bl = origin.getBlock().getRelative(x, y, z);
					if(bl.getType().equals(type)) list.add(bl);
				}
			}
		}

	}

	public static boolean IsSmeltable(Item item) {
			switch(item.getItemStack().getType()) {
				case RAW_IRON: return true;
				case RAW_COPPER: return true;
				case RAW_GOLD: return true;
				case ANCIENT_DEBRIS: return true;
				case RAW_GOLD_BLOCK: return true;
				case RAW_IRON_BLOCK: return true;
				case RAW_COPPER_BLOCK: return true;
				case SAND: return true;
				case RED_SAND: return true;
				case KELP: return true;
				case CACTUS: return true;
				case CHORUS_FRUIT: return true;
				case CLAY: return true;
				case COBBLESTONE: return true;
				case WET_SPONGE: return true;
				case NETHERRACK: return true;
				case STONE_BRICKS: return true;
				case DARK_OAK_WOOD: return true;
				case SPRUCE_WOOD: return true;
				case ACACIA_WOOD: return true;
				case BIRCH_WOOD: return true;
				case JUNGLE_WOOD: return true;
				case OAK_WOOD: return true;
				case QUARTZ_BLOCK: return true;
				case STRIPPED_DARK_OAK_WOOD: return true;
				case STRIPPED_SPRUCE_WOOD: return true;
				case STRIPPED_ACACIA_WOOD: return true;
				case STRIPPED_BIRCH_WOOD: return true;
				case STRIPPED_JUNGLE_WOOD: return true;
				case STRIPPED_OAK_WOOD: return true;
				default: break;
			}
			return false;
	}
	
	public static boolean IsXpAutosmeltDrop(Item item) {
		switch(item.getItemStack().getType()) {
			case RAW_IRON: return true;
			case RAW_COPPER: return true;
			case RAW_GOLD: return true;
			case ANCIENT_DEBRIS: return true;
			case RAW_GOLD_BLOCK: return true;
			case RAW_IRON_BLOCK: return true;
			case RAW_COPPER_BLOCK: return true;
			case DARK_OAK_WOOD: return true;
			case SPRUCE_WOOD: return true;
			case ACACIA_WOOD: return true;
			case BIRCH_WOOD: return true;
			case JUNGLE_WOOD: return true;
			case OAK_WOOD: return true;
			case STRIPPED_DARK_OAK_WOOD: return true;
			case STRIPPED_SPRUCE_WOOD: return true;
			case STRIPPED_ACACIA_WOOD: return true;
			case STRIPPED_BIRCH_WOOD: return true;
			case STRIPPED_JUNGLE_WOOD: return true;
			case STRIPPED_OAK_WOOD: return true;
			default: break;
		}
		return false;
}
	
	public static Material SmeltedCounterpart(Material mat) {
			switch(mat) {
				case RAW_IRON: return Material.IRON_INGOT;
				case RAW_COPPER: return Material.COPPER_INGOT;
				case RAW_GOLD: return Material.GOLD_INGOT;
				case ANCIENT_DEBRIS: return Material.NETHERITE_SCRAP;
				case RAW_GOLD_BLOCK: return Material.GOLD_BLOCK;
				case RAW_IRON_BLOCK: return Material.IRON_BLOCK;
				case RAW_COPPER_BLOCK: return Material.COPPER_BLOCK;
				case SAND:
				case RED_SAND:
					return Material.GLASS;
				case KELP: return Material.DRIED_KELP;
				case CACTUS: return Material.GREEN_DYE;
				case CHORUS_FRUIT: return Material.POPPED_CHORUS_FRUIT;
				case CLAY: return Material.TERRACOTTA;
				case COBBLESTONE: return Material.STONE;
				case WET_SPONGE: return Material.SPONGE;
				case NETHERRACK: return Material.NETHER_BRICK;
				case STONE_BRICKS: return Material.CRACKED_STONE_BRICKS;
				case DARK_OAK_WOOD:
				case SPRUCE_WOOD:
				case ACACIA_WOOD:
				case BIRCH_WOOD:
				case JUNGLE_WOOD:
				case OAK_WOOD:
				case STRIPPED_DARK_OAK_WOOD:
				case STRIPPED_SPRUCE_WOOD:
				case STRIPPED_BIRCH_WOOD:
				case STRIPPED_OAK_WOOD:
				case STRIPPED_JUNGLE_WOOD:
				case STRIPPED_ACACIA_WOOD:
					return Material.CHARCOAL;
				case QUARTZ_BLOCK: return Material.SMOOTH_QUARTZ;

				default: return null;
			}
		
	}
	
	public static int GetRandomNumber(int min, int max) {
		return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
	}

	public static int GetArmorPoints(Material mat) {
		switch (mat)
		{
		case CHAINMAIL_CHESTPLATE: return 5;
		case LEATHER_CHESTPLATE: return 3;
		case IRON_CHESTPLATE: return 6;
		case GOLDEN_CHESTPLATE: return 5;
		case NETHERITE_CHESTPLATE: return 8;
		case DIAMOND_CHESTPLATE: return 8;
		
		case LEATHER_LEGGINGS: return 2;
		case CHAINMAIL_LEGGINGS: return 4;
		case IRON_LEGGINGS: return 5;
		case GOLDEN_LEGGINGS: return 3;
		case DIAMOND_LEGGINGS: return 6;
		case NETHERITE_LEGGINGS: return 6;
		
		default: return 0;
		}
	}
	
	public static int GetToughnessPoints(Material mat) {
		switch (mat)
		{
		case NETHERITE_CHESTPLATE: return 3;
		case DIAMOND_CHESTPLATE: return 2;
		
		case DIAMOND_LEGGINGS: return 2;
		case NETHERITE_LEGGINGS: return 3;
		
		default: return 0;
		}
	}
	
	public static int GetKnockbackPoints(Material mat) {
		switch(mat) {
		case NETHERITE_CHESTPLATE: return 1;
		case NETHERITE_LEGGINGS: return 1;
		default: return 0;
		}
	}
	
	public static List<Enchantment> GetEnabledEnchants() {
		List<Enchantment> enchs = new ArrayList<>();
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("AntigravityEnabled")) enchs.add(CustomEnchantsManager.ANTIGRAVITY);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("AssassinEnabled")) enchs.add(CustomEnchantsManager.ASSASSIN);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("AutosmeltEnabled")) enchs.add(CustomEnchantsManager.AUTOSMELT);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("BeheadingEnabled")) enchs.add(CustomEnchantsManager.BEHEADING);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("DeflectEnabled")) enchs.add(CustomEnchantsManager.DEFLECT);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("DirectEnabled")) enchs.add(CustomEnchantsManager.DIRECT);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("DirectEnabled")) enchs.add(CustomEnchantsManager.DISARMING);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("DirectEnabled")) enchs.add(CustomEnchantsManager.DISRUPTION);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("ElementalprotectionEnabled")) enchs.add(CustomEnchantsManager.ELEMENTALPROTECTION);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("ExperienceboostEnabled")) enchs.add(CustomEnchantsManager.EXP_BOOST);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("EyesofowlEnabled")) enchs.add(CustomEnchantsManager.OWLEYES);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("FeedingmoduleEnabled")) enchs.add(CustomEnchantsManager.FEEDINGMODULE);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("FlamingfallEnabled")) enchs.add(CustomEnchantsManager.FLAMINGFALL);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("GourmandEnabled")) enchs.add(CustomEnchantsManager.GOURMAND);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("HealthboostEnabled")) enchs.add(CustomEnchantsManager.HEALTHBOOST);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("HeavenslightnessEnabled")) enchs.add(CustomEnchantsManager.HEAVENSLIGHTNESS);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("IcyEnabled")) enchs.add(CustomEnchantsManager.ICY);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("LavawalkerEnabled")) enchs.add(CustomEnchantsManager.LAVA_WALKER);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("LeapingEnabled")) enchs.add(CustomEnchantsManager.LEAPING);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("LifestealEnabled")) enchs.add(CustomEnchantsManager.LIFESTEAL);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("LumberjackEnabled")) enchs.add(CustomEnchantsManager.LUMBERJACK);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("OresightEnabled")) enchs.add(CustomEnchantsManager.ORESIGHT);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("ReplantingEnabled")) enchs.add(CustomEnchantsManager.REPLANTING);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("ShadowstepEnabled")) enchs.add(CustomEnchantsManager.SHADOWSTEP);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("SoulboundEnabled")) enchs.add(CustomEnchantsManager.SOULBOUND);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("SplittingEnabled")) enchs.add(CustomEnchantsManager.SPLITTING);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("SteppingEnabled")) enchs.add(CustomEnchantsManager.STEPPING);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("StonefistsEnabled")) enchs.add(CustomEnchantsManager.STONEFISTS);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("ThermalplatingEnabled")) enchs.add(CustomEnchantsManager.THERMALPLATING);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("ThrustersEnabled")) enchs.add(CustomEnchantsManager.THRUSTERS);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("TravelerEnabled")) enchs.add(CustomEnchantsManager.TRAVELER);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("UnbreakableEnabled")) enchs.add(CustomEnchantsManager.NOBREAKABLE);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("VeinmineEnabled")) enchs.add(CustomEnchantsManager.VEINMINE);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("WideEnabled")) enchs.add(CustomEnchantsManager.WIDE);
		return enchs;
	}

	public static int GetEnchIndex(Enchantment thing, List<Enchantment> list) {
		for(int i = 0; i < list.size(); i++) if(list.get(i).equals(thing)) return i;
		return -1;
	}

	public static int GetSlot(int i) {
		switch(i) {
		case 0:
			return 20;
		case 1:
			return 21;
		case 2:
			return 22;
		case 3:
			return 29;
		case 4:
			return 30;
		case 5:
			return 31;
		case 6:
			return 38;
		case 7:
			return 39;
		case 8:
			return 40;
		}
		return -1;
	}
	public static boolean CheckPickaxeTypes(ItemStack item) {
		switch(item.getType()) {
			case DIAMOND_PICKAXE: return true;
			case GOLDEN_PICKAXE: return true;
			case WOODEN_PICKAXE: return true;
			case IRON_PICKAXE: return true;
			case STONE_PICKAXE: return true;
			case NETHERITE_PICKAXE: return true;
			default:
				return false;
		}
	}
	
	public static boolean CheckSwordTypes(ItemStack item) {
		switch(item.getType()) {
		case DIAMOND_SWORD: return true;
		case GOLDEN_SWORD: return true;
		case WOODEN_SWORD: return true;
		case IRON_SWORD: return true;
		case STONE_SWORD: return true;
		case NETHERITE_SWORD: return true;
			default: return false;
		}
	}
	
	public static boolean CheckShovelTypes(ItemStack item) {
		switch(item.getType()) {
			case DIAMOND_SHOVEL: return true;
			case GOLDEN_SHOVEL: return true;
			case WOODEN_SHOVEL: return true;
			case IRON_SHOVEL: return true;
			case STONE_SHOVEL: return true;
			case NETHERITE_SHOVEL: return true;
			default: return false;
		}
	}
	
	public static boolean CheckHoeTypes(ItemStack item) {
		switch(item.getType()) {
			case DIAMOND_HOE: return true;
			case GOLDEN_HOE: return true;
			case WOODEN_HOE: return true;
			case IRON_HOE: return true;
			case STONE_HOE: return true;
			case NETHERITE_HOE: return true;
			default: return false;
		}
	}
	
	public static boolean CheckAxeTypes(ItemStack item) {
		switch(item.getType()) {
			case DIAMOND_AXE: return true;
			case GOLDEN_AXE: return true;
			case WOODEN_AXE: return true;
			case IRON_AXE: return true;
			case STONE_AXE: return true;
			case NETHERITE_AXE: return true;
			default: return false;
		}
	}
	
	public static boolean CheckHelmetTypes(ItemStack item) {
		switch(item.getType()) {
			case DIAMOND_HELMET: return true;
			case GOLDEN_HELMET: return true;
			case LEATHER_HELMET: return true;
			case IRON_HELMET: return true;
			case TURTLE_HELMET: return true;
			case NETHERITE_HELMET: return true;
			case CHAINMAIL_HELMET: return true;
			default: return false;
		}
	}
	
	public static boolean CheckChestplateTypes(ItemStack item) {
		switch(item.getType()) {
			case DIAMOND_CHESTPLATE: return true;
			case GOLDEN_CHESTPLATE: return true;
			case LEATHER_CHESTPLATE: return true;
			case IRON_CHESTPLATE: return true;
			case NETHERITE_CHESTPLATE: return true;
			case CHAINMAIL_CHESTPLATE: return true;
			default: return false;
		}
	}
	
	public static boolean CheckLeggingsTypes(ItemStack item) {
		switch(item.getType()) {
			case DIAMOND_LEGGINGS: return true;
			case GOLDEN_LEGGINGS: return true;
			case LEATHER_LEGGINGS: return true;
			case IRON_LEGGINGS: return true;
			case NETHERITE_LEGGINGS: return true;
			case CHAINMAIL_LEGGINGS: return true;
			default: return false;
		}
	}
	
	public static boolean CheckBootsTypes(ItemStack item) {
		switch(item.getType()) {
			case DIAMOND_BOOTS: return true;
			case GOLDEN_BOOTS: return true;
			case LEATHER_BOOTS: return true;
			case IRON_BOOTS: return true;
			case NETHERITE_BOOTS: return true;
			case CHAINMAIL_BOOTS: return true;
			default: return false;
		}
	}

	public static boolean CheckNonStrippedLogTypes(Material item) {
		switch(item) {
			case ACACIA_LOG: return true;
			case ACACIA_WOOD: return true;
			case BIRCH_LOG: return true;
			case BIRCH_WOOD: return true;
			case CRIMSON_HYPHAE: return true;
			case CRIMSON_STEM: return true;
			case DARK_OAK_LOG: return true;
			case DARK_OAK_WOOD: return true;
			case JUNGLE_LOG: return true;
			case JUNGLE_WOOD: return true;
			case OAK_LOG: return true;
			case OAK_WOOD: return true;
			case SPRUCE_LOG: return true;
			case SPRUCE_WOOD: return true;
			case WARPED_HYPHAE: return true;
			case WARPED_STEM: return true;
			default: return false;
		}
	}
	
	public static Material GetStrippedVariantWood(Material item) {
		switch(item) {
			case ACACIA_LOG: return Material.STRIPPED_ACACIA_LOG;
			case ACACIA_WOOD: return Material.STRIPPED_ACACIA_WOOD;
			case BIRCH_LOG: return Material.STRIPPED_BIRCH_LOG;
			case BIRCH_WOOD: return Material.STRIPPED_BIRCH_WOOD;
			case CRIMSON_HYPHAE: return Material.STRIPPED_CRIMSON_HYPHAE;
			case CRIMSON_STEM: return Material.STRIPPED_CRIMSON_STEM;
			case DARK_OAK_LOG: return Material.STRIPPED_DARK_OAK_LOG;
			case DARK_OAK_WOOD: return Material.STRIPPED_DARK_OAK_WOOD;
			case JUNGLE_LOG: return Material.STRIPPED_JUNGLE_LOG;
			case JUNGLE_WOOD: return Material.STRIPPED_JUNGLE_WOOD;
			case OAK_LOG: return Material.STRIPPED_OAK_LOG;
			case OAK_WOOD: return Material.STRIPPED_OAK_WOOD;
			case SPRUCE_LOG: return Material.STRIPPED_SPRUCE_LOG;
			case SPRUCE_WOOD: return Material.STRIPPED_SPRUCE_WOOD;
			case WARPED_HYPHAE: return Material.STRIPPED_WARPED_HYPHAE;
			case WARPED_STEM: return Material.STRIPPED_WARPED_STEM;
			default: return null;
		}
	}
	
	public static boolean CheckArmorTypes(ItemStack item) {
		 if(CheckHelmetTypes(item) || CheckChestplateTypes(item) || CheckLeggingsTypes(item) || CheckBootsTypes(item)) return true;
		 return false;
	}
	
	public static boolean CheckToolsTypes(ItemStack item) {
		if(CheckPickaxeTypes(item) || CheckShovelTypes(item) || CheckHoeTypes(item) || CheckAxeTypes(item)) return true;
		return false;
	}
	
	public static String GetNameByLevel(int l, int max) {
		if(l == 1 && max == 1) return "";
		
		switch(l) {
		case 1:
			return "I";
		case 2:
			return "II";
		case 3:
			return "III";
		case 4:
			return "IV";
		case 5:
			return "V";
		case 6:
			return "VI";
		case 7:
			return "VII";
		case 8:
			return "VIII";
		case 9:
			return "IX";
		case 10:
			return "X";
		case 11:
			return "XI";
		case 12:
			return "XII";
		case 13:
			return "XIII";
		case 14:
			return "XIV";
		case 15:
			return "XV";
		case 16:
			return "XVI";
		case 17:
			return "XVII";
		case 18:
			return "XVIII";
		case 19:
			return "XIX";
		case 20:
			return "XX";
		}
		return "";
	}
	
	public static boolean ContainsCustomEnchant(ItemStack item) {
		boolean containsCustom = false;		
		for(Enchantment enchantment : CustomEnchantsManager.custom_enchants) {
			if(item.getItemMeta().hasEnchant(enchantment)) containsCustom = true;
		}
		return containsCustom;
	}
	
	public static boolean IsCustomEnchant(Enchantment ench) {
		boolean isCustom = false;
		
		for(Enchantment enchant : CustomEnchantsManager.custom_enchants) {
			if (ench.equals(enchant)) {
				isCustom = true;
				break;
			}
		}
		return isCustom;
	}

	public static boolean IsLumberjackBlock(Material item) {
		Material[] mats = LumberjackBlocks();
		for(Material mat : mats) {
			if(mat.equals(item)) return true;
		}
		return false;
	}
	
	public static boolean IsVeinmineBlock(Material item) {
		Material[] mats = VeinmineBlocks();
		for(Material mat : mats) {
			if(mat.equals(item)) return true;
		}
		return false;
	}
	
	public static boolean PlayerContainsFoodItems(Player player) {
		PlayerInventory inv = player.getInventory();
		ItemStack[] content = inv.getContents();
		for(ItemStack item : content) {
			if(item == null) continue;
			for(Material food : FeedingModuleFoods()) 
			{
				if(food == item.getType()) return true;
			}
		}
		return false;
	}
	
	public static ItemStack GetFirstFoodItem(Player player) {
		PlayerInventory inv = player.getInventory();
		ItemStack[] content = inv.getContents();
		for(ItemStack item : content) {
			if(item == null) continue;
			for(Material food : FeedingModuleFoods()) {
				if(item.getType() == food) return item;
			}
		}
		return null;
	}
	
	public static Material[] FeedingModuleFoods() {
		List<String> configEntries = ExpandedEnchants.getInstance().getConfig().getStringList("FeedingModuleFoods");
		Material[] mats = new Material[configEntries.size()];
		for(int i = 0; i < configEntries.size(); i++){
			Material mat = Material.valueOf(configEntries.get(i));
			mats[i] = mat;
		}
		return mats;
	}
	
	public static Material[] VeinmineBlocks() {
		List<String> configEntries = ExpandedEnchants.getInstance().getConfig().getStringList("VeinmineBlocks");
		Material[] mats = new Material[configEntries.size()];
		for(int i = 0; i < configEntries.size(); i++){
			Material mat = Material.valueOf(configEntries.get(i));
			mats[i] = mat;
		}
		return mats;
	}

	public static Material[] LumberjackBlocks() {
		List<String> configEntries = ExpandedEnchants.getInstance().getConfig().getStringList("LumberjackBlocks");
		
		Material[] mats = new Material[configEntries.size()];
		
		for(int i = 0; i < configEntries.size(); i++){
			Material mat = Material.valueOf(configEntries.get(i));
			mats[i] = mat;
		}
		
		
		return mats;
	}

	public static int GetFoodPoints(Material mat) {
		switch(mat) {
		case APPLE:
			case CHORUS_FRUIT:
			case ENCHANTED_GOLDEN_APPLE:
			case GOLDEN_APPLE:
			case ROTTEN_FLESH:
				return 4;
		case BAKED_POTATO:
			case BREAD:
			case COOKED_COD:
			case COOKED_RABBIT:
				return 5;
		case BEETROOT:
			case POTATO:
			case DRIED_KELP:
			case PUFFERFISH:
			case TROPICAL_FISH:
				return 1;
		case BEETROOT_SOUP:
			case COOKED_CHICKEN:
			case COOKED_MUTTON:
			case COOKED_SALMON:
			case GOLDEN_CARROT:
			case HONEY_BOTTLE:
			case MUSHROOM_STEW:
			case SUSPICIOUS_STEW:
				return 6;
			case CARROT:
			case BEEF:
			case PORKCHOP:
			case RABBIT:
				return 3;
		case COOKED_BEEF:
			case COOKED_PORKCHOP:
			case PUMPKIN_PIE:
				return 8;
			case COOKIE:
			case GLOW_BERRIES:
			case MELON_SLICE:
			case SWEET_BERRIES:
			case POISONOUS_POTATO:
			case SALMON:
			case SPIDER_EYE:
			case MUTTON:
			case COD:
			case CHICKEN:
				return 2;
			case RABBIT_STEW: return 10;
		}

		return 0;
	}

	public static String GetEnchantmentName(Enchantment ench) {
		
		if(ench.equals(Enchantment.ARROW_DAMAGE)) return "Сила";
		if(ench.equals(Enchantment.ARROW_FIRE)) return "Воспламенение";
		if(ench.equals(Enchantment.ARROW_INFINITE)) return "Бесконечность";
		if(ench.equals(Enchantment.ARROW_KNOCKBACK)) return "Отбрасывание";
		if(ench.equals(Enchantment.BINDING_CURSE)) return "Проклятие несъёмности";
		if(ench.equals(Enchantment.CHANNELING)) return "Громовержец";
		if(ench.equals(Enchantment.DAMAGE_ALL)) return "Острота";
		if(ench.equals(Enchantment.DAMAGE_ARTHROPODS)) return "Бич членистоногих";
		if(ench.equals(Enchantment.DAMAGE_UNDEAD)) return "Небесная кара";
		if(ench.equals(Enchantment.DEPTH_STRIDER)) return "Подводная ходьба";
		if(ench.equals(Enchantment.DIG_SPEED)) return "Эффективность";
		if(ench.equals(Enchantment.PROTECTION_ENVIRONMENTAL )) return "Защита";
		if(ench.equals(Enchantment.PROTECTION_FIRE )) return "Огнеупорность";
		if(ench.equals(Enchantment.PROTECTION_FALL )) return "Невесомость";
		if(ench.equals(Enchantment.PROTECTION_EXPLOSIONS )) return "Взрывоустойчивость";
		if(ench.equals(Enchantment.PROTECTION_PROJECTILE )) return "Защита от снарядов";
		if(ench.equals(Enchantment.OXYGEN )) return "Подводное дыхание";
		if(ench.equals(Enchantment.WATER_WORKER )) return "Подводник";
		if(ench.equals(Enchantment.THORNS )) return "Шипы";
		if(ench.equals(Enchantment.FIRE_ASPECT)) return "Заговор огня";
		if(ench.equals(Enchantment.LOOT_BONUS_MOBS )) return "Добыча";
		if(ench.equals(Enchantment.SILK_TOUCH )) return "Шелковое касание";
		if(ench.equals(Enchantment.DURABILITY )) return "Прочность";
		if(ench.equals(Enchantment.LOOT_BONUS_BLOCKS )) return "Удача";
		if(ench.equals(Enchantment.LUCK )) return "Везучий рыбак";
		if(ench.equals(Enchantment.LURE )) return "Приманка";
		if(ench.equals(Enchantment.LOYALTY)) return "Верность";
		if(ench.equals(Enchantment.MULTISHOT)) return "Тройной выстрел";
		if(ench.equals(Enchantment.MENDING)) return "Починка";
		if(ench.equals(Enchantment.PIERCING)) return "Пронзающая стрела";
		if(ench.equals(Enchantment.QUICK_CHARGE)) return "Быстрая перезарядка";
		if(ench.equals(Enchantment.RIPTIDE)) return "Тягун";
		if(ench.equals(Enchantment.SWEEPING_EDGE)) return "Разящий клинок";
		if(ench.equals(Enchantment.VANISHING_CURSE)) return "Проклятье утраты";
		if(ench.equals(Enchantment.FROST_WALKER)) return "Ледоход";
		if(ench.equals(Enchantment.IMPALING)) return "Пронзатель";
		if(ench.equals(Enchantment.KNOCKBACK)) return "Отдача";
		if(ench.equals(Enchantment.SOUL_SPEED)) return "Скорость души";
		return "";
	}
	
}