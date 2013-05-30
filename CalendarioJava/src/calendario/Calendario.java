package calendario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;

public class Calendario extends JDialog {
	
	/**
	 * Calendario basic vers�o 1 em Java
	 * @author Fellipe Adorno Claudino da Costa
	 * @since 02 de maio de 2013
	 */
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scroll;
	private JTable table;
	private Calendar calendar;
	
	private JComboBox<String> mes;
	private JSpinner ano;
	private PersonalizaCalendario rend;
	private FeriadosAno feriados;

	public Calendario(Frame frame, boolean visible) {
		super(frame, true);
		this.calendar = Calendar.getInstance();
		init(visible);
	}
	
	private void init(boolean visible) {
		setTitle("Calend�rio 1.0");
		getContentPane().setLayout(new BorderLayout(5,5));
		UIManager.put("ToolTip.foreground", new ColorUIResource(new Color(255, 137, 0)));
		UIManager.put("ToolTip.background", new ColorUIResource(Color.WHITE));
		//UIManager.put("Tooltip.border", BorderFactory.createCompoundBorder(new EtchedBorder(new Color(50, 50, 50),  new Color(50, 50, 50)), new EmptyBorder(5,5, 5, 5)));
		((JComponent) getContentPane()).setBorder(
				BorderFactory.createCompoundBorder(
						new EtchedBorder(new Color(255, 255, 255), new Color(255, 255, 255)),
						new EmptyBorder(5, 5, 5, 5)));
		
		JPanel header = new JPanel();
		header.setLayout(new BorderLayout(5, 5));
	
		String[] m = {"Janeiro", "Fevereiro", "Mar�o", "Abril", "Maio", "Junho",
				"Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
		mes = new JComboBox<String>(m);
		mes.setSelectedIndex(calendar.get(Calendar.MONTH));
		header.add(mes, BorderLayout.WEST);
	
		ano = new JSpinner();
		ano.setValue((Object) calendar.get(Calendar.YEAR)); 
		header.add(ano, BorderLayout.EAST);
		
		ano.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				changeDate(mes.getSelectedIndex(), (Integer) ano.getValue());
			}
		});
		mes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeDate(mes.getSelectedIndex(), (Integer) ano.getValue());
			}
		});
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				calendar = null;
				setVisible(false);
			}
		});
		getContentPane().add(header, BorderLayout.NORTH);
		scroll = new JScrollPane();
		getContentPane().add(scroll, BorderLayout.CENTER);
		
		setSize(250, 197);
		//setLocation(frame.getLocation().x + (frame.getSize().width/2 - getSize().width/2),
		//frame.getLocation().y + (frame.getSize().height/2 - getSize().height/2));
		//calendar.set(Calendar.MONTH, mes.getSelectedIndex());
		changeDate(mes.getSelectedIndex(), (Integer) ano.getValue());
	
		setVisible(visible);
	}
	/**
	 * M�todo que muda o m�s do calend�rio
	 * @param mes int "M�s a parecer no calend�rio"
	 * @param ano int "Ano a parecer no calend�rio"
	 */
	public void changeDate(final int mes, final int ano) {
		try {
			FileInputStream fileIn = new FileInputStream(ano+".year");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			feriados = (FeriadosAno) in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
			feriados = new FeriadosAno(ano);
		}
		calendar.set(Calendar.MONTH, mes);
		calendar.set(Calendar.YEAR, ano);
		rend = new PersonalizaCalendario(calendar, feriados);
		final Object[] col = {"D", "S", "T", "Q", "Q", "S", "S"};
		initTable(col, getCalendario());
		scroll.setViewportView(table);
	}
	
	private Object[][] getCalendario() {
		Object[][] obj = new Object[6][7];
		int j = 0, month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DATE, 1);
		while(this.calendar.get(Calendar.MONTH) == month) {
			obj[j][calendar.get(Calendar.DAY_OF_WEEK) - 1] = calendar.get(Calendar.DATE);
			j = (calendar.get(Calendar.DAY_OF_WEEK) == 7) ? ++j : j ;
			calendar.add(Calendar.DATE , 1);
		}
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return obj;
	}
	
	private void initTable(Object[] col, Object[][] row) {
		table = new JTable();
		@SuppressWarnings("serial")
		DefaultTableModel dtm = new DefaultTableModel(row, col){  
			public boolean isCellEditable(int rowIndex, int mColIndex) {  
                return false;  
            }  
        };  
		table.setModel(dtm);
		table.getSelectionModel().setSelectionMode(  
				  ListSelectionModel.SINGLE_SELECTION);  
		table.getColumnModel().getSelectionModel().setSelectionMode(  
				  ListSelectionModel.SINGLE_SELECTION);  
		table.setCellSelectionEnabled(true);
		table.setDefaultRenderer(Object.class, rend);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setCursor(new Cursor(Cursor.HAND_CURSOR));
		table.setFocusable(false);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable)e.getSource();
			    int row = target.getSelectedRow();
			    int col = target.getSelectedColumn();
			    if(target.getValueAt(row, col) != null) {
			    	calendar.set(Calendar.DAY_OF_MONTH, (Integer) target.getValueAt(row, col));
					table.repaint();
				    if(e.getClickCount() > 1) {
						onSelect(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
						setVisible(false);
				    }
			    }
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				rend.mudarCorSelecionado(-1, -1);
				table.repaint();
			}
		});
		table.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				if(table.getValueAt(row, col) != null)
					rend.mudarCorSelecionado(row, col);
				else
					rend.mudarCorSelecionado(-1, -1);
				table.repaint();
			}
		});
	}
	/**
	 * Quando uma data for selecionada, uma data ser� enviada � esse m�todo que poder� ser subescrevido por seus objetos ou filhos.
	 * @param day "Volta o dia do m�s selecionado"
	 * @param month "Volta o m�s do ano selecionado"
	 * @param year "Volta o ano selecionado"
	 */
	public void onSelect(int day, int month, int year) {}
	
	/**
	 * Seta ano do JSpinner
	 * @param ano int
	 */
	public void setAno(int ano) {
		this.ano.setValue((Object) ano);
	}
	/**
	 * Obtem ano
	 * @return ano int "Ano selecionado"
	 */
	public int getAno() {
		return Integer.parseInt((String) this.ano.getValue());
	}
	/**
	 * Seta o mes do JComboBox
	 * @param mes Object "Nome do Mes"
	 */
	public void setMes(Object mes) {
		this.mes.setSelectedItem(mes);
	}
	/**
	 * Obtem mes
	 * @return mes Object "Mes selecionado"
	 */
	public Object getMes() {
		return this.mes.getSelectedItem();
	}
	
	public Calendar getValue() {
                while(isVisible()) {}
                if(calendar != null)
                        return calendar;
                return null;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                Calendar r = getValue();
                if(r != null) {
                    return f.format(getValue().getTime());
                }
                return null;
	}
}