package pfe.extraction.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import pfe.extraction.beans.Configuration;
import pfe.extraction.dao.ConfigurationDAO;
import pfe.extraction.dao.DAOException;

public class BlogAlgorithmExtraction implements IAlgorithmExtraction {

	private Elements elements = null;
	private Document document = null;
	private ConfigurationDAO configurationDAO = new ConfigurationDAO();

	/**
	 * @param url
	 * @param configuration
	 * @return
	 * @throws DAOException 
	 */
	@Override
	public List<String> extractUrlPosts(String url, Configuration configuration) throws DAOException {
		String selector;
			selector = configurationDAO.getTagByTypeContent(
					configuration.getIdConfiguration(), POST).getSelector();
		List<String> urlList = new ArrayList<String>();
		try {
			document = Jsoup.connect(url).timeout(60000).get();
			elements = document.select(selector);
			for (int i = 0; i < elements.size(); i++) {
				urlList.add(elements.get(i).attr("href"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlList;
	}

	/**
	 * @param url
	 * @param configuration
	 * @return
	 * @throws DAOException 
	 */
	@Override
	public String extractArticleContent(String url, Configuration configuration) throws DAOException {
		String article = null;
		String selector = configurationDAO.getTagByTypeContent(
				configuration.getIdConfiguration(), ARTICLE).getSelector();
		try {
			document = Jsoup.connect(url).timeout(60000).get();
			elements = document.select(selector);
			article = elements.text();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return article;
	}

	@Override
	public List<String> extractCommentContent(String url,
			Configuration configuration) throws DAOException {
		List<String> commentList = new ArrayList<String>();
		String selector = configurationDAO.getTagByTypeContent(
				configuration.getIdConfiguration(), COMMENT).getSelector();

		try {
			document = Jsoup.connect(url).timeout(60000).get();
			elements = document.select(selector);
			for (int i = 0; i < elements.size(); i++) {
				System.out.println(" comment ==> " + elements.get(i).text());
				commentList.add(elements.get(i).text());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return commentList;
	}

	@Override
	public List<String> extractUserContent(String url,
			Configuration configuration) throws DAOException {
		/*
		 * cette méthode est incomplet car il extrait les noms des utilsateurs
		 * (auteurs des commentaires) seulement ??
		 */
		List<String> UserList = new ArrayList<String>();
		String selector = configurationDAO.getTagByTypeContent(
				configuration.getIdConfiguration(), USER).getSelector();
		try {
			document = Jsoup.connect(url).timeout(60000).get();
			elements = document.select(selector);
			for (int i = 0; i < elements.size(); i++) {
				System.out.println(" user ==> " + elements.get(i).text());
				UserList.add(elements.get(i).text());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return UserList;
	}

}
