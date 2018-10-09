package pfe.extraction.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pfe.extraction.beans.Tag;
import pfe.extraction.utils.HibernateUtil;

public class TagDAO {
	public void addTag(Tag t) {
		Session session =  HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(t);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	
	public void updateTag(Tag t) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.update(t);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	
	public void deleteTag(Tag t) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.delete(t);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	
	public Tag getTagById(Long idTag) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Tag Tag = null;
		try {
			tx = session.beginTransaction();
			Tag = (Tag) session.get(Tag.class, idTag);
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return Tag;

	}

	
	public List<Tag> getAllTags() {
		List<Tag> listeTags = new ArrayList<Tag>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			listeTags = session.createQuery("FROM Tag").list();
			if (!tx.wasCommitted())
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return listeTags;

	}
}
