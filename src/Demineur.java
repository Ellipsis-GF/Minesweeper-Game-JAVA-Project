import java.util.*;
import java.io.*;

final public class Demineur {
    private int width;
    private int height;
    private int minesCount;
    private int flagsCount;
    private Case[][] grid;

    // Constructeur de Demineur en fonction des dimensions, du nombre de mines (et enventuellement des coordonnées de la case de départ)
    public Demineur(int width, int height, int minesCount) {
        if (width*height <= minesCount) {
            throw new IllegalArgumentException("le nombre de mines est trop élevé");
        } else if (width > 50 || height > 50 || width < 5 || height < 5){
            throw new IllegalArgumentException("la taille de la grille doit être comprise entre 2 et 50 pour la largeur et la hauteur");
        } 
        this.width = width;
        this.height = height;
        this.minesCount = minesCount;
        this.flagsCount = minesCount;
        this.grid = new Case[width][height];
        this.initGrid(width, height, minesCount);
    }

    // Constructeur de Demineur où tout est initilisé à 0 qui sert en cas de chargement de niveau
    public Demineur() {
        this.width = 0;
        this.height = 0;
        this.minesCount = 0;
        this.flagsCount = 0;
        this.grid = new Case[0][0];
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    private int getMinesCount() {
        return this.minesCount;
    }

    private int getFlagsCount() {
        return this.flagsCount;
    }

    private void setMinesCount(int mines) {
        this.minesCount = mines;
    }

    private void setFlagsCount(int flags) {
        if (flags > this.getMinesCount()){
            throw new IllegalArgumentException("le nombre de drapeaux ne peut pas être supérieur au nombre de mines");
        }
        this.flagsCount = flags;
    }


    private Case getCase(int y, int x) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        } else {
            return null;
        }
    }

    private boolean isFirstTry() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (this.grid[y][x].isVisible()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void linkNeighbors () {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Case current = grid[y][x];
                for (Direction direction : Direction.values()) {
                    int dx = 0;
                    int dy = 0;
                    switch (direction) {
                        case NORTH:
                            dy = -1;
                            break;
                        case SOUTH:
                            dy = 1;
                            break;
                        case EAST:
                            dx = 1;
                            break;
                        case WEST:
                            dx = -1;
                            break;
                        case NORTHEAST:
                            dx = 1;
                            dy = -1;
                            break;
                        case NORTHWEST:
                            dx = -1;
                            dy = -1;
                            break;
                        case SOUTHEAST:
                            dx = 1;
                            dy = 1;
                            break;
                        case SOUTHWEST:
                            dx = -1;
                            dy = 1;
                            break;
                    }
                    int neighborX = x + dx;
                    int neighborY = y + dy;
                    Case neighbor = getCase(neighborY, neighborX);
                    if (neighbor != null) {
                        current.setNeighbor(direction, neighbor);
                    }
                }
            }
        }
    }

    // On remplit les attribut de l'objet en fonction des dimensions de la grille, du nombre de mines, (et enevntuelle des coordonnées de la case de départ
    private void initGrid(int width, int height, int numMines, int... caseDepart) {
        if (caseDepart.length != 0 && caseDepart.length != 2) {
            throw new IllegalArgumentException("seulement 2 coordonnées pour la case de Départ sont requises");
        }
        // Initialiser la grille
        this.width = width;
        this.height = height;
        this.minesCount = numMines;
        this.grid = new Case[height][width];

        // Initialiser les cases de la grille
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.grid[y][x] = new Case();
            }
        }
        // Placer les mines aléatoirement dans la grille
        Random random = new Random();
        int numMinesPlaced = 0;
        while (numMinesPlaced < this.minesCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Case current = this.grid[y][x];
            if (caseDepart.length == 2) {
                // on évite de mettre une bombe sur la case dont les coordonnées sont données en argument (dans le cas d'un premier coup)
                if (!current.isMine() && ((caseDepart[0] < x-1 || caseDepart[0] > x+1) || (caseDepart[1] < y-1 || caseDepart[1] > y+1))) {
                    current.setMine(true);
                    numMinesPlaced++;
                }
            } else {
                if (!current.isMine()) {
                    current.setMine(true);
                    numMinesPlaced++;
                }
            }
        }
        linkNeighbors(); // On lie les cases entre elles
    }

    public void displayGrid() {
        // Effacer le terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Affichage de la grille 

        // Afficher l'entête pour les colones
        if (this.width >= 10) {
            System.out.print(" ");
        }

        for (int nbColones = 1; nbColones <= width; nbColones++) {
            if (nbColones < 11) {
                System.out.print("   ");
                System.out.print(nbColones);
            } else {
                System.out.print("  ");
                System.out.print(nbColones);
            }
        }
        System.out.println();

        if (this.width >= 10) {
            System.out.print(" ");
        }
        System.out.print(" \u250C\u2500\u2500\u2500\u252C\u2500\u2500\u2500");
        for (int i = 0; i < width - 2; i++) {
            System.out.print("\u252C\u2500\u2500\u2500");
        }
        System.out.print("\u2510");

        System.out.println();
        for (int y = 0; y < height; y++) {
            if (this.width >= 10 && y+1 < 10) {
                System.out.print( (y+1) + " " + "\u2502" );
            }
            else {
                System.out.print( (y+1) + "\u2502" );
            }
            for (int x = 0; x < width; x++) {
                Case current = this.grid[y][x];
                if (current.isFlag()) {
                    System.out.print(" \u2691 "); // Drapeau
                } else if (!current.isVisible()) {
                    System.out.print(" \u25A0 "); // Case non révélée
                } else if (current.isMine()) {
                    System.out.print(" \u2620 "); // Mine
                } else {
                    int numAdjacentMines = current.getMinesAround();
                    if (numAdjacentMines == 0) {
                        System.out.print("   "); // Zéro mine adjacente
                    } else {
                        System.out.print(" " + numAdjacentMines + " "); // Nombre de mines adjacentes
                    }
                }
                System.out.print("\u2502");
            }
            System.out.println();
            if (y < height - 1){
                if (this.width >= 10) {
                    System.out.print("  " + "\u251C\u2500\u2500\u2500" );
                }
                else {
                    System.out.print(" " + "\u251C\u2500\u2500\u2500" );
                }
                for (int x = 1; x < width; x++) {
                    System.out.print("\u253C\u2500\u2500\u2500");
                }
                System.out.println("\u2524");
            }
        }
        if (this.width >= 10) {
            System.out.print(" ");
        }
        System.out.print(" \u2514\u2500\u2500\u2500\u2534\u2500\u2500\u2500");
        for (int i = 0; i < width - 2; i++) {
            System.out.print("\u2534\u2500\u2500\u2500");
        }
        System.out.println("\u2518");
        // On affiche les informations utiles sous la grille :
        // le nombre de mines, le nombre de drapeau restants
        System.out.println("Mines : " + this.getMinesCount() + "   Flags : " + this.getFlagsCount());
    }

    public void reveal(int cellRow, int cellColumn){
        if (!this.getCase(cellRow, cellColumn).isFlag()){
            if (this.isFirstTry()){
                // Si c'est le premier coup on réinitialise la grille en prenant compte des coordonnées de la case visée
                // afin de faire en sorte que le premier coup ne cible pas une bombe
                this.initGrid(this.width, this.height, this.minesCount, cellRow, cellColumn);
            }
            this.getCase(cellRow, cellColumn).uncover();
            if (this.getCase(cellRow, cellColumn).isMine()) {
                this.revealAll();
            }
        }
        this.displayGrid();  
    }

    public void mark(int cellRow, int cellColumn){
        if (this.getCase(cellRow, cellColumn).isFlag() == true) {
            this.setFlagsCount(this.getFlagsCount() + 1);
            this.getCase(cellRow, cellColumn).setFlag(false);
        } else if ((this.getFlagsCount() > 0) && (!this.getCase(cellRow, cellColumn).isVisible())){
            this.setFlagsCount(this.getFlagsCount() - 1);
            this.getCase(cellRow, cellColumn).setFlag(true);
        }               
        this.displayGrid();
    }

    public boolean isFinished() {
        // La partie est finie si toutes les cases ont été révélées
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Case current = this.grid[y][x];
                if (!current.isMine() && !current.isVisible()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isDefeat() {
        // La partie est une défaite si toutes les cases ont été révélées
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Case current = this.grid[y][x];
                if (current.isMine() && current.isVisible()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isVictory() {
        // La partie est gagnée si toutes les mines sont marquées et que toutes les cases non-minées sont révélées
        if (!isFinished()) {
            return false;
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Case current = this.grid[y][x];
                if (current.isMine() && !current.isFlag()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void revealAll() {
        // Révèle toutes les cases de la grille
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Case current = this.grid[y][x];
                current.setVisible(true);
            }
        }
    }

    public void save(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            for (int i = 0; i < this.width; i++) {
                for (int j = 0; j < this.height; j++) {
                    if (!grid[i][j].isVisible()){
                        if (grid[i][j].isMine()){
                            if (grid[i][j].isFlag()) {
                                writer.write("B");
                            } else {
                                writer.write("M");
                            }
                        } else if (grid[i][j].isFlag()){
                            writer.write("F");
                        } else {
                            writer.write("C");
                        }
                    } else {
                        writer.write("V");
                    }
                }
                writer.write(System.lineSeparator()); // passer à la ligne suivante
            }
            writer.close();
        }
        catch (IOException e){
            System.out.println("Une erreur est survenue lors de l'écriture : " + e.getMessage());
        }
    }

    public void load(String filename) {
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            List <String> grilleStr = new ArrayList<>();
            String ligne;
            int nblignes = 0;
            int nbcolones = 0;
            this.height = 0;
            
            while ((ligne = bufferedReader.readLine()) != null) {
                this.width = 0;
                grilleStr.add(ligne);
                for (int i = 0; i < ligne.length(); i++){
                    this.width += 1;
                }
                this.height += 1;
            }

            this.grid = new Case[this.width][this.height];

            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j < this.width; j++){
                    if (grilleStr.get(i).charAt(j) == 'M'){
                        this.grid[i][j] = new Case();
                        this.grid[i][j].setMine(true);
                        this.minesCount += 1;
                        this.flagsCount += 1;
                    } else if (grilleStr.get(i).charAt(j) == 'F'){
                        this.grid[i][j] = new Case();
                        this.grid[i][j].setFlag(true);
                        this.flagsCount -= 1;
                    } else if (grilleStr.get(i).charAt(j) == 'C'){
                        this.grid[i][j] = new Case();
                    } else if (grilleStr.get(i).charAt(j) == 'V'){
                        this.grid[i][j] = new Case();
                        this.grid[i][j].setVisible(true);
                    }   else if (grilleStr.get(i).charAt(j) == 'B'){
                        this.grid[i][j] = new Case();
                        this.grid[i][j].setFlag(true);
                        this.grid[i][j].setMine(true);
                        this.minesCount += 1;
                    }
                    else {
                        throw new IllegalArgumentException();
                    }
                }
            }
            this.linkNeighbors();
        }
        catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la lecture : " + e.getMessage());
        }
    }
}