/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package my.vendingMachine;

import javax.swing.JOptionPane;

/**
 *
 * @author brady
 */
public class VendingMachine extends javax.swing.JFrame {

    
    // Initial coin counts in machine (2€, 1€, 50c, 20c, 10c)
    public static int[] coinCounts = {3, 5, 4, 4, 3}; 
    // Coin values in cents
    private int[] coinValues = {200, 100, 50, 20, 10}; 
    private int machineTotal = 0;    // Total money in machine
    private int totalInserted = 0;   // Current amount inserted by customer

    // Product stock and pricing info
    public static int[] drinkStock = {5, 5, 5, 5};  // Current stock levels
    private int[] drinkPrices = {200, 250, 220, 190}; // Prices in cents
    private String[] drinkNames = {"Fanta", "Monster", "Lucozade", "Pepsi"}; // Available drinks

    // Methods for handling coin insertions
    private void plus10c() {
        updateInsertedAmount(4);
    }

    private void plus20c() {
        updateInsertedAmount(3);
    }

    private void plus50c() {
        updateInsertedAmount(2);
    }

    private void plus1euro() {
        updateInsertedAmount(1);
    }

    private void plus2euro() {
        updateInsertedAmount(0);
    }

    // Updates the machine's state when money is inserted
    private void updateInsertedAmount(int coinIndex) {
        totalInserted = totalInserted + coinValues[coinIndex];
        coinCounts[coinIndex] = coinCounts[coinIndex] + 1;
        displayInsertedAmount();
        updateTotal();
        updateFloatDisplay();
    }

    // Updates display to show current amount inserted
    private void displayInsertedAmount() {
        double amount = totalInserted / 100.0;
        jTextFieldInsertedAmount.setText("€" + amount);
    }

    // Updates display showing available coins in machine
    private void updateFloatDisplay() {
        jTextField2EuroFloat.setText("" + coinCounts[0]);
        jTextField1EuroFloat.setText("" + coinCounts[1]);
        jTextField50cFloat.setText("" + coinCounts[2]);
        jTextField20cFloat.setText("" + coinCounts[3]);
        jTextField10cFloat.setText("" + coinCounts[4]);
    }

    // Main method for processing drink purchases
    private void buyDrink(int drinkIndex) {
        // Check stock availability
        if (drinkStock[drinkIndex] <= 0) {
            JOptionPane.showMessageDialog(null, drinkNames[drinkIndex] + " is out of stock.");
            return;
        }

        // Verify sufficient payment
        if (totalInserted < drinkPrices[drinkIndex]) {
            int remaining = drinkPrices[drinkIndex] - totalInserted;
            double remainingEuros = remaining / 100.0;
            JOptionPane.showMessageDialog(null, "you need €" + remainingEuros + " more to buy " + drinkNames[drinkIndex]);
            return;
        }

        // Calculate change required
        int change = totalInserted - drinkPrices[drinkIndex];

        // Verify change can be made
        if (change > 0) {
            if (!checkChange(change)) {
                JOptionPane.showMessageDialog(null, "Machine cannot make change. Transaction cancelled.");
                giveChange(totalInserted);
                resetInsertedAmount();
                return;
            }
        }

        // Complete transaction
        dispenseDrink(drinkIndex);
        if (change > 0) {
            giveChange(change);
        }
        resetInsertedAmount();
        updateStockDisplay();
    }

    // Verifies if correct change can be made
    private boolean checkChange(int amount) {
        int remainingAmount = amount;
        int[] tempCoinCounts = new int[coinCounts.length];

        // Create temporary copy of coin counts
        for (int i = 0; i < coinCounts.length; i++) {
            tempCoinCounts[i] = coinCounts[i];
        }

        // Attempt to make change using available coins
        int i = 0;
        while (remainingAmount > 0 && i < coinValues.length) {
            while (remainingAmount >= coinValues[i] && tempCoinCounts[i] > 0) {
                remainingAmount = remainingAmount - coinValues[i];
                tempCoinCounts[i] = tempCoinCounts[i] - 1;
            }
            i = i + 1;
        }

        if (remainingAmount == 0) {
            return true;
        } else {
            return false;
        }
    }

    // Dispenses selected drink and displays image
    private void dispenseDrink(int drinkIndex) {
        drinkStock[drinkIndex] = drinkStock[drinkIndex] - 1;
        JOptionPane.showMessageDialog(null, "Dispensing " + drinkNames[drinkIndex]);

        // Display the correct drink image
        if (drinkIndex == 0) {
            jLabelFantaImage.setVisible(true);
        } else if (drinkIndex == 1) {
            jLabelMonsterImage.setVisible(true);
        } else if (drinkIndex == 2) {
            jLabelLucozadeImage.setVisible(true);
        } else if (drinkIndex == 3) {
            jLabelPepsiImage.setVisible(true);
        }

        // Hide image after 7 seconds
        new Thread(() -> {
            try {
                Thread.sleep(7000);
                jLabelFantaImage.setVisible(false);
                jLabelMonsterImage.setVisible(false);
                jLabelLucozadeImage.setVisible(false);
                jLabelPepsiImage.setVisible(false);
            } catch (InterruptedException e) {
            }
        }).start();
    }

    // Resets inserted amount to zero
    private void resetInsertedAmount() {
        totalInserted = 0;
        displayInsertedAmount();
    }

    // Processes and dispenses change
    private void giveChange(int change) {
        if (change == 0) {
            JOptionPane.showMessageDialog(null, "No change to dispense");
            return;
        }

        JOptionPane.showMessageDialog(null, "Change due: €" + (change / 100.0));
        int remainingChange = change;

        // Process coins from largest to smallest
        int i = 0;
        while (i < coinValues.length && remainingChange > 0) {
            while (remainingChange >= coinValues[i] && coinCounts[i] > 0) {
                // Dispense individual coins
                JOptionPane.showMessageDialog(null, "Dispensing €" + (coinValues[i] / 100.0));
                remainingChange = remainingChange - coinValues[i];
                coinCounts[i] = coinCounts[i] - 1;
            }
            i = i + 1;
        }

        updateFloatDisplay();
    }

    // Updates total money in machine
    private void updateTotal() {
        machineTotal = 0;
        int i = 0;
        while (i < coinCounts.length) {
            machineTotal = machineTotal + (coinCounts[i] * coinValues[i]);
            i = i + 1;
        }
    }

    // Updates display of remaining drink stock
    private void updateStockDisplay() {
        jTextFieldFantaStock.setText("" + drinkStock[0]);
        jTextFieldMonsterStock.setText("" + drinkStock[1]);
        jTextFieldLucozadeStock.setText("" + drinkStock[2]);
        jTextFieldPepsiStock.setText("" + drinkStock[3]);
    }

    // Handles purchase cancel
    private void buyCancel() {
        if (totalInserted > 0) {
            JOptionPane.showMessageDialog(null, "Transaction cancelled");
            giveChange(totalInserted);
            resetInsertedAmount();
        }
    }

    /**
     * 
     */
    public VendingMachine() {
        initComponents();
        updateFloatDisplay();
        updateStockDisplay();
        updateTotal();
        jLabelFantaImage.setVisible(false);
        jLabelMonsterImage.setVisible(false);
        jLabelLucozadeImage.setVisible(false);
        jLabelPepsiImage.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanelGreen = new javax.swing.JPanel();
        jLabelVendingMachine = new javax.swing.JLabel();
        jPanelFloat = new javax.swing.JPanel();
        jLabel2Euro = new javax.swing.JLabel();
        jLabel1Euro = new javax.swing.JLabel();
        jLabel50c = new javax.swing.JLabel();
        jLabel20c = new javax.swing.JLabel();
        jLabel10c = new javax.swing.JLabel();
        jTextField50cFloat = new javax.swing.JTextField();
        jTextField2EuroFloat = new javax.swing.JTextField();
        jTextField1EuroFloat = new javax.swing.JTextField();
        jTextField10cFloat = new javax.swing.JTextField();
        jTextField20cFloat = new javax.swing.JTextField();
        jLabelChange = new javax.swing.JLabel();
        jTextFieldChange = new javax.swing.JTextField();
        jPanelBuy = new javax.swing.JPanel();
        jTextFieldMonsterStock = new javax.swing.JTextField();
        jTextFieldLucozadeStock = new javax.swing.JTextField();
        jTextFieldFantaStock = new javax.swing.JTextField();
        jTextFieldPepsiStock = new javax.swing.JTextField();
        jButtonFanta = new javax.swing.JButton();
        jButtonMonster = new javax.swing.JButton();
        jButtonLucozade = new javax.swing.JButton();
        jButtonPepsi = new javax.swing.JButton();
        jLabelFantaCost = new javax.swing.JLabel();
        jLabelMonsterCost = new javax.swing.JLabel();
        jLabelLucozadeCost = new javax.swing.JLabel();
        jLabelPepsiCost = new javax.swing.JLabel();
        jPanelDispense = new javax.swing.JPanel();
        jLabelMonsterImage = new javax.swing.JLabel();
        jLabelLucozadeImage = new javax.swing.JLabel();
        jLabelPepsiImage = new javax.swing.JLabel();
        jLabelFantaImage = new javax.swing.JLabel();
        jPanelCoins = new javax.swing.JPanel();
        jButton1Euro = new javax.swing.JButton();
        jButton50c = new javax.swing.JButton();
        jButton20c = new javax.swing.JButton();
        jButton10c = new javax.swing.JButton();
        jButton2Euro = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonStock = new javax.swing.JButton();
        jButtonFloat = new javax.swing.JButton();
        jLabelInsertedAmount = new javax.swing.JLabel();
        jTextFieldInsertedAmount = new javax.swing.JTextField();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Vending Machine");

        jPanelGreen.setBackground(new java.awt.Color(224, 249, 224));

        jLabelVendingMachine.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabelVendingMachine.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelVendingMachine.setText("Vending Machine");

        jPanelFloat.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2Euro.setText("€2");

        jLabel1Euro.setText("€1");

        jLabel50c.setText("50c");

        jLabel20c.setText("20c");

        jLabel10c.setText("10c");

        jTextField50cFloat.setEditable(false);

        jTextField2EuroFloat.setEditable(false);
        jTextField2EuroFloat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2EuroFloatActionPerformed(evt);
            }
        });

        jTextField1EuroFloat.setEditable(false);

        jTextField10cFloat.setEditable(false);

        jTextField20cFloat.setEditable(false);

        jLabelChange.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelChange.setText("Change:");

        jTextFieldChange.setEditable(false);
        jTextFieldChange.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldChange.setText("€0.00");

        javax.swing.GroupLayout jPanelFloatLayout = new javax.swing.GroupLayout(jPanelFloat);
        jPanelFloat.setLayout(jPanelFloatLayout);
        jPanelFloatLayout.setHorizontalGroup(
            jPanelFloatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFloatLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabelChange)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelFloatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFloatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFloatLayout.createSequentialGroup()
                        .addComponent(jLabel10c)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField10cFloat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFloatLayout.createSequentialGroup()
                        .addComponent(jLabel20c)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField20cFloat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelFloatLayout.createSequentialGroup()
                        .addComponent(jLabel2Euro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField2EuroFloat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelFloatLayout.createSequentialGroup()
                        .addComponent(jLabel1Euro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1EuroFloat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelFloatLayout.createSequentialGroup()
                        .addComponent(jLabel50c)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField50cFloat, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFloatLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTextFieldChange, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelFloatLayout.setVerticalGroup(
            jPanelFloatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFloatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFloatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2Euro)
                    .addComponent(jTextField2EuroFloat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFloatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1Euro)
                    .addComponent(jTextField1EuroFloat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFloatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50c)
                    .addComponent(jTextField50cFloat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFloatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20c)
                    .addComponent(jTextField20cFloat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFloatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10c)
                    .addComponent(jTextField10cFloat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelChange)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelBuy.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldMonsterStock.setEditable(false);

        jTextFieldLucozadeStock.setEditable(false);

        jTextFieldFantaStock.setEditable(false);

        jTextFieldPepsiStock.setEditable(false);
        jTextFieldPepsiStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPepsiStockActionPerformed(evt);
            }
        });

        jButtonFanta.setText("Fanta");
        jButtonFanta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFantaActionPerformed(evt);
            }
        });

        jButtonMonster.setText("Monster");
        jButtonMonster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMonsterActionPerformed(evt);
            }
        });

        jButtonLucozade.setText("Lucozade");
        jButtonLucozade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLucozadeActionPerformed(evt);
            }
        });

        jButtonPepsi.setText("Pepsi");
        jButtonPepsi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPepsiActionPerformed(evt);
            }
        });

        jLabelFantaCost.setText("€2.00");

        jLabelMonsterCost.setText("€2.50");

        jLabelLucozadeCost.setText("€2.20");

        jLabelPepsiCost.setText("€1.90");

        javax.swing.GroupLayout jPanelBuyLayout = new javax.swing.GroupLayout(jPanelBuy);
        jPanelBuy.setLayout(jPanelBuyLayout);
        jPanelBuyLayout.setHorizontalGroup(
            jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBuyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonFanta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldFantaStock))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelBuyLayout.createSequentialGroup()
                        .addComponent(jButtonMonster)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonLucozade, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPepsi))
                    .addGroup(jPanelBuyLayout.createSequentialGroup()
                        .addComponent(jTextFieldMonsterStock, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldLucozadeStock, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldPepsiStock, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(7, Short.MAX_VALUE))
            .addGroup(jPanelBuyLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabelFantaCost)
                .addGap(54, 54, 54)
                .addComponent(jLabelMonsterCost)
                .addGap(55, 55, 55)
                .addComponent(jLabelLucozadeCost)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelPepsiCost)
                .addGap(28, 28, 28))
        );
        jPanelBuyLayout.setVerticalGroup(
            jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBuyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonFanta)
                    .addComponent(jButtonMonster)
                    .addComponent(jButtonLucozade)
                    .addComponent(jButtonPepsi))
                .addGap(14, 14, 14)
                .addGroup(jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldMonsterStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldFantaStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldLucozadeStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldPepsiStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBuyLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabelFantaCost))
                    .addGroup(jPanelBuyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelLucozadeCost)
                            .addComponent(jLabelMonsterCost)
                            .addComponent(jLabelPepsiCost))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanelDispense.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelDispense.setMaximumSize(new java.awt.Dimension(332, 110));
        jPanelDispense.setMinimumSize(new java.awt.Dimension(332, 110));
        jPanelDispense.setName(""); // NOI18N
        jPanelDispense.setPreferredSize(new java.awt.Dimension(332, 110));

        jLabelMonsterImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/vendingMachine/monster.png"))); // NOI18N

        jLabelLucozadeImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/vendingMachine/lucozade.png"))); // NOI18N

        jLabelPepsiImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/vendingMachine/pepsi.png"))); // NOI18N

        jLabelFantaImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/vendingMachine/fanta.png"))); // NOI18N

        javax.swing.GroupLayout jPanelDispenseLayout = new javax.swing.GroupLayout(jPanelDispense);
        jPanelDispense.setLayout(jPanelDispenseLayout);
        jPanelDispenseLayout.setHorizontalGroup(
            jPanelDispenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDispenseLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabelFantaImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelPepsiImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelLucozadeImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelMonsterImage)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelDispenseLayout.setVerticalGroup(
            jPanelDispenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDispenseLayout.createSequentialGroup()
                .addGroup(jPanelDispenseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelMonsterImage)
                    .addComponent(jLabelLucozadeImage)
                    .addComponent(jLabelPepsiImage)
                    .addComponent(jLabelFantaImage))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelCoins.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton1Euro.setText("€1");
        jButton1Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1EuroActionPerformed(evt);
            }
        });

        jButton50c.setText("50c");
        jButton50c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50cActionPerformed(evt);
            }
        });

        jButton20c.setText("20c");
        jButton20c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20cActionPerformed(evt);
            }
        });

        jButton10c.setText("10c");
        jButton10c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10cActionPerformed(evt);
            }
        });

        jButton2Euro.setText("€2");
        jButton2Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2EuroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCoinsLayout = new javax.swing.GroupLayout(jPanelCoins);
        jPanelCoins.setLayout(jPanelCoinsLayout);
        jPanelCoinsLayout.setHorizontalGroup(
            jPanelCoinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCoinsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCoinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10c, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton20c, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton50c, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton1Euro, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton2Euro, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelCoinsLayout.setVerticalGroup(
            jPanelCoinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCoinsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2Euro, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1Euro, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton50c, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton20c, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton10c, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonStock.setText("Stock");
        jButtonStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStockActionPerformed(evt);
            }
        });

        jButtonFloat.setText("Float");
        jButtonFloat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFloatActionPerformed(evt);
            }
        });

        jLabelInsertedAmount.setText("Inserted Amount:");

        jTextFieldInsertedAmount.setEditable(false);
        jTextFieldInsertedAmount.setText("€0.00");

        javax.swing.GroupLayout jPanelGreenLayout = new javax.swing.GroupLayout(jPanelGreen);
        jPanelGreen.setLayout(jPanelGreenLayout);
        jPanelGreenLayout.setHorizontalGroup(
            jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGreenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGreenLayout.createSequentialGroup()
                        .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addComponent(jLabelVendingMachine, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(jButtonClose, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelGreenLayout.createSequentialGroup()
                        .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanelFloat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonStock, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelGreenLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanelBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelGreenLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanelDispense, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanelGreenLayout.createSequentialGroup()
                                        .addComponent(jLabelInsertedAmount)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldInsertedAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelCoins, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonFloat, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanelGreenLayout.setVerticalGroup(
            jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGreenLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonClose, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelVendingMachine, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGreenLayout.createSequentialGroup()
                        .addComponent(jPanelCoins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonFloat, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanelGreenLayout.createSequentialGroup()
                            .addComponent(jPanelBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanelGreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelInsertedAmount)
                                .addComponent(jTextFieldInsertedAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jPanelDispense, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelGreenLayout.createSequentialGroup()
                            .addComponent(jPanelFloat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonStock, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelGreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelGreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2EuroFloatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2EuroFloatActionPerformed

    }//GEN-LAST:event_jTextField2EuroFloatActionPerformed

    private void jTextFieldPepsiStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPepsiStockActionPerformed

    }//GEN-LAST:event_jTextFieldPepsiStockActionPerformed

    private void jButtonFantaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFantaActionPerformed
        buyDrink(0);
    }//GEN-LAST:event_jButtonFantaActionPerformed

    private void jButtonMonsterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMonsterActionPerformed
        buyDrink(1);
    }//GEN-LAST:event_jButtonMonsterActionPerformed

    private void jButtonLucozadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLucozadeActionPerformed
        buyDrink(2);
    }//GEN-LAST:event_jButtonLucozadeActionPerformed

    private void jButtonPepsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPepsiActionPerformed
        buyDrink(3);
    }//GEN-LAST:event_jButtonPepsiActionPerformed

    private void jButton1EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1EuroActionPerformed
        plus1euro();
    }//GEN-LAST:event_jButton1EuroActionPerformed

    private void jButton50cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50cActionPerformed
        plus50c();
    }//GEN-LAST:event_jButton50cActionPerformed

    private void jButton20cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20cActionPerformed
        plus20c();
    }//GEN-LAST:event_jButton20cActionPerformed

    private void jButton10cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10cActionPerformed
        plus10c();
    }//GEN-LAST:event_jButton10cActionPerformed

    private void jButton2EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2EuroActionPerformed
        plus2euro();
    }//GEN-LAST:event_jButton2EuroActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
//while (insertedAmount > 0) {
//        if (insertedAmount >= 200 && coinAmount[0] > 0) {
//            coinAmount[0]--;
//            insertedAmount -= 200;
//        } else if (insertedAmount >= 100 && coinAmount[1] > 0) {
//            coinAmount[1]--;
//            insertedAmount -= 100;
//        } else if (insertedAmount >= 50 && coinAmount[2] > 0) {
//            coinAmount[2]--;
//            insertedAmount -= 50;
//        } else if (insertedAmount >= 20 && coinAmount[3] > 0) {
//            coinAmount[3]--;
//            insertedAmount -= 20;
//        } else if (insertedAmount >= 10 && coinAmount[4] > 0) {
//            coinAmount[4]--;
//            insertedAmount -= 10;
//        } else {
//            break; // If no coins can be returned
//        }
//    }
//    
        buyCancel();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStockActionPerformed
        StockFrame stockFrame = new StockFrame();
        stockFrame.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButtonStockActionPerformed

    private void jButtonFloatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFloatActionPerformed
        FloatFrame floatFrame = new FloatFrame();
        floatFrame.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButtonFloatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VendingMachine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VendingMachine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VendingMachine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VendingMachine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VendingMachine().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10c;
    private javax.swing.JButton jButton1Euro;
    private javax.swing.JButton jButton20c;
    private javax.swing.JButton jButton2Euro;
    private javax.swing.JButton jButton50c;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonFanta;
    private javax.swing.JButton jButtonFloat;
    private javax.swing.JButton jButtonLucozade;
    private javax.swing.JButton jButtonMonster;
    private javax.swing.JButton jButtonPepsi;
    private javax.swing.JButton jButtonStock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10c;
    private javax.swing.JLabel jLabel1Euro;
    private javax.swing.JLabel jLabel20c;
    private javax.swing.JLabel jLabel2Euro;
    private javax.swing.JLabel jLabel50c;
    private javax.swing.JLabel jLabelChange;
    private javax.swing.JLabel jLabelFantaCost;
    private javax.swing.JLabel jLabelFantaImage;
    private javax.swing.JLabel jLabelInsertedAmount;
    private javax.swing.JLabel jLabelLucozadeCost;
    private javax.swing.JLabel jLabelLucozadeImage;
    private javax.swing.JLabel jLabelMonsterCost;
    private javax.swing.JLabel jLabelMonsterImage;
    private javax.swing.JLabel jLabelPepsiCost;
    private javax.swing.JLabel jLabelPepsiImage;
    private javax.swing.JLabel jLabelVendingMachine;
    private javax.swing.JPanel jPanelBuy;
    private javax.swing.JPanel jPanelCoins;
    private javax.swing.JPanel jPanelDispense;
    private javax.swing.JPanel jPanelFloat;
    private javax.swing.JPanel jPanelGreen;
    private javax.swing.JTextField jTextField10cFloat;
    private javax.swing.JTextField jTextField1EuroFloat;
    private javax.swing.JTextField jTextField20cFloat;
    private javax.swing.JTextField jTextField2EuroFloat;
    private javax.swing.JTextField jTextField50cFloat;
    private javax.swing.JTextField jTextFieldChange;
    private javax.swing.JTextField jTextFieldFantaStock;
    private javax.swing.JTextField jTextFieldInsertedAmount;
    private javax.swing.JTextField jTextFieldLucozadeStock;
    private javax.swing.JTextField jTextFieldMonsterStock;
    private javax.swing.JTextField jTextFieldPepsiStock;
    // End of variables declaration//GEN-END:variables
}
