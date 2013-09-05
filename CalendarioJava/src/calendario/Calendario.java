package calendario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;


/**
* Calendario básico contendo um layout básico produzido para atender um
* trabalho de conclusão de curso oferecido pelo pelos alunos Fellipe Adorno
* e Ketlin Monteiro para a Instituição de Ensino IFG - campus Inhumas.
* 
* @author Fellipe Adorno Claudino da Costa
* @version  1.1
* @see Serializable
*/
public class Calendario extends JDialog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scroll;
	private JTable table;
        /**
         * Guarda a data inicialmente selecionada
         * @since 1.0
         */
	private Calendar calendar;
	
        /**
         * Guarda os meses em portugues para serem selecionados
         * @since 1.0
         */
	private JComboBox<String> mes;
        
        /**
         * Guarda o ano selecionado
         * @since 1.0
         */
	private JSpinner ano;
	private PersonalizaCalendario rend;
        
        /**
         * Guarda os feriados de um ano específico.
         * @since 1.1
         */
	private FeriadosAno feriados;

        /**
         * @param frame Frame pai do JDialog
         */
	public Calendario(Frame frame) {
		super(frame, true);
		this.calendar = Calendar.getInstance();
		setTitle("Calendar 1.0");
		init();
	}
	
        /**
         * Inicializa o processo de construção do layout e preenchimento
         * dos componentes swing necessário para o Calendário.
         * @since 1.0
         */
	private void init() {
		getContentPane().setLayout(new BorderLayout(5,5));
		UIManager.put("ToolTip.foreground", new ColorUIResource(new Color(255, 137, 0)));
		UIManager.put("ToolTip.background", new ColorUIResource(Color.WHITE));
//		UIManager.put("Tooltip.border", BorderFactory.createCompoundBorder(new EtchedBorder(new Color(50, 50, 50),  new Color(50, 50, 50)), new EmptyBorder(5,5, 5, 5)));
		((JComponent) getContentPane()).setBorder(
				BorderFactory.createCompoundBorder(
						new EtchedBorder(new Color(255, 255, 255), new Color(255, 255, 255)),
						new EmptyBorder(5, 5, 5, 5)));
		
		JPanel header = new JPanel();
		header.setLayout(new BorderLayout(5, 5));
	
		String[] m = {"Janeiro", "Fevereiro", "Mar�o", "Abril", "Maio", "Junho",
				"Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
		mes = new JComboBox<>(m);
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
                        @Override
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
	
	}
        
	/**
	 * Método que muda o mês e ano do calendário. Implementado para 
         * especificamente para a própria classe quando for acionado a mudança
         * de uma data (Mês e Ano) na interface do Calendário.
         * 
         * @since 1.1
	 * @param mes Mês a parecer no calendário
	 * @param ano Ano a parecer no calendário
	 */
	public void changeDate(final int mes, final int ano) {
		try {
                    FileInputStream fileIn = new FileInputStream(ano+".year");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    feriados = (FeriadosAno) in.readObject();
                    in.close();
                    fileIn.close();
		} catch (IOException | ClassNotFoundException e) {
			feriados = new FeriadosAno(ano);
		}
		calendar.set(Calendar.MONTH, mes);
		calendar.set(Calendar.YEAR, ano);
		rend = new PersonalizaCalendario(calendar, feriados);
		final Object[] col = {"D", "S", "T", "Q", "Q", "S", "S"};
		initTable(col, getCalendario());
		scroll.setViewportView(table);
	}
	
        /**
         * Esse método obtem os dias ordenados por semana em um Array simples
         * dos tipo Object[][].
         * 
         * @since 1.0
         * @return Object[][]
         */
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
	
        /**
         * Inicializa o processo de construção da tabela de dias.
         * 
         * @since 1.0
         * @param col Letras simbolizando o dia da semana comecando pelo domingo.
         * @param row Todos os dias ordenados pelas semanas.
         * @see #getCalendario() 
         */
	private void initTable(Object[] col, Object[][] row) {
		table = new JTable();
		@SuppressWarnings("serial")
		DefaultTableModel dtm = new DefaultTableModel(row, col){  
                    @Override
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
	 * Esse método é deve ser sobrecarregado pelo objeto que o chama para quando uma data for selecionada ele ser execultado;
         * No entando, Tal como a classe não é abstrata, não deve ser implementado como tal.
         * 
         * @since 1.0
         * @deprecated na versão 1.1
	 * @param day Dia que foi
	 * @param month "Volta o m�s do ano selecionado"
	 * @param year "Volta o ano selecionado"
	 */
        
	public void onSelect(int day, int month, int year) {}
	
	/**
	 * Seta ano do JSpinner
         * 
         * @since 1.1
	 * @param ano Ano selecionado
	 */
	public void setAno(int ano) {
		this.ano.setValue((Object) ano);
	}
        
	/**
	 * Obtem ano selecionado.
         * 
         * @since 1.1
	 * @return Ano selecionado
	 */
	public int getAno() {
		return Integer.parseInt((String) this.ano.getValue());
	}
        
	/**
	 * Seta o mes no JComboBox.
         * 
         * @since 1.1
	 * @param mes Nome do Mes
	 */
	public void setMes(Object mes) {
		this.mes.setSelectedItem(mes);
	}
        
	/**
	 * Obtem mes selecionado.
         * 
         * @since 1.1
	 * @return Mes selecionado
	 */
	public Object getMes() {
		return this.mes.getSelectedItem();
	}
	
        /**
         * Obtem em formato de Calendar a data completa selecionada.
         * 
         * @since 1.1
         * @return Data Selecionada
         */
	public Calendar getValue() {
                while(isVisible()) {}
                if(calendar != null)
                        return calendar;
                return null;
	}
	
        /**
         * Obtem em formato de data dd/MM/yyyy a data completa selecionada.
         * 
         * @since 1.1
         * @return Data Selecionada Em dd/MM/yyyy
         * @see #getValue()
         */
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