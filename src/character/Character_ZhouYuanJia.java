package character;

import buff.Buff;
import game.Team;
import utils.Utils;

public class Character_ZhouYuanJia extends Character{

    private int skillThreeCD = 5;

    public Character_ZhouYuanJia(Team team, int location) {
        super("周园佳", 800, 400, 55, 275, 40, 20, team, location);
    }

    @Override
    public void characterMotion() {
        super.characterMotion();
        skillThreeCD++;
        skillOne();
        if (Utils.random((int) (150 - Utils.getAnotherTeam(this).getMyTeamHPPercent() * 100))
                && Utils.enoughMp(this, 100)) {
            skillTwo();
        }
        if (skillThreeCD > 5 && Utils.random(50 + round * 10)) {
            skillThree();
        } else {
            normalAtk();
        }
    }

    /**
     * 昂扬向上（被动）
     * 你的回合开始时，如果你处于被封印的状态，提高你100%的MP回复一回合
     */
    private void skillOne() {
        if (Utils.isForbidden(this)) {
            Utils.print(getName() + "由于被封印，提高了MP回复效果");
            Utils.addBuff(getMpRecoverBuffs(), new Buff("昂扬向上", 2, false, 1));
        }
    }

    /**
     * 波谲云诡（主动） 100MP
     * 选定对对方随机四个目标，有30%的概率使ta受治疗的效果不超过30%两回合
     */
    private void skillTwo() {
        for (Character character : Utils.getAnotherTeam(this).getNumCharacter(4)) {
            if (Utils.calculateForbidSuccess(this, character, 30)) {
                Utils.print(getName() + "对" + character.getName() + "使用了波谲云诡，封印成功，降低对方的受治疗效果");
                Utils.addBuff(character.getForbidBuffs(), new Buff("波谲云诡", 0, false, 3));
            } else {
                Utils.print(getName() + "对" + character.getName() + "使用了波谲云诡，但是失败了");
            }
        }
    }

    /**
     * 不没的微笑（主动） 0MP
     * 回复我方所有人150点MP，使用后跳过该回合。该技能有5回合CD
     */
    private void skillThree() {
        for (Character character : getTeam().getStillAlive()) {
            Utils.print(getName() + "对" + character.getName() + "使用了不没的微笑，回复了ta 150点MP");
            Utils.mpRecoverCalculate(character, 150);
        }
        skillThreeCD = 0;
    }
}
