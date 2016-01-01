package io.github.hsyyid.commandsigns.utils;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;

public class CommandSign
{
	private String users;
	private Location<World> location;
	private ArrayList<String> commands = new ArrayList<String>();
	private boolean oneTime;

	public CommandSign(Location<World> location, boolean oneTime)
	{
		this.location = location;
		this.oneTime = oneTime;
		this.users = "";
	}
	
	public void setUsers(String users)
	{
		this.users = users;
	}
	
	public void setOneTime(boolean oneTime)
	{
		this.oneTime = oneTime;
	}

	public void setLocation(Location<World> location)
	{
		this.location = location;
	}

	public void setCommands(ArrayList<String> commands)
	{
		this.commands = commands;
	}

	public void addCommand(String command)
	{
		this.commands.add(command);
	}

	public Text addCommand(String command, int commandNumber)
	{
		try
		{
			this.commands.set(commandNumber, command);
			return Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully set new command!");
		}
		catch (Exception e)
		{
			return Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "The command number you specified is not present.");
		}
	}

	public Text removeCommand(int commandNumber)
	{
		try
		{
			this.commands.remove(commandNumber);
			return Text.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully removed command!");
		}
		catch (Exception e)
		{
			return Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "The command number you specified is not present.");
		}
	}

	public String getUsers()
	{
		return users;
	}
	
	public boolean getOneTime()
	{
		return oneTime;
	}

	public Location<World> getLocation()
	{
		return location;
	}

	public ArrayList<String> getCommands()
	{
		return commands;
	}

}
