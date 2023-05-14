import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
public class OseroGui {
    static int width = 500;
    static int height = 550;
    int lm = 50;    // 左側余白
    int tm = 100;   // 上側余白
    int cs = 50;  //マスのサイズ
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
    String partner = "";
    String turn = "";
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
    JPanel osero_page;
    Timer timer;

    OseroGui(Client cl){
        client = cl;
        mainFrame = new JFrame("オセロアプリ");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(width, height);
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
        // mainFrame.pack();
        mainFrame.setVisible(true);
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
                cardLayout.show(containerPanel, "signup");
            }
        });

        button2 = new JButton("Login");
        button2.setPreferredSize(new Dimension(150, 50));
        button2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                cardLayout.show(containerPanel, "login");
            }
        });
        first_page.add(button1);
        first_page.add(button2);
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
                        cardLayout.show(containerPanel, "standby");
                    } else {
                        JFrame Error_frame = new JFrame();
                        JOptionPane.showMessageDialog(Error_frame, "登録済み");
                    }
                }catch(IOException er){

                }  
            }
        });
        signup_page.add(signup_name);
        signup_page.add(signup_pass);
        signup_page.add(button4);
    }
    public void standby_page(){
        standby_page = new JPanel();
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
        standby_page.add(announce);
        standby_page.add(access);
    }
    public class Reversi extends JPanel {    
        // コンストラクタ（初期化処理）
        public Reversi() {
            setPreferredSize(new Dimension(WIDTH,HEIGHT));
            addMouseListener(new MouseProc());
        }
     
        // 画面描画
        public void paintComponent(Graphics g) {
            // 背景
            g.setColor(Color.gray);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.BLACK);
            g.drawString("対戦相手：" + partner, 0, 50);
            g.drawString("あなたは" + turn + "です", 0, 70);
            // 盤面描画
            for (int i = 0; i < 8; i++) {
                int y = tm + cs * i;
                for (int j = 0; j < 8; j++) {
                    int x = lm + cs * j;
                    g.setColor(new Color(0, 170, 0));
                    g.fillRect(x, y, cs, cs);
                    g.setColor(Color.black);
                    g.drawRect(x, y, cs, cs);
                    if (board[i][j] != 0) {
                        if (board[i][j] == 1) {
                            g.setColor(Color.black);
                        } else {
                            g.setColor(Color.white);
                        }
                        g.fillOval(x+cs/10, y+cs/10, cs*8/10, cs*8/10);
                    }
                }
            }
        }
     
        // クリックされた時の処理用のクラス
        class MouseProc extends MouseAdapter {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                // 盤の外側がクリックされたときは何もしないで終了
                if (x < lm) return;
                if (x >= lm+cs*8) return;
                if (y < tm) return;
                if (y >= tm+cs*8) return;
                // クリックされたマスを特定
                int row = (y - tm) / cs;
                int col = (x - lm) / cs;
                if (board[row][col] == 0) {
                    if(!(timer.isRunning())){
                        client.out.println(col);
                        client.out.println(row);
                        reload();
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
                    // TODO Auto-generated catch block
                }
            }
        }
        osero_page.repaint();
    }
    public void GameStart(){
        client.out.println("gamestart"); 
        try{
            partner = client.in.readLine();
            turn = client.in.readLine();
        }catch(IOException e){
            
        }
        cardLayout.show(containerPanel, "osero");
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    while(!((client.in.readLine()).equals("STOP"))){
                        return;
                    }
                    reload();
                    timer.stop();
                } catch (IOException er) {
                    // TODO: handle exception
                }
            }
        });
        timer.start();

    }
}