package io.github.hsyyid.commandsigns.utils;

import java.util.UUID;

public class CommandSignModifier
{
	private String command;
	private UUID playerUUID;
	private int commandNumber;
	private boolean toRemove;
	private boolean oneTime;

	public CommandSignModifier(String command, UUID playerUUID)
	{
		this.command = command;
		this.playerUUID = playerUUID;
		this.toRemove = false;
	}

	public CommandSignModifier(String command, UUID playerUUID, boolean oneTime)
	{
		this.command = command;
		this.playerUUID = playerUUID;
		this.toRemove = false;
		this.oneTime = oneTime;
	}

	public CommandSignModifier(int commandNumber, UUID playerUUID)
	{
		this.commandNumber = commandNumber;
		this.playerUUID = playerUUID;
		this.toRemove = true;
	}

	public String getCommand()
	{
		return command;
	}

	public boolean isToRemove()
	{
		return toRemove;
	}

	public int getCommandNumber()
	{
		return commandNumber;
	}

	public UUID getPlayerUniqueId()
	{
		return playerUUID;
	}

	public boolean isOneTime()
	{
		return oneTime;
	}
}
