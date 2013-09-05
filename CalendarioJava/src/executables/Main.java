package executables;


import calendario.Calendario;
import java.awt.Frame;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) {
		Calendario calendario = new Calendario(new Frame());
                calendario.setTitle("Calendário");
                calendario.setVisible(true);
                String s = calendario.toString();
		
		if(s!=null)
			JOptionPane.showMessageDialog(null, s, "Data selecionada:", -1, null);
                
		System.exit(0);
	}
}
