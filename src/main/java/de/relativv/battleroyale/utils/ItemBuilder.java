package de.relativv.battleroyale.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class ItemBuilder {
 
	public ItemStack item;
	
	
	public ItemBuilder(Material item) {
		this.item = new ItemStack(item);
	}
	
	public ItemBuilder(Material item, int amount) {
		this.item = new ItemStack(item, amount);
	}
	
	public ItemBuilder(Material item, int amount, short data) {
		this.item = new ItemStack(item, amount, data);
	}
	
	public ItemBuilder setDisPlayname(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(meta);
		
		return this;
	}	
		
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	
	
	public ItemBuilder setUnbreakable() {
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
		
		
		return this;
	}
	
	
	public ItemBuilder setDurability(short durability) {
		item.setDurability(durability);
		return this;
	}
	
	
	
	public ItemBuilder setLore(String... lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder addEnchantment(Enchantment ench, int lvl) {
		
		item.addEnchantment(ench, lvl);
		
		return this;
	}
	
	public ItemBuilder addUnsafeEnchantment(Enchantment ench, int lvl) {
		
		item.addUnsafeEnchantment(ench, lvl);
		
		return this;
	}
	
	
	public ItemBuilder setSkullOwner(String owner) {
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(owner);
		item.setItemMeta(meta);
		
		return this;
	}
	
	
	public ItemBuilder setColor(Color clr) {
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(clr);
		item.setItemMeta(meta);
		
		return this;
	}
	
	public ItemBuilder setColorRGB(int red, int green, int blue) {
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(red, green, blue));
		item.setItemMeta(meta);
		
		return this;
	}
	

	
	
	public ItemStack build() {
		return item;
	}


}
