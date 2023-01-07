package me.nologic.ee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import me.nologic.ee.util.AssassinInfo;
import me.nologic.ee.util.CustomRecipe;
import me.nologic.ee.util.Functions;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.loot.LootTables;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import me.nologic.ee.util.LanguageManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EnchantmentHandler implements Listener {

	public Functions functions = new Functions();
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("SoulboundEnabled"))
			HandleSoulboundEnchant(e);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("SoulboundTotemEnabled")) HandleSoulboundTotem(e);
	}

	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("SoulboundEnabled"))
			HandleSoulboundRespawn(e);
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("DeflectEnabled"))
			HandleDeflectEnchant(e);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("ElementalprotectionEnabled"))
			HandleElementalProtectionEnchant(e);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("FlamingfallEnabled"))
			HandleFlamingFallEnchant(e);
	}

	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("AntigravityEnabled"))
			HandleAntiGravityEnchant(e);
	}

	@EventHandler()
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("LumberjackEnabled"))
			HandleLumberjackEnchant(event);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("VeinmineEnabled"))
			HandleVeinmineEnchant(event);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("WideEnabled"))
			HandleWideEnchant(event);
		CheckForShulker(event);
	}

	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent event)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("BeheadingEnabled"))
			HandleBeheadingEnchant(event);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("DirectEnabled"))
			HandleDirectEntityEnchant(event);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("LifestealEnabled"))
			HandleLifestealEnchant(event);
	}

	@EventHandler
	public void onBlockDropItemEvent(BlockDropItemEvent event)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("AutosmeltEnabled"))
			HandleAutoSmeltEnchant(event);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("DirectEnabled"))
			HandleDirectBlockEnchant(event);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("ReplantingEnabled"))
			CheckReplantingItemDrop(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPrepareAnvilEvent(PrepareAnvilEvent event)
	{
		if (event.getInventory() == null)
			return;
		ItemStack fs = event.getInventory().getItem(0);
		ItemStack ss = event.getInventory().getItem(1);
		if (fs == null)
			return;
		for (HumanEntity he : event.getViewers()) {
			isModified.put(he.getUniqueId(), false);
			removeLastEnchant.put(he.getUniqueId(), null);
		}
		if(ss == null) {
			HandleCustomRenaming(fs, event);
			return;
		}
		event.getInventory().setRepairCost(5);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("AllowResourceEnchant"))
			HandleResourceEnchants(fs, ss, event);
		HandleSameItemUpgrades(fs, ss, event);
		HandleCustomEnchantBooks(fs, ss, event);
		HandleBookMerge(fs, ss, event);
		HandleBookEnchant(fs, ss, event);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("AllowResourceEnchant"))
			HandleBookResourceEnchant(fs, ss, event);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("AllowDisenchanting"))
			HandleDisenchant(fs, ss, event);
	}

	@EventHandler
	public void onPlayerExpChangeEvent(PlayerExpChangeEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("ExperienceboostEnabled"))
			HandleExpBoostEnchant(e);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMoveEvent(PlayerMoveEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("LavawalkerEnabled"))
			HandleLavaWalkerEnchant(e);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("SteppingEnabled"))
			HandleSteppingEnchant(e);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("IcyEnabled"))
			HandleIcyEnchant(e);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("DisruptionEnabled")) HandleDisruptEnchant(e);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("AssassinEnabled")) HandlePlayerAttackEntity(e);
	}

	@EventHandler
	public void onPrepareItemCraftEvent(PrepareItemCraftEvent e)
	{
		CheckForCustomRecipes(e);
	}

	@EventHandler
	public void onCraftItemEvent(CraftItemEvent e)
	{
		HandleCraftedItem(e);
		CheckForDiscoverRecipes(e);
	}

	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("FeedingmoduleEnabled"))
			HandleFeedingModuleEnchant(e);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("GourmandEnabled"))
			HandleGourmandEnchant(e);
	}

	@EventHandler
	public void onEntityTargetEvent(EntityTargetEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("ShadowstepEnabled"))
			HandleShadowStepEnchant(e);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("AssassinEnabled")) HandleAssassinMobTarget(e);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e)
	{
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("ReplantingEnabled"))
			CheckReplantingEnchant(e);
		if (ExpandedEnchants.getInstance().getConfig().getBoolean("WideEnabled"))
			HandleWideSpecialActionEnchant(e);
	}

	@EventHandler
	public void onEntitySpawnEvent(EntitySpawnEvent e)
	{
		CheckWanderingTrader(e);
	}

	@EventHandler
	public void onLootGenerateEvent(LootGenerateEvent e) {
		CheckEndCityLoot(e);
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("DisarmingEnabled")) HandleDisarmingEnchant(e);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("SplittingEnabled")) HandleSplitEnchant(e);
	}
	
	@EventHandler
	public void onEntityTeleportEvent(EntityTeleportEvent e) {
		CheckPreventTeleport(e);
	}
	
	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("AssassinEnabled")) HandleAssassinEnchant(e);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("OresightEnabled")) HandleOresightEnchant(e);
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("ThrustersEnabled")) HandleThrustersEnchant(e);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(ExpandedEnchants.getInstance().getConfig().getBoolean("AssassinEnabled")) HandleAssassinJoinEvent(e);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPrepareItemEnchantEvent(PrepareItemEnchantEvent e) {
		List<Enchantment> possible = new ArrayList<>();
		for(Enchantment ench : Functions.GetEnabledEnchants()) {
			if(ench.canEnchantItem(e.getItem())) {
				possible.add(ench);
			}
		}
		if(possible.size() < 1) return;
		if(Functions.GetRandomNumber(1, 100) > ExpandedEnchants.getInstance().getConfig().getInt("EnchantingTableChance")) return;
		if(e.getOffers()[2] == null) return;
		e.getOffers()[2].setCost(40);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEnchantItemEvent(EnchantItemEvent e) {
		if(e.getExpLevelCost() != 40) return;
		List<Enchantment> possible = new ArrayList<>();
		for(Enchantment ench : Functions.GetEnabledEnchants()) {
			if(ench.canEnchantItem(e.getItem())) {
				possible.add(ench);
			}
		}
		if(possible.size() < 1) return;
		Enchantment en = possible.get(Functions.GetRandomNumber(0, possible.size() - 1));
		ItemStack item = e.getItem();

		Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
		{		
			item.addUnsafeEnchantment(en, 1);
			
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + en.getName() + " " + Functions.GetNameByLevel(item.getEnchantmentLevel(en), en.getMaxLevel()));
			meta.setLore(lore);
			item.setItemMeta(meta);
		});
	}
	
	@EventHandler
	public void onVillagerAcquireTradeEvent(VillagerAcquireTradeEvent e) {
		HandleNewVillagerTrade(e);
	}
	
	public void HandleSoulboundTotem(PlayerDeathEvent e) {

		ItemStack offhand = e.getEntity().getInventory().getItemInOffHand();
		ItemStack mainhand = e.getEntity().getInventory().getItemInMainHand();
		
		if(!offhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(1)) && !offhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(2)) && !mainhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(1)) && ! mainhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(2))) return;
		if(offhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(1)) || offhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(2))) {
			e.setKeepInventory(true);
			e.getDrops().clear();
			
			e.getEntity().playEffect(EntityEffect.TOTEM_RESURRECT);
			if(offhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(2)) && ExpandedEnchants.getInstance().getConfig().getBoolean("SoulboundTotemLevel2Enabled")) {
				e.setDroppedExp(0);
				e.setKeepLevel(true);
			}
			offhand.setAmount(offhand.getAmount() - 1);
		}
		else if(mainhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(1)) || mainhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(2))) {
			e.setKeepInventory(true);
			e.getDrops().clear();
			e.getEntity().playEffect(EntityEffect.TOTEM_RESURRECT);
			if(mainhand.equals(ExpandedEnchants.itemManager.SoulboundTotem(2)) && ExpandedEnchants.getInstance().getConfig().getBoolean("SoulboundTotemLevel2Enabled")) {
				e.setDroppedExp(0);
				e.setKeepLevel(true);
			}
			mainhand.setAmount(mainhand.getAmount() - 1);
		}
	}
	
	public List<UUID> nonSplitArrows = new ArrayList<>();
	public void HandleSplitEnchant(ProjectileHitEvent e) {
		if(!(e.getEntity() instanceof AbstractArrow)) return;
		if(!(e.getEntity().getShooter() instanceof Player)) return;
		AbstractArrow arrow = (AbstractArrow) e.getEntity();
		if(nonSplitArrows.contains(arrow.getUniqueId())) return;
		Player player = (Player) e.getEntity().getShooter();
		if(e.getHitEntity() == null) return;
		
		if(player.getInventory().getItemInMainHand().getType() != Material.BOW) return;
		if(!player.getInventory().getItemInMainHand().hasItemMeta()) return;
		if(!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.SPLITTING)) return;
		int arrowAmount = player.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(CustomEnchantsManager.SPLITTING) + 3;
		List<Entity> entities = e.getHitEntity().getNearbyEntities((arrowAmount + 3), (arrowAmount + 3), (arrowAmount + 3));
		LivingEntity hitEntity = (LivingEntity) e.getHitEntity();
		List<LivingEntity> livingEntities = new ArrayList<>();
		for(Entity ent : entities) {
			if(ent.equals(player)) continue;
			if(ent instanceof LivingEntity) livingEntities.add((LivingEntity) ent);
		}
		
		if(livingEntities.size() == 0) return;
		for(int i = 0; i < (livingEntities.size() < arrowAmount ? livingEntities.size() : arrowAmount); i++) {
			Entity ent = livingEntities.get(i);
			if(!hitEntity.hasLineOfSight(ent)) continue;
			Projectile newArrow = player.launchProjectile(arrow.getClass());
			newArrow.setBounce(false);
			newArrow.setGlowing(true);
			newArrow.teleport(new Location(hitEntity.getWorld(), hitEntity.getLocation().getX(), hitEntity.getLocation().getY() + hitEntity.getEyeHeight(), hitEntity.getLocation().getZ()), TeleportCause.PLUGIN);
			Vector direction = new Vector(ent.getLocation().getX() - newArrow.getLocation().getX(), ent.getLocation().getY() - newArrow.getLocation().getY() + ent.getHeight(), ent.getLocation().getZ() - newArrow.getLocation().getZ());
			newArrow.setGravity(false);
			newArrow.setVelocity(direction.normalize().multiply(0.5));
			AbstractArrow newArr = (AbstractArrow) newArrow;
			newArr.setPickupStatus(PickupStatus.DISALLOWED);
			nonSplitArrows.add(newArr.getUniqueId());
			Bukkit.getScheduler().runTaskLater(ExpandedEnchants.getInstance(), () -> {
				newArrow.remove();
				nonSplitArrows.remove(newArrow.getUniqueId());
			}, 100);
		}
		
	}
	
	public void HandleNewVillagerTrade(VillagerAcquireTradeEvent e) {
		if (Functions.GetRandomNumber(0, 100) > ExpandedEnchants.getInstance().getConfig().getInt("LibrarianTradeChance"))
			return;
		AbstractVillager vil = e.getEntity();
		if(!(vil instanceof Villager)) return;
		Villager villager = (Villager) vil;
		if(!villager.getProfession().equals(Villager.Profession.LIBRARIAN)) return;
		if(villager.getVillagerLevel() < 4) return;
		
		MerchantRecipe r = e.getRecipe();
		if(r.getResult().getType() != Material.ENCHANTED_BOOK) return;
		
		int ranEnch = Functions.GetRandomNumber(0, Functions.GetEnabledEnchants().size() - 1);
		ItemStack book = ExpandedEnchants.itemManager.CreateCustomBook(Functions.GetEnabledEnchants().get(ranEnch), 1);
		int ingredientIndex = -1;
		for(int i = 0; i < CustomEnchantsManager.custom_enchants.size(); i++) if(Functions.GetEnabledEnchants().get(ranEnch).equals(CustomEnchantsManager.custom_enchants.get(i))) ingredientIndex = i;
		MerchantRecipe newRecipe = new MerchantRecipe(book, Functions.GetRandomNumber(2, 5));
		ArrayList<ItemStack> ing = EnchantmentInformation.tradeCosts[ingredientIndex].getItems();
		newRecipe.setIngredients(ing);
		e.setRecipe(newRecipe);
	}
	
	HashMap<UUID, Integer> spawnedShulkers = new HashMap<>();
	public void HandleOresightEnchant(PlayerToggleSneakEvent e) {
		if(!e.isSneaking()) return;
		Player player = e.getPlayer();
		if (player == null) return;
		if (player.getInventory().getHelmet() == null) return;
		if (!player.getInventory().getHelmet().hasItemMeta()) return;
		if (!player.getInventory().getHelmet().getItemMeta().hasEnchant(CustomEnchantsManager.ORESIGHT)) return;
		
		int level = player.getInventory().getHelmet().getItemMeta().getEnchantLevel(CustomEnchantsManager.ORESIGHT);
		
		int oreSightRange = ExpandedEnchants.getInstance().getConfig().getInt("OreSightRange");
		
		if(level == 1 || (ExpandedEnchants.getInstance().getConfig().getBoolean("OreSightAddUp") && level > 1)) {
			List<Block> coalBlocks = new ArrayList<>();
			Functions.GetBlocksInBox(coalBlocks, player.getLocation(), oreSightRange, Material.COAL_ORE);
			Functions.GetBlocksInBox(coalBlocks, player.getLocation(), oreSightRange, Material.DEEPSLATE_COAL_ORE);
			for (Block block : coalBlocks)
			{
				SpawnGlowingShulker(block, ChatColor.BLACK, "ee_os_coal");
			}
		}
		if(level == 2 || (ExpandedEnchants.getInstance().getConfig().getBoolean("OreSightAddUp") && level > 2)) {
			List<Block> ironBlocks = new ArrayList<>();
			Functions.GetBlocksInBox(ironBlocks, player.getLocation(), oreSightRange, Material.IRON_ORE);
			Functions.GetBlocksInBox(ironBlocks, player.getLocation(), oreSightRange, Material.DEEPSLATE_IRON_ORE);
			for (Block block : ironBlocks)
			{
				SpawnGlowingShulker(block, ChatColor.GOLD, "ee_os_iron");
			}
			List<Block> copperBlocks = new ArrayList<>();
			Functions.GetBlocksInBox(copperBlocks, player.getLocation(), oreSightRange, Material.COPPER_ORE);
			Functions.GetBlocksInBox(copperBlocks, player.getLocation(), oreSightRange, Material.DEEPSLATE_COPPER_ORE);
			for (Block block : copperBlocks)
			{
				SpawnGlowingShulker(block, ChatColor.RED, "ee_os_copper");
			}
		}
		if(level == 3 || (ExpandedEnchants.getInstance().getConfig().getBoolean("OreSightAddUp") && level > 3)) {
			List<Block> goldBlocks = new ArrayList<>();
			Functions.GetBlocksInBox(goldBlocks, player.getLocation(), oreSightRange, Material.GOLD_ORE);
			Functions.GetBlocksInBox(goldBlocks, player.getLocation(), oreSightRange, Material.DEEPSLATE_GOLD_ORE);
			for (Block block : goldBlocks)
			{
				SpawnGlowingShulker(block, ChatColor.YELLOW, "ee_os_gold");
			}
		}
		if(level == 4 || (ExpandedEnchants.getInstance().getConfig().getBoolean("OreSightAddUp") && level > 4)) {
			List<Block> redstoneBlocks = new ArrayList<>();
			Functions.GetBlocksInBox(redstoneBlocks, player.getLocation(), oreSightRange, Material.REDSTONE_ORE);
			Functions.GetBlocksInBox(redstoneBlocks, player.getLocation(), oreSightRange, Material.DEEPSLATE_REDSTONE_ORE);
			for (Block block : redstoneBlocks)
			{
				SpawnGlowingShulker(block, ChatColor.DARK_RED, "ee_os_redstone");
			}
			List<Block> lapisBlocks = new ArrayList<>();
			Functions.GetBlocksInBox(lapisBlocks, player.getLocation(), oreSightRange, Material.DEEPSLATE_LAPIS_ORE);
			Functions.GetBlocksInBox(lapisBlocks, player.getLocation(), oreSightRange, Material.LAPIS_ORE);
			for (Block block : lapisBlocks)
			{
				SpawnGlowingShulker(block, ChatColor.DARK_BLUE, "ee_os_lapis");
			}
		}
		if(level == 5 || (ExpandedEnchants.getInstance().getConfig().getBoolean("OreSightAddUp") && level > 5)) {
			List<Block> emeraldBlocks = new ArrayList<>();
			Functions.GetBlocksInBox(emeraldBlocks, player.getLocation(), oreSightRange, Material.EMERALD_ORE);
			Functions.GetBlocksInBox(emeraldBlocks, player.getLocation(), oreSightRange, Material.DEEPSLATE_EMERALD_ORE);
			for (Block block : emeraldBlocks)
			{
				SpawnGlowingShulker(block, ChatColor.GREEN, "ee_os_emerald");
			}
			List<Block> diamondBlocks = new ArrayList<>();
			Functions.GetBlocksInBox(diamondBlocks, player.getLocation(), oreSightRange, Material.DEEPSLATE_DIAMOND_ORE);
			Functions.GetBlocksInBox(diamondBlocks, player.getLocation(), oreSightRange, Material.DIAMOND_ORE);
			for (Block block : diamondBlocks)
			{
				SpawnGlowingShulker(block, ChatColor.AQUA, "ee_os_diamond");
				
			}
		}
		if(level == 6) {
			List<Block> debrisBlocks = new ArrayList<>();
			Functions.GetBlocksInBox(debrisBlocks, player.getLocation(), 10, Material.ANCIENT_DEBRIS);
			for (Block block : debrisBlocks)
			{
				SpawnGlowingShulker(block, ChatColor.RED, "ee_os_debris");
			}
		}
	}
	
	HashMap<UUID, Integer> thrustCooldown = new HashMap<>();
	public void HandleThrustersEnchant(PlayerToggleSneakEvent e) {
		if(!e.getPlayer().isGliding()) return;
		if(!e.isSneaking()) return;
		if(!e.getPlayer().getInventory().getChestplate().getItemMeta().hasEnchant(CustomEnchantsManager.THRUSTERS)) return;
		
		if(thrustCooldown.containsKey(e.getPlayer().getUniqueId())) {
			if(thrustCooldown.get(e.getPlayer().getUniqueId()) > 0) {
				e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR);
				TextComponent component = new TextComponent(ChatColor.DARK_RED + LanguageManager.instance.GetTranslatedValue("thrusters-cooldown").replace("{seconds}", ChatColor.WHITE + "" + thrustCooldown.get(e.getPlayer().getUniqueId())));
				//component.setColor(ChatColor.DARK_RED);
				e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
				return;
			}
		}
		Location dir = e.getPlayer().getLocation();
		Vector velo = new Vector(dir.getDirection().getX(), dir.getDirection().getY(), dir.getDirection().getZ()).normalize();
		int level = e.getPlayer().getInventory().getChestplate().getItemMeta().getEnchantLevel(CustomEnchantsManager.THRUSTERS);
		Vector newVelo = velo.multiply(level);
		e.getPlayer().setVelocity(e.getPlayer().getVelocity().normalize().add(newVelo));
		thrustCooldown.put(e.getPlayer().getUniqueId(), 6 - level);

	}
	
	public void CountdownThrust() {
		for (UUID player : thrustCooldown.keySet())
		{
			int oldValue = thrustCooldown.get(player);
			if (oldValue > 0)
				thrustCooldown.put(player, oldValue - 1);
			else
				oldValue = 0;
		}
	}
	
	public void SpawnGlowingShulker(Block block, ChatColor color, String teamName) {
		boolean shouldContinue = true;
		for (UUID sh : spawnedShulkers.keySet()) if(Bukkit.getEntity(sh).getLocation().getBlock().equals(block)) {
			shouldContinue = false;
			spawnedShulkers.put(sh, 15);
		}
		if(!shouldContinue) return;
		Shulker shulker = (Shulker) block.getWorld().spawnEntity(block.getLocation(), EntityType.SHULKER);
		shulker.setAI(false);
		shulker.setInvulnerable(true);
		shulker.setInvisible(true);
		shulker.setGlowing(true);
		Bukkit.getScoreboardManager().getMainScoreboard().getTeam(color + teamName).addEntry(shulker.getUniqueId().toString());
		spawnedShulkers.put(shulker.getUniqueId(), 15);
	}
	
	public void CountdownShulker() {
		List<UUID> removeShulker = new ArrayList<>();
		for (UUID ent : spawnedShulkers.keySet())
		{
			int oldValue = spawnedShulkers.get(ent);
			if (oldValue > 0)
				spawnedShulkers.put(ent, oldValue - 1);
			
			else
				removeShulker.add(ent);
		}
		for(UUID uuid : removeShulker) {
			if(Bukkit.getEntity(uuid) != null) Bukkit.getEntity(uuid).remove();
			spawnedShulkers.remove(uuid);
		}
	}
	
	public void CheckForShulker(BlockBreakEvent e) {
		List<UUID> removeShulker = new ArrayList<>();
		for (UUID sh : spawnedShulkers.keySet()) if(Bukkit.getEntity(sh).getLocation().getBlock().equals(e.getBlock())) removeShulker.add(sh);
		
		for(UUID uuid : removeShulker) {
			if(Bukkit.getEntity(uuid) != null) Bukkit.getEntity(uuid).remove();
			spawnedShulkers.remove(uuid);
		}
	}
	
	public void HandleFlamingFallEnchant(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		if(e.getCause() != DamageCause.FALL) return;
		Player player = (Player) e.getEntity();
		if(player.getInventory().getBoots() == null) return;
		if(!player.getInventory().getBoots().hasItemMeta()) return;
		if(!player.getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantsManager.FLAMINGFALL)) return;
		
		player.spawnParticle(Particle.FLAME, player.getLocation().add(0, 0.1, 0), 250, 3, 0, 3, 0);	
		List<Entity> entities = player.getNearbyEntities(3, 3, 3);
		for(Entity ent :  entities) {
			ent.setFireTicks((int) Math.max(20, Math.min(200, e.getDamage() * 20)));
		}
		
	}
	
	public void HandleAssassinEnchant(PlayerToggleSneakEvent e) {
			Player player = e.getPlayer();
			if(player.isGliding()) return;
			if (player.getInventory().getBoots() == null)
				return;
			if (player.getInventory().getBoots().getItemMeta() == null)
				return;
			if (!player.getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantsManager.ASSASSIN))
				return;
			int level = player.getInventory().getBoots().getItemMeta().getEnchantLevel(CustomEnchantsManager.ASSASSIN);
			int countdown = 8 + (4 * level);
			
			AssassinInfo containsPlayer = null;
			for(AssassinInfo i : assassinInfo) if(i.getPlayer().equals(player.getUniqueId())) containsPlayer = i;
			if(containsPlayer == null) {
					if(e.isSneaking()) {
						BossBar bossBar = Bukkit.createBossBar(LanguageManager.instance.GetTranslatedValue("assassin-hidden-for").replace("{seconds}", "" + countdown), BarColor.GREEN, BarStyle.SOLID, BarFlag.DARKEN_SKY);
						bossBar.addPlayer(e.getPlayer());
						int refill = 48 - (2 * level);
						AssassinInfo info = new AssassinInfo(player.getUniqueId(), countdown, refill, bossBar);
						assassinInfo.add(info);
						for(Player p : Bukkit.getOnlinePlayers()) p.hidePlayer(ExpandedEnchants.getInstance(), player);
				}
			}
			else {
				if(e.isSneaking()) {
					if(countdown != containsPlayer.getCountdownMax()) containsPlayer.setCountdownMax(countdown);
					if(48 - (2 * level) != containsPlayer.getRefillMax()) containsPlayer.setRefillMax(48 - (2 * level));
					if(containsPlayer.getCountdown() > 0) {
						containsPlayer.setTryCountdown(true);
						for (Player p : Bukkit.getOnlinePlayers()) p.hidePlayer(ExpandedEnchants.getInstance(), player);
					}
					else {
						containsPlayer.setTryCountdown(false);
						for(Player p : Bukkit.getOnlinePlayers()) p.showPlayer(ExpandedEnchants.getInstance(), player);
					}
					if(!containsPlayer.bar.getPlayers().contains(player)) containsPlayer.bar.addPlayer(player);
					containsPlayer.bar.setVisible(true);
				}
				else {
					containsPlayer.setTryCountdown(false);
					if(!containsPlayer.bar.getPlayers().contains(player)) containsPlayer.bar.addPlayer(player);
					containsPlayer.bar.setVisible(false);
					for(Player p : Bukkit.getOnlinePlayers()) p.showPlayer(ExpandedEnchants.getInstance(), player);
				}
			}
	}
	
	public ArrayList<AssassinInfo> assassinInfo = new ArrayList<>();
		
	public void HandleAssassinJoinEvent(PlayerJoinEvent e) {
		for(AssassinInfo p : assassinInfo) {
			if(Bukkit.getPlayer(p.getPlayer()) == null) continue;
			if(p.getCountdown() > 0) e.getPlayer().hidePlayer(ExpandedEnchants.getInstance(), Bukkit.getPlayer(p.getPlayer()));
		}
	}
	
	public void HandleAssassinMobTarget(EntityTargetEvent e) {
		if(e.getTarget() instanceof Player) {
			Player player = (Player) e.getTarget();
			AssassinInfo info = null;
			for(AssassinInfo i : assassinInfo) if(i.getPlayer().equals(player.getUniqueId())) info = i;
			if(info != null) if(info.getTryCountdown() && info.getShouldCountdown()) e.setCancelled(true);
			
		}
	}
	
	public void HandlePlayerAttackEntity(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player)) return;
		Player player = (Player) e.getDamager();
		AssassinInfo info = null;
		for(AssassinInfo i : assassinInfo) if(i.getPlayer().equals(player.getUniqueId())) info = i;
		if(info == null) return;
		info.setShouldCountdown(false);
		info.setRefill(info.getRefillMax());
		info.bar.setColor(BarColor.YELLOW);
		info.bar.setTitle(LanguageManager.instance.GetTranslatedValue("assassin-recharging").replace("{seconds}", "" + Math.round(info.getRefillMax())));
		info.setShouldRefill(true);
		for(Player p : Bukkit.getOnlinePlayers()) p.showPlayer(ExpandedEnchants.getInstance(), Bukkit.getPlayer(info.getPlayer()));
		
	}
	
	public void HandleAssassinCountdowns() {
		if(assassinInfo.size() == 0) return;
		for(AssassinInfo info : assassinInfo) {
			if(info.getTryCountdown()) {
				if(info.getShouldCountdown()) {
					info.setCountdown(info.getCountdown() - 1);
					if(info.getCountdown() <= 0) {
						info.setShouldCountdown(false);
						info.setRefill(info.getRefillMax());
						info.bar.setColor(BarColor.YELLOW);
						info.bar.setTitle(LanguageManager.instance.GetTranslatedValue("assassin-recharging").replace("{seconds}", "" + Math.round(info.getRefillMax())));
						info.setShouldRefill(true);
						for(Player p : Bukkit.getOnlinePlayers()) if(Bukkit.getOnlinePlayers().contains(Bukkit.getOfflinePlayer(info.getPlayer()))) p.showPlayer(ExpandedEnchants.getInstance(), Bukkit.getPlayer(info.getPlayer()));
					}
					else {
						info.bar.setProgress(Math.min(info.getCountdown() / info.getCountdownMax(), 1));
						info.bar.setTitle(LanguageManager.instance.GetTranslatedValue("assassin-hidden-for").replace("{seconds}", "" + Math.round(info.getCountdown())));
					}
				}
			}
			if(info.getShouldRefill()) {
				info.setRefill(info.getRefill() - 1);
				if(info.getRefill() == 0) {
					info.setShouldCountdown(true);
					info.setShouldRefill(false);
					info.setCountdown(info.getCountdownMax());
					info.bar.setColor(BarColor.GREEN);
					info.bar.setTitle(LanguageManager.instance.GetTranslatedValue("assassin-hidden-for").replace("{seconds}", "" + Math.round(info.getCountdownMax())));
				}
				else {
					info.bar.setProgress(1 - (info.getRefill() / info.getRefillMax()));
					info.bar.setTitle(LanguageManager.instance.GetTranslatedValue("assassin-recharging").replace("{seconds}", "" + Math.round(info.getRefill())));
				}
			}
		}
	}
	
	public Map<UUID, Integer> preventTeleport = new HashMap<>();
	public void HandleDisruptEnchant(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && (e.getEntity() instanceof Enderman || e.getEntity() instanceof Shulker)) {
			Player player = (Player) e.getDamager();
			if(player.getInventory().getItemInMainHand() == null) return;
			if(!player.getInventory().getItemInMainHand().hasItemMeta()) return;
			if(!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.DISRUPTION)) return;
			int level = player.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(CustomEnchantsManager.DISRUPTION);
			preventTeleport.put(e.getEntity().getUniqueId(), 30);
			e.setDamage(e.getDamage() + (2.5 * level));
		}
	}
	
	public void CheckPreventTeleport(EntityTeleportEvent e) {
		if(!preventTeleport.containsKey(e.getEntity().getUniqueId())) return;
		if(preventTeleport.get(e.getEntity().getUniqueId()) > 0) e.setCancelled(true);
	}
	
	public void CountdownDisruptEnchant()
	{
		for (UUID uuid : preventTeleport.keySet())
		{
			int oldValue = preventTeleport.get(uuid);
			if (oldValue > 0)
				preventTeleport.put(uuid, oldValue - 1);
			else
				oldValue = 0;

		}
	}
	
	public Map<UUID, Integer> disarmCountdown = new HashMap<UUID, Integer>();
	public void HandleDisarmingEnchant(ProjectileHitEvent e) {
		if(e.getEntity() instanceof FishHook && e.getHitEntity() instanceof Player) {
			if(e.getEntity().getShooter() instanceof Player) {
				Player player = (Player) e.getEntity().getShooter();
				Player enemy = (Player) e.getHitEntity();
				if(player.getInventory().getItemInMainHand() == null) return;
				if(!player.getInventory().getItemInMainHand().hasItemMeta()) return;
				if(!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.DISARMING)) return;
				int level = player.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(CustomEnchantsManager.DISARMING);
				int ran = Functions.GetRandomNumber(0, 100);
				if (ran > (level * 2)) return;
				if(disarmCountdown.containsKey(player.getUniqueId())) if(disarmCountdown.get(player.getUniqueId()) > 0) return;
				
				disarmCountdown.put(player.getUniqueId(), 30);
				boolean allNull = true;
				for(ItemStack item : enemy.getInventory().getArmorContents()) if(item != null) allNull = false;
				if(allNull && enemy.getInventory().getItemInMainHand() == null) return;
				if(allNull) {
					enemy.dropItem(true);

				}
				else if(enemy.getInventory().getItemInMainHand() == null) {
					ItemStack[] armors = enemy.getInventory().getArmorContents();
					List<Integer> occupiedSlots = new ArrayList<>();
					for(int i = 0; i < 4; i++) if(armors[i] != null) occupiedSlots.add(i);
					int slot = occupiedSlots.get(Functions.GetRandomNumber(0, occupiedSlots.size() - 1));
					enemy.getWorld().dropItemNaturally(enemy.getLocation(), armors[slot]).setPickupDelay(40);;
				    armors[slot] = null;
				    enemy.getInventory().setArmorContents(armors);
				}
				else {
					int num = Functions.GetRandomNumber(1, 2);
					if(num == 1) {
						enemy.dropItem(true);
					}
					else {
						ItemStack[] armors = enemy.getInventory().getArmorContents();
						List<Integer> occupiedSlots = new ArrayList<>();
						for(int i = 0; i < 4; i++) if(armors[i] != null) occupiedSlots.add(i);
						int slot = occupiedSlots.get(Functions.GetRandomNumber(0, occupiedSlots.size() - 1));
						enemy.getWorld().dropItemNaturally(enemy.getLocation(), armors[slot]).setPickupDelay(40);
					    armors[slot] = null;
					    enemy.getInventory().setArmorContents(armors);
					}
				}
				Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
				{
					enemy.updateInventory();
				});
			}
		}
	}
	
	public void CountdownDisarmEnchant()
	{
		for (UUID player : disarmCountdown.keySet())
		{
			int oldValue = disarmCountdown.get(player);
			if (oldValue > 0)
				disarmCountdown.put(player, oldValue - 1);
			else
				oldValue = 0;

		}
	}
	
	public void CheckEndCityLoot(LootGenerateEvent e) {
		if(!LootTables.END_CITY_TREASURE.getLootTable().equals(e.getLootTable())) return;
		int ran = Functions.GetRandomNumber(0, 100);
		if (ran > ExpandedEnchants.getInstance().getConfig().getInt("EndCityChance"))
			return;
		//List<ItemStack> items = e.getLoot();
		int ranEnch = Functions.GetRandomNumber(0, Functions.GetEnabledEnchants().size());
		ItemStack enchant = ExpandedEnchants.itemManager.CreateCustomBook(Functions.GetEnabledEnchants().get(ranEnch), 1);
		e.getLoot().add(enchant);
	}
	
	public void CheckWanderingTrader(EntitySpawnEvent e)
	{
		if (e.getEntityType() != EntityType.WANDERING_TRADER)
			return;
		int ran = Functions.GetRandomNumber(0, 100);
		if (ran > ExpandedEnchants.getInstance().getConfig().getInt("WanderingTraderChance"))
			return;
		WanderingTrader trader = (WanderingTrader) e.getEntity();
		List<MerchantRecipe> recipes = trader.getRecipes();
		List<MerchantRecipe> newRecipes = new ArrayList<MerchantRecipe>();
		for (MerchantRecipe r : recipes)
			newRecipes.add(r);

		if (Functions.GetEnabledEnchants().size() == 0)
			return;
		int ranEnch = Functions.GetRandomNumber(0, Functions.GetEnabledEnchants().size() - 1);
		ItemStack enchant = ExpandedEnchants.itemManager.CreateCustomBook(Functions.GetEnabledEnchants().get(ranEnch), 1);
		int ingredientIndex = -1;
		for(int i = 0; i < CustomEnchantsManager.custom_enchants.size(); i++) if(Functions.GetEnabledEnchants().get(ranEnch).equals(CustomEnchantsManager.custom_enchants.get(i))) ingredientIndex = i;
		MerchantRecipe bookRecipe = new MerchantRecipe(enchant, Functions.GetRandomNumber(2, 5));
		ArrayList<ItemStack> ing = EnchantmentInformation.tradeCosts[ingredientIndex].getItems();
		bookRecipe.setIngredients(ing);
		newRecipes.add(bookRecipe);
		trader.setRecipes(newRecipes);
	}

	Ageable replantingBlock = null;

	public void CheckReplantingEnchant(PlayerInteractEvent e)
	{
		if (!e.hasBlock())
			return;
		if (!e.hasItem())
			return;
		if (e.getItem().getItemMeta() == null)
			return;
		if (!e.getItem().getItemMeta().hasEnchant(CustomEnchantsManager.REPLANTING))
			return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		BlockData data = e.getClickedBlock().getBlockData();
		Location loc = e.getClickedBlock().getLocation();
		if (data instanceof Ageable)
		{
			Ageable ageData = (Ageable) data;
			if (ageData.getAge() != ageData.getMaximumAge())
				return;
			if (data.getMaterial() == Material.WHEAT)
			{
				replantingBlock = ageData;
				e.getPlayer().breakBlock(e.getClickedBlock());
				loc.getBlock().setType(Material.WHEAT);
			}
			if (data.getMaterial() == Material.CARROTS)
			{
				replantingBlock = ageData;
				e.getPlayer().breakBlock(e.getClickedBlock());
				loc.getBlock().setType(Material.CARROTS);
			}
			if (data.getMaterial() == Material.POTATOES)
			{
				replantingBlock = ageData;
				e.getPlayer().breakBlock(e.getClickedBlock());
				loc.getBlock().setType(Material.POTATOES);
			}
			if (data.getMaterial() == Material.BEETROOTS)
			{
				replantingBlock = ageData;
				e.getPlayer().breakBlock(e.getClickedBlock());
				loc.getBlock().setType(Material.BEETROOTS);
				
			}
			if (data.getMaterial() == Material.NETHER_WART)
			{
				replantingBlock = ageData;
				e.getPlayer().breakBlock(e.getClickedBlock());
				loc.getBlock().setType(Material.NETHER_WART);
			}
			if(!e.getItem().getItemMeta().hasEnchant(CustomEnchantsManager.NOBREAKABLE)) { 
				Damageable dam = (Damageable) e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
				dam.setDamage(dam.getDamage() + 1);
				e.getItem().setItemMeta(dam);
			}
		}
	}

	public void CheckReplantingItemDrop(BlockDropItemEvent e)
	{
		if (e.getPlayer() == null)
			return;
		Player player = e.getPlayer();
		if (player.getInventory().getItemInMainHand() == null)
			return;
		if (player.getInventory().getItemInMainHand().getItemMeta() == null)
			return;
		if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.REPLANTING))
			return;
		BlockData data = e.getBlock().getBlockData();
		if (data == null)
			return;
		if (data.getMaterial() == Material.AIR)
			data = replantingBlock;
		if (data instanceof Ageable)
		{
			Ageable ageData = (Ageable) data;
			if (ageData.getAge() != ageData.getMaximumAge())
				return;
			List<Item> items = e.getItems();
			if (data.getMaterial() == Material.WHEAT)
			{
				for (Item item : items)
				{
					if (item.getItemStack().getType().equals(Material.WHEAT_SEEDS))
					{
						item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
						break;
					}
				}
			}
			if (data.getMaterial() == Material.CARROTS)
			{
				for (Item item : items)
					if (item.getItemStack().getType().equals(Material.CARROT))
					{
						item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
						break;
					}
			}
			if (data.getMaterial() == Material.POTATOES)
			{
				for (Item item : items)
					if (item.getItemStack().getType().equals(Material.POTATO))
					{
						item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
						break;
					}
			}
			if (data.getMaterial() == Material.BEETROOTS)
			{
				for (Item item : items)
					if (item.getItemStack().getType().equals(Material.BEETROOT))
					{
						item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
						break;
					}
			}
			if (data.getMaterial() == Material.NETHER_WART)
			{
				for (Item item : items)
					if (item.getItemStack().getType().equals(Material.NETHER_WART))
					{
						item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
						break;
					}
			}
		}
		replantingBlock = null;
	}

	public void CheckForDiscoverRecipes(CraftItemEvent e)
	{
		if (e.getInventory() == null)
			return;
		if (e.getInventory().getResult() == null)
			return;
		if (e.getInventory().getResult().getType() == Material.CRAFTING_TABLE)
		{
			ArrayList<NamespacedKey> keys = new ArrayList<NamespacedKey>();
			for (CustomRecipe recipe : ExpandedEnchants.customRecipes)
				keys.add(recipe.recipe.getKey());
		}
	}

	public void CheckForCustomRecipes(PrepareItemCraftEvent e)
	{
		Recipe recipe = e.getRecipe();
		if (recipe == null)
			return;
		if (((Keyed) recipe).getKey() == null)
			return;

		CustomRecipe customRecipe = null;
		for (CustomRecipe cer : ExpandedEnchants.customRecipes)
		{
			if (cer.recipe.getKey().equals(((Keyed) recipe).getKey()))
				customRecipe = cer;
		}
		if (customRecipe == null)
			return;
		
		CraftingInventory inv = e.getInventory();
		ItemStack[] ingredients = inv.getMatrix();
		boolean canCraft = true;

		if(ingredients[1] != null) if(ingredients[1].getType() == Material.POTION || ingredients[1].getType() == Material.ENCHANTED_BOOK || ingredients[1].getType() == Material.LINGERING_POTION || ingredients[1].getType() == Material.SPLASH_POTION)
			canCraft = CheckItemInRecipes(customRecipe.recipe.getKey().toString(), ingredients[1]);
		if(ingredients[4] != null) if(ingredients[4].getType() == Material.ENCHANTED_BOOK) {
			if(ingredients[4].getItemMeta().getEnchants().size() > 0) {
				Enchantment ench = (Enchantment) ingredients[4].getItemMeta().getEnchants().keySet().toArray()[0];
				if(ench.equals(recipe.getResult().getItemMeta().getEnchants().keySet().toArray()[0])) {
					if(ingredients[4].getItemMeta().getEnchantLevel(ench) < ench.getMaxLevel()) {
						 inv.setResult(ExpandedEnchants.itemManager.CreateCustomBook(ench, ingredients[4].getItemMeta().getEnchantLevel(ench) + 1));
					}
					else canCraft = false;

				}
				else canCraft = false; 
			}
			else {
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) ingredients[4].getItemMeta();
				if(meta != null) if(meta.getStoredEnchants().size() > 0) canCraft = false;
			}
		}
		for (int i = 0; i < customRecipe.amounts.length; i++)
		{
			if (ingredients[i] == null)
			{
				if(customRecipe.amounts[i] == 0) continue;
				else
				{
					canCraft = false;
					break;
				}
			}
			boolean foundHigher = false;
			for(ItemStack item : customRecipe.items) if(item != null && ingredients[i] != null) if(item.getType().equals(ingredients[i].getType())) if(item.getAmount() > ingredients[i].getAmount()) foundHigher = true;
			if (foundHigher)
			{
				canCraft = false;
				break;
			}
		}
		if (!canCraft)
			inv.setResult(null);

	}

	public boolean CheckItemInRecipes(String recipe, ItemStack ingredient)
	{
		boolean canCraft = true;
		boolean[] configResult = null;
		switch (recipe)
		{
		case "minecraft:ee_recipe_flamingfall":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_flamingfall");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta fallmeta = (PotionMeta) ingredient.getItemMeta();
			if (fallmeta.getBasePotionData().getType() == PotionType.FIRE_RESISTANCE && fallmeta.getBasePotionData().isExtended());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_owleyes":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_owleyes");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta nightmeta = (PotionMeta) ingredient.getItemMeta();
			if (nightmeta.getBasePotionData().getType() == PotionType.NIGHT_VISION && nightmeta.getBasePotionData().isExtended());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_assassin":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_assassin");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta assassin = (PotionMeta) ingredient.getItemMeta();
			if (assassin.getBasePotionData().getType() == PotionType.INVISIBILITY && assassin.getBasePotionData().isExtended());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_elemental":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_elemental");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta elementalMeta = (PotionMeta) ingredient.getItemMeta();
			if (elementalMeta.getBasePotionData().getType() == PotionType.POISON && elementalMeta.getBasePotionData().isUpgraded());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_healthboost":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_healthboost");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta healthmeta = (PotionMeta) ingredient.getItemMeta();
			if (healthmeta.getBasePotionData().getType() == PotionType.INSTANT_HEAL && healthmeta.getBasePotionData().isUpgraded());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_heavenslightness":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_heavenslightness");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta heavensmeta = (PotionMeta) ingredient.getItemMeta();
			if (heavensmeta.getBasePotionData().getType() == PotionType.SLOW_FALLING && heavensmeta.getBasePotionData().isExtended());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_icy":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_icy");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta icymeta = (PotionMeta) ingredient.getItemMeta();
			if (icymeta.getBasePotionData().getType() == PotionType.SLOWNESS && icymeta.getBasePotionData().isUpgraded());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_leaping":
			boolean[] configResultle = CheckConfigForRecipes(ingredient, "ee_recipe_leaping");
			canCraft = configResultle[1];
			if(!configResultle[0]) break;
			
			PotionMeta leapingmeta = (PotionMeta) ingredient.getItemMeta();
			if (leapingmeta.getBasePotionData().getType() == PotionType.JUMP && leapingmeta.getBasePotionData().isUpgraded());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_lifesteal":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_lifesteal");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta lifemeta = (PotionMeta) ingredient.getItemMeta();
			if (lifemeta.getBasePotionData().getType() == PotionType.INSTANT_HEAL && lifemeta.getBasePotionData().isUpgraded());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_stonefists":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_stonefists");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta stonemeta = (PotionMeta) ingredient.getItemMeta();
			if (stonemeta.getBasePotionData().getType() == PotionType.STRENGTH && stonemeta.getBasePotionData().isUpgraded());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_thermalplating":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_thermalplating");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta thermalmeta = (PotionMeta) ingredient.getItemMeta();
			if (thermalmeta.getBasePotionData().getType() == PotionType.FIRE_RESISTANCE && thermalmeta.getBasePotionData().isExtended());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_traveler":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_traveler");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			PotionMeta travelermeta = (PotionMeta) ingredient.getItemMeta();
			if (travelermeta.getBasePotionData().getType() == PotionType.SPEED && travelermeta.getBasePotionData().isUpgraded());
			else canCraft = false;
			break;
		case "minecraft:ee_recipe_unbreakable":
			configResult = CheckConfigForRecipes(ingredient, "ee_recipe_unbreakable");
			canCraft = configResult[1];
			if(!configResult[0]) break;
			
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) ingredient.getItemMeta();
			if (meta.hasStoredEnchant(Enchantment.DURABILITY)) {if (meta.getStoredEnchantLevel(Enchantment.DURABILITY) != 10)	canCraft = false; }
			else canCraft = false;
			break;
		
		default:
			String str = recipe.replace("minecraft:", "");
			configResult = CheckConfigForRecipes(ingredient, str);
			canCraft = configResult[1];
			
		}
		return canCraft;
	}
	
	public boolean[] CheckConfigForRecipes(ItemStack ingredient, String name) {
		boolean[] returns = new boolean[] {
				true, true
		};
		
		FileConfiguration recipeData = new YamlConfiguration();
		try { recipeData.load("plugins/ExpandedEnchants/recipes.yml"); }
		catch (InvalidConfigurationException e) { e.printStackTrace(); recipeData = null;}
		catch(IOException e) {recipeData = null;};
		if(recipeData != null) { 
			if(recipeData.getConfigurationSection(name) != null) {
				if(recipeData.isConfigurationSection(name)) {
					if(recipeData.getConfigurationSection(name + ".Advanced").contains("Enchantment")) {
						NamespacedKey enchName = NamespacedKey.fromString(recipeData.getConfigurationSection(name + ".Advanced").getString("Enchantment"));
						Enchantment ench = Enchantment.getByKey(enchName);
						if(Functions.IsCustomEnchant(ench)) {
							ItemMeta meta = ingredient.getItemMeta();
							if(meta.hasEnchant(ench)) if(meta.getEnchantLevel(ench) != recipeData.getConfigurationSection(name + ".Advanced").getInt("Level")) returns[1] = false;
						}
						else {
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) ingredient.getItemMeta();
							if (meta.hasStoredEnchant(ench)) if (meta.getStoredEnchantLevel(ench) != recipeData.getConfigurationSection(name + ".Advanced").getInt("Level")) returns[1] = false;
							else returns[1] = false;
						}
						returns[0] = false;
					}
					else if(recipeData.getConfigurationSection(name + ".Advanced").contains("Potion")) {
						String potionName = recipeData.getConfigurationSection(name + ".Advanced").getString("Potion");
						PotionType potion = PotionType.valueOf(potionName);
						PotionMeta meta = (PotionMeta) ingredient.getItemMeta();
						if(meta.getBasePotionData().getType() != potion) returns[1] = false;
						if(meta.getBasePotionData().isExtended() != recipeData.getConfigurationSection(name + ".Advanced").getBoolean("Extended")) returns[1] = false;
						if(meta.getBasePotionData().isUpgraded() != recipeData.getConfigurationSection(name + ".Advanced").getBoolean("Upgraded")) returns[1] = false;
						
						returns[0] = false;
						
					}
				}
				
			}
		}
		return returns;
	}
	
	HashMap<UUID, ArrayList<ItemStack>> itemsToPreserve = new HashMap<>();

	public void HandleSoulboundEnchant(PlayerDeathEvent e)
	{
		if (e.getEntity() == null)
			return;
		if (e.getKeepInventory())
			return;
		ArrayList<ItemStack> preserveItems = new ArrayList<>();
		List<ItemStack> items = e.getDrops();
		for (ItemStack item : items)
			if (item.getItemMeta().hasEnchant(CustomEnchantsManager.SOULBOUND))
			{
				preserveItems.add(item);
			}
		for (ItemStack item : preserveItems)
			e.getDrops().remove(item);
		itemsToPreserve.put(e.getEntity().getUniqueId(), preserveItems);
	}

	public void HandleSoulboundRespawn(PlayerRespawnEvent e)
	{
		if (!itemsToPreserve.containsKey(e.getPlayer().getUniqueId()))
			return;
		Player player = e.getPlayer();
		for (ItemStack item : itemsToPreserve.get(player.getUniqueId()))
		{
			player.getInventory().addItem(item);
		}
		itemsToPreserve.remove(player.getUniqueId());
	}


	ArrayList<UUID> noFallDamage = new ArrayList<>();

	public void HandleElementalProtectionEnchant(EntityDamageEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			if (player == null)
				return;
			if (player.getInventory().getChestplate() == null)
				return;
			if (!player.getInventory().getChestplate().hasItemMeta())
				return;
			if (!player.getInventory().getChestplate().getItemMeta()
					.hasEnchant(CustomEnchantsManager.ELEMENTALPROTECTION))
				return;
			if (player.getGameMode() == GameMode.SPECTATOR)
				return;

			int level = player.getInventory().getChestplate().getItemMeta()
					.getEnchantLevel(CustomEnchantsManager.ELEMENTALPROTECTION);
			if (e.getCause() == DamageCause.POISON)
				e.setCancelled(true);
			if (e.getCause() == DamageCause.WITHER && level >= 2)
				e.setCancelled(true);
			if (e.getCause() == DamageCause.VOID && e.getDamage() == 4 && level >= 3)
			{
				e.setCancelled(true);
				if (!noFallDamage.contains(player.getUniqueId()))
					noFallDamage.add(player.getUniqueId());
				player.teleport(
						new Location(player.getWorld(), player.getLocation().getX(), 128, player.getLocation().getZ()),
						TeleportCause.PLUGIN);
			}
			if (e.getCause() == DamageCause.FALL && noFallDamage.contains(player.getUniqueId()))
			{
				e.setCancelled(true);
				noFallDamage.remove(player.getUniqueId());
			}
		}
	}

	public void HandleShadowStepEnchant(EntityTargetEvent e)
	{
		if (e.getTarget() instanceof Player)
		{
			Player player = (Player) e.getTarget();
			if (player.getInventory().getBoots() == null)
				return;
			if (player.getInventory().getBoots().getItemMeta() == null)
				return;
			if (!player.getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantsManager.SHADOWSTEP))
				return;

			int level = player.getInventory().getBoots().getItemMeta()
					.getEnchantLevel(CustomEnchantsManager.SHADOWSTEP);
			double followRange = 16;
			switch (level)
			{
			case 1:
				followRange = 12;
				break;
			case 2:
				followRange = 8;
				break;
			case 3:
				followRange = 6;
				break;
			case 4:
				followRange = 4;
				break;
			case 5:
				followRange = 2;
				break;
			}
			if (e.getEntity().getLocation().distance(player.getLocation()) > followRange)
			{
				e.setCancelled(true);
			}

		}
	}

	public void HandleCraftedItem(CraftItemEvent e)
	{
		Recipe recipe = e.getRecipe();
		if (recipe == null)
			return;
		if (((Keyed) recipe).getKey() == null)
			return;
		CustomRecipe customRecipe = null;
		for (CustomRecipe cer : ExpandedEnchants.customRecipes)
		{
			if (cer.recipe.getKey().equals(((Keyed) recipe).getKey()))
				customRecipe = cer;
		}
		if (customRecipe == null)
			return;
		CraftingInventory inv = e.getInventory();
		ItemStack[] ingredients = inv.getMatrix();
		ItemStack[] leftOvers = ingredients;
		ItemStack result = inv.getResult();
		for (int i = 0; i < customRecipe.amounts.length; i++)
		{
			if (ingredients[i] == null)
			{
				continue;
			}
			int amount = 0;
			for(ItemStack item : customRecipe.items) if(item != null) if(item.getType().equals(ingredients[i].getType())) amount = item.getAmount(); 
			int leftOverAmount = ingredients[i].getAmount() - amount;
			if (leftOverAmount == 0)
				leftOvers[i] = null;
			else
				leftOvers[i].setAmount(leftOverAmount);
		}
		inv.setMatrix(leftOvers);
		e.getWhoClicked().setItemOnCursor(result);
	}

	public void ConstantCheckMethods()
	{
		CountdownDeflectEnchant();
		CountdownDisarmEnchant();
		CountdownDisruptEnchant();
		HandleAssassinCountdowns();
		CountdownShulker();
		CountdownThrust();
		CountdownIcy();
		
		if (Bukkit.getOnlinePlayers().size() == 0)
			return;
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			HandleOwlEyesEnchant(player);
			HandleHeavensLightnessEnchant(player);
			HandleThermalPlatingEnchant(player);
			HandleLeapingEnchant(player);
		}
		
	}

	public void CountdownDeflectEnchant()
	{
		for (UUID player : deflectCountdown.keySet())
		{
			int oldValue = deflectCountdown.get(player);
			if (oldValue > 0)
				deflectCountdown.put(player, oldValue - 1);
			else
				oldValue = 0;

		}
	}
	
	public void CountdownIcy() {
		for (UUID player : canFreeze.keySet())
		{
			double value = canFreeze.get(player);
			if (value > 0) canFreeze.put(player, value - 1);
			else value = 0;
		}
	}


	public HashMap<UUID, Integer> deflectCountdown = new HashMap<>();

	public void HandleDeflectEnchant(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (player.getInventory().getChestplate() == null)
				return;
			if (!player.getInventory().getChestplate().hasItemMeta())
				return;
			if (!player.getInventory().getChestplate().getItemMeta().hasEnchant(CustomEnchantsManager.DEFLECT))
				return;
			if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
				return;
			if (!player.getInventory().contains(Material.EMERALD)) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR);
				TextComponent component = new TextComponent(LanguageManager.instance.GetTranslatedValue("deflect-no-emeralds"));
				component.setColor(ChatColor.RED);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
				return;
			}
			if (!player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD),
					ExpandedEnchants.getInstance().getConfig().getInt("DeflectCost")))
			{
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR);
				TextComponent component = new TextComponent(LanguageManager.instance.GetTranslatedValue("deflect-not-enough"));
				component.setColor(ChatColor.RED);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
				return;
			}
			if (!deflectCountdown.containsKey(player.getUniqueId()))
			{
				e.setCancelled(true);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR);
				TextComponent component = new TextComponent(LanguageManager.instance.GetTranslatedValue("deflect-damage-deflected"));
				component.setColor(ChatColor.GREEN);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
				player.getInventory().removeItem(
						new ItemStack(Material.EMERALD, ExpandedEnchants.getInstance().getConfig().getInt("DeflectCost")));
			} else if (deflectCountdown.get(player.getUniqueId()) == 0)
			{
				e.setCancelled(true);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR);
				TextComponent component = new TextComponent(LanguageManager.instance.GetTranslatedValue("deflect-damage-deflected"));
				component.setColor(ChatColor.GREEN);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
				player.getInventory().removeItem(
						new ItemStack(Material.EMERALD, ExpandedEnchants.getInstance().getConfig().getInt("DeflectCost")));
			} else
			{
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR);
				TextComponent component = new TextComponent(
					ChatColor.RED +	LanguageManager.instance.GetTranslatedValue("deflect-cooldown").replace("{seconds}", ChatColor.WHITE + "" + deflectCountdown.get(player.getUniqueId())));
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
				return;
			}
			deflectCountdown.put(player.getUniqueId(), 31 - (player.getInventory().getChestplate().getItemMeta()
					.getEnchantLevel(CustomEnchantsManager.DEFLECT) * 4));
		}
	}

	public ArrayList<Player> thermalPlayers = new ArrayList<Player>();

	public void HandleThermalPlatingEnchant(Player player)
	{
		if (player != null)
		{
			if (player.getInventory().getChestplate() != null && player.getInventory().getHelmet() != null
					&& player.getInventory().getBoots() != null && player.getInventory().getLeggings() != null)
			{
				if (player.getInventory().getChestplate().hasItemMeta()
						&& player.getInventory().getHelmet().hasItemMeta()
						&& player.getInventory().getBoots().hasItemMeta()
						&& player.getInventory().getLeggings().hasItemMeta())
				{
					if (player.getInventory().getChestplate().getItemMeta()
							.hasEnchant(CustomEnchantsManager.THERMALPLATING)
							&& player.getInventory().getHelmet().getItemMeta()
									.hasEnchant(CustomEnchantsManager.THERMALPLATING)
							&& player.getInventory().getBoots().getItemMeta()
									.hasEnchant(CustomEnchantsManager.THERMALPLATING)
							&& player.getInventory().getLeggings().getItemMeta()
									.hasEnchant(CustomEnchantsManager.THERMALPLATING))
					{
						PotionEffect effect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999999, 0, false,
								false, false);
						if (!player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
							player.addPotionEffect(effect);
						if (!thermalPlayers.contains(player))
							thermalPlayers.add(player);
						return;
					}
				}
			}
		}
		if (thermalPlayers.contains(player))
		{
			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			thermalPlayers.remove(player);
		}
	}

	public ArrayList<Player> heavensPlayers = new ArrayList<Player>();

	public void HandleHeavensLightnessEnchant(Player player)
	{
		if (player != null)
		{
			if (player.getInventory().getChestplate() != null)
			{
				if (player.getInventory().getChestplate().hasItemMeta())
				{
					if (player.getInventory().getChestplate().getItemMeta()
							.hasEnchant(CustomEnchantsManager.HEAVENSLIGHTNESS))
					{
						PotionEffect effect = new PotionEffect(PotionEffectType.SLOW_FALLING, 99999999, 0, false, false,
								false);
						if (!player.hasPotionEffect(PotionEffectType.SLOW_FALLING))
							player.addPotionEffect(effect);
						if (!heavensPlayers.contains(player))
							heavensPlayers.add(player);
						return;
					}
				}
			}
		}
		if (heavensPlayers.contains(player))
		{
			player.removePotionEffect(PotionEffectType.SLOW_FALLING);
			heavensPlayers.remove(player);
		}
	}

	public ArrayList<Player> owleyesPlayers = new ArrayList<Player>();

	public void HandleOwlEyesEnchant(Player player)
	{
		if (player != null)
		{
			if (player.getInventory().getHelmet() != null)
			{
				if (player.getInventory().getHelmet().hasItemMeta())
				{
					if (player.getInventory().getHelmet().getItemMeta().hasEnchant(CustomEnchantsManager.OWLEYES))
					{
						PotionEffect effect = new PotionEffect(PotionEffectType.NIGHT_VISION, 99999999, 0, false, false,
								false);
						if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
							player.addPotionEffect(effect);
						if (!owleyesPlayers.contains(player))
							owleyesPlayers.add(player);
						return;
					}
				}
			}
		}
		if (owleyesPlayers.contains(player))
		{
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			owleyesPlayers.remove(player);
		}
	}

	public ArrayList<Player> leapingPlayers = new ArrayList<Player>();

	public void HandleLeapingEnchant(Player player)
	{
		if (player != null)
		{
			if (player.getInventory().getBoots() != null)
			{
				if (player.getInventory().getBoots().hasItemMeta())
				{
					if (player.getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantsManager.LEAPING))
					{
						PotionEffect effect = new PotionEffect(PotionEffectType.JUMP, 999999, player.getInventory()
								.getBoots().getItemMeta().getEnchantLevel(CustomEnchantsManager.LEAPING), false, false,
								false);
						player.removePotionEffect(PotionEffectType.JUMP);
						player.addPotionEffect(effect);
						if (!leapingPlayers.contains(player))
							leapingPlayers.add(player);
						return;
					}
				}
			}
		}
		if (leapingPlayers.contains(player))
		{
			player.removePotionEffect(PotionEffectType.JUMP);
			leapingPlayers.remove(player);
		}
	}

	public HashMap<UUID, Double> canFreeze = new HashMap<UUID, Double>();

	public void HandleIcyEnchant(EntityDamageByEntityEvent e)
	{
		if (e.getEntity() instanceof LivingEntity)
		{
			LivingEntity other = (LivingEntity) e.getEntity();
			if (e.getDamager() instanceof Player)
			{
				Player player = (Player) e.getDamager();
				if (other.isDead())
					return;
				if (player == null)
					return;
				if (player.getInventory().getItemInMainHand() == null)
					return;
				if (!player.getInventory().getItemInMainHand().hasItemMeta())
					return;
				if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.ICY))
					return;
				if (player.getGameMode() == GameMode.SPECTATOR)
					return;

				int level = player.getInventory().getItemInMainHand().getItemMeta()
						.getEnchantLevel(CustomEnchantsManager.ICY) * 20;
				if (!canFreeze.containsKey(player.getUniqueId()) || (canFreeze.get(player.getUniqueId()) == 0 && canFreeze.containsKey(player.getUniqueId())))
				{
					PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, level, 3, false, false, false);
					other.addPotionEffect(effect);
					other.getWorld().playSound(other.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
					other.getWorld().spawnParticle(Particle.CLOUD, other.getLocation(), 20, 0, 1, 0, .1);
					canFreeze.put(player.getUniqueId(), (double) 10);
					if (other instanceof Player)
					{
						PotionEffect effect2 = new PotionEffect(PotionEffectType.JUMP, level, 128, false, false, false);
						other.addPotionEffect(effect2);
					}
				}
			}
		}
	}

	ArrayList<Block> checkedBlocks = new ArrayList<Block>();
	ArrayList<Block> veinMinedBlocks = new ArrayList<Block>();

	@SuppressWarnings("unchecked")
	public void HandleLumberjackEnchant(BlockBreakEvent e)
	{
		if (veinMinedBlocks.contains(e.getBlock()))
			return;
		Block originBlock = e.getBlock();
		if (!Functions.IsLumberjackBlock(originBlock.getType()))
			return;
		Player player = e.getPlayer();
		if (player == null)
			return;
		if (player.getInventory().getItemInMainHand() == null)
			return;
		if (!player.getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.LUMBERJACK))
			return;
		if (player.getGameMode() == GameMode.SPECTATOR)
			return;
		int maxMineSize = 512;
		checkedBlocks.clear();
		newBlocks.clear();
		newBlocks.add(originBlock);
		CheckSurroundingBlocks((ArrayList<Block>)newBlocks.clone(), maxMineSize);
		switch (ExpandedEnchants.getInstance().getConfig().getString("LumberjackMode"))
		{
		case "SNEAKING":
			if (!player.isSneaking())
				return;
			break;
		case "STANDING":
			if (player.isSneaking())
				return;
			break;
		case "BOTH":
			break;
		}
		Damageable itemMeta = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
		if (!itemMeta.isUnbreakable())
		{
			if (itemMeta.hasEnchant(Enchantment.DURABILITY))
				itemMeta.setDamage(itemMeta.getDamage()
						+ (checkedBlocks.size() / itemMeta.getEnchantLevel(Enchantment.DURABILITY) + 1));
			else
				itemMeta.setDamage(itemMeta.getDamage() + checkedBlocks.size());
		}
		float foodAmount = 5 - itemMeta.getEnchantLevel(CustomEnchantsManager.LUMBERJACK);
		if(player.getSaturation() > 0) {
			if(player.getSaturation() > foodAmount) player.setSaturation(player.getSaturation() - foodAmount);
			else {
				foodAmount = foodAmount - player.getSaturation();
				player.setSaturation(0);
			}
		}
		if(player.getFoodLevel() > 0) player.setFoodLevel((int) (player.getFoodLevel() - foodAmount));
		for (Block block : checkedBlocks)
		{
			if (block != null)
			{
				veinMinedBlocks.add(block);
				player.breakBlock(block);
			}
		}
		player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
		if (veinMinedBlocks.size() > 0)
			e.setDropItems(false);
		veinMinedBlocks.clear();

	}

	@SuppressWarnings("unchecked")
	public void HandleVeinmineEnchant(BlockBreakEvent e)
	{
		if (veinMinedBlocks.contains(e.getBlock()))
			return;
		Block originBlock = e.getBlock();
		if (!Functions.IsVeinmineBlock(originBlock.getType()))
			return;
		Player player = e.getPlayer();
		if (player == null)
			return;
		if (player.getInventory().getItemInMainHand() == null)
			return;
		if (!player.getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.VEINMINE))
			return;
		if (player.getGameMode() == GameMode.SPECTATOR)
			return;
		int maxMineSize = 512;
		checkedBlocks.clear();
		newBlocks.clear();
		newBlocks.add(originBlock);
		CheckSurroundingBlocks((ArrayList<Block>)newBlocks.clone(), maxMineSize);
		switch (ExpandedEnchants.getInstance().getConfig().getString("VeinmineMode"))
		{
		case "SNEAKING":
			if (!player.isSneaking())
				return;
			break;
		case "STANDING":
			if (player.isSneaking())
				return;
			break;
		case "BOTH":
			break;
		}
		Damageable itemMeta = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
		if (!itemMeta.isUnbreakable())
		{
			if (itemMeta.hasEnchant(Enchantment.DURABILITY))
				itemMeta.setDamage(itemMeta.getDamage() + (checkedBlocks.size() / itemMeta.getEnchantLevel(Enchantment.DURABILITY) + 1));
			else
				itemMeta.setDamage(itemMeta.getDamage() + checkedBlocks.size());
		}
		float foodAmount = 5 - itemMeta.getEnchantLevel(CustomEnchantsManager.VEINMINE);
		if(player.getSaturation() > 0) {
			
			if(player.getSaturation() > foodAmount) {
				player.setSaturation(player.getSaturation() - foodAmount);
				foodAmount = 0;
			}
			else {
				foodAmount = foodAmount - player.getSaturation();
				player.setSaturation(0);
			}
		}
		if(player.getFoodLevel() > 0) player.setFoodLevel((int) (player.getFoodLevel() - foodAmount));
		for (Block block : checkedBlocks)
		{
			if (block != null)
			{
				veinMinedBlocks.add(block);
				player.breakBlock(block);
			}
		}
		player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
		if (veinMinedBlocks.size() > 0)
			e.setDropItems(false);
		veinMinedBlocks.clear();
	}

	ArrayList<Block> newBlocks = new ArrayList<>();
	@SuppressWarnings("unchecked")
	public void CheckSurroundingBlocks(ArrayList<Block> blocks, int maxSize)
	{
		if (checkedBlocks.size() >= maxSize)
			return;
		
		for(Block block : blocks) {
			newBlocks.remove(block);
			if (block.getRelative(BlockFace.DOWN).getType().equals(block.getType())) {
				if (!checkedBlocks.contains(block.getRelative(BlockFace.DOWN)))
				{
					checkedBlocks.add(block.getRelative(BlockFace.DOWN));
					if (checkedBlocks.size() < maxSize)
						newBlocks.add(block.getRelative(BlockFace.DOWN));
					else
						return;
				}
			}
			if (block.getRelative(BlockFace.UP).getType().equals(block.getType())) {
				if (!checkedBlocks.contains(block.getRelative(BlockFace.UP)))
				{
					checkedBlocks.add(block.getRelative(BlockFace.UP));
					if (checkedBlocks.size() < maxSize)
						newBlocks.add(block.getRelative(BlockFace.UP));
					else
						return;
				}
			}
			if (block.getRelative(BlockFace.NORTH).getType().equals(block.getType())) {
				if (!checkedBlocks.contains(block.getRelative(BlockFace.NORTH)))
				{
					checkedBlocks.add(block.getRelative(BlockFace.NORTH));
					if (checkedBlocks.size() < maxSize)
						newBlocks.add(block.getRelative(BlockFace.NORTH));
					else
						return;
				}
			}
			if (block.getRelative(BlockFace.SOUTH).getType().equals(block.getType())) {
				if (!checkedBlocks.contains(block.getRelative(BlockFace.SOUTH)))
				{
					checkedBlocks.add(block.getRelative(BlockFace.SOUTH));
					if (checkedBlocks.size() < maxSize)
						newBlocks.add(block.getRelative(BlockFace.SOUTH));
					else
						return;
				}
			}
			if (block.getRelative(BlockFace.WEST).getType().equals(block.getType())) {
				if (!checkedBlocks.contains(block.getRelative(BlockFace.WEST)))
				{
					checkedBlocks.add(block.getRelative(BlockFace.WEST));
					if (checkedBlocks.size() < maxSize)
						newBlocks.add(block.getRelative(BlockFace.WEST));
					else
						return;
				}
			}
			if (block.getRelative(BlockFace.EAST).getType().equals(block.getType())) {
				if (!checkedBlocks.contains(block.getRelative(BlockFace.EAST)))
				{
					checkedBlocks.add(block.getRelative(BlockFace.EAST));
					if (checkedBlocks.size() < maxSize)
						newBlocks.add(block.getRelative(BlockFace.EAST));
					else
						return;
				}
			}
		}
		if(newBlocks.size() == 0) return;
		if(checkedBlocks.size() < maxSize) CheckSurroundingBlocks((ArrayList<Block>) newBlocks.clone(), maxSize);
	}

	public void HandleWideEnchant(BlockBreakEvent e)
	{
		if (veinMinedBlocks.contains(e.getBlock()))
			return;
		Player player = e.getPlayer();
		if (player == null)
			return;
		if (player.getInventory().getItemInMainHand() == null)
			return;
		if (!player.getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.WIDE))
			return;
		if (player.getGameMode() == GameMode.SPECTATOR)
			return;

		BlockFace face = GetBlockFace(player);
		if (face == null)
			return;
		ArrayList<Block> blocks = CheckWideBlocks(e.getBlock(), face);

		switch (ExpandedEnchants.getInstance().getConfig().getString("WideActivationMode"))
		{
		case "SNEAKING":
			if (!player.isSneaking())
				return;
			break;
		case "STANDING":
			if (player.isSneaking())
				return;
			break;
		case "BOTH":
			break;
		}
		Damageable itemMeta = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
		if (!itemMeta.isUnbreakable())
		{
			if (itemMeta.hasEnchant(Enchantment.DURABILITY))
				itemMeta.setDamage(
						itemMeta.getDamage() + (blocks.size() / itemMeta.getEnchantLevel(Enchantment.DURABILITY) + 1));
			else
				itemMeta.setDamage(itemMeta.getDamage() + blocks.size());
		}
		for (Block block : blocks)
		{
			if (block != null)
			{
				veinMinedBlocks.add(block);
				player.breakBlock(block);
			}
		}
		player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
		veinMinedBlocks.clear();
		// e.setDropItems(false);
	}

	public void HandleWideSpecialActionEnchant(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		if (player == null)
			return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (player.getInventory().getItemInMainHand() == null)
			return;
		if (!player.getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.WIDE))
			return;
		if (player.getGameMode() == GameMode.SPECTATOR)
			return;
		ItemStack item = player.getInventory().getItemInMainHand();
		switch (ExpandedEnchants.getInstance().getConfig().getString("WideSpecialActionMode"))
		{
		case "SNEAKING":
			if (!player.isSneaking())
				return;
			break;
		case "STANDING":
			if (player.isSneaking())
				return;
			break;
		case "BOTH":
			break;
		}
		if (Functions.CheckHoeTypes(item))
		{
			Material mat = e.getClickedBlock().getType();
			if (mat == Material.DIRT || mat == Material.GRASS_BLOCK || mat == Material.DIRT_PATH)
			{
				ArrayList<Block> blocks = GetSurroundingWideBlocks(e.getClickedBlock(), BlockFace.UP);
				for (Block block : blocks)
				{
					Material bmat = block.getType();
					if (bmat == Material.DIRT || bmat == Material.GRASS_BLOCK || bmat == Material.DIRT_PATH)
					{
						if (new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ()).getBlock()
								.getType() == Material.AIR)
						{
							block.setType(Material.FARMLAND);
						}
					}
				}
				Damageable itemMeta = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
				if (!itemMeta.isUnbreakable())
				{
					if (itemMeta.hasEnchant(Enchantment.DURABILITY))
						itemMeta.setDamage(itemMeta.getDamage()
								+ (blocks.size() / itemMeta.getEnchantLevel(Enchantment.DURABILITY) + 1));
					else
						itemMeta.setDamage(itemMeta.getDamage() + blocks.size());
				}
				item.setItemMeta(itemMeta);
			}
		}
		if (Functions.CheckAxeTypes(item))
		{
			if (!Functions.CheckNonStrippedLogTypes(e.getClickedBlock().getType()))
				return;
			ArrayList<Block> blocks = GetSurroundingWideBlocks(e.getClickedBlock(), GetBlockFace(player));
			for (Block block : blocks)
			{
				Material bmat = block.getType();
				if (Functions.CheckNonStrippedLogTypes(bmat))
				{
					block.setType(Functions.GetStrippedVariantWood(bmat));
				}
			}
			Damageable itemMeta = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
			if (!itemMeta.isUnbreakable())
			{
				if (itemMeta.hasEnchant(Enchantment.DURABILITY))
					itemMeta.setDamage(itemMeta.getDamage()
							+ (blocks.size() / itemMeta.getEnchantLevel(Enchantment.DURABILITY) + 1));
				else
					itemMeta.setDamage(itemMeta.getDamage() + blocks.size());
			}
			item.setItemMeta(itemMeta);
		}
		if (Functions.CheckShovelTypes(item))
		{
			Material mat = e.getClickedBlock().getType();
			if (mat == Material.DIRT || mat == Material.GRASS_BLOCK || mat == Material.COARSE_DIRT
					|| mat == Material.MYCELIUM || mat == Material.PODZOL || mat == Material.ROOTED_DIRT)
			{
				ArrayList<Block> blocks = GetSurroundingWideBlocks(e.getClickedBlock(), BlockFace.UP);
				for (Block block : blocks)
				{
					Material bmat = block.getType();
					if (bmat == Material.DIRT || bmat == Material.GRASS_BLOCK || bmat == Material.COARSE_DIRT
							|| bmat == Material.MYCELIUM || bmat == Material.PODZOL || bmat == Material.ROOTED_DIRT)
					{
						if (new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ()).getBlock()
								.getType() == Material.AIR)
						{
							block.setType(Material.DIRT_PATH);
						}
					}
				}
				Damageable itemMeta = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
				if (!itemMeta.isUnbreakable())
				{
					if (itemMeta.hasEnchant(Enchantment.DURABILITY))
						itemMeta.setDamage(itemMeta.getDamage()
								+ (blocks.size() / itemMeta.getEnchantLevel(Enchantment.DURABILITY) + 1));
					else
						itemMeta.setDamage(itemMeta.getDamage() + blocks.size());
				}
				item.setItemMeta(itemMeta);
			}
		}
	}

	public BlockFace GetBlockFace(Player player)
	{
		Set<Material> transparent = new HashSet<>();
		transparent.add(Material.WATER); transparent.add(Material.LAVA); transparent.add(Material.AIR);
		List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(transparent, 100);
		if (lastTwoTargetBlocks.size() != 2 /*|| !lastTwoTargetBlocks.get(1).getType().isOccluding()*/) {
			return null;
		}
			
		Block targetBlock = lastTwoTargetBlocks.get(1);
		Block adjacentBlock = lastTwoTargetBlocks.get(0);
		return targetBlock.getFace(adjacentBlock);
	}

	public ArrayList<Block> GetSurroundingWideBlocks(Block block, BlockFace face)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		Location loc = block.getLocation();

		if (face.equals(BlockFace.UP) || face.equals(BlockFace.DOWN))
		{
			blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ() + 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ() - 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ() - 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ() + 1).getBlock());
		}
		if (face.equals(BlockFace.SOUTH) || face.equals(BlockFace.NORTH))
		{
			blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY() + 1, loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY() - 1, loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY() - 1, loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY() + 1, loc.getZ()).getBlock());
		}
		if (face.equals(BlockFace.EAST) || face.equals(BlockFace.WEST))
		{
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ()).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ() + 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ() - 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ() - 1).getBlock());
			blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ() + 1).getBlock());
		}

		return blocks;
	}

	public ArrayList<Block> CheckWideBlocks(Block block, BlockFace face)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		Location loc = block.getLocation();

		if (face.equals(BlockFace.UP) || face.equals(BlockFace.DOWN))
		{
			if (new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ()).getBlock().getType().equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ() + 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ() + 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ() - 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ() - 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ() - 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ() - 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ() + 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ() + 1).getBlock());
		}
		if (face.equals(BlockFace.SOUTH) || face.equals(BlockFace.NORTH))
		{
			if (new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX() + 1, loc.getY() + 1, loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY() + 1, loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX() - 1, loc.getY() - 1, loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY() - 1, loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX() + 1, loc.getY() - 1, loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() + 1, loc.getY() - 1, loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX() - 1, loc.getY() + 1, loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX() - 1, loc.getY() + 1, loc.getZ()).getBlock());
		}
		if (face.equals(BlockFace.EAST) || face.equals(BlockFace.WEST))
		{
			if (new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ()).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ()).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ() + 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ() + 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ() - 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ() - 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ() - 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ() - 1).getBlock());
			if (new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ() + 1).getBlock().getType()
					.equals(block.getType()))
				blocks.add(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ() + 1).getBlock());
		}

		return blocks;
	}

	@SuppressWarnings("deprecation")
	public void HandleSteppingEnchant(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		if (player == null)
			return;
		if (player.getInventory().getBoots() == null) return;
		if (!player.getInventory().getBoots().hasItemMeta()) return;
		if (!player.getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantsManager.STEPPING)) return;
		
		Vector v = e.getPlayer().getVelocity();
		// Bukkit.getServer().getConsoleSender().sendMessage("x: " + (e.getTo().getX() -
		// e.getFrom().getX()) + ", " + (e.getTo().getY() - e.getFrom().getY()) + ", " +
		// (e.getTo().getZ() - e.getFrom().getZ()));
		if (new Location(e.getTo().getWorld(), e.getTo().getX() + 0.5, Math.round(e.getTo().getY()), e.getTo().getZ())
				.getBlock().getType() != Material.AIR)
		{// block infront exists
			if (e.getTo().getX() > e.getFrom().getX())
			{
				if (e.getTo().getX() - e.getFrom().getX() > 0.11)
				{
					if (new Location(e.getTo().getWorld(), e.getTo().getX() + 0.5, Math.round(e.getTo().getY()),
							e.getTo().getZ()).getBlock().getType().isSolid())
					{ // is solid
						if (new Location(e.getTo().getWorld(), e.getTo().getX() + 0.5, Math.round(e.getTo().getY()) + 1,
								e.getTo().getZ()).getBlock().getType().isTransparent())
						{ // block above other is air
							if (new Location(e.getFrom().getWorld(), e.getFrom().getX(),
									Math.round(e.getFrom().getY()) - 1, e.getFrom().getZ()).getBlock()
											.getType() != Material.AIR)
							{ // underneath is not air
								e.setTo(new Location(e.getTo().getWorld(), e.getTo().getX() + 0.3,
										Math.round(e.getTo().getY()) + 1, e.getTo().getZ(), e.getTo().getYaw(),
										e.getTo().getPitch()));
								Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
								{
									e.getPlayer().setVelocity(v);
								});

							}
						}
					}
				}
			}
		}
		if (new Location(e.getTo().getWorld(), e.getTo().getX() - 0.5, Math.round(e.getTo().getY()), e.getTo().getZ())
				.getBlock().getType() != Material.AIR)
		{// block infront exists
			if (e.getTo().getX() < e.getFrom().getX())
			{
				if (e.getTo().getX() - e.getFrom().getX() < -0.11)
				{
					if (new Location(e.getTo().getWorld(), e.getTo().getX() - 0.5, Math.round(e.getTo().getY()),
							e.getTo().getZ()).getBlock().getType().isSolid())
					{ // is solid
						if (new Location(e.getTo().getWorld(), e.getTo().getX() - 0.5, Math.round(e.getTo().getY()) + 1,
								e.getTo().getZ()).getBlock().getType().isTransparent())
						{ // block above other is air
							if (new Location(e.getFrom().getWorld(), e.getFrom().getX(),
									Math.round(e.getFrom().getY()) - 1, e.getFrom().getZ()).getBlock()
											.getType() != Material.AIR)
							{ // underneath is not air
								e.setTo(new Location(e.getTo().getWorld(), e.getTo().getX() - 0.3,
										Math.round(e.getTo().getY()) + 1, e.getTo().getZ(), e.getTo().getYaw(),
										e.getTo().getPitch()));
								Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
								{
									e.getPlayer().setVelocity(v);
								});

							}
						}
					}
				}
			}
		}
		if (new Location(e.getTo().getWorld(), e.getTo().getX(), Math.round(e.getTo().getY()), e.getTo().getZ() + 0.5)
				.getBlock().getType() != Material.AIR)
		{// block infront exists
			if (e.getTo().getZ() > e.getFrom().getZ())
			{
				if (e.getTo().getZ() - e.getFrom().getZ() > 0.11)
				{
					if (new Location(e.getTo().getWorld(), e.getTo().getX(), Math.round(e.getTo().getY()),
							e.getTo().getZ() + 0.5).getBlock().getType().isSolid())
					{ // is solid
						if (new Location(e.getTo().getWorld(), e.getTo().getX(), Math.round(e.getTo().getY()) + 1,
								e.getTo().getZ() + 0.5).getBlock().getType().isTransparent())
						{ // block above other is air
							if (new Location(e.getFrom().getWorld(), e.getFrom().getX(),
									Math.round(e.getFrom().getY()) - 1, e.getFrom().getZ()).getBlock()
											.getType() != Material.AIR)
							{ // underneath is not air
								e.setTo(new Location(e.getTo().getWorld(), e.getTo().getX(),
										Math.round(e.getTo().getY()) + 1, e.getTo().getZ() + 0.3, e.getTo().getYaw(),
										e.getTo().getPitch()));
								Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
								{
									e.getPlayer().setVelocity(v);
								});

							}
						}
					}
				}
			}
		}
		if (new Location(e.getTo().getWorld(), e.getTo().getX(), Math.round(e.getTo().getY()), e.getTo().getZ() - 0.5)
				.getBlock().getType() != Material.AIR)
		{// block infront exists
			if (e.getTo().getZ() < e.getFrom().getZ())
			{
				if (e.getTo().getZ() - e.getFrom().getZ() < -0.11)
				{
					if (new Location(e.getTo().getWorld(), e.getTo().getX(), Math.round(e.getTo().getY()),
							e.getTo().getZ() - 0.5).getBlock().getType().isSolid())
					{ // is solid
						if (new Location(e.getTo().getWorld(), e.getTo().getX(), Math.round(e.getTo().getY()) + 1,
								e.getTo().getZ() - 0.5).getBlock().getType().isTransparent())
						{ // block above other is air
							if (new Location(e.getFrom().getWorld(), e.getFrom().getX(),
									Math.round(e.getFrom().getY()) - 1, e.getFrom().getZ()).getBlock()
											.getType() != Material.AIR)
							{ // underneath is not air
								e.setTo(new Location(e.getTo().getWorld(), e.getTo().getX(),
										Math.round(e.getTo().getY()) + 1, e.getTo().getZ() - 0.3, e.getTo().getYaw(),
										e.getTo().getPitch()));
								Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
								{
									e.getPlayer().setVelocity(v);
								});

							}
						}
					}
				}
			}
		}
	}

	public void HandleGourmandEnchant(FoodLevelChangeEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			if (player == null)
				return;
			if (player.getInventory().getHelmet() == null)
				return;
			if (!player.getInventory().getHelmet().hasItemMeta())
				return;
			if (!player.getInventory().getHelmet().getItemMeta().hasEnchant(CustomEnchantsManager.GOURMAND))
				return;
			if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
				return;
			if (player.getInventory().getHelmet().getItemMeta().hasEnchant(CustomEnchantsManager.FEEDINGMODULE))
				return;
			if (e.getItem() == null)
				return;
			double level = 1.3;
			if (player.getInventory().getHelmet().getItemMeta().getEnchantLevel(CustomEnchantsManager.GOURMAND) == 2)
				level = 1.5;
			int extra = (int) Math.round(Functions.GetFoodPoints(e.getItem().getType()) * level);
			e.setFoodLevel(player.getFoodLevel() + extra);
		}
	}

	public void HandleFeedingModuleEnchant(FoodLevelChangeEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			if (player == null)
				return;
			if (player.getInventory().getHelmet() == null)
				return;
			if (!player.getInventory().getHelmet().hasItemMeta())
				return;
			if (!player.getInventory().getHelmet().getItemMeta().hasEnchant(CustomEnchantsManager.FEEDINGMODULE))
				return;
			if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
				return;
			if (!Functions.PlayerContainsFoodItems(player))
				return;
			ItemStack item = Functions.GetFirstFoodItem(player);
			if (item == null)
				return;
			double multiplier = 1;
			if (player.getInventory().getHelmet().getItemMeta().hasEnchant(CustomEnchantsManager.GOURMAND)
					&& ExpandedEnchants.getInstance().getConfig().getBoolean("GourmandEnabled"))
			{
				if (player.getInventory().getHelmet().getItemMeta()
						.getEnchantLevel(CustomEnchantsManager.GOURMAND) == 1)
					multiplier = 1.3;
				else
					multiplier = 1.5;
			}
			int foodValue = (int) Math.round(Functions.GetFoodPoints(item.getType()) * multiplier);
			if (player.getFoodLevel() == 20)
				return;
			if ((20 - player.getFoodLevel()) >= foodValue)
			{
				int totalEat = player.getFoodLevel() + foodValue;
				player.setFoodLevel(totalEat);
				e.setFoodLevel(totalEat);
			} else if (ExpandedEnchants.getInstance().getConfig().getBoolean("EnableSaturationMode") && player.getFoodLevel() < 20)
			{
				e.setFoodLevel(player.getFoodLevel() + foodValue);
			} else
				return;
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
			item.setAmount(item.getAmount() - 1);
		}
	}

	@SuppressWarnings("deprecation")
	public void HandleLifestealEnchant(EntityDeathEvent e)
	{
		LivingEntity mob = e.getEntity();
		Player player = mob.getKiller();
		if (player == null)
			return;
		if (player.getInventory().getItemInMainHand() == null)
			return;
		if (!player.getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.LIFESTEAL))
			return;
		if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
			return;
		int level = player.getInventory().getItemInMainHand().getItemMeta()
				.getEnchantLevel(CustomEnchantsManager.LIFESTEAL);
		double health = player.getHealth();
		double healing = 1 * level;
		double total = health + healing;
		if (total > player.getMaxHealth())
			total = player.getMaxHealth();
		player.setHealth(total);
	}

	public void HandleLavaWalkerEnchant(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		if (player == null)
			return;
		if (player.getInventory().getBoots() == null)
			return;
		if (!player.getInventory().getBoots().hasItemMeta())
			return;
		if (!player.getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantsManager.LAVA_WALKER))
			return;

		Location location = e.getTo();
		Location underLoc = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
		Block block = underLoc.getBlock();
		if (block.getType() == Material.LAVA)
		{
			block.setType(Material.BASALT);
		}
	}

	public void HandleExpBoostEnchant(PlayerExpChangeEvent e)
	{
		Player player = e.getPlayer();
		if (player == null)
			return;
		if (player.getInventory().getChestplate() == null)
			return;
		if (!player.getInventory().getChestplate().hasItemMeta())
			return;
		if (!player.getInventory().getChestplate().getItemMeta().hasEnchant(CustomEnchantsManager.EXP_BOOST))
			return;
		if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)
			return;

		double chance = player.getInventory().getChestplate().getItemMeta()
				.getEnchantLevel(CustomEnchantsManager.EXP_BOOST) * 0.15;
		double random = Math.random();

		if (random < chance)
		{
			e.setAmount(e.getAmount() * 2);
		}

	}

	public void HandleBeheadingEnchant(EntityDeathEvent e)
	{
		LivingEntity mob = e.getEntity();
		Player player = mob.getKiller();
		if (player == null)
			return;
		if (player.getInventory().getItemInMainHand() == null)
			return;
		if (!player.getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.BEHEADING))
			return;
		if (player.getGameMode() == GameMode.SPECTATOR)
			return;

		double chance = player.getInventory().getItemInMainHand().getItemMeta()
				.getEnchantLevel(CustomEnchantsManager.BEHEADING) * 0.05;
		double random = Math.random();

		if (random < chance)
		{
			if (mob.getType() == EntityType.ZOMBIE || mob.getType() == EntityType.ZOMBIE_VILLAGER
					|| mob.getType() == EntityType.HUSK)
				e.getDrops().add(new ItemStack(Material.ZOMBIE_HEAD));
			if (mob.getType() == EntityType.SKELETON || mob.getType() == EntityType.STRAY)
				e.getDrops().add(new ItemStack(Material.SKELETON_SKULL));
			if (mob.getType() == EntityType.WITHER_SKELETON)
				e.getDrops().add(new ItemStack(Material.WITHER_SKELETON_SKULL));
			if (mob.getType() == EntityType.CREEPER)
				e.getDrops().add(new ItemStack(Material.CREEPER_HEAD));
			if (mob.getType() == EntityType.ENDER_DRAGON)
				e.getDrops().add(new ItemStack(Material.DRAGON_HEAD));
		}
	}

	public void HandleAntiGravityEnchant(EntityShootBowEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			if (player.getInventory().getItemInMainHand() == null)
				return;
			if (!player.getInventory().getItemInMainHand().hasItemMeta())
				return;
			if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.ANTIGRAVITY))
				return;
			Entity arrow = e.getProjectile();
			arrow.setGravity(false);
		}
	}

	public void HandleDirectEntityEnchant(EntityDeathEvent e)
	{
		LivingEntity mob = e.getEntity();
		Player player = mob.getKiller();
		if (player == null)
			return;
		if (player.getInventory().getItemInMainHand() == null)
			return;
		if (!player.getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.DIRECT))
			return;
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
			return;
		if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.AUTOSMELT)) return;

		List<ItemStack> drops = e.getDrops();
		if (drops.isEmpty())
			return;
		ItemStack[] items = new ItemStack[drops.size()];
		for(int i = 0; i < drops.size(); i++) {
			items[i] = drops.get(i);
			//Main.Log("DropAmount: " + items[i].getAmount());
		}
		HashMap<Integer, ItemStack>	notFit = player.getInventory().addItem(items);
		if(!notFit.isEmpty()) {
			for(int i = 0; i < drops.size(); i++) {
				//Main.Log("Item: " + drops.get(i).getItemStack());
				boolean remove = true;
				//Main.Log("AmountBefore: " + event.getItems().get(i).getItemStack().getAmount());
				for(Entry<Integer, ItemStack> entry : notFit.entrySet()) if(entry.getValue().getType().equals(e.getDrops().get(i).getType())) {
					remove = false;
					ItemStack editted = e.getDrops().get(i);
					//Main.Log("EntryAmount: " + entry.getValue().getAmount());
					editted.setAmount(entry.getValue().getAmount());
					e.getDrops().set(i, editted);
				}
				if(remove) e.getDrops().remove(i);
				//Main.Log("AmountAfter: " + event.getItems().get(i).getItemStack().getAmount());
			}
		}
		else e.getDrops().clear();
	}

	public void HandleDirectBlockEnchant(BlockDropItemEvent event)
	{
		if (event.getPlayer().getInventory().getItemInMainHand() == null)
			return;
		if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.DIRECT))
			return;
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR)
			return;
			
		if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.AUTOSMELT)) return;
		Player player = event.getPlayer();

		List<Item> drops = event.getItems();
		if (drops.isEmpty())
			return;
		ItemStack[] items = new ItemStack[drops.size()];
		for(int i = 0; i < drops.size(); i++) {
			items[i] = drops.get(i).getItemStack(); 
			//Main.Log("DropAmount: " + items[i].getAmount());
		}
		HashMap<Integer, ItemStack>	notFit = player.getInventory().addItem(items);
		if(!notFit.isEmpty()) {
			for(int i = 0; i < drops.size(); i++) {
				//Main.Log("Item: " + drops.get(i).getItemStack());
				boolean remove = true;
				//Main.Log("AmountBefore: " + event.getItems().get(i).getItemStack().getAmount());
				for(Entry<Integer, ItemStack> entry : notFit.entrySet()) if(entry.getValue().getType().equals(event.getItems().get(i).getItemStack().getType())) {
					remove = false;
					ItemStack editted = event.getItems().get(i).getItemStack();
					//Main.Log("EntryAmount: " + entry.getValue().getAmount());
					editted.setAmount(entry.getValue().getAmount());
					event.getItems().get(i).setItemStack(editted);
				}
				if(remove) event.getItems().remove(i);
				//Main.Log("AmountAfter: " + event.getItems().get(i).getItemStack().getAmount());
			}
		}
		else event.setCancelled(true);
		
	}

	public void HandleAutoSmeltEnchant(BlockDropItemEvent e)
	{
		if (e.getBlock() == null)
			return;
		if (e.getPlayer().getInventory().getItemInMainHand() == null)
			return;
		if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.AUTOSMELT))
			return;
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE || e.getPlayer().getGameMode() == GameMode.SPECTATOR)
			return;
		if (e.getBlock().getState() instanceof Container)
			return;
		List<Item> drops = e.getItems();
		int i = 0;
		for (Item item : drops)
		{
			if (item == null)
				continue;
			ItemStack dropItem = item.getItemStack();
			if(Functions.IsSmeltable(item)) dropItem.setType(Functions.SmeltedCounterpart(item.getItemStack().getType()));
			if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantsManager.DIRECT)) {
				HashMap<Integer, ItemStack>	notFit = e.getPlayer().getInventory().addItem((dropItem));
				if(!notFit.isEmpty()) e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), dropItem);
				
			}
			else {
				e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(dropItem));
			}
			if(ExpandedEnchants.getInstance().getConfig().getBoolean("RewardXpOnAutosmelt") && Functions.IsXpAutosmeltDrop(item)) {
				ExperienceOrb orb = (ExperienceOrb) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.EXPERIENCE_ORB);
				orb.setExperience(1);
			}
			i++;
			
		}
		for (int y = 0; y < i; y++)
		{
			e.getItems().remove(y);
		}

	}

	public Map<UUID, Enchantment> removeLastEnchant = new HashMap<>();
	public Map<UUID, Boolean> isModified = new HashMap<>();

	@SuppressWarnings("deprecation")
	public void HandleDisenchant(ItemStack fs, ItemStack ss, PrepareAnvilEvent e)
	{
		if (ss.getType() != Material.BOOK)
			return;
		if (fs.getType() == Material.ENCHANTED_BOOK)
		{
			if (Functions.ContainsCustomEnchant(fs))
				return;
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) fs.getItemMeta();
			Object[] enchs = meta.getStoredEnchants().keySet().toArray();
			if (enchs.length <= 1)
				return;
			Enchantment removeEnchant = (Enchantment) enchs[enchs.length - 1];
			for(Object i : enchs) {
				Enchantment ench = (Enchantment) i;

				if(e.getInventory().getRenameText().equalsIgnoreCase(Functions.GetEnchantmentName(ench))) removeEnchant = ench;
			}
			ItemStack resultBook = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta resultMeta = (EnchantmentStorageMeta) resultBook.getItemMeta();
			resultMeta.addStoredEnchant(removeEnchant, meta.getStoredEnchantLevel(removeEnchant), true);

			resultBook.setItemMeta(resultMeta);
			meta.removeStoredEnchant(removeEnchant);
			for (HumanEntity he : e.getInventory().getViewers()) {
				removeLastEnchant.put(he.getUniqueId(), removeEnchant);
			}
			e.setResult(resultBook);
			for (HumanEntity he : e.getViewers())
				isModified.put(he.getUniqueId(), true);
		} 
		else
		{
			Object[] enchs = fs.getItemMeta().getEnchants().keySet().toArray();
			if (enchs.length == 0)
				return;
			Enchantment removeEnchant = (Enchantment) enchs[enchs.length - 1];
			for(Object i : enchs) {
				Enchantment ench = (Enchantment) i;
				if(e.getInventory().getRenameText().equalsIgnoreCase(ench.getName())) removeEnchant = ench;
			}
			ItemStack resultBook = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta resultMeta = (EnchantmentStorageMeta) resultBook.getItemMeta();
			if (Functions.IsCustomEnchant(removeEnchant))
			{
				resultBook.addUnsafeEnchantment(removeEnchant, fs.getItemMeta().getEnchantLevel(removeEnchant));
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY + removeEnchant.getName() + " " + Functions.GetNameByLevel(fs.getItemMeta().getEnchantLevel(removeEnchant), removeEnchant.getMaxLevel()));
				ItemMeta customResultMeta = resultBook.getItemMeta();
				customResultMeta.setLore(lore);
				//customResultMeta.setDisplayName(e.getInventory().getRenameText());
				resultBook.setItemMeta(customResultMeta);
				for (HumanEntity he : e.getInventory().getViewers()) {
					removeLastEnchant.put(he.getUniqueId(), removeEnchant);
				}
				e.setResult(resultBook);
				for (HumanEntity he : e.getInventory().getViewers())
					isModified.put(he.getUniqueId(), true);
			} 
			else
			{
				resultMeta.addStoredEnchant(removeEnchant, fs.getItemMeta().getEnchantLevel(removeEnchant), true);
				for (HumanEntity he : e.getInventory().getViewers()) {
					removeLastEnchant.put(he.getUniqueId(), removeEnchant);
				}
				//resultMeta.setDisplayName(e.getInventory().getRenameText());
				resultBook.setItemMeta(resultMeta);
				e.setResult(resultBook);
				for (HumanEntity he : e.getViewers())
					isModified.put(he.getUniqueId(), true);
			}
		}
	}

	
	public void HandleBookResourceEnchant(ItemStack fs, ItemStack ss, PrepareAnvilEvent e)
	{
		if (fs.getType() == Material.BOOK)
		{
			Enchantment ench = ExpandedEnchants.recipeManager.GetEnchantment(ss.getType());
			if (ench == null)
				return;
			ItemStack resultItem = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) resultItem.getItemMeta();
			if (ss.getAmount() < ExpandedEnchants.recipeManager.GetAmount(ench))
				return;
			meta.addStoredEnchant(ench, 1, true);
			meta.setDisplayName(e.getInventory().getRenameText());
			resultItem.setItemMeta(meta);
			e.setResult(resultItem);

			for (HumanEntity he : e.getViewers())
				isModified.put(he.getUniqueId(), true);
			Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
			{
				if (ExpandedEnchants.getInstance().getConfig().getBoolean("ResourceEnchantXpEnabled"))
					e.getInventory().setRepairCost(1);
				for (HumanEntity he : e.getViewers())
				{
					Player pl = (Player) he;
					pl.updateInventory();
				}
			});
		}
		if (fs.getType() == Material.ENCHANTED_BOOK)
		{
			if (Functions.ContainsCustomEnchant(fs))
				return;
			Enchantment ench = ExpandedEnchants.recipeManager.GetEnchantment(ss.getType());
			if (ench == null)
				return;
			ItemStack resultItem = new ItemStack(fs);
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) fs.getItemMeta();
			;
			if (ss.getAmount() < ExpandedEnchants.recipeManager.GetAmount(ench))
				return;
			int MaxLevel = ExpandedEnchants.getInstance().getConfig().getInt("MaxVanillaEnchantLevel");
			if(!ExpandedEnchants.getInstance().getConfig().getBoolean("EnableVanillaOverride")) MaxLevel = ench.getMaxLevel();
			if (meta.hasStoredEnchant(ench))
			{
				if (ench.getMaxLevel() == 1)
					meta.addStoredEnchant(ench, 1, false);
				else if (ench == Enchantment.LURE)
				{
					if (meta.getStoredEnchantLevel(ench) < (MaxLevel < 3 ? MaxLevel : 3))
					{
						meta.addStoredEnchant(ench, meta.getStoredEnchantLevel(ench) + 1, true);
					}
				} else if (ench == Enchantment.DEPTH_STRIDER)
				{
					if (meta.getStoredEnchantLevel(ench) < (MaxLevel < 3 ? MaxLevel : 3))
					{
						meta.addStoredEnchant(ench, meta.getStoredEnchantLevel(ench) + 1, true);
					}
				} else if (ench == Enchantment.QUICK_CHARGE)
				{
					if (meta.getStoredEnchantLevel(ench) < (MaxLevel < 5 ? MaxLevel : 5))
					{
						meta.addStoredEnchant(ench, meta.getStoredEnchantLevel(ench) + 1, true);
					}
				} else if (meta.getStoredEnchantLevel(ench) < MaxLevel)
				{
					meta.addStoredEnchant(ench, meta.getStoredEnchantLevel(ench) + 1, true);
				}
			} else if (!meta.hasConflictingStoredEnchant(ench))
			{
				meta.addStoredEnchant(ench, 1, true);
			}
			meta.setDisplayName(e.getInventory().getRenameText());
			resultItem.setItemMeta(meta);
			if (resultItem.equals(fs))
				resultItem = null;
			e.setResult(resultItem);

			for (HumanEntity he : e.getViewers())
				isModified.put(he.getUniqueId(), true);
			Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
			{
				if (ExpandedEnchants.getInstance().getConfig().getBoolean("ResourceEnchantXpEnabled"))
					e.getInventory().setRepairCost((meta.getStoredEnchantLevel(ench) + 1) * (meta.getStoredEnchantLevel(ench) + 1));
				e.getInventory().setMaximumRepairCost(e.getInventory().getRepairCost() + 1);
				for (HumanEntity he : e.getViewers())
				{
					Player pl = (Player) he;
					pl.updateInventory();
				}
			});
			
		}

	}

	public void HandleBookEnchant(ItemStack fs, ItemStack ss, PrepareAnvilEvent e)
	{
		if (ss.getType() != Material.ENCHANTED_BOOK || fs.getType() == Material.ENCHANTED_BOOK)
			return;

		if (Functions.ContainsCustomEnchant(ss))
			return;
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) ss.getItemMeta();
		ItemMeta itemMeta = fs.getItemMeta();
		ItemStack resultItem = new ItemStack(fs);
		ItemMeta resultMeta = resultItem.getItemMeta();
		for (Enchantment item : meta.getStoredEnchants().keySet())
		{
			for (Enchantment item2 : resultMeta.getEnchants().keySet())
			{
				if (item.equals(item2))
					continue;
				if (item.conflictsWith(item2) || item2.conflictsWith(item)) {
					e.setResult(null);
					return;
				}
			}
			if (!item.canEnchantItem(fs)) {
				e.setResult(null);
				return;
			}
			if (fs.getItemMeta().hasEnchant(item))
			{
				if (meta.getStoredEnchantLevel(item) == itemMeta.getEnchantLevel(item))
				{
					int MaxLvl = ExpandedEnchants.getInstance().getConfig().getInt("MaxVanillaEnchantLevel");
					if(!ExpandedEnchants.getInstance().getConfig().getBoolean("EnableVanillaOverride")) MaxLvl = item.getMaxLevel();
					if (item.getMaxLevel() == 1);
					else if (item == Enchantment.QUICK_CHARGE)
					{
						if (itemMeta.getEnchantLevel(item) < (MaxLvl < 5 ? MaxLvl : 5))
							resultMeta.addEnchant(item, itemMeta.getEnchantLevel(item) + 1, true);
					} else if (item == Enchantment.LURE)
					{
						if (itemMeta.getEnchantLevel(item) < (MaxLvl < 3 ? MaxLvl : 3))
							resultMeta.addEnchant(item, itemMeta.getEnchantLevel(item) + 1, true);
					} else if (item == Enchantment.DEPTH_STRIDER)
					{
						if (itemMeta.getEnchantLevel(item) < (MaxLvl < 3 ? MaxLvl : 3))
							resultMeta.addEnchant(item, itemMeta.getEnchantLevel(item) + 1, true);
					} else if (itemMeta.getEnchantLevel(item) < MaxLvl)
					{
						resultMeta.addEnchant(item, itemMeta.getEnchantLevel(item) + 1, true);
					}
				}
				if (meta.getStoredEnchantLevel(item) > itemMeta.getEnchantLevel(item))
				{
					resultMeta.addEnchant(item, meta.getStoredEnchantLevel(item), true);
				}
				if (meta.getStoredEnchantLevel(item) < itemMeta.getEnchantLevel(item))
				{
					resultMeta.addEnchant(item, itemMeta.getEnchantLevel(item), true);
				}
			} else
			{
				resultMeta.addEnchant(item, meta.getStoredEnchantLevel(item), true);
			}
			resultMeta.setDisplayName(e.getInventory().getRenameText());
			resultItem.setItemMeta(resultMeta);
		}
		if (fs.equals(resultItem))
			resultItem = null;
		e.setResult(resultItem);

		for (HumanEntity he : e.getViewers())
			isModified.put(he.getUniqueId(), true);
		Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
		{
			e.getInventory().setRepairCost(5);
			for (HumanEntity he : e.getViewers())
			{
				Player pl = (Player) he;
				pl.updateInventory();
			}
		});

	}

	@SuppressWarnings("deprecation")
	public void HandleBookMerge(ItemStack fs, ItemStack ss, PrepareAnvilEvent e)
	{
		if (fs.getType() != Material.ENCHANTED_BOOK || ss.getType() != Material.ENCHANTED_BOOK)
			return;
		if (Functions.ContainsCustomEnchant(fs) && Functions.ContainsCustomEnchant(ss))
		{
			Enchantment ench = (Enchantment) fs.getEnchantments().keySet().toArray()[0];
			if (!ench.equals(ss.getEnchantments().keySet().toArray()[0]))
				return;
			int highestlevel = fs.getItemMeta().getEnchantLevel(ench) >= ss.getItemMeta().getEnchantLevel(ench)
					? fs.getItemMeta().getEnchantLevel(ench)
					: ss.getItemMeta().getEnchantLevel(ench);
			ItemStack item = new ItemStack(fs);
			if (fs.getItemMeta().getEnchantLevel(ench) == ss.getItemMeta().getEnchantLevel(ench))
				highestlevel = fs.getItemMeta().getEnchantLevel(ench) + 1;
			if (highestlevel > ench.getMaxLevel())
				return;
			item.addUnsafeEnchantment(ench, highestlevel);
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + ench.getName() + " "
					+ Functions.GetNameByLevel(item.getItemMeta().getEnchantLevel(ench), ench.getMaxLevel()));
			meta.setLore(lore);
			meta.setDisplayName(e.getInventory().getRenameText());
			item.setItemMeta(meta);
			e.setResult(item);

			for (HumanEntity he : e.getViewers())
				isModified.put(he.getUniqueId(), true);
			Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
			{
				e.getInventory().setRepairCost(5);
				for (HumanEntity he : e.getViewers())
				{
					Player pl = (Player) he;
					pl.updateInventory();
				}
			});
			return;
		} else if (Functions.ContainsCustomEnchant(fs) || Functions.ContainsCustomEnchant(ss))
		{
			e.setResult(null);

			return;
		}
		ItemStack resultItem = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta fsMeta = (EnchantmentStorageMeta) fs.getItemMeta();
		EnchantmentStorageMeta ssMeta = (EnchantmentStorageMeta) ss.getItemMeta();
		EnchantmentStorageMeta resultMeta = fsMeta;

		Set<Enchantment> ssEnchs = ssMeta.getStoredEnchants().keySet();

		for (Enchantment item : ssEnchs)
		{
			if (resultMeta.hasStoredEnchant(item))
			{
				if (item.getMaxLevel() == 1)
					resultMeta.addStoredEnchant(item, 1, false);
				else if (item.equals(Enchantment.QUICK_CHARGE) && (resultMeta.getStoredEnchantLevel(item) == 5 || ssMeta.getStoredEnchantLevel(item) == 5))
				{
					resultMeta.addStoredEnchant(item, 5, true);
				}
				else if (item.equals(Enchantment.LURE) && (resultMeta.getStoredEnchantLevel(item) == 3 || ssMeta.getStoredEnchantLevel(item) == 3))
				{
					resultMeta.addStoredEnchant(item, 3, true);
				} 
				else if (item.equals(Enchantment.DEPTH_STRIDER) && (resultMeta.getStoredEnchantLevel(item) == 3 || ssMeta.getStoredEnchantLevel(item) == 3))
				{
					resultMeta.addStoredEnchant(item, 3, true);
				} 
				else if (resultMeta.getStoredEnchantLevel(item) == 10 || ssMeta.getStoredEnchantLevel(item) == 10)
				{
					resultMeta.addStoredEnchant(item, 10, true);
				} 
				else if (resultMeta.getStoredEnchantLevel(item) == ssMeta.getStoredEnchantLevel(item))
				{
				int Maxlvl = ExpandedEnchants.getInstance().getConfig().getInt("MaxVanillaEnchantLevel");
				if(!ExpandedEnchants.getInstance().getConfig().getBoolean("EnableVanillaOverride")) Maxlvl = item.getMaxLevel();
				if (resultMeta.getStoredEnchantLevel(item) < Maxlvl)
						resultMeta.addStoredEnchant(item, resultMeta.getStoredEnchantLevel(item) + 1, true);
				}
				else if (resultMeta.getStoredEnchantLevel(item) > ssMeta.getStoredEnchantLevel(item))
				{
					resultMeta.addStoredEnchant(item, resultMeta.getStoredEnchantLevel(item), true);
				} 
				else if (resultMeta.getStoredEnchantLevel(item) < ssMeta.getStoredEnchantLevel(item))
				{
					resultMeta.addStoredEnchant(item, ssMeta.getStoredEnchantLevel(item), true);
				}

			} else if (!resultMeta.hasConflictingStoredEnchant(item))
			{
				resultMeta.addStoredEnchant(item, ssMeta.getStoredEnchantLevel(item), true);
			}
		}
		resultMeta.setDisplayName(e.getInventory().getRenameText());
		resultItem.setItemMeta(resultMeta);
		if (resultItem.equals(fs))
			resultItem = null;
		e.setResult(resultItem);

		for (HumanEntity he : e.getViewers())
			isModified.put(he.getUniqueId(), false);
		Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
		{
			e.getInventory().setRepairCost(5);
			for (HumanEntity he : e.getViewers())
			{
				Player pl = (Player) he;
				pl.updateInventory();
			}
		});

	}

	
	@SuppressWarnings("deprecation")
	public void HandleCustomEnchantBooks(ItemStack fs, ItemStack ss, PrepareAnvilEvent e)
	{
		if (ss.getType() != Material.ENCHANTED_BOOK)
			return;
		if (fs.getType() == Material.ENCHANTED_BOOK)
			return;
		if (!Functions.ContainsCustomEnchant(ss))
			return;
		Enchantment ench = (Enchantment) ss.getEnchantments().keySet().toArray()[0];
		int level = ss.getItemMeta().getEnchantLevel(ench);
		if (fs.getItemMeta().hasEnchant(ench))
		{
			if (fs.getItemMeta().getEnchantLevel(ench) > level)
				return;
			if (ench.getMaxLevel() == 1)
				return;
			ItemStack resultItem = new ItemStack(fs);
			int addLevel = fs.getItemMeta().getEnchantLevel(ench) == level ? level + 1 : level;
			if (addLevel > ench.getMaxLevel())
				return;
			resultItem.addUnsafeEnchantment(ench, addLevel);
			ItemMeta meta = resultItem.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if (meta.hasLore())
				lore = meta.getLore();
			int i = 0;
			int num = -1;
			for (String line : lore)
			{
				if (line.contains(ench.getName()))
				{
					num = i;
				}
				i++;
			}
			lore.remove(num);
			lore.add(ChatColor.GRAY + ench.getName() + " "
					+ Functions.GetNameByLevel(resultItem.getItemMeta().getEnchantLevel(ench), ench.getMaxLevel()));
			meta.setLore(lore);
			meta.setDisplayName(e.getInventory().getRenameText());
			resultItem.setItemMeta(meta);
			if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.TRAVELER))
			{
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
				
				itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED,
						new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed",
								itemMeta.getEnchantLevel(CustomEnchantsManager.TRAVELER) * .4,
								AttributeModifier.Operation.ADD_SCALAR, EquipmentSlot.LEGS));
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				resultItem.setItemMeta(itemMeta);
			}
			if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.STONEFISTS))
			{
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
				
				itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
						new AttributeModifier(UUID.randomUUID(), "generic.attackDamage",
								itemMeta.getEnchantLevel(CustomEnchantsManager.STONEFISTS) * 3,
								AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				resultItem.setItemMeta(itemMeta);
			}
			if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.HEALTHBOOST))
			{
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
				
				itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH,
						new AttributeModifier(UUID.randomUUID(), "generic.maxHealth",
								itemMeta.getEnchantLevel(CustomEnchantsManager.HEALTHBOOST) * 2,
								AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				resultItem.setItemMeta(itemMeta);
			}
			e.setResult(resultItem);

			for (HumanEntity he : e.getViewers())
				isModified.put(he.getUniqueId(), true);
			Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
			{
				e.getInventory().setRepairCost(10);
				for (HumanEntity he : e.getViewers())
				{
					Player pl = (Player) he;
					pl.updateInventory();
				}
			});
			return;
		}
		if (!ench.canEnchantItem(fs))
			return;
		if (fs.getItemMeta().hasConflictingEnchant(ench))
			return;

		for(Enchantment enc : fs.getItemMeta().getEnchants().keySet()) {
			if(enc.conflictsWith(ench) || ench.conflictsWith(enc)) return;
		}
		
		ItemStack resultItem = new ItemStack(fs);
		resultItem.addUnsafeEnchantment(ench, ss.getItemMeta().getEnchantLevel(ench));
		ItemMeta meta = resultItem.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if (meta.hasLore())
			lore = meta.getLore();
		lore.add(ChatColor.GRAY + ench.getName() + " "
				+ Functions.GetNameByLevel(resultItem.getItemMeta().getEnchantLevel(ench), ench.getMaxLevel()));
		meta.setLore(lore);
		meta.setDisplayName(e.getInventory().getRenameText());
		resultItem.setItemMeta(meta);
		if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.NOBREAKABLE))
		{
			resultItem.setDurability((short) 100000);
			ItemMeta itemMeta = resultItem.getItemMeta();
			itemMeta.setUnbreakable(true);
			resultItem.setItemMeta(itemMeta);
			resultItem.removeEnchantment(Enchantment.DURABILITY);
			resultItem.removeEnchantment(Enchantment.MENDING);
		}
		if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.TRAVELER))
		{
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
			
			itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED,
					new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed",
							itemMeta.getEnchantLevel(CustomEnchantsManager.TRAVELER) * .4,
							AttributeModifier.Operation.ADD_SCALAR, EquipmentSlot.LEGS));
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			resultItem.setItemMeta(itemMeta);
		}
		if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.STONEFISTS))
		{
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
			
			itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
					new AttributeModifier(UUID.randomUUID(), "generic.attackDamage",
							itemMeta.getEnchantLevel(CustomEnchantsManager.STONEFISTS) * 3,
							AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			resultItem.setItemMeta(itemMeta);
		}
		if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.HEALTHBOOST))
		{
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
			
			itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH,
					new AttributeModifier(UUID.randomUUID(), "generic.maxHealth",
							itemMeta.getEnchantLevel(CustomEnchantsManager.HEALTHBOOST) * 2,
							AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			resultItem.setItemMeta(itemMeta);
		}
		e.setResult(resultItem);

		for (HumanEntity he : e.getViewers())
			isModified.put(he.getUniqueId(), true);
		Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
		{
			e.getInventory().setRepairCost(10);
			for (HumanEntity he : e.getViewers())
			{
				Player pl = (Player) he;
				pl.updateInventory();
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void HandleSameItemUpgrades(ItemStack fs, ItemStack ss, PrepareAnvilEvent e)
	{
		if (fs.getType() != ss.getType())
			return;
		if (fs.getType() == Material.ENCHANTED_BOOK)
			return;

		Object[] ssEnchants = ss.getItemMeta().getEnchants().keySet().toArray();

		ItemStack resultItem = new ItemStack(fs);

		for (Object ench : ssEnchants)
		{
			Enchantment enc = (Enchantment) ench;
			int ssLevel = ss.getItemMeta().getEnchantLevel(enc);
			if (fs.getItemMeta().hasEnchant(enc))
			{
				int fsLevel = fs.getItemMeta().getEnchantLevel(enc);
				if (Functions.IsCustomEnchant(enc))
				{
					if (ssLevel > fsLevel)
						resultItem.addUnsafeEnchantment(enc, ssLevel);
					if (ssLevel == fsLevel)
					{
						if (ssLevel == enc.getMaxLevel())
							continue;
						else
						{
							resultItem.addUnsafeEnchantment(enc, ssLevel + 1);
							if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.TRAVELER))
							{
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
								
								itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED,
										new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed",
												itemMeta.getEnchantLevel(CustomEnchantsManager.TRAVELER) * .4,
												AttributeModifier.Operation.ADD_SCALAR, EquipmentSlot.LEGS));
								itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
								resultItem.setItemMeta(itemMeta);
							}
							if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.STONEFISTS))
							{
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
								
								itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
										new AttributeModifier(UUID.randomUUID(), "generic.attackDamage",
												itemMeta.getEnchantLevel(CustomEnchantsManager.STONEFISTS) * 3,
												AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
								itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
								resultItem.setItemMeta(itemMeta);
							}
							if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.HEALTHBOOST))
							{
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
								
								itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH,
										new AttributeModifier(UUID.randomUUID(), "generic.maxHealth",
												itemMeta.getEnchantLevel(CustomEnchantsManager.HEALTHBOOST) * 2,
												AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
								itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
								resultItem.setItemMeta(itemMeta);
							}
						}
					}
				} else
				{
					if (ssLevel > fsLevel)
						resultItem.addUnsafeEnchantment(enc, ssLevel);
					if (ssLevel == fsLevel)
					{
						int Mxlvl = ExpandedEnchants.getInstance().getConfig().getInt("MaxVanillaEnchantLevel");
						if(!ExpandedEnchants.getInstance().getConfig().getBoolean("EnableVanillaOverride")) Mxlvl = enc.getMaxLevel();
						if (enc.getMaxLevel() == 1)
							continue;
						else if (ssLevel == (Mxlvl < 3 ? Mxlvl : 3) && enc.equals(Enchantment.LURE))
							continue;
						else if (ssLevel == (Mxlvl < 3 ? Mxlvl : 3) && enc.equals(Enchantment.DEPTH_STRIDER))
							continue;
						else if (ssLevel == (Mxlvl < 5 ? Mxlvl : 5) && enc.equals(Enchantment.QUICK_CHARGE))
							continue;
						else if (ssLevel == Mxlvl)
							continue;
						else
							resultItem.addUnsafeEnchantment(enc, ssLevel + 1);
					}
				}
				if (Functions.IsCustomEnchant(enc))
				{
					ItemMeta meta = resultItem.getItemMeta();
					List<String> lore = new ArrayList<String>();
					if (meta.hasLore())
						lore = meta.getLore();
					int num = -1;
					for (int i = 0; i < lore.size(); i++)
						if (lore.get(i).contains(enc.getName()))
							num = i;
					lore.remove(num);
					lore.add(ChatColor.GRAY + enc.getName() + " "
							+ Functions.GetNameByLevel(resultItem.getEnchantmentLevel(enc), enc.getMaxLevel()));
					meta.setLore(lore);
					meta.setDisplayName(e.getInventory().getRenameText());
					resultItem.setItemMeta(meta);
				}
			} else
			{
				if (!fs.getItemMeta().hasConflictingEnchant(enc))
				{
					resultItem.addUnsafeEnchantment(enc, ssLevel);
					if (Functions.IsCustomEnchant(enc))
					{
						ItemMeta meta = resultItem.getItemMeta();
						List<String> lore = new ArrayList<String>();
						if (meta.hasLore())
							lore = meta.getLore();
						lore.add(ChatColor.GRAY + enc.getName() + " "
								+ Functions.GetNameByLevel(resultItem.getEnchantmentLevel(enc), enc.getMaxLevel()));
						meta.setLore(lore);
						meta.setDisplayName(e.getInventory().getRenameText());
						resultItem.setItemMeta(meta);
						if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.NOBREAKABLE))
						{
							resultItem.setDurability((short) 100000);
							ItemMeta itemMeta = resultItem.getItemMeta();
							itemMeta.setUnbreakable(true);
							resultItem.setItemMeta(itemMeta);
							resultItem.removeEnchantment(Enchantment.DURABILITY);
							resultItem.removeEnchantment(Enchantment.MENDING);
						}
						if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.TRAVELER))
						{
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
							
							itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED,
									new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed",
											itemMeta.getEnchantLevel(CustomEnchantsManager.TRAVELER) * .4,
											AttributeModifier.Operation.ADD_SCALAR));
							itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
							resultItem.setItemMeta(itemMeta);
						}
						if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.STONEFISTS))
						{
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
						if (resultItem.getItemMeta().hasEnchant(CustomEnchantsManager.HEALTHBOOST))
						{
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
							
							itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH,
									new AttributeModifier(UUID.randomUUID(), "generic.maxHealth",
											itemMeta.getEnchantLevel(CustomEnchantsManager.HEALTHBOOST) * 2,
											AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
							itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
							resultItem.setItemMeta(itemMeta);
						}
					}
				}
			}
		}
		if (resultItem.equals(fs))
			resultItem = null;
		e.setResult(resultItem);

		for (HumanEntity he : e.getViewers())
			isModified.put(he.getUniqueId(), true);
		Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
		{
			e.getInventory().setRepairCost(5);
			for (HumanEntity he : e.getViewers())
			{
				Player pl = (Player) he;
				pl.updateInventory();
			}
		});

	}

	public void HandleResourceEnchants(ItemStack fs, ItemStack ss, PrepareAnvilEvent e)
	{
		if (fs.getType() == Material.ENCHANTED_BOOK)
			return;
		Enchantment goal = ExpandedEnchants.recipeManager.GetEnchantment(ss.getType());
		if (goal == null)
			return;
		if (!goal.canEnchantItem(fs))
			return;
		if (ss.getAmount() < ExpandedEnchants.recipeManager.GetAmount(goal))
			return;

		if (fs.getItemMeta().hasConflictingEnchant(goal))
		{

			if (fs.getItemMeta().hasEnchant(goal))
			{
				int enchLvl = fs.getItemMeta().getEnchantLevel(goal);
				ItemMeta im = fs.getItemMeta();
				if (goal.getMaxLevel() == 1)
					return;
				im.removeEnchant(goal);
				fs.setItemMeta(im);
				if (fs.getItemMeta().hasConflictingEnchant(goal))
					return;
				im.addEnchant(goal, enchLvl, true);
				fs.setItemMeta(im);
			} else
				return;

		}
		;

		if (fs.getItemMeta().hasEnchants())
		{
			int MaxLevel = ExpandedEnchants.getInstance().getConfig().getInt("MaxVanillaEnchantLevel");
			if(!ExpandedEnchants.getInstance().getConfig().getBoolean("EnableVanillaOverride")) MaxLevel = goal.getMaxLevel();
			if (fs.getItemMeta().hasEnchant(goal))
			{
				if ((goal == Enchantment.DEPTH_STRIDER
						&& fs.getItemMeta().getEnchantLevel(goal) == (MaxLevel < 3 ? MaxLevel : 3))
						|| (goal == Enchantment.LURE
								&& fs.getItemMeta().getEnchantLevel(goal) == (MaxLevel < 3 ? MaxLevel : 3))
						|| (goal == Enchantment.QUICK_CHARGE
								&& fs.getItemMeta().getEnchantLevel(goal) == (MaxLevel < 5 ? MaxLevel : 5)))
					return;
				if (fs.getItemMeta()
						.getEnchantLevel(goal) == MaxLevel)
					return;
				if (goal.getMaxLevel() == 1)
					return;
			}
		}

		ItemStack result = new ItemStack(fs);

		ItemMeta meta = fs.getItemMeta();
		AnvilInventory inv = e.getInventory();
		meta.setDisplayName(inv.getRenameText());

		int lvl = 1;
		int extra = 0;
		if (fs.getItemMeta().hasEnchant(goal))
			extra = meta.getEnchantLevel(goal);
		int tot = lvl + extra;
		meta.addEnchant(goal, tot, true);
		meta.setDisplayName(e.getInventory().getRenameText());
		Repairable repair = (Repairable) meta;
		repair.setRepairCost(0);
		result.setItemMeta(meta);
		e.getInventory().setItem(2, result);
		e.setResult(result);

		e.getInventory().setMaximumRepairCost(20000);
		for (HumanEntity he : e.getViewers())
			isModified.put(he.getUniqueId(), true);
		Bukkit.getScheduler().runTaskLater(ExpandedEnchants.getInstance(), () -> {
			if (ExpandedEnchants.getInstance().getConfig().getBoolean("ResourceEnchantXpEnabled")) {
				e.getInventory().setRepairCost(tot * tot);
				e.getView().setProperty(Property.REPAIR_COST, e.getInventory().getRepairCost());
			}

			for (HumanEntity he : e.getViewers()) {
				Player pl = (Player) he;
				pl.updateInventory();
			}
		}, 2);
	}

	public void HandleCustomRenaming(ItemStack fs, PrepareAnvilEvent e) {
		if(Functions.ContainsCustomEnchant(fs)) {
			ItemStack result = fs.clone();
			ItemMeta meta = result.getItemMeta();
			meta.setDisplayName(e.getInventory().getRenameText());
			result.setItemMeta(meta);
			e.setResult(result);
			for (HumanEntity he : e.getViewers())
				isModified.put(he.getUniqueId(), false);
			Bukkit.getScheduler().runTask(ExpandedEnchants.getInstance(), () ->
			{
				e.getInventory().setRepairCost(1);
				for (HumanEntity he : e.getViewers())
				{
					Player pl = (Player) he;
					pl.updateInventory();
				}
			});
		}
	}
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		if (!e.isCancelled())
		{
			HumanEntity ent = e.getWhoClicked();
			if (ent == null)
				return;
			// not really necessary
			if (ent instanceof Player)
			{
				Player player = (Player) ent;
				Inventory inv = e.getInventory();

				// see if the event is about an anvil
				if (inv instanceof AnvilInventory)
				{
					InventoryView view = e.getView();
					int rawSlot = e.getRawSlot();
					if (rawSlot == view.convertSlot(rawSlot))
					{
						AnvilInventory ainv = (AnvilInventory) inv;
						if (rawSlot == 2) {
							if(!isModified.containsKey(ent.getUniqueId())) return;
							if (!isModified.get(ent.getUniqueId())) return;
							ItemStack item = e.getCurrentItem();
							if (item != null) {
								ItemMeta meta = item.getItemMeta();
								if (meta != null) {
									if (ainv.getRepairCost() > player.getLevel())
										return;
									player.setItemOnCursor(item);
									player.setLevel(player.getLevel() - ainv.getRepairCost());
									ItemStack cost = new ItemStack(inv.getContents()[1]);
									Enchantment ench = ExpandedEnchants.recipeManager.GetEnchantment(cost.getType());
									if (ench != null) {
										int amount = ExpandedEnchants.recipeManager.GetAmount(ench);
										int tot = cost.getAmount();
										int left = tot - amount;
										cost.setAmount(left);
										ItemStack[] content = new ItemStack[] { null, cost, null };
										inv.setContents(content);

									} 
									else if (inv.getContents()[1].getType() == Material.BOOK)
									{
										ItemStack firstItem = inv.getContents()[0];
										int amount = inv.getContents()[1].getAmount();
										ItemStack bookItem = inv.getContents()[1];
										bookItem.setAmount(amount - 1);
										if (removeLastEnchant.containsKey(ent.getUniqueId()))
										{
											if (removeLastEnchant.get(ent.getUniqueId()) != null)
											{
												if (firstItem.getType() == Material.ENCHANTED_BOOK)
												{
													EnchantmentStorageMeta firstMeta = (EnchantmentStorageMeta) firstItem.getItemMeta();
													Enchantment enchant = removeLastEnchant.get(ent.getUniqueId());
													firstMeta.removeStoredEnchant(enchant);
													firstItem.setItemMeta(firstMeta);
												} else
												{
													Enchantment removeEnch = removeLastEnchant.get(ent.getUniqueId());
													firstItem.removeEnchantment(removeEnch);
													if (Functions.IsCustomEnchant(removeEnch))
													{
														ItemMeta enchmeta = firstItem.getItemMeta();
														if (removeEnch.equals(CustomEnchantsManager.NOBREAKABLE))
															enchmeta.setUnbreakable(false);
														if (removeEnch.equals(CustomEnchantsManager.TRAVELER))
															enchmeta.removeAttributeModifier(
																	Attribute.GENERIC_MOVEMENT_SPEED);
														if (removeEnch.equals(CustomEnchantsManager.HEALTHBOOST))
															enchmeta.removeAttributeModifier(
																	Attribute.GENERIC_MAX_HEALTH);
														if (removeEnch.equals(CustomEnchantsManager.STONEFISTS))
															enchmeta.removeAttributeModifier(
																	Attribute.GENERIC_ATTACK_DAMAGE);
														enchmeta = ExpandedEnchants.itemManager.RemoveCustomEnchantmentLore(enchmeta, removeEnch);
														firstItem.setItemMeta(enchmeta);
													}
												}
												for (HumanEntity he : e.getClickedInventory().getViewers())
													removeLastEnchant.put(he.getUniqueId(), null);
											}
										}
										inv.setContents(new ItemStack[] { firstItem, bookItem, null });
									}
									else inv.setContents(new ItemStack[] { null, null, null });
									player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
									isModified.put(ent.getUniqueId(), false);

								}
							}
						}
					}
				}
			}
		}
	}
}
