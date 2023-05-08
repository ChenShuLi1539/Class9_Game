package character;

import buff.Buff;
import game.Team;
import utils.Utils;

public class Character_XuYue extends Character{

    public int coin = 0;

    public Character_XuYue(Team team, int location) {
        super("许悦", 750, 0, 30, 280, 30, 30, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
    }

    @Override
    public void characterMotion() {
        if (team.hasFallDownCharacter() && enoughCoin(5)) {
            skillThree();
        }
        if (enoughCoin(7)) {
            skillFour();
        }
        if (Utils.random(35) && enoughCoin(4)) {
            skillFive();
        }
        normalAtk();
    }

    @Override
    public String printState() {
        return "HP:" + currentHp + "/" + maxHp + " 钱币:" + coin;
    }

    /**
     * 振奋（被动）
     * 你周围的人加速10%
     */
    private void skillOne() {
        Character[] list = team.getLocationList();
        if (location - 3 >= 0 && list[location - 3] != null) {
            Utils.addBuff(list[location - 3].spdBuffs, new Buff("振奋", 1.1, true, 999));
        }
        if (location + 3 < 9 && list[location + 3] != null) {
            Utils.addBuff(list[location + 3].spdBuffs, new Buff("振奋", 1.1, true, 999));
        }
        if ((location - 1) / 3 == location / 3 && location - 1 >= 0 && list[location - 1] != null) {
            Utils.addBuff(list[location - 1].spdBuffs, new Buff("振奋", 1.1, true, 999));
        }
        if ((location + 1) / 3 == location / 3 && location + 1 < 9 && list[location + 1] != null) {
            Utils.addBuff(list[location + 1].spdBuffs, new Buff("振奋", 1.1, true, 999));
        }
    }

    /**
     * 钱庄（被动）
     * 我方每进行一个回合，钱币+1
     */

    /**
     * 生死时分的救助（主动） 5钱币
     * 救起并治疗我方一人100*（1+对方已损失HP%)的HP
     */
    private void skillThree() {
        Character character = team.getRandomOneFallDown();
        Utils.print(getName() + "对" + character.getName() + "使用了生死时分的救助");
        Utils.cureCalculate(this, character,
                100 * (1 + ((character.getMaxHp() - character.getCurrentHp()) / character.getMaxHp())));
    }

    /**
     * 财政紧缩（主动） 7钱币
     * 对对方全体使用，有75%的几率使目标下回合mp消耗加倍
     */
    private void skillFour() {
        for (Character character : Utils.getAnotherTeam(this).getStillAlive()) {
            if (Utils.calculateForbidSuccess(this, character, 75)) {
                Utils.addBuff(character.getForbidBuffs(), new Buff("财政紧缩", 0, false, 2));
                Utils.print(getName() + "对" + character.getName() + "成功使用了财政紧缩，使ta下回合MP消耗加倍了");
            } else {
                Utils.print(getName() + "对" + character.getName() + "使用了财政紧缩，但是失败了");
            }

        }
    }

    /**
     * 奖励（主动） 4钱币
     * 使我方一人下回合造成的伤害增加20%
     */
    public void skillFive() {
        Character character = team.getRandomOneStillAliveByMagic();
        Utils.print(getName() + "对" + character.getName() + "使用了奖励，" + character.getName() + "的伤害提高了");
        Utils.addBuff(character.getDamageBuffs(), new Buff("奖励", 1.2, false, 2));
    }

    private boolean enoughCoin(int value) {
        if (coin >= value) {
            coin -= value;
            Utils.print(getName() + "消耗了" + value + "点钱币，剩余" + coin + "点");
            return true;
        } else {
            return false;
        }
    }
}
