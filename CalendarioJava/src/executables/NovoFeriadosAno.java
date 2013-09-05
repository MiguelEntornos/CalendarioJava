package executables;

import calendario.Calendario;
import calendario.FeriadosAno;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class NovoFeriadosAno {

	public static void main(String[] args) {
		final JFrame f = new JFrame();
		int a = Integer.parseInt(JOptionPane.showInputDialog(f, "Digite o ano"));
		while(a != 0) {
			final int ano = a; 
			final FeriadosAno feriados = new FeriadosAno(ano);
			
			String s = "";
			while(s != null) {
				@SuppressWarnings("serial")
				Calendario c = new Calendario(f) {
					@SuppressWarnings("unused")
					public void onChangeDate(int month, int year) {
						if(year == ano) {
							super.changeDate(month, year);
						} else {
							setAno(ano);
						}
					}
					public void onSelect(int day, int month, int year) {
						String r = JOptionPane.showInputDialog(f, "Digite a descricao");
						feriados.setFeriado(month, day, r);
					}
				};
                                c.setVisible(true);
				s = c.toString();
				JOptionPane.showMessageDialog(f, "Salvando "+s);
			}
			if(JOptionPane.showConfirmDialog(f, "Voc� deseja salvar?") == 0) {
				System.out.println("Salvando...");
		        FileOutputStream fileOut;
				try {
					fileOut = new FileOutputStream(feriados.getAno() + ".year");
			        ObjectOutputStream out = new ObjectOutputStream(fileOut);
					out.writeObject(feriados);
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			a = Integer.parseInt(JOptionPane.showInputDialog(f, "Digite o ano"));
			
		}
		System.exit(0);
	}
}
