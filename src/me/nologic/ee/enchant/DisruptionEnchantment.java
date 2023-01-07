package me.nologic.ee.enchant;

import me.nologic.ee.util.Functions;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class DisruptionEnchantment extends Enchantment
{
	
	public final String name;
	public final int maxLvl;

	public DisruptionEnchantment(String namespace, String name, int lvl)
	{
		super(NamespacedKey.minecraft(namespace));
		this.name = name;
		this.maxLvl = lvl;
	}

	@Override
	public boolean canEnchantItem(ItemStack arg0)
	{
		if(Functions.CheckSwordTypes(arg0)) return true;
		if(Functions.CheckAxeTypes(arg0)) return true;
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment arg0)
	{
		if(arg0.equals(Enchantment.DAMAGE_ARTHROPODS)) return true;
		if(arg0.equals(Enchantment.DAMAGE_ALL)) return true;
		if(arg0.equals(Enchantment.DAMAGE_UNDEAD)) return true;
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
		return maxLvl;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int getStartLevel()
	{
		return 0;
	}

	@Override
	public boolean isCursed()
	{
		return false;
	}

	@Override
	public boolean isTreasure()
	{
		return false;
	}
	
	
	
}
