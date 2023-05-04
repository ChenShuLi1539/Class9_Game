package character;

import buff.Buff;
import game.Team;
import utils.Utils;

public class Character_WuTong extends Character{
    public Character_WuTong(Team team, int location) {
        super("吴童", 950, 350, 55, 230, 40, 40, team, location);
        skillOne();
    }

    @Override
    public void characterMotion() {
        skillTwo();
        if (currentHp > 400 && Utils.enoughMp(this, 100)) {
            skillThree();
        } else {
            normalAtk();
        }
    }

    /**
     * 皮糙肉厚（被动）
     * 你受到的物理伤害减少15%
     */
    private void skillOne() {
        Utils.addBuff(reduceAtkBuffs, new Buff("皮糙肉厚", 0.85, true, 999));
    }

    /**
     * 革新（被动）
     * 在你的HP低于40%时，你的每次普通攻击都会额外造成25%*（对方当前HP）的魔法伤害
     */
    private void skillTwo() {
        if (currentHp < 0.4 * maxHp) {
            Utils.addBuff(specialBuffs, new Buff("革新", 0, false, 1));
        }
    }

    /**
     * 剪刀脚（主动） 100MP
     * 对任意一人造成80点物理伤害，并减少其10%速度储存量和15%速度一回合
     */
    private void skillThree() {
        Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
        Utils.print(getName() + "对" + character.getName() + "使用了剪刀脚");
        Utils.attackCalculate(this, character, 80);
        character.spdRemain *= 0.9;
        Utils.addBuff(character.spdBuffs, new Buff("剪刀脚", 0.85, false, 1));
        Utils.print(character.getName() + "的速度被降低了");
    }
}
