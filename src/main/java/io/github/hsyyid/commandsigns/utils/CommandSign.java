package io.github.hsyyid.commandsigns.utils;

import java.util.ArrayList;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class CommandSign
{
    private Location<World> location;
    private ArrayList<String> commands = new ArrayList<String>();
    
    public CommandSign(Location<World> location)
    {
        this.location = location;
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
            return Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully set new command!");
        }
        catch(Exception e)
        {
            return Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "The command number you specified is not present.");
        }
    }
    
    public Text removeCommand(int commandNumber)
    {
        try
        {
            this.commands.remove(commandNumber);
            return Texts.of(TextColors.GOLD, "[CommandSigns]: ", TextColors.GRAY, "Successfully removed command!");
        }
        catch(Exception e)
        {
            return Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "The command number you specified is not present.");
        }
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

