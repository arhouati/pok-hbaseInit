package pfe.extraction.algorithms;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.select.Elements;

import pfe.extraction.beans.SourceBlog;
import pfe.extraction.utils.Util;

public class BlogAlgorithmPagination implements IAlgorithmPagination {
	
	private Elements elements = null;
	private String url = null;

	/**
	 * @param source
	 * @return
	 */
	@Override
	public List<String> extractSimplePagination(SourceBlog source) {
	 List<String> urlList = new ArrayList<String>();
		elements = Util.connect(source, PAGINATION);
		for (int j = 0; j < elements.size(); j++) {
			url = elements.get(j).attr("href");
			urlList.add(url);
		}
		return urlList;
	}

	/**
	 * @param selector
	 * @return
	 */
	public int getNumberOfPages(SourceBlog source) {
		int np = 0;
		String npString = null;
		elements = Util.connect(source, PAGINATION);
		npString = elements.get(elements.size() - 2).text();
		np = Util.validateParserNumberofPage(npString);

		return np;
	}

	/**
	 * @param source
	 * @return
	 */
	@Override
	public List<String> extractDynamicPagination(SourceBlog source) {
		 List<String> urlList = new ArrayList<String>();
		int np = getNumberOfPages(source);
		int i = 1;
		URL mainUrl;
		try {
			mainUrl = new URL(source.getUrl());
			do {
				elements = Util.connect(source, PAGINATION);
				url = elements.last().attr("href");
				url = Util.getAbsoluteURL(mainUrl, url);
				source.setUrl(url);
				urlList.add(source.getUrl());
				i++;
			} while (i < np);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return urlList;
	}

	/**
	 * @param source
	 * @param configuration
	 * @return
	 */
	@Override
	public List<String> extractIteratedPagination(SourceBlog source) {
		 List<String> urlList = new ArrayList<String>();
		do {
			elements = Util.connect(source, PAGINATION);
			url = elements.attr("href");
			source.setUrl(url);
			if (urlList.contains(url) == true) {
				break;
			} else {
				urlList.add(source.getUrl());
			}
		} while (true);

		return urlList;
	}

	@Override
	public List<String> extractAjaxPagination(SourceBlog source) {
		return null;
	}

}
