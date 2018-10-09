package pfe.extraction.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pfe.extraction.beans.SourceFacebook;
import pfe.extraction.dao.DAOException;
import pfe.extraction.dao.SourceFacebookDAO;
import pfe.extraction.utils.Util;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Comment;
import com.restfb.types.Post;

public class FacebookAlgorithm implements IAlgorithmExtractionRest {
	SourceFacebookDAO daoFB = new SourceFacebookDAO();

	@Override
	public Map<String, String> extractPostsFromPage(long idSource) {
		SourceFacebook sourceFacebook = new SourceFacebook();
		try {
			sourceFacebook = (SourceFacebook) daoFB.getSourceById(idSource);
		} catch (DAOException e) {

			System.out.println(e.getMessage());
		}
		FacebookClient facebookClient = Util
				.connectWithAccessToken(sourceFacebook);
		Map<String, String> results = new HashMap<String, String>();
		Connection<Post> myPosts = facebookClient.fetchConnection(
				sourceFacebook.getIdPage() + "/posts", Post.class,
				Parameter.with("limit", 2000));
		for (int i = 0; i < myPosts.getData().size(); i++) {
			Post p = myPosts.getData().get(i);
			if (p.getMessage() != null)
				results.put(p.getId(), p.getMessage());
			else
				results.put(p.getId(), "");
		}
		return results;
	}

	@Override
	public List<String> extractCommentsFromPost(long idSource, String postID) {
		SourceFacebook sourceFacebook = new SourceFacebook();
		try {
			sourceFacebook = (SourceFacebook) daoFB.getSourceById(idSource);
		} catch (DAOException e) {
			System.out.println(e.getMessage());
		}
		List<String> comments = new ArrayList<String>();
		FacebookClient facebookClient = Util
				.connectWithAccessToken(sourceFacebook);
		Connection<Comment> myComments = facebookClient.fetchConnection(postID
				+ "/comments", Comment.class, Parameter.with("limit", 2000));

		for (int i = 0; i < myComments.getData().size(); i++) {
			Comment c = myComments.getData().get(i);
			comments.add(c.getMessage());
		}
		return comments;
	}

}