package pfe.extraction.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pfe.extraction.beans.Source;
import pfe.extraction.beans.SourceBlog;
import pfe.extraction.utils.HibernateUtil;

public class SourceBlogDAO {
	public void addSource(SourceBlog s) throws DAOException {
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
					"L'ajout de de la source blog n'est pas réalisée ");
		} finally {
			session.close();
		}

	}

	public void updateSource(SourceBlog s) throws DAOException {
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
			throw new DAOException("La modification de de la source blog : "
					+ s.getUrl() + " n'est pas réalisée ");
		} finally {
			session.close();
		}
	}

	public void deleteSource(SourceBlog s) throws DAOException {
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
					"La suppression de de la source blog n'est pas réalisée correctement.");
		} finally {
			session.close();
		}
	}

	public Source getSourceById(Long idSource) throws DAOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Source Source = null;
		try {
			tx = session.beginTransaction();
			Source = (Source) session.get(SourceBlog.class, idSource);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			throw new DAOException(
					"La récupération de la source blog avec l'id : " + idSource
							+ " n'est pas réalisée ");
		} finally {
			session.close();
		}
		return Source;

	}

	public List<Source> getAllSources() {
		List<Source> listeSources = new ArrayList<Source>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query q = session
					.createQuery("FROM SourceBlog where TYPE_SRC =:BL");
			q.setParameter("BL", "BL");
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
