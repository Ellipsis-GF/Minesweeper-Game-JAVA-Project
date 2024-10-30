import java.util.*;

final public class Case {
    private boolean mine;
    private boolean flag;
    private boolean visible;
    private int minesAround;
    private Map<Direction, Case> neighbors;

    public Case() {
        this.mine = false;
        this.flag = false;
        this.visible = false;
        this.minesAround = 0;
        this.neighbors = new EnumMap<>(Direction.class);
    }

    public boolean isMine() {
        return this.mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isFlag() {
        return this.flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getMinesAround() {
        return this.minesAround;
    }

    // Permet de réactualiser le nombre de mines autour de la case
    private void updateMinesAround(){
        this.minesAround = 0;
        for (Direction direction : Direction.values()) {
            if (this.neighbors.get(direction) != null && this.neighbors.get(direction).isMine()) {
                this.minesAround += 1;
            }
        }
    }

    // Permet d'ajouter/remplacer une case voisine dans une direction choisis
    public void setNeighbor(Direction direction, Case neighbor) {
        this.neighbors.put(direction, neighbor);
        this.updateMinesAround();
    }

    // Découvre la case en question et de manière récursive toutes ses voisines qui ne sont pas des mines. (seulement si la case est a proximité d'aucune bombe)
    public void uncover() {
        if (!this.isVisible()) {
            this.setVisible(true);
            if (this.minesAround == 0) {
                for (Case neighbor : this.neighbors.values()) {
                    neighbor.uncover();
                }
            }
        }
    }
}
