package calendario;


import java.awt.Frame;

import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) {
		String s = new Calendario(new Frame(), true).toString();
		
		if(s!=null)
			JOptionPane.showMessageDialog(null, s, "Data selecionada:", -1, null);
		System.exit(0);
	}
}
