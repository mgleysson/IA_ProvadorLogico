import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static final String IMPLIES = "implies";
	public static final String AND = "and";
	public static final String OR = "or";
	public static final String NOT = "not";
	
	static List<String> premissas = new ArrayList<String>();;
	static String conclusao = "";
	
	public static void carregarBase1(){
		String premissa1 = "A implies B";
		String premissa2 = "C implies not B";
		
		premissas.add(premissa1);
		premissas.add(premissa2);
		conclusao = "A implies not C";
	}
	
	public static void carregarBase2() {
		String premissa1 = "A implies B";
		String premissa2 = "B implies C";
		String premissa3 = "C implies D";
		String premissa4 = "not D";
		String premissa5 = "A or E";
		
		premissas.add(premissa1);
		premissas.add(premissa2);
		premissas.add(premissa3);
		premissas.add(premissa4);
		premissas.add(premissa5);
		
		conclusao = "E";
	}
	
	public static void carregarBase3() {
		String premissa1 = "not C";
		String premissa2 = "not C implies M";
		String premissa3 = "M implies E";
		
		premissas.add(premissa1);
		premissas.add(premissa2);
		premissas.add(premissa3);
		
		conclusao = "E";
	}
	
	public static void carregarBase4() {
		String premissa1 = "A and not not B";
		String premissa2 = "C";
		String premissa3 = "D";
		
		premissas.add(premissa1);
		premissas.add(premissa2);
		premissas.add(premissa3);
		
		conclusao = "A and D";
	}
	
	public static void carregarBase5() {
		String premissa1 = "not not B";
		String premissa2 = "C and A";
		String premissa3 = "D and E";
		
		premissas.add(premissa1);
		premissas.add(premissa2);
		premissas.add(premissa3);
		
		conclusao = "B and D";
	}
	
	
	public static void main(String[] args) {
		
		//Para testar o programa basta carregar cada base de dados uma por vez.
		
		carregarBase1();
		
		//carregarBase2();
		
		//carregarBase3();
		
		//carregarBase4();
		
		//carregarBase5();
		
		
		InferenceEngine inference = new InferenceEngine(premissas,conclusao);
		
		inference.conclusaoNasPremissas();
		inference.verificarAplicacaoRegras();
		
		
	}

}
