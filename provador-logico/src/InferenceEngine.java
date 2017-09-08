import java.util.List;

public class InferenceEngine {

	private List<String> premissas;
	private String conclusao;
	private int flag = 0;

	public InferenceEngine(List<String> premissas, String conclusao) {
		this.premissas = premissas;
		this.conclusao = conclusao;

		this.listarPremissas();
	}

	public void conclusaoNasPremissas() {

		for (int i = 0; i < premissas.size(); i++) {
			String temp = premissas.get(i);

			if (conclusao.equals(temp)) {
				System.out.println("\nA conclusão é verdadeira devido a premissa " + (i + 1) + ". " + temp);
				flag = 1;
			}
		}

		// Testando o caso em que a conclusão é equivalente (por exemplo, A->B =
		// ~AvB)
		String conclusaoEquivalente = "";

		if (conclusao.contains(Main.IMPLIES)) {
			String[] termos = conclusao.split(" ");

			for (int i = 0; i < termos.length; i++) {
				if (termos[i].equals(Main.IMPLIES)) {
					String[] operandos = buscarOperandos(termos, i);
					conclusaoEquivalente = "not " + operandos[0] + " or " + operandos[1];
				}
			}

		} else {
			if (conclusao.contains(Main.AND) || conclusao.contains(Main.OR)) {
				String[] termos = conclusao.split(" ");

				for (int i = 0; i < termos.length; i++) {
					if (termos[i].equals(Main.AND)) {
						String[] operandos = buscarOperandos(termos, i);
						conclusaoEquivalente = operandos[1] + " and " + operandos[0];
					} else {
						if (termos[i].equals(Main.OR)) {
							String[] operandos = buscarOperandos(termos, i);
							conclusaoEquivalente = operandos[1] + " or " + operandos[0];
						}
					}
				}

			}
		}

		for (int i = 0; i < premissas.size(); i++) {
			String temp = premissas.get(i);

			if (conclusaoEquivalente.equals(temp)) {
				System.out.println("\nA conclusão é verdadeira devido a premissa " + (i + 1) + ". " + temp);
				flag = 1;
			}
		}

	}

	public void listarPremissas() {

		System.out.println("===============");
		System.out.println("Premissas: ");
		for (int i = 0; i < premissas.size(); i++) {
			String temp = premissas.get(i);
			System.out.println((i + 1) + ". " + temp);
		}
		System.out.println();
		System.out.println("Conclusão: ");
		System.out.println(conclusao);
		System.out.println("===============");

		this.conclusaoNasPremissas();

	}

	public void adicionarPremissa(String novaPremissa) {
		if (!(premissas.contains(novaPremissa))) {
			premissas.add(novaPremissa);
		}
	}

	public void verificarAplicacaoRegras() {

		for (int i = 0; i < premissas.size() && flag == 0; i++) {
			String prem = premissas.get(i);

			String[] itens = prem.split(" ");

			for (int j = 0; j < itens.length; j++) {

				// Se um fato for encontrado, verifica se dá pra aplicar And
				// Introduction
				if (itens.length == 1) {
					introduction(itens[0]);
				}

				// Se um operador 'implies' for encontrado verifica se dá pra
				// aplicar Modus Ponens e aplica Equivalência
				if (itens[j].equals(Main.IMPLIES)) {

					String[] operandos = buscarOperandos(itens, j);

					modusPonens(operandos[0], operandos[1]);
					equivalence(operandos[0], operandos[1]);

				}

				// Se um operandor 'and' for encontrado aplica-se And
				// Elimination
				if (itens[j].equals(Main.AND)) {

					String[] operandos = buscarOperandos(itens, j);

					elimination(operandos[0], operandos[1]);

				}

				// Se um operador 'or' for encontrado verifica se dá aplicar
				// Resolução e Resolução Unitária
				if (itens[j].equals(Main.OR)) {

					String[] operandos = buscarOperandos(itens, j);

					unitResolution(operandos[0], operandos[1]);
					resolution(operandos[0], operandos[1], i);

				}

				// Se dois operadores 'not' forem encontrados um após o outro,
				// aplica-se a regra Dupla Negação
				if (j != itens.length - 1) {
					if (itens[j].equals(Main.NOT) && itens[j + 1].equals(Main.NOT)) {

						doubleNegation(itens, j);

					}
				}

			}

		}

	}

	// Busca operandos de uma premissa qualquer
	public String[] buscarOperandos(String[] itens, int j) {
		String temp = "";

		for (int i = 0; i < j; i++) {
			if (i == j - 1) {
				temp = temp + itens[i];
			} else {
				temp = temp + itens[i] + " ";
			}

		}

		String operando1 = temp;

		temp = "";

		for (int i = j + 1; i < itens.length; i++) {
			if (i == itens.length - 1) {
				temp = temp + itens[i];
			} else {
				temp = temp + itens[i] + " ";
			}

		}

		String operando2 = temp;

		String[] operandos = { operando1, operando2 };

		return operandos;

	}

	/*
	 * Aplicação da regra And Introduction:
	 * 
	 * A B
	 * 
	 * Resultado: (A and B)
	 */
	public void introduction(String fato) {

		for (int i = 0; i < premissas.size() && flag == 0; i++) {

			String premissa = premissas.get(i);

			if (premissa.length() == 1 && !premissa.equals(fato)) {
				System.out.println("Aplicando AND Introdução nos fatos " + fato + " e " + premissa);
				premissa = fato + " and " + premissa;
				adicionarPremissa(premissa);
				listarPremissas();
			}

		}

	}

	/*
	 * Aplicação da regra Modus Ponens:
	 * 
	 * A implies B A
	 * 
	 * Resultado: B
	 */
	public void modusPonens(String op1, String op2) {

		for (int j = 0; j < premissas.size() && flag == 0; j++) {

			if (premissas.get(j).equals(op1)) {
				System.out.println("Aplicando a regra Modus Ponens com (" + op1 + " " + Main.IMPLIES + " " + op2
						+ ") e " + premissas.get(j) + ":");
				adicionarPremissa(op2);
				listarPremissas();
			}
		}

	}

	/*
	 * Aplicação da Regra de Equivalência:
	 * 
	 * A implies B
	 * 
	 * Resultado: not A or B
	 */
	public void equivalence(String op1, String op2) {

		if (flag == 0) {
			System.out.println("Aplicando a Equivalencia em (" + op1 + " implies " + op2 + "):");
			String prem = "not " + op1 + " or " + op2;

			adicionarPremissa(prem);
			listarPremissas();
		}

	}

	/*
	 * Aplicação da Regra de And Elimination:
	 * 
	 * A and B
	 * 
	 * Resultado: A
	 */
	public void elimination(String op1, String op2) {

		if (flag == 0) {
			System.out.println("Aplicando a regra AND Eliminação em (" + op1 + " and " + op2 + "):");

			String prem = op1;
			adicionarPremissa(prem);
			listarPremissas();
		}

	}

	/*
	 * Aplicação da Regra de Resolução Unitária:
	 * 
	 * A or B not B
	 * 
	 * Resultado: A
	 */
	public void unitResolution(String op1, String op2) {

		for (int i = 0; i < premissas.size() && flag == 0; i++) {

			if (premissas.get(i).equals("not " + op2)) {
				System.out.print(
						"Aplicando a regra Resolução Unitária em: (" + op1 + " or " + op2 + ") e (not " + op2 + "):");
				System.out.println();

				adicionarPremissa(op1);
			}

		}

		listarPremissas();

	}

	/*
	 * Aplicação da Regra de Resolução:
	 * 
	 * A or B not B or C
	 * 
	 * Resultado: A or C
	 */
	public void resolution(String op1, String op2, int codPremissa) {

		for (int i = 0; i < premissas.size() && flag == 0; i++) {

			// verificando se dá pra aplicar Resolução
			if (premissas.get(i).contains("or")) {
				if (!premissas.get(i).contains("not not " + op1) && !premissas.get(i).contains("not not " + op2)) {
					if (premissas.get(i).contains("not " + op2) || premissas.get(i).contains("not " + op1)) {
						String[] simbolos = premissas.get(i).split(" ");
						String[] operandos = null;
						for (int j = 0; j < simbolos.length; j++) {
							if (simbolos[j].equals("or")) {
								operandos = buscarOperandos(simbolos, j);
							}
						}

						String prem = "";

						System.out.println(
								"Aplicando a regra Resolução nas premissas " + (codPremissa + 1) + " e " + (i + 1));

						// Formando a sentença resultante da regra de Resolucao,
						// considerando a ordem A v B 'ou' B v C
						if (operandos[1].equals("not " + op2)) {
							prem = op1 + " or " + operandos[0];
						} else {
							if (operandos[1].equals("not " + op1))
								prem = op2 + " or " + operandos[0];
						}

						if (operandos[0].equals("not " + op1)) {
							prem = op2 + " or " + operandos[1];
						} else {
							if (operandos[0].equals("not " + op2)) {
								prem = op1 + " or " + operandos[1];
							}
						}

						adicionarPremissa(prem);
						listarPremissas();

					}
				}
			}
		}

	}

	/*
	 * Aplicação da Regra de Dupla Negação:
	 * 
	 * not not A
	 * 
	 * Resultado: A
	 */
	public void doubleNegation(String[] itens, int j) {

		if (flag == 0) {
			System.out.println();
			System.out.print("Aplicando a regra Dupla Negação na sentença: ");

			for (int i = 0; i < itens.length; i++) {
				System.out.print(itens[i] + " ");

			}

			itens[j] = "";
			itens[j + 1] = "";
			String prem = "";

			System.out.print("\nSentença resultante: ");
			for (int i = 0; i < itens.length; i++) {

				if (i == itens.length - 1) {
					prem = prem + itens[i];
				} else {
					prem = prem + itens[i] + " ";
				}

			}

			// Retirando os espaços gerados na hora da geração da regra
			// resultante!
			String premissaFormatoPadrao = "";

			if (prem.contains("   ")) {
				premissaFormatoPadrao = prem.replaceAll("   ", " ");
			} else {
				if (prem.contains("  ")) {
					premissaFormatoPadrao = prem.replaceAll("  ", "");
				}
			}

			System.out.println(premissaFormatoPadrao);

			adicionarPremissa(premissaFormatoPadrao);
			listarPremissas();
		}

	}

}