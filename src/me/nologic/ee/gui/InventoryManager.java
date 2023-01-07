package me.nologic.ee.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.nologic.ee.ExpandedEnchants;
import me.nologic.ee.util.CustomRecipe;
import me.nologic.ee.util.Functions;
import me.nologic.ee.util.LanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryManager implements Listener {

	public Inventory startInv;
	public Inventory vanillaChoose;
	public Inventory customChoose;
	public Map<UUID, CustomRecipeInventory> customRecipeInv = new HashMap<>();
	public Map<UUID, VanillaRecipeInventory> vanillaRecipeInv = new HashMap<>();

	public InventoryManager() {
		startInv = Bukkit.createInventory(null, 27, "§5§l" + LanguageManager.instance.GetTranslatedValue("inventory-title-recipes"));
		ItemStack vanillaItem = new ItemStack(Material.ENCHANTED_BOOK, 1);
		ItemStack customItem = new ItemStack(Material.ENCHANTED_BOOK, 1);
		ItemMeta vanillaMeta = vanillaItem.getItemMeta();
		ItemMeta customMeta = customItem.getItemMeta();
		vanillaMeta.setDisplayName("§b§l" + LanguageManager.instance.GetTranslatedValue("inventory-title-vanilla"));
		customMeta.setDisplayName("§b§l" + LanguageManager.instance.GetTranslatedValue("inventory-title-custom"));
		vanillaItem.setItemMeta(vanillaMeta);
		customItem.setItemMeta(customMeta);
		startInv.setItem(12, vanillaItem);
		startInv.setItem(14, customItem);

		vanillaChoose = Bukkit.createInventory(null, 54, "§5§l" + LanguageManager.instance.GetTranslatedValue("inventory-title-choose"));
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner("MHF_ArrowLeft");
		meta.setDisplayName("§e§l" + LanguageManager.instance.GetTranslatedValue("inventory-back"));
		item.setItemMeta(meta);
		vanillaChoose.setItem(0, item);

		customChoose = Bukkit.createInventory(null, 45, "§5§l" + LanguageManager.instance.GetTranslatedValue("inventory-title-choose"));
		customChoose.setItem(0, item);
		ItemStack enchBook = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta enchMeta = enchBook.getItemMeta();
		List<String> lore = new ArrayList<>();
		for (String string : LanguageManager.instance.GetTranslatedList("inventory-lore-empty-book"))
			lore.add("§f" + string);
		enchMeta.setLore(lore);
		enchBook.setItemMeta(enchMeta);
		customChoose.setItem(8, enchBook);

		for (int i = 9; i < 47; i++) {
			ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
			EnchantmentStorageMeta bookmeta = (EnchantmentStorageMeta) book.getItemMeta();
			bookmeta.addStoredEnchant(ExpandedEnchants.recipeManager.recipes[i - 9].ench, 1, true);
			book.setItemMeta(bookmeta);
			vanillaChoose.setItem(i, book);
		}
		for (int i = 0; i < Functions.GetEnabledEnchants().size(); i++) {
			ItemStack book = ExpandedEnchants.itemManager.CreateCustomBook(Functions.GetEnabledEnchants().get(i), 1);
			customChoose.setItem(i + 9, book);
		}

	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (e.getClickedInventory() == null) return;
		Inventory topInv = player.getOpenInventory().getTopInventory();
		if (topInv.equals(startInv) || topInv.equals(vanillaChoose) || topInv.equals(customChoose)) {
			e.setCancelled(true);
		}
		if (vanillaRecipeInv.containsKey(player.getUniqueId()))
			if (player.getOpenInventory().getTopInventory().equals(vanillaRecipeInv.get(player.getUniqueId()).inv))
				e.setCancelled(true);
		if (customRecipeInv.containsKey(player.getUniqueId()))
			if (player.getOpenInventory().getTopInventory().equals(customRecipeInv.get(player.getUniqueId()).inv))
				e.setCancelled(true);
		if (e.getClickedInventory().equals(startInv)) {
			if (e.getSlot() == 12) {
				player.closeInventory();
				player.openInventory(vanillaChoose);
			}
			if (e.getSlot() == 14) {
				player.closeInventory();
				player.openInventory(customChoose);
			}
		}

		if (e.getClickedInventory().equals(vanillaChoose)) {
			if (e.getSlot() == 0) {
				player.closeInventory();
				player.openInventory(startInv);
				return;
			}
			if (e.getCurrentItem() != null) {
				ItemStack item = e.getCurrentItem();
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
				Enchantment enchantment = (Enchantment) meta.getStoredEnchants().keySet().toArray()[0];
				String name = Functions.GetEnchantmentName(enchantment);
				player.closeInventory();
				vanillaRecipeInv.put(player.getUniqueId(), new VanillaRecipeInventory(ExpandedEnchants.recipeManager.GetRecipe(enchantment), "§5§l" + name));
				player.openInventory(vanillaRecipeInv.get(player.getUniqueId()).inv);
			}
		}

		if (e.getClickedInventory().equals(customChoose)) {
			if (e.getSlot() == 0) {
				player.closeInventory();
				player.openInventory(startInv);
				return;
			}
			if (e.getCurrentItem() != null) {
				if (e.getSlot() == 8) {
					player.closeInventory();
					customRecipeInv.put(player.getUniqueId(), new CustomRecipeInventory(new CustomRecipe(null, null)));
					player.openInventory(customRecipeInv.get(player.getUniqueId()).inv);
				} else {
					ItemStack item = e.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					Enchantment ench = (Enchantment) meta.getEnchants().keySet().toArray()[0];
					player.closeInventory();
					customRecipeInv.put(player.getUniqueId(), new CustomRecipeInventory(ExpandedEnchants.GetCustomEnchantmentRecipe(ench)));
					player.openInventory(customRecipeInv.get(player.getUniqueId()).inv);
				}
			}
		}
		if (vanillaRecipeInv.containsKey(player.getUniqueId())) {
			if (e.getClickedInventory().equals(vanillaRecipeInv.get(player.getUniqueId()).inv)) {
				if (e.getSlot() == 0) {
					player.closeInventory();
					player.openInventory(vanillaChoose);
				}
			}
		}
		if (customRecipeInv.containsKey(player.getUniqueId())) {
			if (e.getClickedInventory().equals(customRecipeInv.get(player.getUniqueId()).inv)) {
				if (e.getSlot() == 0) {
					player.closeInventory();
					player.openInventory(customChoose);
				}
			}
		}
	}

}