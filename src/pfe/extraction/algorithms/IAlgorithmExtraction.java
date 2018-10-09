package pfe.extraction.algorithms;

import java.util.List;

import pfe.extraction.beans.Configuration;
import pfe.extraction.dao.DAOException;

public interface IAlgorithmExtraction {
	public static String ARTICLE = "article";
	public static String COMMENT = "comment";
	public static String USER = "user";
   public static String  POST="post";
	public List<String> extractUrlPosts(String url, Configuration configuration) throws DAOException;

	public String extractArticleContent(String url, Configuration configuration) throws DAOException;

	public List<String> extractCommentContent(String url, Configuration configuration) throws DAOException;

	public List<String> extractUserContent(String url, Configuration configuration) throws DAOException;
}
