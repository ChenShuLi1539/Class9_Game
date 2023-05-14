package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Character_HuBoNing extends Character{
    public Character_HuBoNing(Team team, int location) {
        super("胡伯宁", 800, 400, 40, 270, 30, 50, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
        skillTwo();
    }

    @Override
    public void characterMotion() {
        skillThree();
        if (Utils.enoughMp(this, 130)) {
            skillFour();
        } else {
            normalAtk();
        }
    }

    /**
     * 密钥（被动）
     * 我方所有人的MP回复效果提高30%
     */
    private void skillOne() {
        for (Character character : team.getCharacterList()) {
            Utils.addBuff(character.getMpRecoverBuffs(), new Buff("密钥", 1.3, true, 999));
        }
    }

    /**
     * 争强好胜（被动）
     * 当场上有其他人受到治疗时，为你回复50点HP
     */
    private void skillTwo() {
        Utils.addBuff(getSpecialBuffs(), new Buff("争强好胜", 0, true, 999));
    }

    /**
     * 背水一战（被动）
     * 你的回合开始时，如果你的MP小于170，你本回合临时提高30%的速度
     */
    private void skillThree() {
        if (getCurrentMp() < 170) {
            Utils.print(getName() + "由于MP小于一定值，本回合临时提高了速度");
            Utils.addBuff(getSpdBuffs(), new Buff("背水一战", 1.3, false, 1));
        }
    }

    /**
     * 不学无术（主动） 130MP
     * 对对方任意两个目标造成50+（对方普通攻击力）的魔法伤害，并降低对方50%的物理伤害
     */
    private void skillFour() {
        for (Character character : Utils.getAnotherTeam(this).getNumCharacter(2)) {
            Utils.print(getName() + "对" + character.getName() + "使用不学无术，降低了ta的物理伤害");
            Utils.magicAttackCalculate(this, character,
                    (int) (50 + (character.getNormalAtk() * Utils.calculateBuff(character.getNormalAtkBuffs()))));
            Utils.addBuff(character.getReduceAtkBuffs(), new Buff("不学无术", 0.5, false, 2));
        }
    }
}
