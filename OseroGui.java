import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class OseroGui extends JFrame implements ActionListener{
    JLabel announce;
    JLabel partner;
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

    OseroGui(Client cl){
        OseroGui gui = new OseroGui("オセロ", cl);
        gui.setVisible(true); //可視化
    }

    OseroGui(String title, Client cl){
        client = cl;
        setTitle(title);
        setSize(500, 300); //大きさ指定
        setLocationRelativeTo(null); //GUIを中央に配置
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel first_page = new JPanel();
        JPanel login_page = new JPanel();
        JPanel signup_page = new JPanel();
        JPanel standby_page = new JPanel();
        JPanel osero_page = new JPanel();

        //firstpage
        button1 = new JButton("SignUp");
        button1.setPreferredSize(new Dimension(120, 40));
        button1.addActionListener(this);
        button2 = new JButton("Login");
        button2.setPreferredSize(new Dimension(120, 40));
        button2.addActionListener(this);
        first_page.add(button1);
        first_page.add(button2);
            
        //loginpage
        login_name = new JTextField(10);
        login_pass = new JPasswordField(10);
        button3 = new JButton("Login");
        button3.addActionListener(this);
        login_page.add(login_name);
        login_page.add(login_pass);
        login_page.add(button3);

        //signuppage
        signup_name = new JTextField(10);
        signup_pass = new JPasswordField(10);
        button4 = new JButton("SignUp");
        button4.addActionListener(this);
        signup_page.add(signup_name);
        signup_page.add(signup_pass);
        signup_page.add(button4);

        //standbypage
        announce = new JLabel("　　　　　　　");//とりまこれで動いたからそのまま
        access = new JButton("接続");
        access.addActionListener(this);
        standby_page.add(announce);
        standby_page.add(access);

        //oseropage
        partner = new JLabel("");
        osero_page.add(partner);

        card_panel = new JPanel();
        layout = new CardLayout();
        card_panel.setLayout(layout);
        card_panel.add(first_page);
        card_panel.add(login_page);
        card_panel.add(signup_page);
        card_panel.add(standby_page);
        card_panel.add(osero_page);

        getContentPane().add(card_panel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == button1) {//first -> signup
            layout.next(card_panel);
            layout.next(card_panel);
        } else if (e.getSource() == button2){ //first -> login
            layout.next(card_panel);
        } else if (e.getSource() == button3){ //login -> standby
            String name = login_name.getText();
            char[] password = login_pass.getPassword();
            String pass = new String(password);
            client.out.println("login");
            client.out.println(name);
            client.out.println(pass);
            try{
                String ans = client.in.readLine();
                if(ans.equals("SUCCESS")){
                    layout.next(card_panel);
                    layout.next(card_panel);
                } else {
                    JFrame Error_frame = new JFrame();
                    JOptionPane.showMessageDialog(Error_frame, "登録されていない，もしくはパスワードが違う");
                }
            }catch(IOException er){

            }
        } else if (e.getSource() == button4){ //signup -> standby
            String name = signup_name.getText();
            char[] password = signup_pass.getPassword();
            String pass = new String(password);
            client.out.println("signup");
            client.out.println(name);
            client.out.println(pass);
            try{
                String ans = client.in.readLine();
                if(ans.equals("SUCCESS")){
                    layout.next(card_panel);
                } else {
                    JFrame Error_frame = new JFrame();
                    JOptionPane.showMessageDialog(Error_frame, "登録済み");
                }
            }catch(IOException er){

            }
        } else if (e.getSource() == access){
            // layout.next(card_panel);
            announce.setText("対戦相手待ち");
            announce.paintImmediately( announce.getVisibleRect());
            GameStart();

        }
    }
    
    public void GameStart(){
        client.out.println("gamestart"); 
        try {
            String partner_name = client.in.readLine();
            String text = "対戦相手：" + partner_name;
            partner.setText(text);
        } catch (IOException er) {
            er.printStackTrace();
        }
    }

}