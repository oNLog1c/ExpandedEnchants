package me.nologic.ee.enchant;

import me.nologic.ee.util.Functions;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class DirectEnchantment extends Enchantment
{
	
	public final String name;
	public final int maxLvl;

	public DirectEnchantment(String namespace, String name, int lvl)
	{
		super(NamespacedKey.minecraft(namespace));
		// TODO Auto-generated constructor stub
		this.name = name;
		this.maxLvl = lvl;
	}

	@Override
	public boolean canEnchantItem(ItemStack arg0)
	{
		if(arg0.getType() == Material.SHEARS) return true;
		if(arg0.getType() == Material.TRIDENT) return true;
		if(arg0.getType() == Material.CROSSBOW) return true;
		if(arg0.getType() == Material.BOW) return true;
		if(Functions.CheckToolsTypes(arg0)) return true;
		if(Functions.CheckSwordTypes(arg0)) return true;
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget()
	{
		return null;
	}

	@Override
	public int getMaxLevel()
	{
		// TODO Auto-generated method stub
		return maxLvl;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public int getStartLevel()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCursed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTreasure()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
