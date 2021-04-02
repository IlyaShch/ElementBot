import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;
//NzA1NTI2MDQ0MTQ0MTczMTU4.Xqs-bA.hexD1asRurfc-TmOj2528gWuQ-s
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Invite.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.InterfacedEventManager;
import net.dv8tion.jda.internal.requests.Route.Guilds;
import net.dv8tion.jda.api.*;

public class Testing {
	public static String prefix="%";
	
	public static void main(String[]Shmargs) throws LoginException{
		JDABuilder builder = new JDABuilder();
        builder.setToken("");
        builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.watching("joey"));
		
		builder.addEventListeners(new Commands(builder));
		builder.build();
		
		
		//builder.addEventListeners(new GuildMessageReceivedEvent(jda, 0, null));
	}
	

	
}
