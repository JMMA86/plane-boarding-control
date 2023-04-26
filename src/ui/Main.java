package ui;
import model.Controller;

import java.util.Scanner;

public class Main {
    private final Controller controller;
    private final Scanner sc;

    public Main() {
        this.controller = new Controller();
        this.sc = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.showMenu();
    }

    private void showMenu() {
        controller.readDataFromFile();
    }


}
