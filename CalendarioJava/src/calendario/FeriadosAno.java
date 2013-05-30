package calendario;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FeriadosAno implements Serializable {
	private static final long serialVersionUID = -8899838811549241869L;
	private Map<String, String> feriados;
	private int ano;
	
	public FeriadosAno(int ano) {
		this.ano = ano;
		feriados = new HashMap<String, String>();
	}
	
	public void setFeriado(int month, int day, String descricao) {
		feriados.put(month+"/"+day, descricao);
	}
	
	public String getFeriado(int month, int day) {
		return feriados.get(month+"/"+day);
	}
	
	public boolean hasFeriado(int month, int day) {
		if(getFeriado(month, day) != null)
			return true;
		return false;
	}
	
	public int getAno() {
		return ano;
	}
	
}
