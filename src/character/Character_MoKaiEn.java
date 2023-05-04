package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.Iterator;
import java.util.List;

public class Character_MoKaiEn extends Character{
    public Character_MoKaiEn(Team team, int location) {
        super("莫凯恩", 750, 600, 40, 260, 20, 70, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
    }

    @Override
    public void characterMotion() {
        if (team.hasFallDownCharacter() && getCurrentMp() > 75) {
            skillTwo();
        }
        if (Utils.random(60) && Utils.enoughMp(this, 100)) {
            skillThree();
        }
        if (team.getMyTeamHPPercent() < 0.5 && getCurrentHp() > 0.35 * getMaxHp()) {
            setCurrentHp((int) (getCurrentHp() - 0.2 * getMaxHp()));
            skillFour();
        }
        normalAtk();
    }

    /**
     * 来自遥远时空的德意志的援助（被动）
     * 你周围的人魔抗+20
     */
    private void skillOne() {
        Character[] list = team.getLocationList();
        if (location - 3 >= 0 && list[location - 3] != null) {
            list[location - 3].setMdf(list[location - 3].getMdf() + 20);
        }
        if (location + 3 < 9 && list[location + 3] != null) {
            list[location + 3].setMdf(list[location + 3].getMdf() + 20);
        }
        if ((location - 1) / 3 == location / 3 && location - 1 >= 0 && list[location - 1] != null) {
            list[location - 1].setMdf(list[location - 1].getMdf() + 20);
        }
        if ((location + 1) / 3 == location / 3 && location + 1 < 9 && list[location + 1] != null) {
            list[location + 1].setMdf(list[location + 1].getMdf() + 20);
        }
    }

    /**
     * 否极泰来（主动） 0MP
     * 当前MP超过75时才可使用。消耗你所有MP，救起并治疗我方一人等量的HP
     */
    private void skillTwo() {
        Character character = team.getRandomOneFallDown();
        Utils.print(getName() + "对" + character.getName() + "使用了否极泰来");
        Utils.cureCalculate(this, character, getCurrentMp());
        setCurrentMp(0);
    }

    /**
     * 据理力争（主动） 100MP
     * 清除我方一人的物伤DeBuff，并提高其30%的物理伤害两回合
     */
    private void skillThree() {
        Character character = team.getRandomOneStillAliveByMagic();
        Utils.print(getName() + "对" + character.getName() + "使用了据理力争，" + character.getName() + "的物理伤害提高了");
        Iterator<Buff> iterator = character.getAtkBuffs().iterator();
        while (iterator.hasNext()) {
            Buff buff = iterator.next();
            if (buff.getValue() < 0) {
                iterator.remove();
                Utils.print(character.getName() + "清除了" + buff.getName() + "的负面效果");
            }
        }
        Iterator<Buff> iterator2 = character.getDamageBuffs().iterator();
        while (iterator2.hasNext()) {
            Buff buff = iterator2.next();
            if (buff.getValue() < 0) {
                iterator.remove();
                Utils.print(character.getName() + "清除了" + buff.getName() + "的负面效果");
            }
        }
        Utils.addBuff(character.getAtkBuffs(), new Buff("据理力争", 1.3, false, 3));
    }

    /**
     * 身先士卒（主动） 20%最大HP
     * 治疗我方全体50+（目标魔抗）的HP
     */
    private void skillFour() {
        List<Character> list = team.getStillAlive();
        Utils.print(getName() + "使用了身先士卒治疗了我方全体");
        for (Character character : list) {
            Utils.cureCalculate(this, character, 50 + Utils.calculateMdfByPlus(character));
        }
    }
}
