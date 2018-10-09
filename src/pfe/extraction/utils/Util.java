package pfe.extraction.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import pfe.extraction.beans.SourceBlog;
import pfe.extraction.beans.SourceFacebook;
import pfe.extraction.dao.ConfigurationDAO;
import pfe.extraction.dao.DAOException;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

public class Util {
	private static Document document = null;
	private static Elements elements = null;
	private static ConfigurationDAO dao = new ConfigurationDAO();

	/**
	 * method to check that a string contains numbers only and remove other
	 * characters
	 * 
	 * @param chaine
	 * @return
	 */
	public static Integer validateParserNumberofPage(String chaine) {
		int result = 0;
		char[] charArray = chaine.toCharArray();
		// temporary arrayliste
		ArrayList<Character> arrayList = new ArrayList<Character>();
		// rémove spaces
		chaine = chaine.replaceAll(" ", "");
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (Character.isDigit(c)) {
				arrayList.add(charArray[i]);
			} else {
			}
		}
		// convert the arrayliste to a String
		StringBuilder builder = new StringBuilder(arrayList.size());
		for (Character ch : arrayList) {
			builder.append(ch);
		}
		chaine = builder.toString();
		result = Integer.parseInt(chaine);

		return result;
	}

	/**
	 * method allows a connexion to a source
	 * 
	 * @param source
	 * @param configuration
	 *            .
	 * @return
	 */
	public static Elements connect(SourceBlog source, String typeContent) {
		try {
			document = Jsoup.connect(source.getUrl()).timeout(600000).get();
			// System.out.println("Méthode connect :*****Document\n " +
			// document.text());
			String selecteur = dao
					.getTagByTypeContent(
							source.getConfiguration().getIdConfiguration(),
							typeContent).getSelector();
			// System.out.println("Méthode connect :*****selector\n " +
			// selecteur);
			elements = document.select(selecteur);
			// System.out.println("Méthode connect :*****elements\n " +
			// elements.text());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return elements;
	}

	/**
	 * method convert from relative URL to absolute URL
	 * 
	 * @param absoluteUrl
	 * @param relativeUrl
	 * @return
	 */
	public static String getAbsoluteURL(URL absoluteUrl, String relativeUrl) {
		try {
			absoluteUrl = new URL(absoluteUrl, relativeUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return absoluteUrl.toString();
	}

	/**
	 * 
	 * @param sfb
	 *            Entity SourceFacebook wich contains the facebook access token
	 * @return an instance of FacebookClient that can fetch Objects from the
	 *         Graph API Facebook
	 * 
	 */
	public static FacebookClient connectWithAccessToken(SourceFacebook sfb) {
		@SuppressWarnings("deprecation")
		FacebookClient facebookClient = new DefaultFacebookClient(
				sfb.getAccessToken());
		return facebookClient;
	}

	/**
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @param clientPort
	 * @return
	 * @throws MasterNotRunningException
	 * @throws ZooKeeperConnectionException
	 * @throws IOException
	 */
	public static Configuration getConfiguration(String serverIp,
			String serverPort, String clientPort)
			throws MasterNotRunningException, ZooKeeperConnectionException,
			IOException {
		Configuration con = HBaseConfiguration.create();
		con.set("hbase.master", serverIp + ":" + serverPort);
		con.set("hbase.zookeeper.quorum", serverIp);
		con.set("hbase.zookeeper.property.clientPort", clientPort);
		return con;
	}

	/**
	 * 
	 * @param conf
	 * @return
	 * @throws MasterNotRunningException
	 * @throws ZooKeeperConnectionException
	 * @throws IOException
	 */
	public static HBaseAdmin connectToHbase(Configuration conf)
			throws MasterNotRunningException, ZooKeeperConnectionException,
			IOException {
		return new HBaseAdmin(conf);
	}
}
