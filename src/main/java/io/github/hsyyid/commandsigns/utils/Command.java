package io.github.hsyyid.commandsigns.utils;

import java.util.UUID;

import com.google.common.base.Optional;

public class Command
{
    private String command;
    private UUID playerUUID;
    private Integer commandNumber = null;
    private boolean toRemove;
    
    public Command(String command, UUID playerUUID, boolean toRemove)
    {
        this.command = command;
        this.playerUUID = playerUUID;
        this.toRemove = toRemove;
    }

    public Command(String command, int commandNumber, UUID playerUUID, boolean toRemove)
    {
        this.command = command;
        this.playerUUID = playerUUID;
        this.commandNumber = commandNumber;
        this.toRemove = toRemove;
    }
    
    public Command(int commandNumber, UUID playerUUID, boolean toRemove)
    {
        this.playerUUID = playerUUID;
        this.commandNumber = commandNumber;
        this.toRemove = toRemove;
    }

    public String getCommand()
    {
        return command;
    }
    
    public boolean getToRemove()
    {
        return toRemove;
    }

    public Optional<Integer> getCommandNumber()
    {
        if((Integer) commandNumber != null)
        {
            return Optional.of(commandNumber);
        }
        else
        {
            return Optional.absent();
        }
    }

    public UUID getPlayerUUID()
    {
        return playerUUID;
    }
}
