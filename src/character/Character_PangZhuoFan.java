package character;

import buff.Buff;
import game.Team;
import utils.Utils;

public class Character_PangZhuoFan extends Character{
    public Character_PangZhuoFan(Team team, int location) {
        super("庞卓凡", 850, 500, 90, 260, 40, 60, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
        skillThree();
    }

    @Override
    public void afterAttack(Character main, Character passive, int fromWhere, int damage) {
        super.afterAttack(main, passive, fromWhere, damage);
        if (main.equals(this)) {
            skillTwo(damage);
        }
    }

    @Override
    public void characterMotion() {
        if (getCurrentMp() > 300 && Utils.enoughMp(this, 90)) {
            skillFour();
        }
        if (Utils.enoughMp(this, 110)) {
            skillFive();
        } else {
            normalAtk();
        }
    }

    /**
     * 刁蛮（被动）
     * 计算伤害后，其中的20%以MP抵挡
     */
    private void skillOne() {
        Utils.addBuff(specialBuffs, new Buff("刁蛮", 0, true, 999));
    }

    /**
     * 跬步行（被动）
     * 恢复造成伤害10%的MP
     */
    private void skillTwo(int damage) {
        Utils.print(getName() + "通过造成伤害恢复了" + (int) (0.1 * damage * Utils.calculateBuff(getMpRecoverBuffs())) + "点MP");
        currentMp += 0.1 * damage * Utils.calculateBuff(mpRecoverBuffs);
        if (currentMp > maxMp) {
            currentMp = maxMp;
        }
    }

    /**
     * 灵动智慧（被动）
     * 你的普通攻击以魔法伤害计算
     */
    private void skillThree() {
        Utils.addBuff(specialBuffs, new Buff("灵动智慧", 0, true, 999));
    }

    /**
     * 紧张感（主动） 90MP
     * 有20%的几率命中，使目标跳过下一回合
     */
    private void skillFour() {
        Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
        if (Utils.calculateForbidSuccess(this, character, 20)) {
            Utils.addBuff(character.getForbidBuffs(), new Buff("紧张感", 0, false, 2));
            Utils.print(getName() + "使" + character.getName() + "变得紧张了");
        } else {
            Utils.print(getName() + "的紧张感封印没能命中");
        }
    }

    /**
     * 失心（主动） 110MP
     * 对目标造成150+100*（对方已损失MP%）的魔法伤害，并有10%的几率使对方下回合无法普通攻击
     */
    private void skillFive() {
        Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
        double coefficient = 0.0;
        if (character.getMaxMp() != 0) {
            coefficient = ((double) character.getMaxMp() - character.getCurrentMp()) / character.getMaxMp();
        }
        Utils.print(getName() + "对" + character.getName() + "使用了失心");
        Utils.magicAttackCalculate(this, character, (int) (150 + 100 * coefficient));
        if (Utils.calculateForbidSuccess(this, character, 10)) {
            Utils.addBuff(character.getForbidBuffs(), new Buff("失心", 0, false, 2));
            Utils.print(getName() + "使" + character.getName() + "失去了心智，不能进行普通攻击了");
        } else {
            Utils.print(getName() + "的失心封印没能命中");
        }
    }


}
