package br.com.srcm.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.com.srcm.model.Medico;

public class MedicoDao {
	

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("sistema_registro_consultas_medicas");

	public MedicoDao() {}


	public void cadastrarMedico(Medico medico) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			System.out.println(medico);
			em.persist(medico);
			em.getTransaction().commit();
	        System.out.println("Medico Cadastrado com sucesso!");
 
	        } catch (Exception e) {
	            em.getTransaction().rollback();
	            e.printStackTrace();
	        } finally {
	            em.close();
	        }
	}
	
	public Medico buscarPorId(Long id) {
		
		EntityManager em = emf.createEntityManager();
		Medico medico = null;
		try {
			em.getTransaction().begin();
			 // Encontrar o medico pelo ID
            String jpql = "SELECT m FROM Medico m WHERE m.id = :id";
            TypedQuery<Medico> query = em.createQuery(jpql, Medico.class);
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
	
	public void atualizarMedicoPorId(Long id, String nome, String especialidade, String crm) {
		EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();

            // Encontrar o medico pelo ID
            Medico medico = em.find(Medico.class, id);

            if (medico != null) {
                // Atualizar os campos
            	medico.setNome(nome);
            	medico.setCrm(crm);
            	medico.setEspecialidade(especialidade);

                // Persistir as mudanças
                em.merge(medico);

                em.getTransaction().commit();
                System.out.println("medico atualizado com sucesso!");
            } else {
                System.out.println("medico com ID " + id + " não encontrado.");
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        
	}
	
	
	public List<Medico> buscarTodos() {
		EntityManager em = emf.createEntityManager();
		 List<Medico> medicos = null;

	        try {
	        	em.getTransaction().begin();
	            // Criando consulta JPQL para selecionar todos os medicos
	            medicos = em.createQuery("SELECT m FROM Medico m", Medico.class).getResultList();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            em.close();
	        }
	        return medicos;
		
	}
	
//	
	public void excluirPorId(Long id) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			Medico medico = em.find(Medico.class, id);
			
			if(medico != null) {
				em.remove(medico);
				em.getTransaction().commit();
				System.out.println("medico excluido com sucesso!");
			} else {
				System.out.println("medico com ID " + id + " não encontrado.");
			}
			
		} catch (Exception e) {
			em.getTransaction().rollback();
        } finally {
            em.close();
        }
	}
	
	public List<Medico> buscarMedicosPorEspecialidade(String especialidade){
		EntityManager em = emf.createEntityManager();
		List<Medico> medicos = null;

        try {
        	em.getTransaction().begin();
            // Criando consulta JPQL para selecionar todos os medicos
        	TypedQuery<Medico> query = em.createQuery("SELECT m FROM Medico m where m.especialidade = :especialidade", Medico.class);
        	query.setParameter("especialidade", especialidade);
            medicos = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return medicos;
	}
}
