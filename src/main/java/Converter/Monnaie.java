package Converter;

import java.io.Serializable;
import java.util.List;

public class Monnaie implements Serializable {
    private List<String> pays;
    private String nameMonnaie;
    private String codeMonnaie;
    private Double tauxChange;

    public Monnaie(List<String> pays, String nameMonnaie, String codeMonnaie, Double tauxChange) {
        this.pays = pays;
        this.nameMonnaie = nameMonnaie;
        this.codeMonnaie = codeMonnaie;
        this.tauxChange = tauxChange;
    }

    public List<String> getPays() {
        return pays;
    }

    public void setPays(List<String> pays) {
        this.pays = pays;
    }

    public String getNameMonnaie() {
        return nameMonnaie;
    }

    public void setNameMonnaie(String nameMonnaie) {
        this.nameMonnaie = nameMonnaie;
    }

    public String getCodeMonnaie() {
        return codeMonnaie;
    }

    public void setCodeMonnaie(String codeMonnaie) {
        this.codeMonnaie = codeMonnaie;
    }

    public Double getTauxChange() {
        return tauxChange;
    }

    public void setTauxChange(Double tauxChange) {
        this.tauxChange = tauxChange;
    }

    @Override
    public String toString() {
        return "Monnaie{" +
                "pays=" + pays +
                ", nameMonnaie='" + nameMonnaie + '\'' +
                ", codeMonnaie='" + codeMonnaie + '\'' +
                ", tauxChange=" + tauxChange +
                '}';
    }

    public void addPays(String p){
        pays.add(p);
    }
}
