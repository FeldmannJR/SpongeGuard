/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.sponge.guard.region;

/**
 *
 * @author Carlos
 */
public enum EnumFlags {

    BUILD(true, "Se pode construir"),
    PLACE(true, "Se pode colocar blocos(sobescreve build)"),
    BREAK(true, "Se pode quebrar blocos(sobescreve build)"),
    PVP(true, "Se pode acontencer pvp"),
    MOBDAMAGE(true, "Se mobs podem causar dano"),
    EXPDROP("Se pode dropar exp"),
    ITEMDROP("Se pode dropar item"),
    MOBS("Se mobs agressivos podem spawnar"),
    CREEPER_EXPLOSIONS(false, "Se creepers irão quebrar blocos"),
    ENDERMANGRIEF(false, "Se enderman vão roubar blocos"),
    ENDERPEARL("Se é possível jogar enderpearls"),
    ENDERDRAGON_BLOCK_DAMAGE(false, "Se o dragão quebra blocos"),
    SLEEP("Se pode dormir na região"),
    TNT(false, "Se tnt quebra blocos"),
    OTHER_EXPLOSIONS(false, "Other explosions"),
    CHESTS(true, "Pode acessar baus ou outros blocos"),
    WATERFLOW(true, "Se a agua corre na região"),
    LAVAFLOW(true, "Se a lava corre na região"),
    DOORS(true, "Se pode abrir portas, trapdoors"),
    VEHICLEPLACE(true, "Se pode colocar veiculos"),
    VEHICLEDESTROY(true, "Se pode destruir veiculos"),
    LEAFDECAY(true, "Se as folhas caeem"),
    SENDCHAT(true, "Se pode usar o chat"),
    POTIONSPLASH(true, "Se pode jogar possoes"),
    HUNGER("Se player perdem comida na região"),
    ENDERCHEST("Se pode acessar enderchest"),
    DAMAGE("Se pode tomar dano sufocando"),
    DROWN("Se pode se afogar"),
    ANIMALS("Se spawna animais"),
    POKEMONS("Se spawna entidades do pokemon(pokemons ou trainers)"),
    JET(true, "Se pode usar a jet!"),
    SENDPOKES(true, "Se jogadores podem arremecar pokemons"),
    TPSCROLL(false, "Se precisa de ter um tpscroll para ficar na área!");

    public boolean def = true;
    public String help = "";

    private EnumFlags(String help) {
        def = true;
        this.help = help;
    }

    public String getHelp() {
        return help;
    }

    public String getCommandName() {
        return name().toLowerCase().replace("_", "");
    }

    private EnumFlags(boolean def, String help) {
        this.def = def;
        this.help = help;
    }

}
