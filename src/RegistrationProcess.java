
import java.util.ArrayList;
import java.util.HashSet;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RegistrationProcess extends ListenerAdapter {
	private String channelName;
	private HashSet<User> registered = new HashSet<User>();
	private boolean endMsgSent = false;
	private ArrayList<User> signedUp= new <User>ArrayList();
	
	public RegistrationProcess(String s) {
		channelName = s;
	}

	public void onMessageReceived(MessageReceivedEvent event) {
		Guild curGuild = event.getGuild();
		TextChannel registration = curGuild.getTextChannelsByName(channelName, true).get(0);

		if (event.getChannel().equals(registration)) {
			User toCheck = event.getAuthor();

			// gives Signed Up Role
			Role su = curGuild.getRolesByName("Signed Up", true).get(0);
			
			if (registered.size() <= 29) {

				if (!registered.contains(toCheck)) {
					registered.add(toCheck);

					Role tr = curGuild.getRolesByName("Tourney Registered", true).get(0);
					curGuild.addRoleToMember(toCheck.getId(), tr).queue();
					curGuild.addRoleToMember(toCheck.getId(), su).queue();
					signedUp.add(toCheck);
					
					event.getMessage().addReaction("✔").queue();
					
					sendPrivateMessage(toCheck,
							"You are now registered! You will be prompted to confirm in a few days. Failure to confirm within 24hrs will result in confirmations opening to others.");

					if (registered.size() == 30) {
						registration.sendMessage("End").queue();
					}

				} 
			}
			if(registered.size()>=30&&!event.getMember().getRoles().contains(su)) {
				curGuild.addRoleToMember(toCheck.getId(), su).queue();
				sendPrivateMessage(toCheck,
						"You're signed up, however you don't get a chance at first confirmation. This happened because you signed up late (after the max amount of slots allotted for this event)\r\n"
								+ "\r\n"
								+ "This doesn't necessarily mean you won't get the Tournament. If those who are Registered fail to confirm within 24hrs, more people will be given a chance. We also recommend everyone to show up to the event anyways. In the event that some don't show up, we will need fill-ins. We'll pull these fill-ins directly from people waiting in VC. Earlier you come the better.");
				signedUp.add(toCheck);
			}

		}
		
		if (event.getMember().getRoles().contains(curGuild.getRolesByName("Use Bot", true).get(0))) {
			Message msg = event.getMessage();
			String[] args = msg.getContentRaw().split(" ");
			MessageChannel channel = event.getChannel();
			if (args[0].equalsIgnoreCase(Testing.prefix + "getNumRegistered")) {
				channel.sendMessage("There are "+signedUp.size()+" signups").queue();
				
			}else if(args[0].equalsIgnoreCase(Testing.prefix + "checkSignupOrder")) {
				channel.sendMessage(args[1]+" was "+signedUp.indexOf(curGuild.getMemberByTag(args[1]).getUser())+1).queue();
			}
		}

	}

	public void sendPrivateMessage(User user, String content) {
		// openPrivateChannel provides a RestAction<PrivateChannel>
		// which means it supplies you with the resulting channel
		user.openPrivateChannel().queue((channel) -> {
			channel.sendMessage(content).queue();
		});
	}
	
	
	
	

}
