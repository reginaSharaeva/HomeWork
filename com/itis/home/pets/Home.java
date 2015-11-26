package com.itis.home.pets;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Регина on 24.11.2015.
 */
public class Home {

    private Pet firstPet;

    private Pet secondPet;

    public Pet getFirstPet() {return firstPet;}

    public void setFirstPet(Pet firstPet) {this.firstPet = firstPet;}

    public Pet getSecondPet() {return secondPet;}

    public void setSecondPet(Pet secondPet) {this.secondPet = secondPet;}

    public static void main(String... args) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Home home = new Home();
        try (FileReader in = new FileReader("settings.txt");
            BufferedReader bin = new BufferedReader(in)) {

            Class clazz = home.getClass();
            Method[] methods = clazz.getMethods();

            String inputLine = bin.readLine();
            String[] inp = inputLine.split(" ");
            Class cv = Class.forName(inp[1]);
            Object ob1 = cv.newInstance();
            for (Method method : methods) {
                if (method.getName().equals("setFirstPet")) {
                    method.invoke(home, ob1);
                }
            }

            String inputLine2 = bin.readLine();
            String[] inp2 = inputLine2.split(" ");
            Class cv2 = Class.forName(inp2[1]);
            Object ob2 = cv2.newInstance();
            for (Method method : methods) {
                if (method.getName().equals("setSecondPet")) {
                    method.invoke(home, ob2);
                }
            }

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(home.getFirstPet().getName());
        System.out.println(home.getSecondPet().getName());
    }


}


