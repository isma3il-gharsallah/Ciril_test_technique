package com.example.demo.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebControlleur {

    //Déclarer les couleurs d´affichage de simulation dans la console
	private static final String ANSI_BLACK = "\u001B[30m\u25A0";
    private static final String ANSI_RED = "\u001B[0;91m\u25A0";
    private static final String ANSI_GREEN = "\u001B[0;92m\u25A0";
    private static final String ANSI_RESET = "\u001B[30m ";

    //méthode pour afficher la simulation
	public void display(int[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			System.out.print(" ");
        	for (int j = 0; j < grid[0].length; j++) {
				System.out.print((grid[i][j] == 0 ? ANSI_GREEN : grid[i][j] == 1 ? ANSI_RED : ANSI_BLACK) + ANSI_RESET);
			}
			System.out.println();
		}
		System.out.println("---------------------");
	}
    
	
	//méthode pour exécuter la simulation
    @GetMapping("/runSimulation")
    public ResponseEntity<String> runSimulation() {

        //variables pour stocker la largeur et la hauteur de la grille
        int width;
        int height;

        //variable pour stocker la probabilité de propagation
        double p;

        //variable pour stocker le fichier de configuration
        File configFile;


        //essayer de lire le fichier de configuration
        try {
            configFile = new File("C:\\Users\\Admin\\Documents\\workspace-sts-3.9.12.RELEASE\\Ciril_test\\src\\main\\resources\\config.txt");
            Scanner sc = new Scanner(configFile);

            //lire la largeur et la hauteur de la grille
            width = sc.nextInt();
            height = sc.nextInt();

            //lire la probabilité de propagation
            p = sc.nextDouble();

            //créer la grille 
            int[][] grid = new int[height][width];

            //lire les positions des cases initialement en feu
            int numberOfBurningCells = 0;
            while (sc.hasNextInt()) {
                int row = sc.nextInt();
                int col = sc.nextInt();

                //marquer la case comme étant en feu
                grid[row][col] = 1;
                numberOfBurningCells++;
            }
            sc.close();
            
            int totalCellsBurned = 0;
            int steps = 0;
	            
            display(grid);
            System.out.println("    Etat initial");
          	System.out.println("---------------------");
          	

            while (numberOfBurningCells > 0 ) {
             
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[0].length; j++) {
                        //si la case est en feu
                        if (grid[i][j] == 1) {
                            //mettre à jour la grille avec la cendre
                            totalCellsBurned++;
                            grid[i][j] = 2;
                            numberOfBurningCells--;
                            

                            //propager le feu avec une probabilité p
                            if (i > 0 && Math.random() < p) {
                            	if (grid[i - 1][j] !=  2) {
                                    grid[i - 1][j] =  4;
                                }
                            }
                            if (i < grid.length - 1 && Math.random() < p) {
                                if (grid[i + 1][j] !=  2) {
                                	grid[i + 1][j] = 4;
                                }
                            }
                            if (j > 0 && Math.random() < p) {
                                if (grid[i][j - 1] !=  2) {
                                	grid[i][j - 1] = 4;
                               }
                            }
                            if (j < grid[0].length - 1 && Math.random() < p) {
                                if (grid[i][j + 1] !=  2) {
                                	grid[i][j + 1] = 4;
                               }
                            }


                        }
                    }
                }

            

                //calculer le nombre de cases en feu
                numberOfBurningCells = 0;
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[0].length; j++) {
                    	if (grid[i][j] == 4) {
                    		grid[i][j] = 1;
                            numberOfBurningCells++;
                        }
                       
                    }
                }

                //incrémenter le nombre d'étapes
                steps++;
                display(grid);
                System.out.println(" Après l´étape N° "+steps);
              	System.out.println("---------------------");
 
            }

            //retourner le nombre de cases réduites en cendre et le nombre d'étapes écoulées
            return ResponseEntity.ok("Nombre de cases réduites en cendre: " + totalCellsBurned + "; Nombre d'étapes écoulées: " + steps);

        } catch (FileNotFoundException e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Fichier introuvable.");
        }
    }	
	
}