package io.github.hsyyid.commandsigns.utils;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class CommandSign
{
	private Location<World> location;
	private String command;
	
	public CommandSign(Location<World> location, String command)
	{
		this.location = location;
		this.command = command;
	}
	
	public void setLocation(Location<World> location)
	{
		this.location = location;
	}
	
	public void setCommand(String command)
	{
		this.command = command;
	}

	public Location<World> getLocation()
	{
		return location;
	}
	
	public String getCommand()
	{
		return command;
	}
	
}

