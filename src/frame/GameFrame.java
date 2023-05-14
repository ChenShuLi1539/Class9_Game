package frame;

import character.Character;
import character.Character_QinSiNing;
import character.Character_XuYue;
import game.CharacterData;
import game.Game;
import game.Team;
import utils.CharacterDataUtils;
import utils.Utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameFrame extends JFrame {

    private static GameFrame instance = null;

    public JScrollPane jScrollPane;

    public JScrollBar jScrollBar;

    public JPanel gamePanel;

    public JPanel dataPanel;

    public JPanel teamPanel;
    public JPanel logPanel;

    public JLabel log = new JLabel();

    public JPanel tableLayout_1;

    public JPanel tableLayout_2;

    public CharacterPanel[] gridView_1 = new CharacterPanel[9];

    public CharacterPanel[] gridView_2 = new CharacterPanel[9];

    public JTabbedPane jTabbedPane;

    public JComboBox<String> selectBox;

    public JLabel dataLabel;

    public JPanel teamTableLayout_1;

    public JPanel teamTableLayout_2;

    public JPanel[] teamGridView_1 = new JPanel[9];

    public List<JButton> teamGridView_1_add_button = new ArrayList<>();

    public List<JButton> teamGridView_1_remove_button = new ArrayList<>();

    public List<JLabel> teamGridView_1_label = new ArrayList<>();

    public JPanel[] teamGridView_2 = new JPanel[9];

    public JButton startButton;

    public JList<String> characterList;

    public DefaultListModel<String> listModel = new DefaultListModel<>();

    public int selectedCharacterNum = 0;

    public float  resolution = 1;

    public static GameFrame getInstance() {
        if (instance == null) {
            instance = new GameFrame();
        }
        return instance;
    }

    public GameFrame() throws HeadlessException {
        super("游戏界面");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\resolution.txt"));
            String currentLine = reader.readLine();
            resolution = Float.parseFloat(currentLine);
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((int) (1240 * resolution), (int) (980 * resolution));
        setVisible(true);
        setLayout(null);

        gamePanelInit();
        dataPanelInit();
        teamPanelInit();

        jTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.addTab("游戏", gamePanel);
        jTabbedPane.addTab("阵容", teamPanel);
        jTabbedPane.addTab("资料", dataPanel);
        jTabbedPane.setSelectedIndex(1);

        // TODO: 整个窗口加上滚动条，但是失败了
//        JScrollPane jScrollPane1 = new JScrollPane(jTabbedPane);
//        JScrollBar jScrollBar1 = jScrollPane1.getVerticalScrollBar();
//        jScrollBar1.setValue(jScrollBar1.getMaximum());

        setContentPane(jTabbedPane);
    }

    private void gamePanelInit() {
        gamePanel = new JPanel(null);
        logPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jScrollPane = new JScrollPane(logPanel);
        jScrollBar = jScrollPane.getVerticalScrollBar();
        jScrollBar.setValue(jScrollBar.getMaximum());

        tableLayout_1 = new JPanel(new GridLayout(3, 3));
        tableLayout_2 = new JPanel(new GridLayout(3, 3));
        for (int i=0;i<9;i++) {
            gridView_1[i] = new CharacterPanel();
            tableLayout_1.add(gridView_1[i]);
        }
        for (int i=0;i<9;i++) {
            gridView_2[i] = new CharacterPanel();
            tableLayout_2.add(gridView_2[i]);
        }
        tableLayout_1.setBounds((int) (40 * resolution), (int) (10 * resolution),
                (int) (450 * resolution), (int) (450 * resolution));
        tableLayout_2.setBounds((int) (40 * resolution), (int) (500 * resolution),
                (int) (450 * resolution), (int) (450 * resolution));
        jScrollPane.setBounds((int) (530 * resolution), (int) (10 * resolution),
                (int) (600 * resolution), (int) (900 * resolution));
        logPanel.add(log);

        gamePanel.add(tableLayout_1);
        gamePanel.add(tableLayout_2);
        gamePanel.add(jScrollPane);
    }

    private void dataPanelInit() {
        dataPanel = new JPanel(null);
        selectBox = new JComboBox<>();
        dataLabel = new JLabel();

        for (CharacterData characterData : CharacterDataUtils.list) {
            selectBox.addItem(characterData.getName());
        }
        dataLabel.setText("<html>" + CharacterDataUtils.list.get(0).getName() + "<br/>"
                + "HP:" + CharacterDataUtils.list.get(0).getHp() + "  MP:" + CharacterDataUtils.list.get(0).getMp()
                + "  普通攻击力:" + CharacterDataUtils.list.get(0).getNormalAttack()
                + "  速度:" + CharacterDataUtils.list.get(0).getSpd()
                + "  护甲:" + CharacterDataUtils.list.get(0).getDef() + "  魔抗:" + CharacterDataUtils.list.get(0).getMdf()
                + "  定位:" + CharacterDataUtils.list.get(0).getLocation()
                + "<br/><br/>" + CharacterDataUtils.list.get(0).getDesc()
                + "<html/>");
        dataLabel.setBounds((int) (400 * resolution), (int) (40 * resolution),
                (int) (500 * resolution), (int) (900 * resolution));

        selectBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    for (CharacterData characterData : CharacterDataUtils.list) {
                        if (characterData.getName().equals((String) e.getItem())) {
                            dataLabel.setText("<html>" + characterData.getName() + "<br/>"
                                    + "HP:" + characterData.getHp() + "  MP:" + characterData.getMp()
                                    + "  普通攻击力:" + characterData.getNormalAttack()
                                    + "  速度:" + characterData.getSpd()
                                    + "  护甲:" + characterData.getDef() + "  魔抗:" + characterData.getMdf()
                                    + "  定位:" + characterData.getLocation()
                                    + "<br/><br/>" + characterData.getDesc()
                                    + "<html/>");
                            return;
                        }
                    }
                }
            }
        });
        selectBox.setBounds((int) (40 * resolution), (int) (450 * resolution),
                (int) (100 * resolution), (int) (70 * resolution));

        dataPanel.add(selectBox, BorderLayout.NORTH);
        dataPanel.add(dataLabel, BorderLayout.CENTER);
    }

    private void teamPanelInit() {
        teamPanel = new JPanel(null);
        teamTableLayout_1 = new JPanel(new GridLayout(3, 3));
        teamTableLayout_2 = new JPanel(new GridLayout(3, 3));
        startButton = new JButton("开始游戏");
        characterList = new JList<>();


        for (int i=0;i<9;i++) {
            teamGridView_1[i] = new JPanel();
            teamGridView_2[i] = new JPanel();
            teamTableLayout_1.add(teamGridView_1[i]);
            teamTableLayout_2.add(teamGridView_2[i]);

            JButton jButton = new JButton("添加");
            teamGridView_1_add_button.add(jButton);
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = teamGridView_1_add_button.indexOf(jButton);
                    if (selectedCharacterNum > 5) {
                        return;
                    }
                    if (characterList.getSelectedValue() != null) {
                        teamGridView_1_label.get(index).setText(characterList.getSelectedValue());
                        teamGridView_1_label.get(index).setVisible(true);
                        teamGridView_1_remove_button.get(index).setVisible(true);
                        jButton.setVisible(false);
                        listModel.remove(characterList.getSelectedIndex());
                        selectedCharacterNum++;
                    }
                    if (selectedCharacterNum > 5) {
                        for (JButton button : teamGridView_1_add_button) {
                            button.setVisible(false);
                        }
                    }
                }
            });
            teamGridView_1[i].add(jButton);

            JLabel jLabel = new JLabel();
            jLabel.setVisible(false);
            teamGridView_1_label.add(jLabel);
            teamGridView_1[i].add(jLabel);

            JButton jButton1 = new JButton("移除");
            teamGridView_1_remove_button.add(jButton1);
            jButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = teamGridView_1_remove_button.indexOf(jButton1);
                    listModel.addElement(teamGridView_1_label.get(index).getText());
                    teamGridView_1_add_button.get(index).setVisible(true);
                    teamGridView_1_label.get(index).setVisible(false);
                    jButton1.setVisible(false);
                    selectedCharacterNum--;
                    if (selectedCharacterNum < 6) {
                        for (int i = 0; i < 9; i++) {
                            if (!teamGridView_1_add_button.get(i).isVisible()
                                    && !teamGridView_1_remove_button.get(i).isVisible()) {
                                teamGridView_1_add_button.get(i).setVisible(true);
                            }
                        }
                    }
                }
            });
            jButton1.setVisible(false);
            teamGridView_1[i].add(jButton1);
        }

        for (CharacterData characterData : CharacterDataUtils.list) {
            if (characterData.getName().equals("李可禛") || characterData.getName().equals("杨礼政") ||
                    characterData.getName().equals("许悦") || characterData.getName().equals("韦宗言") ||
                    characterData.getName().equals("陈思霖") || characterData.getName().equals("程显雯")) {
                continue;
            }
            listModel.addElement(characterData.getName());
        }
        characterList.setModel(listModel);
        ((DefaultListCellRenderer) characterList.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        teamGridView_2[7].add(new JLabel("李可禛"));
        teamGridView_2[1].add(new JLabel("杨礼政"));
        teamGridView_2[4].add(new JLabel("许悦"));
        teamGridView_2[2].add(new JLabel("韦宗言"));
        teamGridView_2[5].add(new JLabel("陈思霖"));
        teamGridView_2[3].add(new JLabel("程显雯"));

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            initialGame();
                            jTabbedPane.setSelectedIndex(0);
                            Game.getInstance().start();
                            startButton.setVisible(false);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }).start();
            }
        });

        teamTableLayout_1.setBounds((int) (40 * resolution), (int) (10 * resolution),
                (int) (450 * resolution), (int) (450 * resolution));
        teamTableLayout_2.setBounds((int) (40 * resolution), (int) (500 * resolution),
                (int) (450 * resolution), (int) (450 * resolution));
        characterList.setBounds((int) (530 * resolution), (int) (10 * resolution),
                (int) (100 * resolution), (int) (450 * resolution));
        startButton.setBounds((int) (530 * resolution), (int) (500 * resolution),
                (int) (200 * resolution), (int) (40 * resolution));

        teamPanel.add(teamTableLayout_1);
        teamPanel.add(teamTableLayout_2);
        teamPanel.add(characterList);
        teamPanel.add(startButton);
    }

    public void update() {
        Team teamOne = Game.getInstance().teamOne;
        Team teamTwo = Game.getInstance().teamTwo;
        for (int i = 0; i < 9; i++) {
            if (teamOne.getLocationList()[i] != null) {
                gridView_1[i].update(teamOne.getLocationList()[i]);
            }
            if (teamTwo.getLocationList()[8 - i] != null) {
                gridView_2[i].update(teamTwo.getLocationList()[8 - i]);
            }
        }
        log.setText("<html>" + Utils.log + "</html>");
        jScrollBar.setValue(jScrollBar.getMaximum());
    }

    public void initialGame() {
        for (JButton removeButton : teamGridView_1_remove_button) {
            if (removeButton.isVisible()) {
                int index = teamGridView_1_remove_button.indexOf(removeButton);
                Game.getInstance().setCharacter(teamGridView_1_label.get(index).getText(), true, index);
            }
        }
        Game.getInstance().setCharacter("李可禛", false, 1);
        Game.getInstance().setCharacter("杨礼政", false, 7);
        Game.getInstance().setCharacter("许悦", false, 4);
        Game.getInstance().setCharacter("韦宗言", false, 6);
        Game.getInstance().setCharacter("陈思霖", false, 3);
        Game.getInstance().setCharacter("程显雯", false, 5);
    }

    public static class CharacterPanel extends JPanel{

        JLabel name = new JLabel();

        JProgressBar hp = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);

        JProgressBar mp = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);

        JProgressBar spdRemain = new JProgressBar(JProgressBar.HORIZONTAL, 0, 1000);

        public CharacterPanel() {
            add(name);
            name.setVisible(false);
            add(hp);
            hp.setBorderPainted(true);
            hp.setStringPainted(true);
            hp.setForeground(new Color(245, 42, 42));
            hp.setVisible(false);
            add(mp);
            mp.setBorderPainted(true);
            mp.setStringPainted(true);
            mp.setForeground(new Color(14, 73, 219));
            mp.setVisible(false);
            add(spdRemain);
            spdRemain.setBorderPainted(true);
            spdRemain.setStringPainted(true);
            spdRemain.setForeground(new Color(160, 235, 243));
            spdRemain.setUI(new BasicProgressBarUI() {
                @Override
                protected Color getSelectionForeground() {
                    return Color.BLACK;
                }

                @Override
                protected Color getSelectionBackground() {
                    return Color.BLACK;
                }
            });
            spdRemain.setVisible(false);

        }

        public void update(Character character) {
            name.setVisible(true);
            hp.setVisible(true);
            mp.setVisible(true);
            spdRemain.setVisible(true);

            name.setText("<html>" + character.getName() + "</html>");
            hp.setValue(character.getCurrentHp() * 100 / character.getMaxHp());
            if (character instanceof Character_XuYue) {
                mp.setString("钱币:" + ((Character_XuYue) character).coin);
            } else if (character instanceof Character_QinSiNing) {
                mp.setString("意:" + ((Character_QinSiNing) character).mark);
            } else {
                mp.setValue(character.getCurrentMp() * 100 / character.getMaxMp());
            }
            spdRemain.setValue((int) character.getSpdRemain());

            setToolTipText("<html>" + character.getName() + "<br/>"
                    + "HP:" + character.getCurrentHp() + " / " + character.getMaxHp() + "<br/>"
                    + "MP:" + character.getCurrentMp() + " / " + character.getMaxMp() + "<br/>"
                    + "普通攻击力:" +
                        (int) (character.getNormalAtk() * Utils.calculateBuff(character.getNormalAtkBuffs())) + "<br/>"
                    + "速度:" + (int) Utils.calculateSpeed(character) + "<br/>"
                    + "护甲:" + Utils.calculateDefByPlus(character) + "<br/>"
                    + "魔抗:" + Utils.calculateMdfByPlus(character)
                    + "<br/><br/><html/>");
        }

    }
}
