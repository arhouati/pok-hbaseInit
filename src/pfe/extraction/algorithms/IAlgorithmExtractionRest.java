package pfe.extraction.algorithms;

import java.util.List;
import java.util.Map;

import pfe.extraction.beans.Source;

public interface IAlgorithmExtractionRest {
	/**
	 * 
	 * @param idSource
	 *            id of the source
	 * @return Map that contains id's of posts and their contents
	 */
	public Map<String, String> extractPostsFromPage(long idSource);

	/**
	 * 
	 * @param idSource
	 *            id of the source
	 * @param postID
	 *            id of the post which we have to extract its comments
	 * @return List of comments
	 */
	public List<String> extractCommentsFromPost(long idSource, String postID);
}