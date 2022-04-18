package maze;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);
        System.out.println("Enter the size of the maze");
        int i = sc.nextInt();
        int j = sc.nextInt();
        Maze maze = new Maze(i, j);
        maze.generate();
        maze.display();
    }

}
