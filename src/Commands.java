import java.util.EnumSet;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.InterfacedEventManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	private JDABuilder builder;

	public Commands(JDABuilder m) {
		builder = m;

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		Guild curGuild = event.getGuild();
		MessageChannel channel = event.getChannel();

		Message msg = event.getMessage();
		String[] args = msg.getContentRaw().split(" ");

		// tourney registered role
		Role tr = curGuild.getRolesByName("Tourney Registered", true).get(0);
		
		if (event.getMember().getRoles().contains(curGuild.getRolesByName("Use Bot", true).get(0))) {
			// Making ScreenShot Channels Command %makeScreenShotChannels ChannelName
			// TourneyName -----------------------------------
			if (args[0].equalsIgnoreCase(Testing.prefix + "makeScreenshotChannels")) {

				// response

				channel.sendMessage("On it!").queue();

				// Getting everyone from channel

				List<VoiceChannel> vc = curGuild.getVoiceChannelsByName(args[1], true);

				String tName = args[2];
				for (int i = 3; i < args.length; i++) {
					tName += " " + args[i];
				}
				// creating a category for Tourney Registered
				Category c = curGuild.createCategory(tName).complete();
				c.getManager().putPermissionOverride(curGuild.getPublicRole(), null,
						EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_READ)); // removes public role
				c.getManager()
						.putPermissionOverride(tr, EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_READ), null)
						.complete();// adds in tr
				// add Tourney Registered

				// checking for ppl in vc
				if (vc.get(0) == null) {
					channel = event.getChannel();
					channel.sendMessage("No channel found").queue();
				} else {
					for (Member m : vc.get(0).getMembers()) {

						// making text channel
						TextChannel t = curGuild.createTextChannel(m.getEffectiveName()).complete();
						t.getManager().setParent(c).complete();
						t.sendMessage(m.getAsMention()
								+ " This is your screenshot channel, where you'll leave screenshots from each game.\r\n"
								+ //////////////////////////////////////////// Test ME
								"It'll be this screenshot, the one that shows exiles, damage, and placement\r\n"
								+ "https://gyazo.com/6cddba6a906df2f9d102ea35e0ad4444").queue();
						t.getManager().putPermissionOverride(curGuild.getPublicRole(), null,
								EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_READ));// removes public role
						t.getManager().putPermissionOverride(tr,
								EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_READ), null).complete();// adds
																												// in
																												// tr
					}

				}

				// Give Role Command %GiveRole channelName RoleName
				// -------------------------------
			} else if (args[0].equalsIgnoreCase(Testing.prefix + "GiveRole")) {
				List<VoiceChannel> vc = curGuild.getVoiceChannelsByName(args[1], true);
				// checking for ppl in vc
				if (vc.get(0) == null) {
					channel = event.getChannel();
					channel.sendMessage("No channel found").queue();
				} else {
					for (Member m : vc.get(0).getMembers()) {

						// building string name of Role to give
						String rName = args[2];
						for (int i = 3; i < args.length; i++) {
							rName += " " + args[i];
						}

						curGuild.addRoleToMember(m, curGuild.getRolesByName(rName, true).get(0)).complete();

					}

				}
				// Tourney Signup Command %TourneyRegistrationOpen RegistrationChannel
			} else if (args[0].equalsIgnoreCase(Testing.prefix + "TourneyRegistrationOpen")) {
				channel.sendMessage("On it!").queue();
				// building string name of RegistrationChannel
				String cName = args[1];
				for (int i = 2; i < args.length; i++) {
					cName += " " + args[i];
				}

				builder.addEventListeners(new RegistrationProcess(cName));
				builder.removeEventListeners(this);
				try {
					builder.build();
				} catch (LoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}
