package edu.wku.hospital.management.service;
import javax.swing.*;

import edu.wku.hospital.management.exception.NotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This class is used to manage the input and output of the system
 *@author Yiduo Yu
 *@version 1.0
 *@since 5-17-2024
 *
 */

//保存历史文件，序列化
public class IOManager implements Serializable{
    //ALERT: IN THIS PROGRAM, HOME_DIRECTORY IS THE DESKTOP
    private final static File HOME_DIRECTORY = new File(System.getProperty("user.home"), "Desktop");
    private final static File WORKING_DIRECTORY = new File(System.getProperty("user.dir"));
    private File currentDirectory = HOME_DIRECTORY;

    public File getCurrentDirectory(){
        return currentDirectory;
    }


    /**
     * This method is used to choose a file from the file system
     * @return the file that the user selected
     */
    public static File chooseFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String folderPath = fileChooser.getSelectedFile().getPath();
            return new File(folderPath);
        } else {
            // User cancelled the file chooser or an error occurred
            return null;
        }
    }

    public static File chooseDirectory(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String folderPath = fileChooser.getSelectedFile().getPath();
            return new File(folderPath);
        } else {
            // User cancelled the file chooser or an error occurred
            return null;
        }
    }

    public static File getHomeDirectory(){
        return HOME_DIRECTORY;
    }

    public static File getWorkingDirectory(){
        return WORKING_DIRECTORY;
    }

    /**
     * This method is used to choose a directory from the file system
     * @return the directory that the user selected
     */
    public void changeDirectory(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String folderPath = fileChooser.getSelectedFile().getPath();
            currentDirectory = new File(folderPath);
        } else {
           return;
        }
    }

    /**
     * this method is a helper method to searchGivenInfo(String message)
     * @param message is the designated object we want to search (directory or file name)
     * @param mode mode 1: directory, mode 2: file name
     * @return the searchedFile
     */
    public File searchGivenInfo(String message, int mode){
        switch (mode){
        case 1: break;
        case 2: break;
        }

        return new File("");
    }

    /**
     * This method is used to search the designated object in the current directory
     * @param message is the designated object we want to search (directory or file name)
     * @return the searchedFile
     */
    public File searchGivenInfo(String message){
        if (message.contains("/"))
           return searchGivenInfo(message,1);
        else
          return searchGivenInfo(message,2);

    }

    public static void saveSerializable (Serializable o, String path){
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(o);
        } catch (IOException i) {
            i.printStackTrace();
        }
       
   }
    
    public static Serializable loadSerializable (String resource, String file) throws NotFoundException{
        File lfile = new File(file);
        Serializable o;
        if (!lfile.exists()) {
            throw new NotFoundException(resource, file);
        }
        try (FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn)) {
        o = (Serializable) in.readObject();
        return o;
        } 
            catch (IOException i) {
                System.out.println(i.toString());
            return null;
            } 
            catch (ClassNotFoundException c) {
                System.out.println("class not found exception");
                return null;
            }
        }
}
