package uws.service.request;

/*
 * This file is part of UWSLibrary.
 * 
 * UWSLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UWSLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with UWSLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2014 - Astronomisches Rechen Institut (ARI)
 */

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import uws.UWSException;
import uws.service.UWS;
import uws.service.file.UWSFileManager;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * <p>Extract parameters encoded using the Content-type multipart/form-data
 * in an {@link HttpServletRequest}.</p>
 * 
 * <p>
 * 	The created file(s) is(are) stored in the temporary upload directory ({@link UWSFileManager#TMP_UPLOAD_DIR} ; this attribute can be modified if needed).
 * 	This directory is supposed to be emptied regularly in case it is forgotten at any moment by the UWS service implementation to delete unused request files.
 * </p>
 * 
 * <p>
 * 	The size of the full request body is limited by the static attribute {@link #SIZE_LIMIT} before the creation of the file.
 * 	Its default value is: {@link #DEFAULT_SIZE_LIMIT}={@value #DEFAULT_SIZE_LIMIT} bytes.
 * </p>
 * 
 * <p>
 * 	By default, this {@link RequestParser} overwrite parameter occurrences in the map: that's to say if a parameter is provided several times,
 * 	only the last value will be kept. This behavior can be changed by overwriting the function {@link #consumeParameter(String, Object, Map)}
 * 	of this class.
 * </p>
 * 
 * @author Gr&eacute;gory Mantelet (ARI)
 * @version 4.1 (12/2014)
 * @since 4.1
 */
public class MultipartParser implements RequestParser {

	/** HTTP content-type for HTTP request formated in multipart. */
	public static final String EXPECTED_CONTENT_TYPE = "multipart/form-data";

	/** Default maximum allowed size for an HTTP request content: 10 MiB. */
	public static final int DEFAULT_SIZE_LIMIT = 10 * 1024 * 1024;

	/** <p>Maximum allowed size for an HTTP request content. Over this limit, an exception is thrown and the request is aborted.</p>
	 * <p><i>Note:
	 * 	The default value is {@link #DEFAULT_SIZE_LIMIT} (= {@value #DEFAULT_SIZE_LIMIT} MiB).
	 * </i></p>
	 * <p><i>Note:
	 * 	This limit is expressed in bytes and can not be negative.
	 *  Its smallest possible value is 0. If the set value is though negative,
	 *  it will be ignored and {@link #DEFAULT_SIZE_LIMIT} will be used instead.
	 * </i></p> */
	public static int SIZE_LIMIT = DEFAULT_SIZE_LIMIT;

	/** Indicates whether this parser should allow inline files or not. */
	public final boolean allowUpload;

	/** File manager to use to create {@link UploadFile} instances.
	 * It is required by this new object to execute open, move and delete operations whenever it could be asked. */
	protected final UWSFileManager fileManager;

	/**
	 * <p>Build a {@link MultipartParser} forbidding uploads (i.e. inline files).</p>
	 * 
	 * <p>
	 * 	With this parser, when an upload (i.e. submitted inline files) is detected, an exception is thrown by {@link #parse(HttpServletRequest)}
	 * 	which cancels immediately the request.
	 * </p>
	 */
	public MultipartParser(){
		this(false, null);
	}

	/**
	 * Build a {@link MultipartParser} allowing uploads (i.e. inline files).
	 * 
	 * @param fileManager	The file manager to use in order to store any eventual upload. <b>MUST NOT be NULL</b>
	 */
	public MultipartParser(final UWSFileManager fileManager){
		this(true, fileManager);
	}

	/**
	 * <p>Build a {@link MultipartParser}.</p>
	 * 
	 * <p>
	 * 	If the first parameter is <i>false</i>, then when an upload (i.e. submitted inline files) is detected, an exception is thrown
	 * 	by {@link #parse(HttpServletRequest)} which cancels immediately the request.
	 * </p>
	 * 
	 * @param uploadEnabled					<i>true</i> to allow uploads (i.e. inline files), <i>false</i> otherwise.
	 *                     					If <i>false</i>, the two other parameters are useless.
	 * @param fileManager					The file manager to use in order to store any eventual upload. <b>MUST NOT be NULL</b>
	 */
	protected MultipartParser(final boolean uploadEnabled, final UWSFileManager fileManager){
		if (uploadEnabled && fileManager == null)
			throw new NullPointerException("Missing file manager although the upload capability is enabled => can not create a MultipartParser!");

		this.allowUpload = uploadEnabled;
		this.fileManager = fileManager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final Map<String,Object> parse(final HttpServletRequest request) throws UWSException{
		LinkedHashMap<String,Object> parameters = new LinkedHashMap<String,Object>();
		MultipartRequest multipart = null;

		try{

			// Parse the request body:
			multipart = new MultipartRequest(request, UWSFileManager.TMP_UPLOAD_DIR.getPath(), (SIZE_LIMIT < 0 ? DEFAULT_SIZE_LIMIT : SIZE_LIMIT), new FileRenamePolicy(){
				@Override
				public File rename(File file){
					Object reqID = request.getAttribute(UWS.REQ_ATTRIBUTE_ID);
					if (reqID == null || !(reqID instanceof String))
						reqID = (new Date()).getTime();
					char uniq = 'A';
					File f = new File(file.getParentFile(), "UPLOAD_" + reqID + uniq + "_" + file.getName());
					while(f.exists()){
						uniq++;
						f = new File(file.getParentFile(), "UPLOAD_" + reqID + "_" + file.getName());
					}
					return f;
				}
			});

			// Extract all "normal" parameters:
			String param;
			Enumeration<String> e = multipart.getParameterNames();
			while(e.hasMoreElements()){
				param = e.nextElement();
				for(String occurence : multipart.getParameterValues(param))
					consumeParameter(param, occurence, parameters);
			}

			// Extract all inline files as additional parameters:
			e = multipart.getFileNames();
			if (!allowUpload && e.hasMoreElements())
				throw new UWSException(UWSException.BAD_REQUEST, "Uploads are not allowed by this service!");
			while(e.hasMoreElements()){
				param = e.nextElement();
				if (multipart.getFile(param) == null)
					continue;

				/*
				 * TODO !!!POSSIBLE ISSUE!!!
				 * MultipartRequest is not able to deal with multiple files having the same parameter name. However, all files are created/uploaded
				 * but only the last one is accessible through this object....so only the last can be deleted, which could be a problem later
				 * (hence the usage of the system temporary directory).
				 */

				// build its description/pointer:
				UploadFile lob = new UploadFile(param, multipart.getOriginalFileName(param), multipart.getFile(param).toURI().toString(), fileManager);
				lob.mimeType = multipart.getContentType(param);
				lob.length = multipart.getFile(param).length();
				// add it inside the parameters map:
				consumeParameter(param, lob, parameters);
			}

		}catch(IOException ioe){
			throw new UWSException(UWSException.INTERNAL_SERVER_ERROR, ioe, "Internal Error => Impossible to extract parameters from the Multipart HTTP request!");
		}catch(IllegalArgumentException iae){
			String confError = iae.getMessage();
			if (UWSFileManager.TMP_UPLOAD_DIR == null)
				confError = "Missing upload directory!";
			throw new UWSException(UWSException.INTERNAL_SERVER_ERROR, iae, "Internal Error: Incorrect UPLOAD configuration: " + confError);
		}

		return parameters;
	}

	/**
	 * <p>Consume the specified parameter: add it inside the given map.</p>
	 * 
	 * <p>
	 * 	By default, this function is just putting the given value inside the map. So, if the parameter already exists in the map,
	 * 	its old value will be overwritten by the given one.
	 * </p>
	 * 
	 * <p><i>Note:
	 * 	If the old value was a file, it will be deleted from the file system before its replacement in the map.
	 * </i></p>
	 * 
	 * @param name		Name of the parameter to consume.
	 * @param value		Its value.
	 * @param allParams	The list of all parameters read until now.
	 */
	protected void consumeParameter(final String name, final Object value, final Map<String,Object> allParams){
		// If the old value was a file, delete it before replacing its value:
		if (allParams.containsKey(name) && allParams.get(name) instanceof UploadFile){
			try{
				((UploadFile)allParams.get(name)).deleteFile();
			}catch(IOException ioe){}
		}

		// Put the given value in the given map:
		allParams.put(name, value);
	}

	/**
	 * <p>Utility method that determines whether the content of the given request is a multipart/form-data.</p>
	 * 
	 * <p><i>Important:
	 * 	This function just test the content-type of the request. The HTTP method (e.g. GET, POST, ...) is not tested.
	 * </i></p>
	 *
	 * @param request The servlet request to be evaluated. Must be non-null.
	 *
	 * @return	<i>true</i> if the request is multipart,
	 *        	<i>false</i> otherwise.
	 */
	public static final boolean isMultipartContent(final HttpServletRequest request){
		// Extract the content type and determine if it is a multipart request (its content type should start by multipart/form-data"):
		String contentType = request.getContentType();
		if (contentType == null)
			return false;
		else if (contentType.toLowerCase().startsWith(EXPECTED_CONTENT_TYPE))
			return true;
		else
			return false;
	}

}
