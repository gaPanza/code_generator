package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



public class MainScreen {
	private static JButton btnAtualizar;
	private static JFrame frame;
	private static JTextField email;
	private static JTextField viewTitle;
	private static JButton btnAjuda;
	private static JLabel emailLabel;
	private static JButton btnDeletar;
	private static String deleteOption;
	private static HashMap<String, String> fields2 = new HashMap<String,String>();
	private static ArrayList<String> fields = new ArrayList<String>();
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainActivity();
			}
		});

	}

	protected static void mainActivity() {
		frame = mainFrame("", Color.WHITE, false);

	}
	
	//Handles the Main Screen
	public static JFrame mainFrame(String labelInfo, Color color, Boolean visible) {
		if (frame != null)
			frame.dispose();
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainScreen.class.getResource("/logo.gif")));
		frame.setTitle("IPNET - Gerador de Campos Customizáveis 1.0 - SnapShot");
		frame.setBounds(0, 0, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel fundo = new JLabel("");
		fundo.setIcon(new ImageIcon(MainScreen.class.getResource("/logo.jpg")));
		fundo.setBounds(0, 0, 640, 480);
		frame.getContentPane().add(fundo);

		JLabel logo = new JLabel("IPNET Logo");
		ImageIcon icon = new ImageIcon(MainScreen.class.getResource("/logo.gif"));
		icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth() / 2, icon.getIconHeight() / 2, 100));
		logo.setIcon(icon);
		logo.setBounds(60, 125, icon.getIconWidth(), icon.getIconHeight());
		fundo.add(logo);

		emailLabel = new JLabel("Nome no Relatório");
		emailLabel.setBounds(60, logo.getY() + logo.getHeight() + 10, 160, 14);
		emailLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		fundo.add(emailLabel);

		email = new JTextField();
		email.setBounds(60, emailLabel.getY() + emailLabel.getHeight() + 5, 160, 30);
		email.setColumns(10);
		fundo.add(email);
		
		emailLabel = new JLabel("Nome na View");
		emailLabel.setBounds(225, logo.getY() + logo.getHeight() + 10, 160, 14);
		emailLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		fundo.add(emailLabel);
		
		viewTitle = new JTextField();
		viewTitle.setBounds(225, emailLabel.getY() + emailLabel.getHeight() + 5, 160, 30); 
		viewTitle.setColumns(10);
		fundo.add(viewTitle);
		

		btnAtualizar = new JButton("Gerar código");
		btnAtualizar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAtualizar.setBounds(145, email.getY() + email.getHeight() + 10, 160, 30);

		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Add Field to List
				//ArrayList <String>
				
				try{
					if(!(viewTitle.getText().equals(""))){
						fields2.put(email.getText(), viewTitle.getText());
						fields.add(email.getText());
						System.out.println("Campo Adicionado : " +  "Relatório " +email.getText() + "||" + "View " + viewTitle.getText());
						deleteOption = "Campo Adicionado : " +  "Relatório " +email.getText() + "||" + "View " + viewTitle.getText();
					}
				}	catch(NullPointerException ex){
					}
				email.setText(null);
				viewTitle.setText(null);
				
			}
		});
		fundo.add(btnAtualizar);

		btnAjuda = new JButton("Processar");
		btnAjuda.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAjuda.setBounds(145, btnAtualizar.getY() + btnAtualizar.getHeight(), 160, 30);
		btnAjuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Takes the List to a method
				try{
					ProcessRequests.generateCode(fields2,fields);				
				}catch(Exception ez){
					
				}
				
				
			}
		});
		fundo.add(btnAjuda);
		btnDeletar = new JButton("Deletar");
		btnDeletar.setFont(new Font("Tahoma", Font.PLAIN,12));
		btnDeletar.setBounds(145, btnAjuda.getY() + btnAjuda.getHeight() , 160, 30);
		btnDeletar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					for(int i = 0;i<deleteOption.length();i++){
						//System.out.print("\b");
					}
					
					for(int i = 0;i<fields.size();i++){
						if(i+1 == fields.size()){
							fields2.remove(fields.get(i));
							fields.remove(i);
							System.out.println(fields.toString());
						}
					}
					
				}catch(Exception ez){
					
				}
			}
		});
		
		fundo.add(btnDeletar);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(btnAtualizar);
		return frame;
	}
}
