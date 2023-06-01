import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;

public class OseroGui {
    CardLayout cardLayout;
    JPanel containerPanel;
    JFrame mainFrame;
    int board[][] = {
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,1,2,0,0,0},
        {0,0,0,2,1,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
    };
    JLabel announce;
    JLabel record;
    String partner = "";
    String turn = "";
    int win = 0;
    int lose = 0;
    int draw = 0; //*引き分けの場合もあるから追加
    int partner_win = 0;
    int partner_lose = 0;
    int partner_draw = 0; //*引き分けの場合もあるから追加
    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton access;
    JPanel card_panel;
    CardLayout layout;
    JTextField login_name;
    JPasswordField login_pass;
    JTextField signup_name;
    JPasswordField signup_pass;
    Client client;
    JPanel first_page;
    JPanel login_page;
    JPanel signup_page;
    JPanel standby_page;
    Reversi osero_page;
    Timer timer;
    int flag = 1;

    OseroGui(Client cl){
        client = cl;
        mainFrame = new JFrame("オセロアプリ");
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                finishGame();
            }
        });
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        containerPanel = new JPanel();
        cardLayout = new CardLayout();
        containerPanel.setLayout(cardLayout);
        first_page();
        login_page();
        signup_page();
        standby_page();
        osero_page = new Reversi();
        containerPanel.add(first_page, "first");
        containerPanel.add(login_page, "login");
        containerPanel.add(signup_page, "signup");
        containerPanel.add(standby_page, "standby");
        containerPanel.add(osero_page, "osero");
        mainFrame.getContentPane().add(containerPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
    }
    public void first_page(){
        first_page = new JPanel();
        first_page.setLayout(new GridBagLayout());
        first_page.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button1 = new JButton("SignUp");
        button1.setPreferredSize(new Dimension(150, 50));
        button1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                mainFrame.setSize(250, 130);
                cardLayout.show(containerPanel, "signup");
            }
        });

        button2 = new JButton("Login");
        button2.setPreferredSize(new Dimension(150, 50));
        button2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                mainFrame.setSize(250, 130);
                cardLayout.show(containerPanel, "login");
            }
        });
        first_page.add(button1);
        first_page.add(button2);
        mainFrame.setSize(200, 100);
    }
    public void login_page(){
        login_page = new JPanel();
        login_name = new JTextField(10);
        login_pass = new JPasswordField(10);
        JLabel name = new JLabel("ユーザー名：");
        JLabel pass = new JLabel("パスワード：");
        button3 = new JButton("Login");
        button3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String name = login_name.getText();
                char[] password = login_pass.getPassword();
                String pass = new String(password);
                client.out.println("login");
                client.out.println(name);
                client.out.println(pass);
                try{
                    String ans = client.in.readLine();
                    if(ans.equals("SUCCESS")){
                        win = Integer.parseInt(client.in.readLine());
                        lose = Integer.parseInt(client.in.readLine());
                        draw = Integer.parseInt(client.in.readLine());
                        record.setText("戦績："+ (win + lose + draw) +"戦"+ win +"勝" + lose +"負" + draw + "引");
                        mainFrame.setSize(170, 120);
                        cardLayout.show(containerPanel, "standby");
                    } else {
                        JFrame Error_frame = new JFrame();
                        JOptionPane.showMessageDialog(Error_frame, "登録されていない，もしくはパスワードが違う");
                    }
                }catch(IOException er){

                }
            }
        });
        login_page.add(name);
        login_page.add(login_name);
        login_page.add(pass);
        login_page.add(login_pass);
        login_page.add(button3);

    }
    public void signup_page(){
        signup_page = new JPanel();
        signup_name = new JTextField(10);
        signup_pass = new JPasswordField(10);
        JLabel name = new JLabel("ユーザー名：");
        JLabel pass = new JLabel("パスワード：");
        button4 = new JButton("SignUp");
        button4.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String name = signup_name.getText();
                char[] password = signup_pass.getPassword();
                String pass = new String(password);
                client.out.println("signup");
                client.out.println(name);
                client.out.println(pass);
                try{
                    String ans = client.in.readLine();
                    if(ans.equals("SUCCESS")){
                        win = Integer.parseInt(client.in.readLine());
                        lose = Integer.parseInt(client.in.readLine());
	                    draw = Integer.parseInt(client.in.readLine()); //*引き分けの場合もあるから追加
                        record.setText("戦績："+ (win + lose + draw) +"戦"+ win +"勝" + lose +"負" + draw + "引");
                        mainFrame.setSize(170,120);
                        cardLayout.show(containerPanel, "standby");
                    } else {
                        JFrame Error_frame = new JFrame();
                        JOptionPane.showMessageDialog(Error_frame, "登録済み");
                    }
                }catch(IOException er){

                }  
            }
        });
        signup_page.add(name);
        signup_page.add(signup_name);
        signup_page.add(pass);
        signup_page.add(signup_pass);
        signup_page.add(button4);
    }
    public void standby_page(){
        standby_page = new JPanel();
        record = new JLabel();
        announce = new JLabel("　　　　　　　");//とりまこれで動いたからそのまま
        access = new JButton("接続");
        access.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                announce.setText("対戦相手検索中");
                announce.paintImmediately( announce.getVisibleRect());
                GameStart();
            }
        });
        standby_page.add(record);
        standby_page.add(announce);
        standby_page.add(access);
    }
    public class Reversi extends JPanel {  
        MouseProc mouseProc; 
        // コンストラクタ（初期化処理）
        Reversi() {
            addMouseListener(new MouseProc());
        }
     
        // 画面描画
        int left_margin = 50;    // 左側余白
        int top_margin = 100;   // 上側余白
        int trout_size = 50;  //マスのサイズ
        public void paintComponent(Graphics g) {
            // 背景
            g.setColor(new Color(220,220,220));
            g.fillRect(0, 0, 500, 550);
            g.setColor(Color.BLACK);
            g.drawString("対戦相手：" + partner, 0, 20); //* 戦績："+ (win + lose) +"戦"+ win +"勝" + lose +"負"
            g.drawString("あなたの戦績："+ (win + lose + draw) +"戦"+ win +"勝" + lose +"負" + draw + "引", 0, 60);
            g.drawString("対戦相手の戦績："+ (partner_win + partner_lose + partner_draw) +"戦"+ partner_win +"勝" + partner_lose +"負" + partner_draw + "引", 0, 40);
            if(turn.equals("先攻")){
                g.drawString("あなたは黒石です", 0, 80);
            }else{
                g.drawString("あなたは白石です", 0, 80);
            }
            // 盤面描画
            for (int i = 0; i < 8; i++) {
                int y = top_margin + trout_size * i;
                for (int j = 0; j < 8; j++) {
                    int x = left_margin + trout_size * j;
                    g.setColor(new Color(0, 170, 0));
                    g.fillRect(x, y, trout_size, trout_size);
                    g.setColor(Color.black);
                    g.drawRect(x, y, trout_size, trout_size);
                    if (board[i][j] != 0) {
                        if (board[i][j] == 1) {
                            g.setColor(Color.black);
                            g.fillOval(x+trout_size/10, y+trout_size/10, trout_size*8/10, trout_size*8/10);
                        } else if(board[i][j] == 2){
                            g.setColor(Color.white);
                            g.fillOval(x+trout_size/10, y+trout_size/10, trout_size*8/10, trout_size*8/10);
                        }else{
                            g.setColor(Color.red);
                            g.fillOval(x+trout_size*3/10, y+trout_size*3/10, trout_size*2/5, trout_size*2/5);
                        }
                    }
                }
            }
            if(!(flag == 0)){
                Graphics2D g2d = (Graphics2D) g.create();
                float alpha = 0.5f; // ぼかしの透明度（0.0から1.0の範囲）
                int x = 0; // 左上のX座標
                int y = 0; // 左上のY座標
    
    
                // アルファコンポジットの設定
                AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                g2d.setComposite(alphaComposite);
    
                // ぼかしエフェクトの描画
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fill(new Rectangle2D.Double(x, y, 500, 550));
    
                // アルファコンポジットをリセット
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                String text = "";
                if(flag == 1) text = "相手のターン";
                if(flag == 2) text = "WIN";
                if(flag == 3) text = "LOSE";
                if(flag == 4) text = "DRAW";
                Font font = new Font("Arial", Font.BOLD, 40);
                g2d.setFont(font);
                FontRenderContext frc = g2d.getFontRenderContext();
                Rectangle2D textBounds = font.getStringBounds(text, frc);
                int textX = (int) (x + (500 - textBounds.getWidth()) / 2);
                int textY = (int) (y + (550 - textBounds.getHeight()) / 2);
                g2d.setColor(Color.BLACK);
                g2d.drawString(text, textX, textY);
                g2d.dispose();
            }
        }
     
        // クリックされた時の処理用のクラス
        class MouseProc extends MouseAdapter {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                // 盤の外側がクリックされたときは何もしないで終了
                if (x < left_margin) return;
                if (x >= left_margin+trout_size*8) return;
                if (y < top_margin) return;
                if (y >= top_margin+trout_size*8) return;
                // クリックされたマスを特定
                int row = (y - top_margin) / trout_size;
                int col = (x - left_margin) / trout_size;
                if (board[row][col] == 3) {
                    if(!(timer.isRunning())){
                        client.out.println(col);
                        client.out.println(row);
                        flag = 1;
                        reload();
                        mainFrame.setEnabled(false);
                        timer.start();
                    }
                }
            }
        }
    }
    public void reload(){
        for (int i = 0; i < board.length; i++) {
            for(int k = 0; k < board[0].length; k++){
                try {
                    board[i][k] = Integer.parseInt(client.in.readLine());
                } catch (IOException e) {
                    System.out.println("ERROR in reload");
                }
            }
        }
        osero_page.repaint();
    }
    public void GameStart(){
        client.out.println("gamestart"); 
        try{
            partner = client.in.readLine();
            partner_win = Integer.parseInt(client.in.readLine());
            partner_lose = Integer.parseInt(client.in.readLine());
	        partner_draw = Integer.parseInt(client.in.readLine()); //*引き分けの場合もあるから追加
            turn = client.in.readLine();
        }catch(IOException e){
            System.out.println("ERROR in gamestart");
        }
        mainFrame.setSize(500, 550);
        cardLayout.show(containerPanel, "osero");
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String command = client.in.readLine();
                    if(command.equals("START")){
                        flag = 0;
                        reload();
                        timer.stop();          
                        mainFrame.setEnabled(true);   
                    }else if(command.equals("FINISH")){
			            client.out.println("finishrecord"); //*
                        reload();
                        String judge = client.in.readLine();
                        if(judge.equals("WIN")){
                            flag = 2;
                            client.out.println("winrecord"); //*
                            win++; //*
                            partner_lose++; //*
			            }
                        if(judge.equals("LOSE")){
                            flag = 3;
                            client.out.println("loserecord"); //*
                            lose++; //*
                            partner_win++; //*
			            }
                        if(judge.equals("DRAW")){
                            flag = 4;
                            client.out.println("drawrecord"); //*
                            draw++; //*
                            partner_draw++; //*
			            }
                        osero_page.repaint();
                        int black_count = Integer.parseInt(client.in.readLine());
                        int white_count = Integer.parseInt(client.in.readLine());
                        Object[] options = {"もう1度ゲームをする", "アプリを終了する"};
                        JPanel judge_panel = new JPanel();
                        judge_panel.setLayout(new BoxLayout(judge_panel, BoxLayout.Y_AXIS));
                        JLabel label = new JLabel(judge);
                        label.setFont(label.getFont().deriveFont(Font.BOLD, 20f)); // フォントサイズを変更
                        label.setAlignmentX(Component.CENTER_ALIGNMENT);
                        JLabel count = new JLabel("黒 : "+ black_count +" 白 : " + white_count);
                        count.setAlignmentX(Component.CENTER_ALIGNMENT);
                        judge_panel.add(label);
                        judge_panel.add(count);
                        int choice = JOptionPane.showOptionDialog(mainFrame, judge_panel, "ゲーム終了",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                        if (choice == 0) {
                            reset();
                            return;
                        } else if (choice == 1) {
                            finishGame();
                            System.exit(0);
                            return;
                        }
                            

                    }
                } catch (IOException er) {
                    System.out.println("ERROR in gamestart");
                }
            }
        });
        timer.start();

    }
    public void reset(){
        mainFrame.setSize(170, 120);
        record.setText("戦績："+ (win + lose + draw) +"戦"+ win +"勝" + lose +"負" + draw + "引");
        announce.setText("　　　　　　　");
        cardLayout.show(containerPanel, "standby");
        timer.stop();
        mainFrame.setEnabled(true);
        flag = 1;
        board = new int[][]{
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,1,2,0,0,0},
            {0,0,0,2,1,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
        };

    }
    public void finishGame(){
        client.out.println("finish");
        try {
            client.socket.close();        
        } catch (IOException e) {
            System.out.println("ERROR in finishGame");
        }
    }
}
