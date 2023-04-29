package com.planeboarding.planeboardingcontrol.model;

import java.io.*;

public class FileManager {

    private static FileManager instance;

    private FileManager(){}

    public static FileManager getInstance() {
        if(instance == null) instance = new FileManager();
        return instance;
    }


    /** this function reads the content of a file given its path
     * @param pathFile the absolute path of the file that is going to be read
     * @return the readable content of the file
     */
    public String readFile(String pathFile) {
        File file = new File(pathFile);
        String line;
        StringBuilder info = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while( (line = br.readLine()) != null ) {
                info.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info.toString();
    }
}
