package maze;

import java.io.*;
import java.util.Scanner;

public class Menu {
    private enum Option {
        EXIT, GENERATE, LOAD, SAVE, DISPLAY
    }
    private final Option[] OPTIONARR1 = {Option.EXIT, Option.GENERATE, Option.LOAD}; // when the maze is unavailable, there are 3 options to choose
    private final Option[] OPTIONARR2 = {Option.EXIT, Option.GENERATE, Option.LOAD, Option.SAVE, Option.DISPLAY}; // when the maze is available, there are 5 options to choose
    private boolean exit;
    private boolean mazeAvailable;
    private Maze maze;


    public void start() {
        while (!exit) {
            printMenu();
            Option option = getUserOption();
            handleOption(option);
        }
        System.out.println("Bye!");
    }

    private void printMenu() {
        // if the maze is unavailable, only print the menu with 3 options
        System.out.println();
        if (!mazeAvailable) {
            System.out.println("=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "0. Exit");
        } else {
            System.out.println("=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "3. Save the maze\n" +
                    "4. Display the maze\n" +
                    "0. Exit");
        }
    }

    private Option getUserOption() {
        Scanner sc = new Scanner(System.in);

        boolean correctOption = false;
        int userOption = 0;
        while (!correctOption) {
            try {
                 userOption = Integer.parseInt(sc.nextLine());

                // check if the option user choose is correct
                if (!mazeAvailable) {
                    if (userOption >= 0 && userOption <= 2) {
                        correctOption = true;
                    }
                } else {
                    if (userOption >= 0 && userOption <= 4) {
                        correctOption = true;
                    }
                }
            } catch (NumberFormatException e) {
                // ignored
            }

            // if the option is incorrect
            if (!correctOption) {
                System.out.println("Incorrect option. Please try again");
            }
        }

        // return the corresponding option that the use chose
        if (!mazeAvailable) {
            return OPTIONARR1[userOption];
        } else {
            return OPTIONARR2[userOption];
        }


    }

    private void handleOption(Option option) {
        switch (option) {
            case EXIT:
                exit = true;
                break;
            case GENERATE:
                generateMaze();
                break;
            case LOAD:
                loadMaze();
                break;
            case SAVE:
                saveMaze();
                break;
            case DISPLAY:
                displayMaze();
                break;
        }
    }

    private void generateMaze() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the size of a new maze");
        int size = Integer.parseInt(sc.nextLine());
        maze = new Maze(size, size);
        maze.generate();
        maze.display();
        mazeAvailable = true;
    }

    private void loadMaze() {
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();

        try {
            Maze temp = deserialize(fileName);
            if (temp == null) {
                System.out.println("Cannot load the maze. It has an invalid format");
            } else {
                maze = temp;
                mazeAvailable = true;
            }
        } catch (Exception e) {
            System.out.println("Error happened when deserializing the maze");
        }
    }

    private void saveMaze() {

        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();

        try {
            serialize(maze, fileName);
        } catch (IOException e) {
            System.out.println("Error happened when serializing the maze");
            e.printStackTrace();
        }

    }

    private void displayMaze() {
        maze.display();
    }

    private void serialize(Maze maze, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(maze);
        oos.close();
    }

    private Maze deserialize(String fileName) throws IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Maze mazeInFile = (Maze) ois.readObject();
        ois.close();

        return mazeInFile;
    }
}