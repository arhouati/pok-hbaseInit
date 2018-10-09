package pfe.extraction.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pfe.extraction.beans.*;
import pfe.extraction.utils.HibernateUtil;

public class ConfigurationDAO {

	public void addConfiguration(Configuration c) throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(c);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"L'ajout de de la configuration n'est pas réalisée correctement ");
		} finally {
			session.close();
		}

	}

	public void updateConfiguration(Configuration c) throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(c);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"La modification de la configuration n'est pas réussie ");
		} finally {
			session.close();
		}
	}

	public void deleteConfiguration(Configuration c) throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.delete(c);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"La suppression de la configuration n'est pas réalisée ");
		} finally {
			session.close();
		}
	}

	public Configuration getConfigurationById(Long idConfiguration)
			throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Configuration Configuration = null;
		try {
			tx = session.beginTransaction();
			Configuration = (Configuration) session.get(Configuration.class,
					idConfiguration);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"La récupération de la configuration avec l'id  "
							+ idConfiguration
							+ "n'est pas réalisée correctement");
		} finally {
			session.close();
		}
		return Configuration;

	}

	public List<Configuration> getAllConfigurations() throws DAOException {
		List<Configuration> listeConfigurations = new ArrayList<Configuration>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			listeConfigurations = session.createQuery("FROM Configuration")
					.list();
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"Erreur lors de la récupération de la liste des configurations ");
		} finally {
			session.close();
		}
		return listeConfigurations;

	}

	public Tag getTagByTypeContent(Long idConfiguration, String typeContent) throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Tag tag = new Tag();
		try {
			tx = session.beginTransaction();
			tag = (Tag) session
					.createQuery(
							"From Tag t where t.configuration= :configuration and t.typeContent= :typeContent ")
					.setEntity("configuration",
							getConfigurationById(idConfiguration))
					.setString("typeContent", typeContent).uniqueResult();

			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"L'ajout de de la source blog n'est pas réalisée ");
		} finally {
			session.close();
		}
		return tag;
	}
}
