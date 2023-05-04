package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.Random;

public class Character_ChenSiLin extends Character{

    public Character_ChenSiLin(Team team, int location) {
        super("陈思霖", 900, 350, 40, 270, 40, 40, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
    }

    @Override
    public void characterMotion() {
        super.characterMotion();
        if (team.hasFallDownCharacter() && Utils.enoughMp(this, 120)) {
            skillThree();
        }
        if (Utils.random(40) && Utils.enoughMp(this, 90)) {
            skillFour();
        }
        if (Utils.random((int) ((0.8 - team.getMyTeamHPPercent()) * 100)) && Utils.enoughMp(this, 90)) {
            skillTwo();
        }
        normalAtk();
    }

    /**
     * 蓬勃（被动）
     * 你前方一格的友方目标收到的治疗效果提高15%
     */
    private void skillOne() {
        if (location + 3 < 9 && team.getLocationList()[location + 3] != null) {
            Utils.addBuff(team.getLocationList()[location + 3].getHpRecoverBuffs(), new Buff("蓬勃", 1.15, true, 999));
        }
    }

    /**
     * 随遇而安（主动） 90MP
     * 治疗我方HP%最低的目标1~3次，每次治疗50+100*（你已损失HP%）的HP
     */
    private void skillTwo() {
        Character character = team.getHPLowest();
        int times = new Random().nextInt(3) + 1;
        for (int i = 0; i < times; i++) {
            Utils.print(getName() + "对" + character.getName() + "使用了随遇而安");
            Utils.cureCalculate(this, character, 50 + 100 * getLoseHPPercent() / 100);
        }
    }

    /**
     * 醒眠铃（主动） 120MP
     * 救起并治疗目标180HP，但是降低你20点护甲和魔抗至你下个回合开始
     */
    private void skillThree() {
        Character character = team.getRandomOneFallDown();
        Utils.print(getName() + "对" + character.getName() + "使用了醒眠铃救起了ta，但是降低了自身的护甲和魔抗");
        Utils.cureCalculate(this, character, 180);
        Utils.addBuff(getPlusDefBuffs(), new Buff("醒眠铃", -20, false, 1));
        Utils.addBuff(getPlusMdfBuffs(), new Buff("醒眠铃", -20, false, 1));
    }

    /**
     * 协同（主动） 90MP
     * 提高我方所有人10%魔法伤害和20点魔抗两回合
     */
    private void skillFour() {
        Utils.print(getName() + "使用了协同，提高了我方所有人的法伤和魔抗");
        for (Character character : team.getStillAlive()) {
            Utils.addBuff(character.getMatBuffs(), new Buff("协同", 1.1, false, 3));
            Utils.addBuff(character.getPlusMdfBuffs(), new Buff("协同", 20, false, 3));
        }
    }
}
