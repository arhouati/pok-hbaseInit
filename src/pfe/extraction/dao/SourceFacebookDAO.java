package pfe.extraction.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pfe.extraction.beans.Source;
import pfe.extraction.beans.SourceFacebook;
import pfe.extraction.utils.HibernateUtil;

public class SourceFacebookDAO {
	public void addSource(SourceFacebook s) throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(s);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"L'ajout de de la source facebook n'est pas réalisée ");
		} finally {
			session.close();
		}

	}

	public void updateSource(SourceFacebook s) throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(s);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"La modification de de la source facebook : "
							+ s.getIdPage() + " n'est pas réalisée ");
		} finally {
			session.close();
		}
	}

	public void deleteSource(SourceFacebook s) throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.delete(s);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"La suppression de de la source facebook n'est pas réalisée ");
		} finally {
			session.close();
		}
	}

	public Source getSourceById(Long idSource) throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Source source = null;
		try {
			tx = session.beginTransaction();
			source = (Source) session.get(SourceFacebook.class, idSource);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"La récupération de la source facebook avec l'id : "
							+ idSource + " n'est pas réalisée ");
		} finally {
			session.close();
		}
		if (source == null)
			throw new DAOException(
					"La récupération de la source facebook avec l'id : "
							+ idSource + " n'est pas réalisée ");
		return source;

	}

	public List<Source> getAllSources() {
		List<Source> listeSources = new ArrayList<Source>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query q = (Query) session
					.createQuery("FROM SourceFacebook where TYPE_SRC =:FB");
			q.setParameter("FB", "FB");
			listeSources = q.list();
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return listeSources;

	}
}
