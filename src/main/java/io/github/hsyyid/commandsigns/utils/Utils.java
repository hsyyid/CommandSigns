package io.github.hsyyid.commandsigns.utils;

import io.github.hsyyid.commandsigns.Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


public class Utils
{
    public static void writeCommandSignsToJson()
    {
        String json = Main.gson.toJson(Main.commandSigns);
        try
        {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter("CommandSigns.json");

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(json);

            bufferedWriter.flush();
            // Always close files.
            bufferedWriter.close();
        }
        catch (IOException ex)
        {
            System.err.println("Could not save JSON file!");
        }   
    }
    
    public static void readCommandSignsFromJson()
    {

        String json = null;

        try
        {
            json = readFile("CommandSigns.json", StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            System.err.println("Could not read JSON file!");
        }

        if(json != null)
        {
           Main.commandSigns = new ArrayList<CommandSign>(Arrays.asList(Main.gson.fromJson(json, CommandSign[].class)));
        }
        else
        {
            System.err.println("Could not read JSON file!");
        }
    }
    
    static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
}
