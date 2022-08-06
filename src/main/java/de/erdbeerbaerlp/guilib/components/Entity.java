package de.erdbeerbaerlp.guilib.components;

import com.mojang.blaze3d.vertex.PoseStack;

import de.erdbeerbaerlp.guilib.components.GuiComponent;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class Entity extends GuiComponent {
	private EntityType<?> entity;
	private ItemStack MAINHAND = new ItemStack(Items.AIR);
	private ItemStack OFFHAND = new ItemStack(Items.AIR);
	private ItemStack HEAD = new ItemStack(Items.AIR);
	private ItemStack CHEST = new ItemStack(Items.AIR);
	private ItemStack LEGS = new ItemStack(Items.AIR);
	private ItemStack FEET = new ItemStack(Items.AIR);

	private int posX = 0, posY = 0, scale = 25;

	public RenderEntity(int xIn, int yIn, String entityName) {
		super(xIn, yIn, 0, 0);
		this.posX = xIn;
		this.posY = yIn;
		entity = EntityType.byString(entityName).get();
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partial) {
		Entity renderedEntity = entity.create(this.mc.level);

		renderedEntity.setItemSlot(EquipmentSlot.MAINHAND, MAINHAND);
		renderedEntity.setItemSlot(EquipmentSlot.OFFHAND, OFFHAND);

		renderedEntity.setItemSlot(EquipmentSlot.HEAD, HEAD);
		renderedEntity.setItemSlot(EquipmentSlot.CHEST, CHEST);
		renderedEntity.setItemSlot(EquipmentSlot.LEGS, LEGS);
		renderedEntity.setItemSlot(EquipmentSlot.FEET, FEET);

InventoryScreen.renderEntityInInventory(posX, posY, scale, (float) (0 + 250) - mouseX, (float) (0 + 160) - mouseY, (LivingEntity) renderedEntity);

	}

	public void setEntity(String newEntity) {
		if (ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(newEntity))) {
			entity = EntityType.byString(newEntity).get();
			System.out.println("Setting entity to: " + newEntity);
		} else {
			entity = EntityType.byString("minecraft:pig").get();
			System.out.println("Entity not found. Defaulting to Pig.");
		}

	}

	public void setPosition(int X, int Y) {
		posX = X;
		posY = Y;
	}

	public void setScale(int Scale) {
		scale = Scale;
	}

	public void setArmor(EquipmentSlot slot, Item item) {
		switch (slot) {
		case MAINHAND:
			MAINHAND = new ItemStack(item);
			break;
		case OFFHAND:
			OFFHAND = new ItemStack(item);
			break;
		case HEAD:
			HEAD = new ItemStack(item);
			break;
		case CHEST:
			CHEST = new ItemStack(item);
			break;
		case LEGS:
			LEGS = new ItemStack(item);
			break;
		case FEET:
			FEET = new ItemStack(item);
			break;
		}
	}

	public void setArmor(EquipmentSlot slot, Item item, Enchantment enchant, int level) {

		ItemStack stack = new ItemStack(item);
		stack.enchant(enchant, level);

		switch (slot) {
		case MAINHAND:
			MAINHAND = enchant != null ? stack : new ItemStack(item);
			break;
		case OFFHAND:
			OFFHAND = enchant != null ? stack : new ItemStack(item);
			break;
		case HEAD:
			HEAD = enchant != null ? stack : new ItemStack(item);
			break;
		case CHEST:
			CHEST = enchant != null ? stack : new ItemStack(item);
			break;
		case LEGS:
			LEGS = enchant != null ? stack : new ItemStack(item);
			break;
		case FEET:
			FEET = enchant != null ? stack : new ItemStack(item);
			break;
		}
	}

	public ItemStack getArmor(EquipmentSlot slot) {
		ItemStack armorpiece = HEAD;

		switch (slot) {
		case MAINHAND:
			armorpiece = MAINHAND;
			break;
		case OFFHAND:
			armorpiece = OFFHAND;
			break;
		case HEAD:
			armorpiece = HEAD;
			break;
		case CHEST:
			armorpiece = CHEST;
			break;
		case LEGS:
			armorpiece = LEGS;
			break;
		case FEET:
			armorpiece = FEET;
			break;
		}

		return armorpiece;
	}

	public void netheriteSet() {
		MAINHAND = new ItemStack(Items.NETHERITE_SWORD);
		OFFHAND = new ItemStack(Items.TOTEM_OF_UNDYING);
		HEAD = new ItemStack(Items.NETHERITE_HELMET);
		CHEST = new ItemStack(Items.NETHERITE_CHESTPLATE);
		LEGS = new ItemStack(Items.NETHERITE_LEGGINGS);
		FEET = new ItemStack(Items.NETHERITE_BOOTS);
	}

	public void diamondSet() {
		MAINHAND = new ItemStack(Items.DIAMOND_SWORD);
		HEAD = new ItemStack(Items.DIAMOND_HELMET);
		CHEST = new ItemStack(Items.DIAMOND_CHESTPLATE);
		LEGS = new ItemStack(Items.DIAMOND_LEGGINGS);
		FEET = new ItemStack(Items.DIAMOND_BOOTS);
	}

	public void goldSet() {
		MAINHAND = new ItemStack(Items.GOLDEN_SWORD);
		HEAD = new ItemStack(Items.GOLDEN_HELMET);
		CHEST = new ItemStack(Items.GOLDEN_CHESTPLATE);
		LEGS = new ItemStack(Items.GOLDEN_LEGGINGS);
		FEET = new ItemStack(Items.GOLDEN_BOOTS);
	}

	public void ironSet() {
		MAINHAND = new ItemStack(Items.IRON_SWORD);
		HEAD = new ItemStack(Items.IRON_HELMET);
		CHEST = new ItemStack(Items.IRON_CHESTPLATE);
		LEGS = new ItemStack(Items.IRON_LEGGINGS);
		FEET = new ItemStack(Items.IRON_BOOTS);
	}

	@Override
	public boolean charTyped(char arg0, int arg1) {
		return false;
	}

	@Override
	public void mouseClick(double arg0, double arg1, int arg2) {

	}

	@Override
	public void mouseRelease(double arg0, double arg1, int arg2) {

	}
