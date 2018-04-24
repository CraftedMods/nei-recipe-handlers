package craftedMods.recipes.utils;

import java.net.*;

import craftedMods.recipes.NEIRecipeHandlers;
import net.minecraft.event.*;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.*;

public class ModVersion implements Comparable<ModVersion> {
	private EnumVersionState state;
	private String modid;
	private String modname;
	private String version;
	private String mcVersion;
	private String url;

	public ModVersion(EnumVersionState state, String modid, String modname, String version, String mcVersion, String url) {
		this.state = state;
		this.modid = modid;
		this.modname = modname;
		this.version = version;
		this.mcVersion = mcVersion;
		this.url = url;
	}

	public ModVersion(EnumVersionState state, String modid, String modname, String version, String mcVersion) {
		this(state, modid, modname, version, mcVersion, null);
	}

	public EnumVersionState getState() {
		return this.state;
	}

	public String getModid() {
		return this.modid;
	}

	public String getModname() {
		return this.modname;
	}

	public String getVersion() {
		return this.version;
	}

	public String getMcVersion() {
		return this.mcVersion;
	}

	public String getUrl() {
		return this.url;
	}

	public URL getURL() throws MalformedURLException {
		return new URL(this.url);
	}

	public IChatComponent getFormattedChatText() {
		IChatComponent part1 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part1", NEIRecipeHandlers.MODNAME)
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
		IChatComponent part2 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part2")
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
		IChatComponent part3 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part3",
				this.state.toString() + " " + this.version).setChatStyle(
						new ChatStyle().setColor(EnumChatFormatting.YELLOW).setUnderlined(true).setChatClickEvent(new ClickEvent(Action.OPEN_URL, this.url))
								.setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
										(StatCollector.translateToLocalFormatted("neiRecipeHandlers.versionChecker.notification.chat.version.tooltip"))))));
		IChatComponent part4 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part4")
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
		IChatComponent part5 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part5").setChatStyle(new ChatStyle()
				.setColor(EnumChatFormatting.BLUE).setUnderlined(true).setChatClickEvent(new ClickEvent(Action.OPEN_URL, "https://goo.gl/EkxFlC"))
				.setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
						StatCollector.translateToLocalFormatted("neiRecipeHandlers.versionChecker.notification.chat.changelog.tooltip")))));
		IChatComponent part6 = new ChatComponentTranslation("neiRecipeHandlers.versionChecker.notification.chat.part6")
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));

		return part1.appendSibling(part2).appendSibling(part3).appendSibling(part4).appendSibling(part5).appendSibling(part6);
	}

	@Override
	public int compareTo(ModVersion comp) throws IllegalArgumentException {
		if (comp.modid.equals(this.modid)) {
			int stateComp = this.state.compareTo(comp.state);
			int mcVersionComp = this.mcVersion.compareTo(comp.mcVersion);
			int versionComp = this.version.compareTo(comp.version);

			if (mcVersionComp != 0) return mcVersionComp;
			else if (stateComp != 0) return stateComp;
			else return versionComp;

		} else throw new IllegalArgumentException("Modid of comp != modid of this object!");
	}
}
