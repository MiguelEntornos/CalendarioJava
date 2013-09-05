package calendario;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Objeto que contém os feriados de um ano; ele pode ser salvo como objeto serializado;
 * editado decordo com as necessidades; 
 * 
 * @version 1.1
 * @author Fellipe Adorno Claudino da Costa
 * @see Serializable
 */
public class FeriadosAno implements Serializable {
	private static final long serialVersionUID = -8899838811549241869L;
        
        /**
         * Guarda em Map o dia/mes do feriado e sua descrição.
         * @since 1.1
         */
	private Map<String, String> feriados;
        
        /**
         * Guarda o ano dos feriados.
         * @since 1.1
         */
	private int ano;
	
        /**
         * Construtor único.
         * @since 1.1
         * @param ano Ano do feriado
         */
	public FeriadosAno(int ano) {
		this.ano = ano;
		feriados = new HashMap<>();
	}
	
        /**
         * Seta um feriado em uma data (mes e dia) para o ano selecionado.
         * 
         * @since 1.1
         * @param month Mes do feriado
         * @param day Dia do feriado
         * @param descricao Descrição do feriado
         */
	public void setFeriado(int month, int day, String descricao) {
		feriados.put(month+"/"+day, descricao);
	}
	
        /**
         * Obtem o mes e o dia do feriado.
         * 
         * @since 1.1
         * @param month Mes do feriado
         * @param day Dia do Feriado
         * @return Feriado em (mes/dia)
         */
	public String getFeriado(int month, int day) {
            return feriados.get(month+"/"+day);
	}
	
        /**
         * Verifica se tal dia é feriado de um ano selecionado.
         * 
         * @since 1.1
         * @param month Mes do feriado
         * @param day Dia do feriado
         * @return true para sim e false para não
         */
	public boolean hasFeriado(int month, int day) {
            if(getFeriado(month, day) != null)
                    return true;
            return false;
	}
	
        /**
         * Obtem o ano do feriado.
         * 
         * @since 1.1
         * @return Ano do feriado.
         */
	public int getAno() {
		return ano;
	}
	
}
