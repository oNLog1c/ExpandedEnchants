package me.nologic.ee.gui;

import java.util.ArrayList;
import java.util.List;

import me.nologic.ee.EnchantmentInformation;
import me.nologic.ee.ExpandedEnchants;
import me.nologic.ee.util.CustomRecipe;
import me.nologic.ee.util.Functions;
import me.nologic.ee.util.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class CustomItemRecipeInventory implements InventoryHolder {

	public Inventory inv;
	
	public CustomItemRecipeInventory (CustomRecipe recipe) {
		inv = Bukkit.createInventory(null, 54, "§5§l" + recipe.recipe.getResult().getItemMeta().getDisplayName());
		ChangeRecipe(recipe);
	}

	public void ChangeRecipe(CustomRecipe recipe) {
		ItemStack bench = new ItemStack(Material.CRAFTING_TABLE);
		inv.setItem(27, bench);
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner("MHF_ArrowLeft");
		meta.setDisplayName("§e§l" + LanguageManager.instance.GetTranslatedValue("inventory-back"));
		item.setItemMeta(meta);
		inv.setItem(0, item);
		ItemStack itemResult = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta metaResult = (SkullMeta) itemResult.getItemMeta();
		metaResult.setOwner("MHF_ArrowRight");
		metaResult.setDisplayName("§e§l" + LanguageManager.instance.GetTranslatedValue("inventory-result"));
		itemResult.setItemMeta(metaResult);
		inv.setItem(32, itemResult);
		ItemStack result = null;
		if(recipe.recipe != null) {
			ItemStack desc = new ItemStack(Material.PAPER, 1);
			ItemMeta descmeta = desc.getItemMeta();
			descmeta.setDisplayName("§b§l" + LanguageManager.instance.GetTranslatedValue("inventory-description"));
			List<String> desclore = new ArrayList<>();
			for(String descString : EnchantmentInformation.itemDescriptions[Functions.GetItemIndex(recipe.recipe, ExpandedEnchants.customItems)].stringList) {
				desclore.add("§f" + descString);
			}
			descmeta.setLore(desclore);
			desc.setItemMeta(descmeta);
			inv.setItem(2, desc);
			
			for(int i = 0; i < 9; i++) {
				if(recipe.items[i] == null) {
					ItemStack noItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
					ItemMeta noMeta = noItem.getItemMeta();
					noMeta.setDisplayName("§r");
					noItem.setItemMeta(noMeta);
					inv.setItem(Functions.GetSlot(i), noItem);					
					continue;
				}
				ItemStack topItem = new ItemStack(Material.AIR);
				if (i == 1) topItem = recipe.items[i];
				if(topItem.getType() == Material.POTION || topItem.getType() == Material.LINGERING_POTION || topItem.getType() == Material.SPLASH_POTION || topItem.getType() == Material.ENCHANTED_BOOK) {
					inv.setItem(Functions.GetSlot(i), ExpandedEnchants.itemManager.GetSpecialCraftItem(recipe.recipe));
				}
				else { 
					ItemStack craftItem = new ItemStack(recipe.items[i]);
					inv.setItem(Functions.GetSlot(i), craftItem);
				}
				
			}
			result = new ItemStack(recipe.recipe.getResult());
		}
		else {
			inv.setItem(21, new ItemStack(Material.EMERALD));
			inv.setItem(30, new ItemStack(Material.BOOK));
			result = new ItemStack(Material.ENCHANTED_BOOK);
		}
		inv.setItem(33, result);
		
	}
	
	@Override
	public Inventory getInventory()
	{
		return inv;
	}
}
