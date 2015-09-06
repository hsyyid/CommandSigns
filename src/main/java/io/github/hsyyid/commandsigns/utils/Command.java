package io.github.hsyyid.commandsigns.utils;

import java.util.UUID;

public class Command
{
	private String command;
	private UUID playerUUID;
	
	public Command(String command, UUID playerUUID)
	{
		this.command = command;
		this.playerUUID = playerUUID;
	}
	
	public String getCommand()
	{
		return command;
	}
	
	public UUID getPlayerUUID()
	{
		return playerUUID;
	}
}
