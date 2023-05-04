package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.Iterator;

public class Character_ChengXianWen extends Character{

    private boolean useSkillFive = false;
    public Character_ChengXianWen(Team team, int location) {
        super("程显雯", 850, 400, 80, 270, 30, 30, team, location);
    }

    @Override
    public void afterAttack(Character main, Character passive, int fromWhere, int damage) {
        super.afterAttack(main, passive, fromWhere, damage);
        if (main.equals(this)) {
            skillOne(passive);
        }
        if (main.equals(this) && Utils.hasBuff(this, "跌宕的回音") && passive.getCurrentHp() > 0) {
            Iterator<Buff> iterator = getSpecialBuffs().iterator();
            while (iterator.hasNext()) {
                Buff buff = iterator.next();
                if (buff.getName().equals("跌宕的回音")) {
                    iterator.remove();
                }
            }
            Utils.print(getName() + "通过跌宕的回音追加了一段穿透伤害");
            Utils.trueAttackCalculate(this, passive, (int) (0.35 * damage));
        }
    }

    @Override
    public void characterMotion() {
        super.characterMotion();
        skillTwo();
        skillThree();
        skillFour();
        if (Utils.random(30 + getLoseHPPercent()) && Utils.enoughMp(this, 190)) {
            skillFive();
        }
        normalAtk();
        if (useSkillFive) {
            setNormalAtk(getNormalAtk() - 20);
            useSkillFive = false;
        }
    }

    /**
     * 气魄（被动）
     * 当你造成伤害后，降低对方25%当前的速度储存量
     */
    public void skillOne(Character character) {
        int value = (int) (character.getSpdRemain() * 0.25);
        character.setSpdRemain(character.getSpdRemain() * 0.75);
        Utils.print(getName() + "降低了" + character.getName() + value + "点速度储存量");
    }

    /**
     * 矢志不渝的决心（被动）
     * 回合开始时，若你的HP低于55%，则减少对方场上HP最少的目标20点护甲至ta的回合开始
     */
    public void skillTwo() {
        if (getHPPercent() < 55) {
            Utils.print(getName() + "降低了" + Utils.getAnotherTeam(this).getHPLowest().getName() + "的护甲");
            Utils.addBuff(
                    Utils.getAnotherTeam(this).getHPLowest().getPlusDefBuffs(), new Buff("矢志不渝的决心", -20, false, 1));
        }
    }

    /**
     * 坚不可摧的信心（被动）
     * 回合开始时，如果你仍带有降低普通攻击伤害的debuff，你本回合的普通攻击伤害提高15%
     */
    public void skillThree() {
        boolean flag = false;
        for (Buff buff : getNormalAtkBuffs()) {
            if (buff.getValue() < 1.0) {
                flag = true;
                break;
            }
        }
        if (flag) {
            Utils.print(getName() + "由于普通攻击被降低，反而提高了普通攻击伤害");
            Utils.addBuff(getNormalAtkBuffs(), new Buff("坚不可摧的信心", 1.15, false, 1));
        }
    }

    /**
     * 永世不忘的初心（被动）
     * 回合开始时，若对方的存活人数比我方少时，你的速度提高10%一回合
     */
    public void skillFour() {
        if (Utils.getAnotherTeam(this).getStillAlive().size() < team.getStillAlive().size()) {
            Utils.print(getName() + "见胜利在望，乘胜追击，提高了自身的速度");
            Utils.addBuff(getSpdBuffs(), new Buff("永世不忘的初心", 1.1, false, 1));
        }
    }

    /**
     * 跌宕的回音（主动） 190MP
     * 你本回合的普通攻击力+20，普通攻击后对目标再次造成35%*（上一次造成的伤害）的穿透伤害
     */
    public void skillFive() {
        setNormalAtk(getNormalAtk() + 20);
        useSkillFive = true;
        Utils.print(getName() + "使用了跌宕的回音，临时提高了普通攻击力，并且普通攻击后追击");
        Utils.addBuff(getSpecialBuffs(), new Buff("跌宕的回音", 0, false, 1));
    }
}
