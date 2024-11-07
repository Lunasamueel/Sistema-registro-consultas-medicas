package br.com.srcm.tests;

import br.com.srcm.dao.PacienteDao;
import br.com.srcm.model.Paciente;

public class Tests {

	public static void main(String[] args) {
		Paciente paciente = new Paciente("", "000.000.000-21");
		PacienteDao pacienteDao = new PacienteDao();
		pacienteDao.cadastrarPaciente(paciente);
		

	}

}
 