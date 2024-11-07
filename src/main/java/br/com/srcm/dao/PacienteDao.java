package br.com.srcm.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import br.com.srcm.model.Paciente;

public class PacienteDao {
	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("sistema_registro_consultas_medicas");

	public PacienteDao() {}
	
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();


	public void cadastrarPaciente(Paciente paciente) {
		
		Set<ConstraintViolation<Paciente>> violations = validator.validate(paciente);

        // Verifica se há violações
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Paciente> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException("Erro de validação:\n" + sb.toString());
        }
		
		
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			System.out.println(paciente);			
			
			em.persist(paciente);
			em.getTransaction().commit();
	        System.out.println("Paciente Cadastrado com sucesso!");
 
	        } catch (Exception e) {
	            em.getTransaction().rollback();
	            e.printStackTrace();
	        } finally {
	            em.close();
	        }
	}
	
	public Paciente buscarPorId(Long id) {
		
		EntityManager em = emf.createEntityManager();
		Paciente medico = null;
		try {
			em.getTransaction().begin();
			 // Encontrar o paciente pelo ID
            String jpql = "SELECT p FROM Paciente p WHERE p.id = :id";
            TypedQuery<Paciente> query = em.createQuery(jpql, Paciente.class);
            System.out.println(query);
	        query.setParameter("id", id);
	        
	        medico = query.getSingleResult();
			
		} catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
		
		return medico;
		
	}
	
	public void atualizarPacientePorId(Long id, String nome, String cpf) {
		EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();

            // Encontrar o paciente pelo ID
            Paciente paciente = em.find(Paciente.class, id);

            if (paciente != null) {
                // Atualizar os campos
            	paciente.setCpf(cpf);
            	paciente.setNome(nome);

                // Persistir as mudanças
                em.merge(paciente);

                em.getTransaction().commit();
                System.out.println("paciente atualizado com sucesso!");
            } else {
                System.out.println("paciente com ID " + id + " não encontrado.");
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        
	}
	
	
	public List<Paciente> buscarTodos() {
		EntityManager em = emf.createEntityManager();
		 List<Paciente> pacientes = null;

	        try {
	        	em.getTransaction().begin();
	            // Criando consulta JPQL para selecionar todos os medicos
	        	pacientes = em.createQuery("SELECT p FROM Paciente m", Paciente.class).getResultList();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            em.close();
	        }
	        return pacientes;
		
	}
	
//	
	public void excluirPorId(Long id) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			Paciente paciente = em.find(Paciente.class, id);
			
			if(paciente != null) {
				em.remove(paciente);
				em.getTransaction().commit();
				System.out.println("paciente excluido com sucesso!");
			} else {
				System.out.println("paciente com ID " + id + " não encontrado.");
			}
			
		} catch (Exception e) {
			em.getTransaction().rollback();
        } finally {
            em.close();
        }
	}

}
