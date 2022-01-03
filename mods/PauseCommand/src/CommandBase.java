package zombie.commands;

import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombie.commands.serverCommands.AddAllToWhiteListCommand;
import zombie.commands.serverCommands.AddItemCommand;
import zombie.commands.serverCommands.AddUserCommand;
import zombie.commands.serverCommands.AddUserToWhiteListCommand;
import zombie.commands.serverCommands.AddVehicleCommand;
import zombie.commands.serverCommands.AddXPCommand;
import zombie.commands.serverCommands.AlarmCommand;
import zombie.commands.serverCommands.BanSteamIDCommand;
import zombie.commands.serverCommands.BanUserCommand;
import zombie.commands.serverCommands.ChangeOptionCommand;
import zombie.commands.serverCommands.ChopperCommand;
import zombie.commands.serverCommands.ClearCommand;
import zombie.commands.serverCommands.ConnectionsCommand;
import zombie.commands.serverCommands.CreateHorde2Command;
import zombie.commands.serverCommands.CreateHordeCommand;
import zombie.commands.serverCommands.DebugPlayerCommand;
import zombie.commands.serverCommands.GodModeCommand;
import zombie.commands.serverCommands.GrantAdminCommand;
import zombie.commands.serverCommands.GunShotCommand;
import zombie.commands.serverCommands.HelpCommand;
import zombie.commands.serverCommands.InvisibleCommand;
import zombie.commands.serverCommands.KickUserCommand;
import zombie.commands.serverCommands.NoClipCommand;
import zombie.commands.serverCommands.PlayersCommand;
import zombie.commands.serverCommands.QuitCommand;
import zombie.commands.serverCommands.ReleaseSafehouseCommand;
import zombie.commands.serverCommands.ReloadLuaCommand;
import zombie.commands.serverCommands.ReloadOptionsCommand;
import zombie.commands.serverCommands.RemoveAdminCommand;
import zombie.commands.serverCommands.RemoveUserFromWhiteList;
import zombie.commands.serverCommands.RemoveZombiesCommand;
import zombie.commands.serverCommands.ReplayCommands;
import zombie.commands.serverCommands.SaveCommand;
import zombie.commands.serverCommands.SendPulseCommand;
import zombie.commands.serverCommands.ServerMessageCommand;
import zombie.commands.serverCommands.SetAccessLevelCommand;
import zombie.commands.serverCommands.ShowOptionsCommand;
import zombie.commands.serverCommands.StartRainCommand;
import zombie.commands.serverCommands.StopRainCommand;
import zombie.commands.serverCommands.TeleportCommand;
import zombie.commands.serverCommands.TeleportToCommand;
import zombie.commands.serverCommands.ThunderCommand;
import zombie.commands.serverCommands.UnbanSteamIDCommand;
import zombie.commands.serverCommands.UnbanUserCommand;
import zombie.commands.serverCommands.VoiceBanCommand;
import zombie.commands.serverCommands.PauseCommand;
import zombie.commands.serverCommands.UnpauseCommand;
import zombie.core.Translator;
import zombie.core.raknet.UdpConnection;


public abstract class CommandBase {
	private final int playerType;
	private final String username;
	private final String command;
	private String[] commandArgs;
	private boolean parsingSuccessful = false;
	private boolean parsed = false;
	private String message = "";
	protected final UdpConnection connection;
	protected String argsName = "default args name. Nothing match";
	protected static final String defaultArgsName = "default args name. Nothing match";
	private static Class[] childrenClasses = new Class[]{PauseCommand.class, UnpauseCommand.class, SaveCommand.class, ServerMessageCommand.class, ConnectionsCommand.class, AddUserCommand.class, GrantAdminCommand.class, RemoveAdminCommand.class, DebugPlayerCommand.class, QuitCommand.class, AlarmCommand.class, ChopperCommand.class, AddAllToWhiteListCommand.class, KickUserCommand.class, TeleportCommand.class, TeleportToCommand.class, ReleaseSafehouseCommand.class, StartRainCommand.class, StopRainCommand.class, ThunderCommand.class, GunShotCommand.class, ReloadOptionsCommand.class, BanUserCommand.class, BanSteamIDCommand.class, UnbanUserCommand.class, UnbanSteamIDCommand.class, AddUserToWhiteListCommand.class, RemoveUserFromWhiteList.class, ChangeOptionCommand.class, ShowOptionsCommand.class, GodModeCommand.class, VoiceBanCommand.class, NoClipCommand.class, InvisibleCommand.class, HelpCommand.class, ClearCommand.class, PlayersCommand.class, AddItemCommand.class, AddXPCommand.class, AddVehicleCommand.class, CreateHordeCommand.class, CreateHorde2Command.class, ReloadLuaCommand.class, RemoveZombiesCommand.class, SendPulseCommand.class, SetAccessLevelCommand.class, ReplayCommands.class};

	public static Class[] getSubClasses() {
		return childrenClasses;
	}

	public static Class findCommandCls(String string) {
		Class[] classArray = childrenClasses;
		int int1 = classArray.length;
		for (int int2 = 0; int2 < int1; ++int2) {
			Class javaClass = classArray[int2];
			if (!isDisabled(javaClass)) {
				CommandName[] commandNameArray = (CommandName[])javaClass.getAnnotationsByType(CommandName.class);
				CommandName[] commandNameArray2 = commandNameArray;
				int int3 = commandNameArray.length;
				for (int int4 = 0; int4 < int3; ++int4) {
					CommandName commandName = commandNameArray2[int4];
					Pattern pattern = Pattern.compile("^" + commandName.name() + "\\b", 2);
					if (pattern.matcher(string).find()) {
						return javaClass;
					}
				}
			}
		}

		return null;
	}

	public static String getHelp(Class javaClass) {
		CommandHelp commandHelp = (CommandHelp)getAnnotation(CommandHelp.class, javaClass);
		if (commandHelp == null) {
			return null;
		} else if (commandHelp.shouldTranslated()) {
			String string = commandHelp.helpText();
			return Translator.getText(string);
		} else {
			return commandHelp.helpText();
		}
	}

	public static String getCommandName(Class javaClass) {
		Annotation[] annotationArray = javaClass.getAnnotationsByType(CommandName.class);
		return ((CommandName)annotationArray[0]).name();
	}

	public static boolean isDisabled(Class javaClass) {
		DisabledCommand disabledCommand = (DisabledCommand)getAnnotation(DisabledCommand.class, javaClass);
		return disabledCommand != null;
	}

	public static int accessLevelToInt(String string) {
		byte byte1 = -1;
		switch (string.hashCode()) {
		case -2004703995: 
			if (string.equals("moderator")) {
				byte1 = 2;
			}

			break;
		
		case 3302: 
			if (string.equals("gm")) {
				byte1 = 4;
			}

			break;
		
		case 92668751: 
			if (string.equals("admin")) {
				byte1 = 0;
			}

			break;
		
		case 348607190: 
			if (string.equals("observer")) {
				byte1 = 1;
			}

			break;
		
		case 530022739: 
			if (string.equals("overseer")) {
				byte1 = 3;
			}

		
		}
		switch (byte1) {
		case 0: 
			return 32;
		
		case 1: 
			return 1;
		
		case 2: 
			return 4;
		
		case 3: 
			return 8;
		
		case 4: 
			return 16;
		
		default: 
			return 2;
		
		}
	}

	protected CommandBase(String string, String string2, String string3, UdpConnection udpConnection) {
		this.username = string;
		this.command = string3;
		this.connection = udpConnection;
		this.playerType = accessLevelToInt(string2);
		ArrayList arrayList = new ArrayList();
		Matcher matcher = Pattern.compile("([^\"]\\S*|\".*?\")\\s*").matcher(string3);
		while (matcher.find()) {
			arrayList.add(matcher.group(1).replace("\"", ""));
		}

		this.commandArgs = new String[arrayList.size() - 1];
		for (int int1 = 1; int1 < arrayList.size(); ++int1) {
			this.commandArgs[int1 - 1] = (String)arrayList.get(int1);
		}
	}

	public String Execute() throws SQLException {
		return this.canBeExecuted() ? this.Command() : this.message;
	}

	public boolean canBeExecuted() {
		if (this.parsed) {
			return this.parsingSuccessful;
		} else if (!this.PlayerSatisfyRequiredRights()) {
			this.message = this.playerHasNoRightError();
			return false;
		} else {
			this.parsingSuccessful = this.parseCommand();
			return this.parsingSuccessful;
		}
	}

	public boolean isCommandComeFromServerConsole() {
		return this.connection == null;
	}

	protected RequiredRight getRequiredRights() {
		return (RequiredRight)this.getClass().getAnnotation(RequiredRight.class);
	}

	protected CommandArgs[] getCommandArgVariants() {
		Class javaClass = this.getClass();
		return (CommandArgs[])javaClass.getAnnotationsByType(CommandArgs.class);
	}

	public boolean hasHelp() {
		Class javaClass = this.getClass();
		CommandHelp commandHelp = (CommandHelp)javaClass.getAnnotation(CommandHelp.class);
		return commandHelp != null;
	}

	protected String getHelp() {
		Class javaClass = this.getClass();
		return getHelp(javaClass);
	}

	public String getCommandArg(Integer integer) {
		return this.commandArgs != null && integer >= 0 && integer < this.commandArgs.length ? this.commandArgs[integer] : null;
	}

	public boolean hasOptionalArg(Integer integer) {
		return this.commandArgs != null && integer >= 0 && integer < this.commandArgs.length;
	}

	public int getCommandArgsCount() {
		return this.commandArgs.length;
	}

	protected abstract String Command() throws SQLException;

	public boolean parseCommand() {
		CommandArgs[] commandArgsArray = this.getCommandArgVariants();
		if (commandArgsArray.length == 1 && commandArgsArray[0].varArgs()) {
			this.parsed = true;
			return true;
		} else {
			boolean boolean1 = commandArgsArray.length != 0 && this.commandArgs.length != 0 || commandArgsArray.length == 0 && this.commandArgs.length == 0;
			ArrayList arrayList = new ArrayList();
			CommandArgs[] commandArgsArray2 = commandArgsArray;
			int int1 = commandArgsArray.length;
			for (int int2 = 0; int2 < int1; ++int2) {
				CommandArgs commandArgs = commandArgsArray2[int2];
				arrayList.clear();
				this.message = "";
				int int3 = 0;
				boolean1 = true;
				for (int int4 = 0; int4 < commandArgs.required().length; ++int4) {
					String string = commandArgs.required()[int4];
					if (int3 == this.commandArgs.length) {
						boolean1 = false;
						break;
					}

					Matcher matcher = Pattern.compile(string).matcher(this.commandArgs[int3]);
					if (!matcher.matches()) {
						boolean1 = false;
						break;
					}

					for (int int5 = 0; int5 < matcher.groupCount(); ++int5) {
						arrayList.add(matcher.group(int5 + 1));
					}

					++int3;
				}

				if (boolean1) {
					if (int3 == this.commandArgs.length) {
						this.argsName = commandArgs.argName();
						break;
					}

					if (!commandArgs.optional().equals("no value")) {
						Matcher matcher2 = Pattern.compile(commandArgs.optional()).matcher(this.commandArgs[int3]);
						if (matcher2.matches()) {
							for (int int6 = 0; int6 < matcher2.groupCount(); ++int6) {
								arrayList.add(matcher2.group(int6 + 1));
							}
						} else {
							boolean1 = false;
						}
					} else if (int3 < this.commandArgs.length) {
						boolean1 = false;
					}

					if (boolean1) {
						this.argsName = commandArgs.argName();
						break;
					}
				}
			}

			if (boolean1) {
				this.commandArgs = new String[arrayList.size()];
				this.commandArgs = (String[])arrayList.toArray(this.commandArgs);
			} else {
				this.message = this.invalidCommand();
				this.commandArgs = new String[0];
			}

			this.parsed = true;
			return boolean1;
		}
	}

	protected int getAccessLevel() {
		return this.playerType;
	}

	protected String getExecutorUsername() {
		return this.username;
	}

	protected String getCommand() {
		return this.command;
	}

	protected static Object getAnnotation(Class javaClass, Class javaClass2) {
		return javaClass2.getAnnotation(javaClass);
	}

	public boolean isParsingSuccessful() {
		if (!this.parsed) {
			this.parsingSuccessful = this.parseCommand();
		}

		return this.parsingSuccessful;
	}

	private boolean PlayerSatisfyRequiredRights() {
		RequiredRight requiredRight = this.getRequiredRights();
		return (this.playerType & requiredRight.requiredRights()) != 0;
	}

	private String invalidCommand() {
		return this.hasHelp() ? this.getHelp() : Translator.getText("UI_command_arg_parse_failed", this.command);
	}

	private String playerHasNoRightError() {
		return Translator.getText("UI_has_no_right_to_execute_command", this.username, this.command);
	}
}
