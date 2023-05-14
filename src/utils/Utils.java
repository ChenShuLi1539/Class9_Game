package utils;

import buff.Buff;
import character.Character_LiMingKun;
import character.Character_QinSiNing;
import character.Character_YangLiZheng;
import frame.GameFrame;
import game.Game;
import character.Character;
import game.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    
    public static final int FROM_NORMAL_ATTACK = 1;

    public static final int FROM_ATTACK = 2;

    public static final int FROM_MAGIC_ATTACK = 3;

    public static final int FROM_TRUE_ATTACK = 4;

    public static String log = "";
    
    public static boolean random(int probability) {
        return (Math.random() * 100 + 1) <= probability;
    }

    public static Team getAnotherTeam(Character character) {
        if (Game.getInstance().teamOne.equals(character.getTeam())) {
            return Game.getInstance().teamTwo;
        }
        return Game.getInstance().teamOne;
    }

    public static List<Character> getAllStillAlive() {
        List<Character> list = new ArrayList<>();
        list.addAll(Game.getInstance().teamOne.getStillAlive());
        list.addAll(Game.getInstance().teamTwo.getStillAlive());
        return list;
    }

    public static void normalAtkCalculate(Character main, Character passive) {
        main.beforeAttack(main, passive);
        for (Character character : passive.getTeam().getStillAlive()) {
            if (hasBuff(character, "挺身而出") && !character.equals(passive) && random(35)) {
                print(character.getName() + "替" + passive.getName() + "承担了此次普通攻击");
                passive = character;
                break;
            }
        }
        boolean isCriticalStrike = calculateCriticalStrikeSuccess(main, passive);
        String criticalStrikeStr = "";
        int damage;
        if (!hasBuff(main, "灵动智慧")) {
            damage = (int) (2 * main.getNormalAtk() * calculateBuff(main.getNormalAtkBuffs()) * 100 *
                    calculateBuff(passive.getReduceAtkBuffs()) * calculateBuff(main.getDamageBuffs()) /
                        (calculateDefByPlus(passive) * calculateBuff(passive.getDefBuffs()) + 100));
        } else {
            damage = (int) (main.getNormalAtk() * calculateBuff(main.getNormalAtkBuffs()) * 150 *
                    calculateBuff(passive.getReduceMatBuffs()) * calculateBuff(main.getDamageBuffs()) /
                        (calculateMdfByPlus(passive) * calculateBuff(passive.getMdfBuffs()) + 150));
        }
        if (isCriticalStrike) {
            damage *= 1.75;
            criticalStrikeStr = "打出了暴击！！";
        }
        damage = calculateHPMinus(passive, damage, main);
        print(main.getName() + criticalStrikeStr + "对" + passive.getName() + "普通攻击造成了" + damage + "点伤害，" +
                passive.getName() + "剩余" + passive.getCurrentHp() + "点HP");
        if (hasBuff(main, "革新") && passive.getCurrentHp() > 0) {
            print(main.getName() + "的革新追加了伤害");
            magicAttackCalculate(main, passive, (int) (passive.getCurrentHp() * 0.25));
        }
        main.afterAttack(main, passive, FROM_NORMAL_ATTACK, damage);
        passive.afterAttack(main, passive, FROM_NORMAL_ATTACK, damage);
    }

    public static void attackCalculate(Character main, Character passive, int damage) {
        main.beforeAttack(main, passive);
        boolean isCriticalStrike = calculateCriticalStrikeSuccess(main, passive);
        String criticalStrikeStr = "";
        damage = (int) (damage * calculateBuff(main.getAtkBuffs()) * 150 *
                calculateBuff(passive.getReduceAtkBuffs()) * calculateBuff(main.getDamageBuffs()) /
                        (calculateDefByPlus(passive) * calculateBuff(passive.getDefBuffs()) + 150));
        if (isCriticalStrike) {
            damage *= 1.75;
            criticalStrikeStr = "打出了暴击！！";
        }
        damage = calculateHPMinus(passive, damage, main);
        print(main.getName() + criticalStrikeStr + "对" + passive.getName() + "造成了" + damage + "点物理伤害，" +
                passive.getName() + "剩余" + passive.getCurrentHp() + "点HP");
        main.afterAttack(main, passive, FROM_ATTACK, damage);
        passive.afterAttack(main, passive, FROM_ATTACK, damage);
    }

    public static void magicAttackCalculate(Character main, Character passive, int damage) {
        main.beforeAttack(main, passive);
        boolean isCriticalStrike = calculateCriticalStrikeSuccess(main, passive);
        String criticalStrikeStr = "";
        damage = (int) (damage * calculateBuff(main.getMatBuffs()) * 150 *
                calculateBuff(passive.getReduceMatBuffs()) * calculateBuff(main.getDamageBuffs()) /
                (calculateMdfByPlus(passive) * calculateBuff(passive.getMdfBuffs()) + 150));
        if (isCriticalStrike) {
            damage *= 1.75;
            criticalStrikeStr = "打出了暴击！！";
        }
        damage = calculateHPMinus(passive, damage, main);
        print(main.getName() + criticalStrikeStr + "对" + passive.getName() + "造成了" + damage + "点魔法伤害，" +
                passive.getName() + "剩余" + passive.getCurrentHp() + "点HP");
        main.afterAttack(main, passive, FROM_MAGIC_ATTACK, damage);
        passive.afterAttack(main, passive, FROM_MAGIC_ATTACK, damage);
    }

    public static void trueAttackCalculate(Character main, Character passive, int damage) {
        main.beforeAttack(main, passive);
        damage *= calculateBuff(main.getDamageBuffs());
        // 穿透伤害不会暴击
        damage = calculateHPMinus(passive, damage, main);
        print(main.getName() + "对" + passive.getName() + "造成了" + damage + "点穿透伤害，" +
                passive.getName() + "剩余" + passive.getCurrentHp() + "点HP");
        main.afterAttack(main, passive, FROM_TRUE_ATTACK, damage);
        passive.afterAttack(main, passive, FROM_TRUE_ATTACK, damage);
    }

    public static int calculateHPMinus(Character character, int value, Character main) {
        if (hasBuff(character, "刁蛮")) {
            if (character.getCurrentMp() >= 0.2 * value) {
                character.setCurrentMp((int) (character.getCurrentMp() - 0.2 * value));
                print(character.getName() + "使用MP抵挡了" + (int) (0.2 * value) + "点伤害");
                value *= 0.8;
            }
        }
        if (hasBuff(character, "承担")) {
            for (Character character1 : character.getTeam().getStillAlive()) {
                if (character1 instanceof Character_YangLiZheng) {
                    character1.setCurrentHp((int) (character1.getCurrentHp() - 0.25 * value));
                    print(character1.getName() + "为" + character.getName() + "承担了" + (int) (0.25 * value) +
                            "点伤害，剩余" + character1.getCurrentHp() + "点HP");
                    main.afterAttack(main, character1, FROM_TRUE_ATTACK, (int) (0.25 * value));
                    character1.afterAttack(main, character1, FROM_TRUE_ATTACK, (int) (0.25 * value));
                    value *= 0.75;
                    break;
                }
            }
        }
        character.setCurrentHp(character.getCurrentHp() - value);
        return value;
    }

    public static void cureCalculate(Character main, Character passive, int value) {
        value *= calculateHPRecover(passive);
        if (value + passive.getCurrentHp() > passive.getMaxHp()) {
            value = passive.getMaxHp() - passive.getCurrentHp();
        }
        boolean notRevive = passive.getCurrentHp() > 0;
        passive.setCurrentHp(passive.getCurrentHp() + value);
        print(main.getName() + "治疗了" + passive.getName() + value + "点HP");
        if (!notRevive && hasBuff(passive, "百折不挠")) {
            passive.setNormalAtk(passive.getNormalAtk() + 10);
            passive.setDef(passive.getDef() + 10);
            passive.setMdf(passive.getMdf() + 10);
            print(passive.getName() + "被救起后增加了10点护甲、魔抗和普通攻击力");
        }
        for (Character character : getAllStillAlive()) {
            if (hasBuff(character, "争强好胜")) {
                int cureValue = (int) (50 * calculateHPRecover(character));
                if (cureValue + character.getCurrentHp() > character.getMaxHp()) {
                    cureValue = character.getMaxHp() - character.getCurrentHp();
                }
                character.setCurrentHp(character.getCurrentHp() + cureValue);
                print(character.getName() + "因为争强好胜回复了" + cureValue + "点HP");
            }
        }
    }

    public static double calculateBuff(List<Buff> list) {
        double percent = 1.0;
        for (Buff buff : list) {
            percent *= buff.getValue();
        }
        return percent;
    }

    public static double calculateSpeed(Character character) {
        double multiplier = calculateBuff(character.getSpdBuffs());
        if (multiplier > 1.0 && hasBuff(character, "如虎添翼")) {
            multiplier *= 1.15;
        }
        return character.getSpd() * multiplier;
    }

    public static double calculateHPRecover(Character character) {
        double multiplier = calculateBuff(character.getHpRecoverBuffs());
        if (multiplier > 0.3 && hasBuff(character, "波谲云诡")) {
            multiplier = 0.3;
        }
        return multiplier;
    }

    public static void mpRecoverCalculate(Character character, int value) {
        character.setCurrentMp(
                (int) (character.getCurrentMp() + value * Utils.calculateBuff(character.getMpRecoverBuffs())));
        if (character.getCurrentMp() > character.getMaxMp()) {
            character.setCurrentMp(character.getMaxMp());
        }
    }

    public static boolean hasBuff(Character character, String name) {
        for (List<Buff> list : character.getBuffLists()) {
            for (Buff buff : list) {
                if (buff.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean enoughMp(Character character, int value) {
        if (hasBuff(character, "财政紧缩")) {
            value *= 2;
        }
        if (character.getCurrentMp() >= value) {
            character.setCurrentMp(character.getCurrentMp() - value);
            print(character.getName() + "消耗了" + value + "点MP，剩余" + character.getCurrentMp() + "点");
            List<Character> list = Utils.getAnotherTeam(character).getStillAlive();
            for (Character character1 : list) {
                if (character1 instanceof Character_QinSiNing) {
                    int markPlus = value;
                    if (((Character_QinSiNing) character1).mark + markPlus > 600) {
                        markPlus = 600 - ((Character_QinSiNing) character1).mark;
                    }
                    ((Character_QinSiNing) character1).mark += markPlus;
                    print(character1.getName() + "获得了" +
                            markPlus + "点‘意’，当前已有" + ((Character_QinSiNing) character1).mark + "点'意'");
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static void addBuff(List<Buff> list, Buff buff) {
        boolean flag = false;
        for (Buff b : list) {
            if (b.getName().equals(buff.getName())) {
                if (b.getLastTime() <= buff.getLastTime()) {
                    b.setLastTime(buff.getLastTime());
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            list.add(buff);
        }
    }

    /**
     * 加算后得出护甲
     */
    public static int calculateDefByPlus(Character character) {
        int def = character.getDef();
        for (Buff buff : character.getPlusDefBuffs()) {
            def += buff.getValue();
        }
        if (def < 0) {
            def = 0;
        }
        return def;
    }

    /**
     * 加算后得出魔抗
     */
    public static int calculateMdfByPlus(Character character) {
        int mdf = character.getMdf();
        for (Buff buff : character.getPlusMdfBuffs()) {
            mdf += buff.getValue();
        }
        if (mdf < 0) {
            mdf = 0;
        }
        return mdf;
    }

    /**
     * 计算封印几率
     */
    public static boolean calculateForbidSuccess(Character character, Character passive, int probability) {
        for (Buff buff : character.getForbidSuccessPlusBuffs()) {
            probability += buff.getValue();
        }
        for (Buff buff : passive.getForbidResistPlusBuffs()) {
            probability -= buff.getValue();
        }
        return random(probability);
    }

    public static boolean calculateCriticalStrikeSuccess(Character character, Character passive) {
        int probability = 3; // 基础暴击率3点
        for (Buff buff : character.getCriticalStrikePlusBuffs()) {
            probability += buff.getValue();
        }
        for (Buff buff : passive.getCriticalStrikeResistPlusBuffs()) {
            probability -= buff.getValue();
        }
        return random(probability);
    }

    public static boolean isForbidden(Character character) {
        return character.getForbidBuffs().size() > 0;
    }

    public static void print(String str) {
        log += str + "<br/>";
        GameFrame.getInstance().update();
    }
}
