package game;

import character.*;
import character.Character;
import frame.GameFrame;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game instance = null;



    private static boolean isOver = false;

    public Team teamOne = new Team("队伍1_");

    public Team teamTwo = new Team("队伍2_");


    public Game() {
//        teamOne.add(new Character_LiangBeiYuan(teamOne, 3), 3);
//        teamOne.add(new Character_PangZhuoFan(teamOne, 5), 5);
//        teamOne.add(new Character_QinSiNing(teamOne, 8), 8);
//        teamOne.add(new Character_LiKeZhen(teamOne, 1), 1);
//        teamOne.add(new Character_HuBoNing(teamOne, 4), 4);
//
//        teamTwo.add(new Character_WeiZongYan(teamTwo, 6), 6);
//        teamTwo.add(new Character_MoKaiEn(teamTwo, 3), 3);
//        teamTwo.add(new Character_XuYue(teamTwo, 4), 4);
//        teamTwo.add(new Character_YangLiZheng(teamTwo, 7), 7);
//        teamTwo.add(new Character_ChengXianWen(teamTwo, 1), 1);
    }

    public void setCharacter(String name, boolean isTeamOne, int location) {
        Team team = isTeamOne ? teamOne : teamTwo;
        switch (name) {
            case "周锦浩":
                team.add(new Character_ZhouJinHao(team, location), location);
                break;
            case "李可禛":
                team.add(new Character_LiKeZhen(team, location), location);
                break;
            case "吴童":
                team.add(new Character_WuTong(team, location), location);
                break;
            case "李明坤":
                team.add(new Character_LiMingKun(team, location), location);
                break;
            case "许悦":
                team.add(new Character_XuYue(team, location), location);
                break;
            case "何峥磊":
                team.add(new Character_HeZhengLei(team, location), location);
                break;
            case "庞卓凡":
                team.add(new Character_PangZhuoFan(team, location), location);
                break;
            case "杨礼政":
                team.add(new Character_YangLiZheng(team, location), location);
                break;
            case "梁倍源":
                team.add(new Character_LiangBeiYuan(team, location), location);
                break;
            case "胡伯宁":
                team.add(new Character_HuBoNing(team, location), location);
                break;
            case "莫凯恩":
                team.add(new Character_MoKaiEn(team, location), location);
                break;
            case "程显雯":
                team.add(new Character_ChengXianWen(team, location), location);
                break;
            case "韦承洋":
                team.add(new Character_WeiChengYang(team, location), location);
                break;
            case "覃思宁":
                team.add(new Character_QinSiNing(team, location), location);
                break;
            case "韦宗言":
                team.add(new Character_WeiZongYan(team, location), location);
                break;
            case "陈思霖":
                team.add(new Character_ChenSiLin(team, location), location);
                break;
            default:
                break;
        }
    }

    public void start() throws InterruptedException {
        Utils.print("游戏开始！！！！！");
        for (Character character : Utils.getAllStillAlive()) {
            character.prepareSkill();
        }
        GameFrame.getInstance().update();
        while (!isOver) {
            roundGoing();
            Thread.sleep(500);
        }
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void roundGoing() {
        Character character = teamOne.getCharacterList().get(0);
        double shortestTime = 2000;
        for (Character c : Utils.getAllStillAlive()) {
            if (c.getCurrentHp() <= 0) {
                continue;
            }
            double randomSpd = (Math.random() * 11 + 95) / 100;
            double currentSpd = c.getSpd() * Utils.calculateBuff(c.getSpdBuffs()) * randomSpd;
            double time = (1000 - c.getSpdRemain()) / currentSpd;
            if (time < shortestTime) {
                shortestTime = time;
                character = c;
            }
        }
        for (Character c : Utils.getAllStillAlive()) {
            if (!c.equals(character)) {
                c.setSpdRemain(c.getSpdRemain() + c.getSpd() * shortestTime);
            }
        }
        character.motion();
        isOver = isOver();
    }

    private boolean isOver() {
        boolean overFlag = true;
        for (Character c : teamOne.getCharacterList()) {
            if (c.getCurrentHp() > 0) {
                overFlag = false;
                break;
            }
        }
        if (overFlag) {
            Utils.print("游戏结束！！！ 队伍2获胜！！！");
            showDamage();
            return overFlag;
        }
        overFlag = true;
        for (Character c : teamTwo.getCharacterList()) {
            if (c.getCurrentHp() > 0) {
                overFlag = false;
                break;
            }
        }
        if (overFlag) {
            Utils.print("游戏结束！！！ 队伍1获胜！！！");
            showDamage();
        }
        return overFlag;
    }

    private void showDamage() {
        Utils.print("");
        List<Character> list = new ArrayList<>();
        list.addAll(teamOne.getCharacterList());
        list.addAll(teamTwo.getCharacterList());
        for (Character character : list) {
            Utils.print(character.getName() + "共计造成了" + character.getTotalDamage() + "点伤害 "
                    + (character.getCurrentHp() > 0 ? "（存活）" : ""));
        }
    }
}
