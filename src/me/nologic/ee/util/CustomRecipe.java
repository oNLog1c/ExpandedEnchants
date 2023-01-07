package me.nologic.ee.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.nologic.ee.ExpandedEnchants;

public class CustomRecipe
{
	public ShapedRecipe recipe;
	public Integer[] amounts;
	public ItemStack[] items;
	
	public CustomRecipe(ShapedRecipe _recipe, Integer[] _amounts) {
		
		recipe = _recipe;
		amounts = _amounts;
		if(recipe != null && amounts != null) Init();
	}
	
	public void Init() {
		String[] map = recipe.getShape();
		char[] line1 = map[0].toCharArray();
		char[] line2 = map[1].toCharArray();
		char[] line3 = map[2].toCharArray();
		ItemStack[] itemlist = new ItemStack[9];
		for(int i = 0; i < 3; i++) {
			if(line1[i] == ' ') {
				itemlist[i] = null;
				continue;
			}
			ItemStack topItem = recipe.getIngredientMap().get(line1[i]);
			if(topItem != null) {
			if(topItem.getType() == Material.POTION || topItem.getType() == Material.LINGERING_POTION || topItem.getType() == Material.SPLASH_POTION || topItem.getType() == Material.ENCHANTED_BOOK) {
				itemlist[i] = ExpandedEnchants.itemManager.GetSpecialCraftItem(recipe);
				itemlist[i].setAmount(amounts[i]);
			}
			else itemlist[i] = topItem;
			if(itemlist[i] != null) itemlist[i].setAmount(amounts[i]);
			}
		}
		for(int i = 0; i < 3; i++) {
			if(line2[i] == ' ') {
				itemlist[i + 3] = null;
				continue;
			}
			itemlist[i + 3] = recipe.getIngredientMap().get(line2[i]);
			if(itemlist[i + 3] != null) itemlist[i + 3].setAmount(amounts[i + 3]);
		}
		for(int i = 0; i < 3; i++) {
			if(line3[i] == ' ') {
				itemlist[i + 6] = null;
				continue;
			}
			itemlist[i + 6] = recipe.getIngredientMap().get(line3[i]);
			if(itemlist[i + 6] != null) itemlist[i + 6].setAmount(amounts[i + 6]);
		}
		items = itemlist;
	}
	
}
