package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Character_LiKeZhen extends Character{

    public Character_LiKeZhen(Team team, int location) {
        super("李可禛", 750, 450, 40, 250, 30, 60, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
    }

    @Override
    public void characterMotion() {
        if (currentMp >= 200 && Utils.enoughMp(this, 80)) {
            skillThree();
        }
        if (currentHp <= 400 && Utils.enoughMp(this, 80)) {
            skillFour();
        }
        if (Utils.enoughMp(this, 110)) {
            skillTwo();
        } else {
            normalAtk();
        }
    }

    /**
     * 学佬之力（被动）
     * 当场上有人倒下时，恢复100点MP
     */
    private void skillOne() {
        Utils.addBuff(specialBuffs, new Buff("学佬之力", 0, true, 999));
    }

    /**
     * 唇枪舌剑（主动） 110MP
     * 对对方一排人造成130*（1+你已损失HP%）的魔法伤害，如果对方受伤后HP依然比你高，则减少对方20%魔抗两回合
     */
    private void skillTwo() {
        Team anotherTeam = Utils.getAnotherTeam(this);
        List<Character> rowList = anotherTeam.getSameRow(anotherTeam.getRandomOneStillAliveByMagic());
        for (Character character : rowList) {
            Utils.print(getName() + "对" + character.getName() + "使用了唇枪舌剑");
            Utils.magicAttackCalculate(this, character, (int) (130 * (1 + ((maxHp - currentHp) / maxHp))));
            if (character.currentHp > currentHp) {
                Utils.print(getName() + "的唇枪舌剑附加效果使" + character.getName() + "减少了两回合魔抗");
                Utils.addBuff(character.getMdfBuffs(), new Buff("唇枪舌剑", 0.8, false, 2));
            }
        }
    }

    /**
     * 质问（主动） 80MP
     * 减少场上任意两人的30魔抗至他们的回合开始
     */
    private void skillThree() {
        for (Character character : Utils.getAnotherTeam(this).getNumCharacter(2)) {
            Utils.print(getName() + "对" + character.getName() + "使用质问，减少了ta的魔抗");
            Utils.addBuff(character.getPlusMdfBuffs(), new Buff("质问", -30, false, 1));
        }
    }

    /**
     * 委曲求全（主动） 80MP
     * 减少90%你收到的物理伤害一回合
     */
    private void skillFour() {
        Utils.print(getName() + "使用委曲求全，减少了受到的物理伤害一回合");
        Utils.addBuff(reduceAtkBuffs, new Buff("委曲求全", 0.1, false, 1));
    }
}
