/*******************************************************************************
 * Copyright (C) 2019 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.recipes.utils;

import java.lang.annotation.Annotation;
import java.util.*;

import org.apache.logging.log4j.Logger;

import codechicken.nei.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.ItemStackSet;
import craftedMods.recipes.base.RecipeItemSlotImpl;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.event.*;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

public class NEIRecipeHandlersUtils {

	public static ItemStackSet generatePermutations(Collection<ItemStack> stacks) {
		ItemStackSet permutations = ItemStackSet.create();
		for (ItemStack stack : stacks)
			if (stack != null && stack.getItem() != null) {
				List<ItemStack> perms = ItemList.itemMap.get(stack.getItem());
				if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE && perms != null && !perms.isEmpty()) {
					permutations.addAll(perms);
				} else {
					permutations.add(stack);
				}
			}
		return permutations;
	}

    public static boolean areStacksSameType (ItemStack stack1, ItemStack stack2)
    {
        Boolean handlerResult = null;
        for (ItemStackComparisonHandler handler : NEIRecipeHandlers.mod.getNEIIntegrationManager ()
            .getItemStackComparisonHandlers ())
        {
            Boolean result = handler.areStacksOfSameType (stack1, stack2);
            if (result != null)
            {
                if (handlerResult == null)
                {
                    handlerResult = result;
                }
                else if (handlerResult != null && result != handlerResult)
                {
                    handlerResult = null;
                    break;
                }
            }
        }
        return handlerResult == null ? NEIServerUtils.areStacksSameType (stack1, stack2) : handlerResult;
    }

    public static boolean areStacksSameTypeForCrafting (ItemStack stack1, ItemStack stack2)
    {
        Boolean handlerResult = null;
        for (ItemStackComparisonHandler handler : NEIRecipeHandlers.mod.getNEIIntegrationManager ()
            .getItemStackComparisonHandlers ())
        {
            Boolean result = handler.areStacksOfSameTypeForCrafting (stack1, stack2);
            if (result != null)
            {
                if (handlerResult == null)
                {
                    handlerResult = result;
                }
                else if (handlerResult != null && result != handlerResult)
                {
                    handlerResult = null;
                    break;
                }
            }
        }
        return handlerResult == null ? NEIServerUtils.areStacksSameTypeCrafting (stack1, stack2) : handlerResult;
    }

	public static EnumRecipeItemRole createRecipeItemRole(String name) {
		return EnumHelper.addEnum(EnumRecipeItemRole.class, name);
	}

	public static ItemStack[] extractRecipeItems(Object container) {
		ItemStack[] ret = null;
		if (container instanceof String) {
			List<ItemStack> stacks = OreDictionary.getOres((String) container);
			ret = stacks.toArray(new ItemStack[stacks.size()]);
		} else if (container instanceof Item) {
			ret = new ItemStack[] { new ItemStack((Item) container) };
		} else if (container instanceof Block) {
			ret = new ItemStack[] { new ItemStack((Block) container) };
		} else {
			ret = NEIServerUtils.extractRecipeItems(container);
		}
		if (ret != null) {
			for (int i = 0; i < ret.length; i++) {
				ItemStack stack = ret[i];
				if (stack == null || stack.getItem() == null) {
					ret[i] = new ItemStack(Blocks.fire);
				}
			}
		}
		return ret;
	}

	public static List<ItemStack> getItemList() {
		return ItemList.items;
	}

	public static String[] getRecipeHandlerCategories(String unlocalizedName) {
		String[] categories;
		String[] cats = unlocalizedName.split("\\.");
		if (cats.length > 1) {
			categories = new String[cats.length - 1];
			for (int i = 0; i < cats.length - 1; i++) {
				categories[i] = cats[i];
			}
		} else {
			categories = new String[] {};
		}
		return categories;
	}

	public static Collection<ItemStack> readItemStackListFromNBT(NBTTagCompound compound, String tagName) {
		List<ItemStack> ret = new ArrayList<>();
		if (compound.hasKey(tagName)) {
			NBTTagList list = compound.getTagList(tagName, 10);
			for (int i = 0; i < list.tagCount(); i++) {
				ItemStack stack = NEIRecipeHandlersUtils.readItemStackFromNBT(list.getCompoundTagAt(i));
				if (stack != null) {
					ret.add(stack);
				}
			}
		}
		return ret;
	}

	public static void writeItemStackListToNBT(NBTTagCompound compound, String tagName, Collection<? extends ItemStack> stacks) {
		NBTTagList list = new NBTTagList();
		for (ItemStack stack : stacks) {
			NBTTagCompound stackTag = new NBTTagCompound();
			NEIRecipeHandlersUtils.writeItemStackToNBT(stack, stackTag);
			list.appendTag(stackTag);
		}
		compound.setTag(tagName, list);
	}

	public static List<RecipeItemSlot> offset(List<RecipeItemSlot> slotsList, int xOffset, int yOffset) {
		List<RecipeItemSlot> ret = new ArrayList<>();
		for (RecipeItemSlot slot : slotsList) {
			ret.add(slot == null ? null : new RecipeItemSlotImpl(slot.getX() + xOffset, slot.getY() + yOffset));
		}
		return ret;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean registerDefaultResourcePack(IResourcePack pack) {
		return ((List) ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "defaultResourcePacks", "field_110449_ao")).add(pack);
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> discoverRegisteredHandlers(Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses,
			Class<T> handlerClass) {
		Collection<T> handlerInstanceCollection = new ArrayList<>();
		Set<Class<?>> discoveredHandlers = discoveredClasses.get(RegisteredHandler.class).get(handlerClass);
		NEIRecipeHandlers.mod.getLogger()
				.info(String.format("Found %d handlers of type \"%s\" in the classpath", discoveredHandlers.size(), handlerClass.getSimpleName()));
		discoveredHandlers.forEach(clazz -> {
			try {
				Class<T> handler = (Class<T>) clazz;
				if (handler.getAnnotation(RegisteredHandler.class).isEnabled()) {
					handlerInstanceCollection.add(handler.newInstance());
					NEIRecipeHandlers.mod.getLogger()
							.debug(String.format("Registered the handler \"%s\" of type \"%s\"", handler.getName(), handlerClass.getSimpleName()));
				} else {
					NEIRecipeHandlers.mod.getLogger().info(
							String.format("The handler \"%s\" of type \"%s\" was disabled by the author", handler.getName(), handlerClass.getSimpleName()));
				}
			} catch (Exception e) {
				NEIRecipeHandlers.mod.getLogger().error("Couldn't create an instance of class \"" + clazz.getName() + "\"", e);
			}
		});
		return handlerInstanceCollection;
	}

	public static IChatComponent getVersionNotificationChatText(String handlerName, RemoteVersion version) {
		IChatComponent part1 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part1", handlerName)
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
		IChatComponent part2 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part2")
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
		IChatComponent part3 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part3",
				version.getRemoteVersion().toString())
						.setChatStyle(
								version.getDownloadURL() != null
										? new ChatStyle().setColor(EnumChatFormatting.YELLOW).setUnderlined(true)
												.setChatClickEvent(new ClickEvent(Action.OPEN_URL, version.getDownloadURL().toString()))
												.setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT,
														new ChatComponentText(StatCollector.translateToLocalFormatted(
																"neiRecipeHandlers.versionChecker.notification.chat.version.tooltip"))))
										: new ChatStyle().setColor(EnumChatFormatting.YELLOW));
		part1.appendSibling(part2).appendSibling(part3);
		if (version.getChangelogURL() != null) {
			IChatComponent part4 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part4")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
			IChatComponent part5 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part5")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true)
							.setChatClickEvent(new ClickEvent(Action.OPEN_URL, version.getChangelogURL().toString()))
							.setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
									StatCollector.translateToLocalFormatted("neiRecipeHandlers.versionChecker.notification.chat.changelog.tooltip")))));
			IChatComponent part6 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part6")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
			part1.appendSibling(part4).appendSibling(part5).appendSibling(part6);
		}

		return part1;
	}

	public static boolean doVersionCheck(String handlerName, VersionChecker versionChecker, Logger logger) {
		boolean ret = false;
		try {
			logger.debug(String.format("Starting version check for %s...", handlerName));
			versionChecker.checkVersion();
			if (versionChecker.getRemoteVersion() != null) {
				logger.info(String.format("Found a remote version for %s: %s (%s version)", handlerName,
						versionChecker.getRemoteVersion().getRemoteVersion().toString(), versionChecker.compareRemoteVersion().toDisplayString()));
				ret = true;
			}
		} catch (Exception e) {
			logger.error(String.format("Version check failed for %s", handlerName), e);
		}
		return ret;
	}

	public static void writeItemStackToNBT(ItemStack stack, NBTTagCompound compound) {
		compound.setString("Identifier", Item.itemRegistry.getNameForObject(stack.getItem()));
		compound.setInteger("Count", stack.stackSize);
		compound.setInteger("Damage", stack.getItemDamage());
		if (stack.getTagCompound() != null) {
			compound.setTag("Tag", stack.getTagCompound());
		}
	}

	public static ItemStack readItemStackFromNBT(NBTTagCompound compound) {
		ItemStack ret = null;
		Item item = (Item) Item.itemRegistry.getObject(compound.getString("Identifier"));
		if (item != null) {
			int stackSize = compound.getInteger("Count");
			int itemDamage = compound.getInteger("Damage");
			if (itemDamage < 0) {
				itemDamage = 0;
			}
			NBTTagCompound tag = compound.hasKey("Tag", 10) ? compound.getCompoundTag("Tag") : null;
			ret = new ItemStack(item, stackSize, itemDamage);
			if (tag != null) {
				ret.setTagCompound(tag);
			}
		}
		return ret;
	}

}
