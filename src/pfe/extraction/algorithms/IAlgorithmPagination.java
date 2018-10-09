package pfe.extraction.algorithms;

import java.util.List;

import pfe.extraction.beans.SourceBlog;

public interface IAlgorithmPagination {
	public static String PAGINATION = "pagination";


	public List<String> extractSimplePagination(SourceBlog source);

	public List<String> extractDynamicPagination(SourceBlog source);

	public List<String> extractIteratedPagination(SourceBlog source);

	public List<String> extractAjaxPagination(SourceBlog source);
}
