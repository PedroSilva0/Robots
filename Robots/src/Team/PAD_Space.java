/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team;

/**
 *
 * @author Utilizador
 */
public class PAD_Space {

    private int pleasure;
    private int arousal;
    private int dominance;

    public PAD_Space() {
        pleasure = 0;
        arousal = 0;
        dominance = 0;
    }

    public int getArousal() {
        return arousal;
    }

    public int getDominance() {
        return dominance;
    }

    public int getPleasure() {
        return pleasure;
    }

    public void updateArousal(int arousal) {
        this.arousal += arousal;
    }

    public void updateDominance(int dominance) {
        this.dominance += dominance;
    }

    public void updatePleasure(int pleasure) {
        this.pleasure += pleasure;
    }

    public String evaluate() {
        int rate = 0;
        if (pleasure < 0) {
            rate--;
        } else if (pleasure > 0) {
            rate++;
        }
        if (arousal < 0) {
            rate--;
        } else if (arousal > 0) {
            rate++;
        }
        if (dominance < 0) {
            rate--;
        } else if (dominance > 0) {
            rate++;
        }
        switch(rate){
            case -3:
                return "This robot had and awful experience.";
            case -2:
                return "This robot had a bad experience.";
            case -1:
                return "This robot had a slightly bad experience.";
            case 0:
                return "This robot had a neutral experience.";
            case 1:
                return "This robot had a good experience.";
            case 2: 
                return "This robot had a great experience.";
            case 3:
                return "This robot had a awesome experience";
        }
        return "";
    }

}
