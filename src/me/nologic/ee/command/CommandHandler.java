package me.nologic.ee.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.nologic.ee.util.Functions;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.nologic.ee.CustomEnchantsManager;
import me.nologic.ee.ExpandedEnchants;
import me.nologic.ee.util.LanguageManager;
import net.md_5.bungee.api.ChatColor;

public class CommandHandler implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
			if(!(sender instanceof Player))
				return true; 
			Player player = (Player) sender;
			if(args.length == 0) {
				if(!player.hasPermission("ee.command.creative")) {
					sender.sendMessage(ChatColor.RED + "You don't have permission for this command!");
					return true;
				}
				player.setGameMode(GameMode.CREATIVE);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("recipes")) {
				player.openInventory(ExpandedEnchants.inventoryManager.startInv);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("give")) {
				if(args.length == 1) return false;
				int level = 1;
				if(args.length > 2) if(!args[2].equals("")) level = Integer.parseInt(args[2]);
				if(args[1].equalsIgnoreCase("assassin")) return GiveCustomBook(CustomEnchantsManager.ASSASSIN, level, player);
				if(args[1].equalsIgnoreCase("unbreakable")) return GiveCustomBook(CustomEnchantsManager.NOBREAKABLE, level, player);
				if(args[1].equalsIgnoreCase("direct")) return GiveCustomBook(CustomEnchantsManager.DIRECT, level, player);
				if(args[1].equalsIgnoreCase("disarming")) return GiveCustomBook(CustomEnchantsManager.DISARMING, level, player);
				if(args[1].equalsIgnoreCase("disruption")) return GiveCustomBook(CustomEnchantsManager.DISRUPTION, level, player);
				if(args[1].equalsIgnoreCase("autosmelt")) return GiveCustomBook(CustomEnchantsManager.AUTOSMELT, level, player);
				if(args[1].equalsIgnoreCase("antigravity")) return GiveCustomBook(CustomEnchantsManager.ANTIGRAVITY, level, player);
				if(args[1].equalsIgnoreCase("beheading")) return GiveCustomBook(CustomEnchantsManager.BEHEADING, level, player);
				if(args[1].equalsIgnoreCase("expboost")) return GiveCustomBook(CustomEnchantsManager.EXP_BOOST, level, player);
				if(args[1].equalsIgnoreCase("elementalprotection")) return GiveCustomBook(CustomEnchantsManager.ELEMENTALPROTECTION, level, player);
				if(args[1].equalsIgnoreCase("traveler")) return GiveCustomBook(CustomEnchantsManager.TRAVELER, level, player);
				if(args[1].equalsIgnoreCase("lavawalker")) return GiveCustomBook(CustomEnchantsManager.LAVA_WALKER, level, player);
				if(args[1].equalsIgnoreCase("healthboost")) return GiveCustomBook(CustomEnchantsManager.HEALTHBOOST, level, player);
				if(args[1].equalsIgnoreCase("stonefists")) return GiveCustomBook(CustomEnchantsManager.STONEFISTS, level, player);
				if(args[1].equalsIgnoreCase("leaping")) return GiveCustomBook(CustomEnchantsManager.LEAPING, level, player);
				if(args[1].equalsIgnoreCase("owleyes")) return GiveCustomBook(CustomEnchantsManager.OWLEYES, level, player);
				if(args[1].equalsIgnoreCase("heavenslightness")) return GiveCustomBook(CustomEnchantsManager.HEAVENSLIGHTNESS, level, player);
				if(args[1].equalsIgnoreCase("thermalplating")) return GiveCustomBook(CustomEnchantsManager.THERMALPLATING, level, player);
				if(args[1].equalsIgnoreCase("lifesteal")) return GiveCustomBook(CustomEnchantsManager.LIFESTEAL, level, player);
				if(args[1].equalsIgnoreCase("icy")) return GiveCustomBook(CustomEnchantsManager.ICY, level, player);
				if(args[1].equalsIgnoreCase("deflect")) return GiveCustomBook(CustomEnchantsManager.DEFLECT, level, player);
				if(args[1].equalsIgnoreCase("lumberjack")) return GiveCustomBook(CustomEnchantsManager.LUMBERJACK, level, player);
				if(args[1].equalsIgnoreCase("veinmine")) return GiveCustomBook(CustomEnchantsManager.VEINMINE, level, player);
				if(args[1].equalsIgnoreCase("wide")) return GiveCustomBook(CustomEnchantsManager.WIDE, level, player);
				if(args[1].equalsIgnoreCase("gourmand")) return GiveCustomBook(CustomEnchantsManager.GOURMAND, level, player);
				if(args[1].equalsIgnoreCase("stepping")) return GiveCustomBook(CustomEnchantsManager.STEPPING, level, player);
				if(args[1].equalsIgnoreCase("feedingmodule")) return GiveCustomBook(CustomEnchantsManager.FEEDINGMODULE, level, player);
				if(args[1].equalsIgnoreCase("splitting")) return GiveCustomBook(CustomEnchantsManager.SPLITTING, level, player);
				if(args[1].equalsIgnoreCase("shadowstep")) return GiveCustomBook(CustomEnchantsManager.SHADOWSTEP, level, player);
				if(args[1].equalsIgnoreCase("thrusters")) return GiveCustomBook(CustomEnchantsManager.THRUSTERS, level, player);
				if(args[1].equalsIgnoreCase("soulbound")) return GiveCustomBook(CustomEnchantsManager.SOULBOUND, level, player);
				if(args[1].equalsIgnoreCase("replanting")) return GiveCustomBook(CustomEnchantsManager.REPLANTING, level, player);
				if(args[1].equalsIgnoreCase("oresight")) return GiveCustomBook(CustomEnchantsManager.ORESIGHT, level, player);
				if(args[1].equalsIgnoreCase("flamingfall")) return GiveCustomBook(CustomEnchantsManager.FLAMINGFALL, level, player);
				if(args[1].equalsIgnoreCase("soulboundtotem")) {
					player.getInventory().addItem(ExpandedEnchants.itemManager.SoulboundTotem(level));
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("hideenchants")) {
				if(!sender.hasPermission("ee.command.hideenchants")) {
					sender.sendMessage(ChatColor.RED + "You don't have permission for this command!");
					return true;
				}
				if(args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Please give a second argument!");
					return true;
				}
				if(!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) 
				{
					sender.sendMessage(ChatColor.RED + "Please use 'true' or 'false' as a second argument!");
					return true;
				}
				Player p = (Player) sender;
				boolean shouldHide = Boolean.parseBoolean(args[1]);
				if(p.getInventory().getItemInMainHand() == null) { sender.sendMessage(ChatColor.RED + "You are not holding an item!"); return true; }
				if(p.getInventory().getItemInMainHand().getItemMeta() == null) { sender.sendMessage(ChatColor.RED + "You are not holding a valid item!"); return true; }
				
				if(shouldHide) {
					for(Enchantment ench : p.getInventory().getItemInMainHand().getItemMeta().getEnchants().keySet()) {
						if(Functions.IsCustomEnchant(ench)) {
							ItemMeta newMeta = ExpandedEnchants.itemManager.RemoveCustomEnchantmentLore(p.getInventory().getItemInMainHand().getItemMeta(), ench);
							p.getInventory().getItemInMainHand().setItemMeta(newMeta);
						}
					}
					return true;
				}
				else
				{
					for(Enchantment ench : p.getInventory().getItemInMainHand().getItemMeta().getEnchants().keySet()) {
						if(Functions.IsCustomEnchant(ench)) {
							ItemMeta newMeta = ExpandedEnchants.itemManager.RemoveCustomEnchantmentLore(p.getInventory().getItemInMainHand().getItemMeta(), ench);
							newMeta = ExpandedEnchants.itemManager.AddCustomEnchantmentLore(newMeta, ench);
							p.getInventory().getItemInMainHand().setItemMeta(newMeta);
						}
					}
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("reload")) {
				if(!sender.hasPermission("ee.command.reload")) {
					sender.sendMessage(ChatColor.RED + "You don't have permission for this command!");
					return true;
				}
				ExpandedEnchants.getInstance().reloadConfig();
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "[EE]" +  ChatColor.WHITE + " Config reloaded!");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("enchant")) {
				if(args.length == 1) return false;
				int level = 1;
				if(args.length >= 3) level = Integer.parseInt(args[2]);
				if(args[1].equalsIgnoreCase("assassin")) return EnchantPlayerItem(CustomEnchantsManager.ASSASSIN, level, player, true, true);
				if(args[1].equalsIgnoreCase("unbreakable")) return EnchantPlayerItem(CustomEnchantsManager.NOBREAKABLE, level, player, true, true);
				if(args[1].equalsIgnoreCase("direct")) return EnchantPlayerItem(CustomEnchantsManager.DIRECT, level, player, true, true);
				if(args[1].equalsIgnoreCase("disarming")) return EnchantPlayerItem(CustomEnchantsManager.DISARMING, level, player, true, true);
				if(args[1].equalsIgnoreCase("disruption")) return EnchantPlayerItem(CustomEnchantsManager.DISRUPTION, level, player, true, true);
				if(args[1].equalsIgnoreCase("autosmelt")) return EnchantPlayerItem(CustomEnchantsManager.AUTOSMELT, level, player, true, true);
				if(args[1].equalsIgnoreCase("antigravity")) return EnchantPlayerItem(CustomEnchantsManager.ANTIGRAVITY, level, player, true, true);
				if(args[1].equalsIgnoreCase("beheading")) return EnchantPlayerItem(CustomEnchantsManager.BEHEADING, level, player, true, true);
				if(args[1].equalsIgnoreCase("expboost")) return EnchantPlayerItem(CustomEnchantsManager.EXP_BOOST, level, player, true, true);
				if(args[1].equalsIgnoreCase("traveler")) return EnchantPlayerItem(CustomEnchantsManager.TRAVELER, level, player, true, true);
				if(args[1].equalsIgnoreCase("gourmand")) return EnchantPlayerItem(CustomEnchantsManager.GOURMAND, level, player, true, true);
				if(args[1].equalsIgnoreCase("lavawalker")) return EnchantPlayerItem(CustomEnchantsManager.LAVA_WALKER, level, player, true, true);
				if(args[1].equalsIgnoreCase("elementalprotection")) return EnchantPlayerItem(CustomEnchantsManager.ELEMENTALPROTECTION, level, player, true, true);
				if(args[1].equalsIgnoreCase("healthboost")) return EnchantPlayerItem(CustomEnchantsManager.HEALTHBOOST, level, player, true, true);
				if(args[1].equalsIgnoreCase("stonefists")) return EnchantPlayerItem(CustomEnchantsManager.STONEFISTS, level, player, true, true);
				if(args[1].equalsIgnoreCase("leaping")) return EnchantPlayerItem(CustomEnchantsManager.LEAPING, level, player, true, true);
				if(args[1].equalsIgnoreCase("owleyes")) return EnchantPlayerItem(CustomEnchantsManager.OWLEYES, level, player, true, true);
				if(args[1].equalsIgnoreCase("heavenslightness")) return EnchantPlayerItem(CustomEnchantsManager.HEAVENSLIGHTNESS, level, player, true, true);
				if(args[1].equalsIgnoreCase("thermalplating")) return EnchantPlayerItem(CustomEnchantsManager.THERMALPLATING, level, player, true, true);
				if(args[1].equalsIgnoreCase("lifesteal")) return EnchantPlayerItem(CustomEnchantsManager.LIFESTEAL, level, player, true, true);
				if(args[1].equalsIgnoreCase("icy")) return EnchantPlayerItem(CustomEnchantsManager.ICY, level, player, true, true);
				if(args[1].equalsIgnoreCase("deflect")) return EnchantPlayerItem(CustomEnchantsManager.DEFLECT, level, player, true, true);
				if(args[1].equalsIgnoreCase("lumberjack")) return EnchantPlayerItem(CustomEnchantsManager.LUMBERJACK, level, player, true, true);
				if(args[1].equalsIgnoreCase("veinmine")) return EnchantPlayerItem(CustomEnchantsManager.VEINMINE, level, player, true, true);
				if(args[1].equalsIgnoreCase("stepping")) return EnchantPlayerItem(CustomEnchantsManager.STEPPING, level, player, true, true);
				if(args[1].equalsIgnoreCase("wide")) return EnchantPlayerItem(CustomEnchantsManager.WIDE, level, player, true, true);
				if(args[1].equalsIgnoreCase("thrusters")) return EnchantPlayerItem(CustomEnchantsManager.THRUSTERS, level, player, true, true);
				if(args[1].equalsIgnoreCase("feedingmodule")) return EnchantPlayerItem(CustomEnchantsManager.FEEDINGMODULE, level, player, true, true);
				if(args[1].equalsIgnoreCase("soulbound")) return EnchantPlayerItem(CustomEnchantsManager.SOULBOUND, level, player, true, true);
				if(args[1].equalsIgnoreCase("splitting")) return EnchantPlayerItem(CustomEnchantsManager.SPLITTING, level, player, true, true);
				if(args[1].equalsIgnoreCase("shadowstep")) return EnchantPlayerItem(CustomEnchantsManager.SHADOWSTEP, level, player, true, true);
				if(args[1].equalsIgnoreCase("replanting")) return EnchantPlayerItem(CustomEnchantsManager.REPLANTING, level, player, true, true);
				if(args[1].equalsIgnoreCase("oresight")) return EnchantPlayerItem(CustomEnchantsManager.ORESIGHT, level, player, true, true);
				if(args[1].equalsIgnoreCase("flamingfall")) return EnchantPlayerItem(CustomEnchantsManager.FLAMINGFALL, level, player, true, true);
			}
			return false;
	}

	@SuppressWarnings("deprecation")
	public boolean EnchantPlayerItem(Enchantment ench, int level, Player sender, boolean sendMessage, boolean needsPermissions) {
		if(needsPermissions) {
			if(!sender.hasPermission("ee.command.enchant")) {
				sender.sendMessage(ChatColor.RED + LanguageManager.instance.GetTranslatedValue("no-permission"));
				return true;
			}
			if(sender.getInventory().getItemInMainHand() == null) {
				sender.sendMessage(ChatColor.RED + LanguageManager.instance.GetTranslatedValue("no-holding-item"));
				return true;
			}
			if(sender.getInventory().getItemInMainHand().getItemMeta() == null) {
				sender.sendMessage(ChatColor.RED + LanguageManager.instance.GetTranslatedValue("not-holding-valid-item"));
				return true;
			}
		}
		ItemStack fs = sender.getInventory().getItemInMainHand();
		if(fs.getItemMeta().hasEnchant(ench)) {
			if(fs.getItemMeta().getEnchantLevel(ench) > level) {
				sender.sendMessage(ChatColor.RED + LanguageManager.instance.GetTranslatedValue("contains-higher-level"));
				return true;
			}
			if(ench.getMaxLevel() == 1) {
				sender.sendMessage(ChatColor.RED + LanguageManager.instance.GetTranslatedValue("already-contains"));
				return true;
			}
			ItemStack resultItem = new ItemStack(fs);
			int addLevel = fs.getItemMeta().getEnchantLevel(ench) == level ? level + 1 : level;
			if(addLevel > ench.getMaxLevel()) {
				if(sendMessage) sender.sendMessage(ChatColor.RED + LanguageManager.instance.GetTranslatedValue("already-max-level"));
				return true;
			}
			resultItem.addUnsafeEnchantment(ench, addLevel);
			ItemMeta meta = resultItem.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if(meta.hasLore()) lore = meta.getLore();
			int i = 0;
			int num = -1;
			for(String line : lore) {
				if(line.contains(ench.getName())) {
					num = i;
				}
				i++;
			}
			lore.remove(num);
			lore.add(ChatColor.GRAY + ench.getName() + " " + Functions.GetNameByLevel(resultItem.getItemMeta().getEnchantLevel(ench), ench.getMaxLevel()));
			meta.setLore(lore);
			resultItem.setItemMeta(meta);
			if(resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.TRAVELER)) {
				ItemMeta itemMeta = resultItem.getItemMeta();
				itemMeta.removeAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED);
				itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
				itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
				itemMeta.removeAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE);

				if(Functions.GetArmorPoints(resultItem.getType()) > 0)
					itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", Functions.GetArmorPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
				if(Functions.GetToughnessPoints(resultItem.getType()) > 0)
					itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", Functions.GetToughnessPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
				if(Functions.GetKnockbackPoints(resultItem.getType()) > 0)
					itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "generic.knockbackResistance", Functions.GetKnockbackPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
				
				itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", itemMeta.getEnchantLevel(CustomEnchantsManager.TRAVELER) * .4, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlot.LEGS));
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				resultItem.setItemMeta(itemMeta);
			}
			if(resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.STONEFISTS)) {
				ItemMeta itemMeta = resultItem.getItemMeta();
				itemMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
				itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
				itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
				itemMeta.removeAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
				if(Functions.GetArmorPoints(resultItem.getType()) > 0)
					itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", Functions.GetArmorPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				if(Functions.GetToughnessPoints(resultItem.getType()) > 0)
					itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", Functions.GetToughnessPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				if(Functions.GetKnockbackPoints(resultItem.getType()) > 0)
					itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "generic.knockbackResistance", Functions.GetKnockbackPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				
				itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", itemMeta.getEnchantLevel(CustomEnchantsManager.STONEFISTS) * 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				resultItem.setItemMeta(itemMeta);
			}
			if(resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.HEALTHBOOST)) {
				ItemMeta itemMeta = resultItem.getItemMeta();
				itemMeta.removeAttributeModifier(Attribute.GENERIC_MAX_HEALTH);
				itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
				itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
				itemMeta.removeAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
				if(Functions.GetArmorPoints(resultItem.getType()) > 0)
					itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", Functions.GetArmorPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				if(Functions.GetToughnessPoints(resultItem.getType()) > 0)
					itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", Functions.GetToughnessPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				if(Functions.GetKnockbackPoints(resultItem.getType()) > 0)
					itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "generic.knockbackResistance", Functions.GetKnockbackPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				
				itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", itemMeta.getEnchantLevel(CustomEnchantsManager.HEALTHBOOST) * 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				resultItem.setItemMeta(itemMeta);
			}
			sender.getInventory().setItemInMainHand(resultItem);
			if(sendMessage) sender.sendMessage(ChatColor.WHITE + LanguageManager.instance.GetTranslatedValue("no-permission").replace("{enchant}", ChatColor.LIGHT_PURPLE + ench.getName() + (ench.getMaxLevel() > 1 ? " " : "") + Functions.GetNameByLevel(level, ench.getMaxLevel())));
			return true;
			
		}
		if(!ench.canEnchantItem(fs)) {
			sender.sendMessage(ChatColor.RED + LanguageManager.instance.GetTranslatedValue("cant-apply"));
			return true;
		}
		if(fs.getItemMeta().hasConflictingEnchant(ench)) {
			sender.sendMessage(ChatColor.RED + LanguageManager.instance.GetTranslatedValue("contains-conflict"));
			return true;
		}

		ItemStack resultItem = new ItemStack(fs);
		resultItem.addUnsafeEnchantment(ench, level);
		ItemMeta meta = resultItem.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if(meta.hasLore()) lore = meta.getLore();
		lore.add(ChatColor.GRAY + ench.getName() + " " + Functions.GetNameByLevel(resultItem.getItemMeta().getEnchantLevel(ench), ench.getMaxLevel()));
		meta.setLore(lore);
		resultItem.setItemMeta(meta);
		if(resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.NOBREAKABLE)) {
			resultItem.setDurability((short) 100000);
			ItemMeta itemMeta = resultItem.getItemMeta();
			itemMeta.setUnbreakable(true);
			resultItem.setItemMeta(itemMeta);
			resultItem.removeEnchantment(Enchantment.DURABILITY);
			resultItem.removeEnchantment(Enchantment.MENDING);
		}
		if(resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.TRAVELER)) {
			ItemMeta itemMeta = resultItem.getItemMeta();
			itemMeta.removeAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED);
			itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
			itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
			itemMeta.removeAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE);

			if(Functions.GetArmorPoints(resultItem.getType()) > 0)
				itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", Functions.GetArmorPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
			if(Functions.GetToughnessPoints(resultItem.getType()) > 0)
				itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", Functions.GetToughnessPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
			if(Functions.GetKnockbackPoints(resultItem.getType()) > 0)
				itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "generic.knockbackResistance", Functions.GetKnockbackPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
			
			itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", itemMeta.getEnchantLevel(CustomEnchantsManager.TRAVELER) * .4, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlot.LEGS));
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			resultItem.setItemMeta(itemMeta);
		}
		if(resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.STONEFISTS)) {
			ItemMeta itemMeta = resultItem.getItemMeta();
			itemMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
			itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
			itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
			itemMeta.removeAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
			if(Functions.GetArmorPoints(resultItem.getType()) > 0)
				itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", Functions.GetArmorPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			if(Functions.GetToughnessPoints(resultItem.getType()) > 0)
				itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", Functions.GetToughnessPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			if(Functions.GetKnockbackPoints(resultItem.getType()) > 0)
				itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "generic.knockbackResistance", Functions.GetKnockbackPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", itemMeta.getEnchantLevel(CustomEnchantsManager.STONEFISTS) * 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			resultItem.setItemMeta(itemMeta);
		}
		if(resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.HEALTHBOOST)) {
			ItemMeta itemMeta = resultItem.getItemMeta();
			itemMeta.removeAttributeModifier(Attribute.GENERIC_MAX_HEALTH);
			itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
			itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
			itemMeta.removeAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
			if(Functions.GetArmorPoints(resultItem.getType()) > 0)
				itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", Functions.GetArmorPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			if(Functions.GetToughnessPoints(resultItem.getType()) > 0)
				itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", Functions.GetToughnessPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			if(Functions.GetKnockbackPoints(resultItem.getType()) > 0)
				itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "generic.knockbackResistance", Functions.GetKnockbackPoints(resultItem.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			
			itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", itemMeta.getEnchantLevel(CustomEnchantsManager.HEALTHBOOST) * 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			resultItem.setItemMeta(itemMeta);
		}
		
		sender.getInventory().setItemInMainHand(resultItem);
		if(sendMessage) sender.sendMessage(ChatColor.WHITE + LanguageManager.instance.GetTranslatedValue("enchanted-with").replace("{enchant}", ChatColor.LIGHT_PURPLE + ench.getName() + (ench.getMaxLevel() > 1 ? " " : "") + Functions.GetNameByLevel(level, ench.getMaxLevel()) + ChatColor.WHITE));
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean GiveCustomBook(Enchantment ench, int level, Player sender) {
		if(!sender.hasPermission("ee.command.give")) {
			sender.sendMessage(ChatColor.RED + LanguageManager.instance.GetTranslatedValue("no-permission"));
			return true;
		}
		ItemStack item = ExpandedEnchants.itemManager.CreateCustomBook(ench, level);
		if(level > ench.getMaxLevel()) return false;
		if(level < 1) return false;
		sender.getInventory().addItem(item);
		
		sender.sendMessage(ChatColor.WHITE + LanguageManager.instance.GetTranslatedValue("gave-book").replace("{enchant}", ChatColor.LIGHT_PURPLE + ench.getName() + (ench.getMaxLevel() > 1 ? " " : "") + Functions.GetNameByLevel(level, ench.getMaxLevel()) + ChatColor.WHITE));
		return true;
	}
}
