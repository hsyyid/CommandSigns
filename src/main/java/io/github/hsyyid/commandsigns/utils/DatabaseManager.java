package io.github.hsyyid.commandsigns.utils;

import io.github.hsyyid.commandsigns.CommandSigns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class DatabaseManager
{
    private static Gson gson = new GsonBuilder().create();
    
    public static void writeCommandSigns()
    {
        Connection c = null;
        Statement stmt = null;

        try
        {
			try
			{
				Class.forName("org.sqlite.JDBC");
			}
			catch (ClassNotFoundException exception)
			{
				System.out.println("There is no database software on this machine! CommandSigns will not work correctly until this is resolved.");
				return;
			}
			
            c = DriverManager.getConnection("jdbc:sqlite:CommandSigns.db");
            stmt = c.createStatement();
            String sql = ("DROP TABLE IF EXISTS COMMANDSIGNS");
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE COMMANDSIGNS " +
                    "(X DOUBLE PRIMARY KEY     NOT NULL," +
                    " Y           DOUBLE    NOT NULL, " + 
                    " Z           DOUBLE    NOT NULL, " + 
                    " WORLDID     TEXT      NOT NULL, " + 
                    " COMMAND     TEXT      NOT NULL, " +
                    " ONETIME     BOOLEAN   NOT NULL, " +
                    " USERS       TEXT      NOT NULL)";
            stmt.executeUpdate(sql);

            for(CommandSign commandSign : CommandSigns.commandSigns)
            {
                double x = commandSign.getLocation().getX();
                double y = commandSign.getLocation().getY();
                double z = commandSign.getLocation().getZ();
                boolean oneTime = commandSign.getOneTime();
                String users = commandSign.getUsers();

                String worldUUID = commandSign.getLocation().getExtent().getUniqueId().toString();
                String commandJSON = gson.toJson(commandSign.getCommands());

                sql = "INSERT INTO COMMANDSIGNS (X,Y,Z,WORLDID,COMMAND,ONETIME,USERS) " +
                        "VALUES (" + x + "," + y + "," + z + ",'" + worldUUID + "','" + commandJSON + "'," + oneTime + ",'" + users + "');"; 
                stmt.executeUpdate(sql);
            }

            stmt.close();
            c.close();
        }
        catch (Exception e)
        {
            ;
        }
    }

    public static void readCommandSigns()
    {
        Connection c = null;
        Statement stmt = null;

        try
        {
			try
			{
				Class.forName("org.sqlite.JDBC");
			}
			catch (ClassNotFoundException exception)
			{
				System.out.println("There is no database software on this machine! CommandSigns will not work correctly until this is resolved.");
				return;
			}
			
            c = DriverManager.getConnection("jdbc:sqlite:CommandSigns.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM COMMANDSIGNS;");
            ArrayList<CommandSign> commandSigns = new ArrayList<CommandSign>();

            while (rs.next())
            {
            	boolean oneTime = rs.getBoolean("oneTime");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                String users = rs.getString("users");
                String worldUUID = rs.getString("worldid");
                String commandJSON = rs.getString("command");
                ArrayList<String> commands = new ArrayList<String>(Arrays.asList(gson.fromJson(commandJSON, String[].class)));
                World world = CommandSigns.game.getServer().getWorld(UUID.fromString(worldUUID)).get();
                Location<World> location = new Location<World>(world, x, y, z);

                CommandSign commandSign = new CommandSign(location, oneTime);
                commandSign.setCommands(commands);
                commandSign.setUsers(users);
                commandSigns.add(commandSign);
            }

            CommandSigns.commandSigns = commandSigns;
            rs.close();
            stmt.close();
            c.close();
        }
        catch (Exception e)
        {
            ;
        }
    }
}   
