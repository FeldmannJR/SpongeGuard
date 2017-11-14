
import br.com.instamc.sponge.guard.region.Region;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Carlos
 */
public class Teste {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArrayList<Ob> reg = new ArrayList();
        reg.add(new Ob(0, "global"));
        reg.add(new Ob(5, "a"));
        reg.add(new Ob(2, "b"));
        reg.add(new Ob(9, "c"));
        reg.add(new Ob(0, "d"));
        Collections.sort(reg, new Comparator<Ob>() {

            @Override
            public int compare(Ob o1, Ob o2) {
              if(o1.isGlobal()){
                  return 1;
              }
              if(o2.isGlobal()){
                  return -1;
              }
                
                if (o1.getPriority() > o2.getPriority()) {
                    return -1;
                }
                if (o2.getPriority() > o1.getPriority()) {
                    return 1;
                }
                
                return 0;
            }
        });
        for (int x = 0; x < reg.size(); x++) {
            System.out.println(reg.get(x).nome + " - " + reg.get(x).x);
        }
    }

    public static class Ob {

        int x;
        String nome;

        public Ob(int x, String nome) {
            this.x = x;
            this.nome = nome;
        }

        public boolean isGlobal() {
            return nome.equals("global");
        }

        private int getPriority() {
            return x;
        }
    }

}
