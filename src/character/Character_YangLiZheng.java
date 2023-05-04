package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.List;

public class Character_YangLiZheng extends Character{
    public Character_YangLiZheng(Team team, int location) {
        super("杨礼政", 1000, 350, 50, 240, 60, 40, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
    }

    @Override
    public void characterMotion() {
        skillTwo();
        if (Utils.random(50) && Utils.enoughMp(this, 130)) {
            skillThree();
        } else {
            normalAtk();
        }
    }

    /**
     * 承担（被动）
     * 计算伤害后，周围的人受到的25%伤害转移给你
     */
    private void skillOne() {
        Character[] list = team.getLocationList();
        if (location - 3 >= 0 && list[location - 3] != null) {
            Utils.addBuff(list[location - 3].specialBuffs, new Buff("承担", 0, true, 999));
        }
        if (location + 3 < 9 && list[location + 3] != null) {
            Utils.addBuff(list[location + 3].specialBuffs, new Buff("承担", 0, true, 999));
        }
        if ((location - 1) / 3 == location / 3 && location - 1 >= 0 && list[location - 1] != null) {
            Utils.addBuff(list[location - 1].specialBuffs, new Buff("承担", 0, true, 999));
        }
        if ((location + 1) / 3 == location / 3 && location + 1 < 9 && list[location + 1] != null) {
            Utils.addBuff(list[location + 1].specialBuffs, new Buff("承担", 0, true, 999));
        }
    }

    /**
     * 再生力（被动）
     * 你每失去1%的HP，提高1%你的HP回复效果
     */
    private void skillTwo() {
        Utils.addBuff(hpRecoverBuffs, new Buff("再生力",
                1.0 + (((double) getMaxHp() - getCurrentHp()) / getMaxHp()), false, 1));
    }

    /**
     * 善意的抱歉（主动） 130MP
     * 对一排人造成70点穿透伤害，并使其下一回合造成的伤害降低45%
     */
    private void skillThree() {
        Team anotherTeam = Utils.getAnotherTeam(this);
        List<Character> rowList = anotherTeam.getSameRow(anotherTeam.getRandomOneStillAliveByMagic());
        for (Character character : rowList) {
            Utils.print(getName() + "对" + character.getName() + "使用了善意的抱歉，ta造成的伤害降低了");
            Utils.trueAttackCalculate(this, character, 70);
            Utils.addBuff(character.getDamageBuffs(), new Buff("善意的抱歉", 0.55, false, 2));
        }
    }
}
