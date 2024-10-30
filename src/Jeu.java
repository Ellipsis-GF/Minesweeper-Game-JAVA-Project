import java.util.Scanner;
import java.io.File;

final public class Jeu {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int mineNumber = 0, minePercentage = 0, size = 0, cellRow = 0, cellColumn = 0;
        String action = "";
        // Menu de début de jeu, on demande au joueur les infos necessaires à la création du niveau
        System.out.println("Bienvenue dans le Jeu du Démineur !");
        System.out.println("Voulez vous creer une nouvelle partie N ou charger la dernière partie sauvegardée C ?");
        String choixPartie = scanner.next().toUpperCase();
        boolean error = false;
        Demineur grille = new Demineur();
        while ((!choixPartie.equals("N")) && (!choixPartie.equals("C"))){
            System.out.println("Veuillez entrer N pour nouvelle partie ou C pour charger la dernière sauvegardée !");
            choixPartie = scanner.next().toUpperCase();
        }

        
        if (choixPartie.equals("N")){
            // Cas ou le joueur souhaite créer un nouveau niveau
            System.out.println("Entrez la taille de la grille (entier positif entre 5 et 50) : ");
            error = true;
            while (error){  
                try{
                    size = scanner.nextInt();
                    error = false;
                    if (size < 5 || size > 50){
                        System.out.println("veuillez entrez une taille valide (entier positif entre 5 et 50) : ");
                        error = true;
                        scanner.nextLine();
                    }
                } catch (Exception e) {
                    System.out.println("veuillez entrez une taille valide (entier positif entre 5 et 50) : ");
                    error = true;
                    scanner.nextLine();
                }
            }
            System.out.println("Entrez le pourcentage de cases minées : ");
            error = true;
            while (error){  
                try{
                    minePercentage = scanner.nextInt();
                    mineNumber = (int)Math.ceil(((double)(size*size))*((double)minePercentage/100));
                    error = false;
                    if ((size*size) - mineNumber < 9 || mineNumber <= 0){
                        // On vérifie que le joueur ne rentre pas un pourcentage trop élevé qui empecherai la propriété que doit respecter la première case révélé sur la grille
                        // Rappel : Lorsque le joueur découvre la première case, celle-ci et ces voisines ne doivent pas être une mine
                        // Il y a donc au maximum 9 cases qui ne doivent pas être des mines
                        System.out.println("Veuillez entrez un pourcentage de cases minées valide (entre 1 et " + ((((double)(size*size - 9)) / (double)(size*size))*100) + ")");
                        error = true;
                        scanner.nextLine();
                    }
                } catch (Exception e) {
                    System.out.println("Veuillez entrez un pourcentage de cases minées valide (entre 1 et " + ((((double)(size*size - 9)) / (double)(size*size))*100) + ")");
                    error = true;
                    scanner.nextLine();
                }
            }
            grille = new Demineur(size, size, mineNumber);
            System.out.println("Grille initialisée !");
            grille.displayGrid();

        } else {
            // Cas ou le joueur souhaite charger le niveau sauvegardé dans sauvegarde.txt
            File fichier = new File("sauvegarde.txt");
            if (!fichier.exists()){
                // Si le fichier n'existe pas on créer un nouveau niveau et on demande au joueur les infos necessaires pour le créer
                System.out.print("Aucune partie sauvegardée, lancement d'une nouvelle partie");
                System.out.print("Entrez la taille de la grille (entier positif entre 5 et 50) : ");
                error = true;
                while (error){  
                    try{
                        size = scanner.nextInt();
                        error = false;
                        if (size < 5 || size > 50){
                            System.out.println("veuillez entrez une taille valide (entier positif entre 5 et 50) : ");
                            error = true;
                            scanner.nextLine();
                        }
                    } catch (Exception e) {
                        System.out.println("veuillez entrez une taille valide (entier positif entre 5 et 50) : ");
                        error = true;
                        scanner.nextLine();
                    }
                }
                System.out.println("Entrez le pourcentage de cases minées : ");
                error = true;
                while (error){  
                    try{
                        minePercentage = scanner.nextInt();
                        mineNumber = (int)Math.ceil(((double)(size*size))*((double)minePercentage/100));
                        error = false;
                        if ((size*size) - mineNumber < 9 || mineNumber <= 0){
                            // On vérifie que le joueur ne rentre pas un pourcentage trop élevé qui empecherai la propriété que doit respecter la première case révélé sur la grille
                            // Rappel : Lorsque le joueur découvre la première case, celle-ci et ces voisines ne doivent pas être une mine
                            // Il y a donc au maximum 9 cases qui ne doivent pas être des mines
                            System.out.println("Veuillez entrez un pourcentage de cases minées valide (entre 1 et " + ((((double)(size*size - 9)) / (double)(size*size))*100) + ")");
                            error = true;
                            scanner.nextLine();
                        }
                    } catch (Exception e) {
                        System.out.println("Veuillez entrez un pourcentage de cases minées valide (entre 1 et " + ((((double)(size*size - 9)) / (double)(size*size))*100) + ")");
                        error = true;
                        scanner.nextLine();
                    }
                }

                grille = new Demineur(size, size, mineNumber);
                System.out.println("Grille initialisée !");
                grille.displayGrid();
            } else {
                // Si le fichier existe on charge le niveau grâce à la méthode load(String path)
                grille = new Demineur();
                grille.load("sauvegarde.txt");
                System.out.println("Grille initialisée !");
                grille.displayGrid();
            }
        }

        // Boucle principale du jeu
        while (!grille.isDefeat() && !grille.isVictory()) {
            // On informe également l'utilisateur sur le fait qu'il peut toujours :
            // - Sauvegarder la partie actuelle
            // - Charger la dernière partie sauvegardé
            System.out.println("Vous pouvez sauvegarder la partie avec S ou charger la dernière sauvegardée avec C");
            // On demande les coordonnées de la case visé dans le but de la découvrir ou la marquer
            System.out.println("Entrez la ligne de la case que vous voulez découvrir ou marquer (entre 1 et " + grille.getHeight() + "): ");
            String userInput = scanner.next().toUpperCase();
            if (userInput.equals("S")){
                // Si l'utilisateur choisit de sauvegarder, on utilise save(String path) pour le faire
                // Puis on revient au jeu en redemandant la coordonnées y de la case visée
                grille.save("sauvegarde.txt");
                System.out.println("Grille sauvegardée !");
                System.out.println("Entrez la ligne de la case que vous voulez découvrir ou marquer (entre 1 et " + grille.getHeight() + "): ");
                error = true;
                while (error) {
                    try {
                        cellRow = scanner.nextInt() - 1;
                        error = false;
                        if ((cellRow < 0) || (cellRow > grille.getHeight() - 1)){
                            System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getHeight() + " comme numéro de ligne !");
                            error = true;
                            scanner.nextLine();
                        }
                    } catch (Exception e) {
                        System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getHeight() + " comme numéro de ligne !");
                        error = true;
                        scanner.nextLine();
                    }
                }
            }
            else if (userInput.equals("C")) {
                // Si l'utilisateur souhaite charger le dernier niveau sauvegardé
                File fichier = new File("sauvegarde.txt");
                if (!fichier.exists()){
                    // Dans le cas ou il n'y a aucun niveau sauvegardé on ne fait rien et on en informe le joueur
                    System.out.println("Aucune partie sauvegardée");
                } else {
                    // Si il y a un niveau sauvegardé on le charge avec load(String path)
                    grille = new Demineur();
                    grille.load("sauvegarde.txt");
                    System.out.println("Grille initialisée !");
                    grille.displayGrid();
                }
                // On retourne au jeu en demandant la coordonnées y de la case visée
                System.out.println("Entrez la ligne de la case que vous voulez découvrir ou marquer (entre 1 et " + grille.getWidth() + "): ");
                error = true;
                while (error) {
                    try {
                        cellRow = scanner.nextInt() - 1;
                        error = false;
                        if ((cellRow < 0) || (cellRow > grille.getHeight() - 1)){
                            System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getHeight() + " comme numéro de ligne !");
                            error = true;
                            scanner.nextLine();
                        }
                    } catch (Exception e) {
                        System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getHeight() + " comme numéro de ligne !");
                        error = true;
                        scanner.nextLine();
                    }
                }
            }
            else {
                // Si le joueur ne veut ni sauvegarder ni charger un niveau, alors c'est qu'il souhaite jouer un coup
                error = true;
                try {
                    cellRow = Integer.parseInt(userInput) - 1; // On convertit alors son entrée en entier
                    error = false;
                    if ((cellRow < 0) || (cellRow > grille.getHeight() - 1)){
                        System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getHeight() + " comme numéro de ligne !");
                        error = true;
                        scanner.nextLine();
                    }
                } catch (Exception e) {
                    System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getHeight() + " comme numéro de ligne !");
                    error = true;
                    scanner.nextLine();
                }
                while(error){
                    try {
                        cellRow = scanner.nextInt() - 1;
                        error = false;
                        if ((cellRow < 0) || (cellRow > grille.getHeight() - 1)){
                            System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getHeight() + " comme numéro de ligne !");
                            error = true;
                            scanner.nextLine();
                        }
                    } catch (Exception e) {
                        System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getHeight() + " comme numéro de ligne !");
                        error = true;
                        scanner.nextLine();
                    }
                }
            }

            // On demande la coordonnée x de la case visée
            System.out.println("Entrez la colone de la case que vous voulez découvrir ou marquer (entre 1 et " + grille.getWidth() + "): ");
            error = true;
            while(error){
                // On vérifie si la coordonées x existent dans la grille et le joueur entre un nombre jusqu'à ce qu'il soit valide
                try {
                    cellColumn = scanner.nextInt() - 1;
                    error = false;
                    if ((cellColumn < 0) || (cellColumn > grille.getWidth() - 1)){
                        System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getWidth() + " comme numéro de colone !");
                        error = true;
                        scanner.nextLine();
                    }
                } catch (Exception e) {
                    System.out.println("Veuillez entrer un entier entre " + 1 + " et " + grille.getWidth() + " comme numéro de colone !");
                    error = true;
                    scanner.nextLine();
                }
            }

            // Une fois la case visée, le joueur à 2 choix : marquer la case ou découvrir la case
            System.out.println("Voulez-vous découvrir (D) ou marquer (M) la case ?");
            error = true;
            while (error) {
                try {
                    action = scanner.next().toUpperCase();
                    error = false;
                    if ( (!action.equals("D")) && (!action.equals("M")) ) {
                        System.out.println("Veuillez Entrer une action existante ( D pour découvrir ou M pour marquer) ");
                        error = true;
                        scanner.nextLine();
                    }
                } catch (Exception e){
                    System.out.println("Veuillez Entrer une action existante ( D pour découvrir ou M pour marquer) ");
                    error = true;
                    scanner.nextLine();
                }
            }
             
            if (action.equals("D")) {
                //  On révèle la case
                grille.reveal(cellRow, cellColumn);
            } else if (action.equals("M")){
                // On marque la case
                grille.mark(cellRow, cellColumn);
            }
        }

        // On affiche le message de fin en fonction de l'issue de la partie (Victoire ou Défaite)
        if (grille.isVictory()) {
            System.out.println("Bravo, vous avez gagné !");
        } else {
            System.out.println("Dommage vous êtes tombé sur une mine, c'est perdu !");
        }
        scanner.close();
    }
}
