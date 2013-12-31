package com.codetosalvation.mongo.services.web.resource;

import com.codetosalvation.mongo.services.exception.ApplicationException;
import com.codetosalvation.mongo.services.service.GenericCrudService;
import com.codetosalvation.mongo.services.util.MediaTypeUtil;
import com.mongodb.BasicDBObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Path("/{collectionName}")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class GenericCrudResource extends BaseResource {

	@Autowired
	GenericCrudService<BasicDBObject> genericCrudService;

	@GET
	@Path("/{documentId}")
	public Response getDocument(@PathParam("documentId") String docId, @HeaderParam("Accept") String accept,
			@PathParam("collectionName") String collectionName) {
		final MediaType mediaType = MediaTypeUtil.toMediaType(accept);
		try {

			return toResponse(genericCrudService.getDcoument(collectionName, docId), mediaType);

		} catch (final ApplicationException e) {
			return toApplicationErrorResponse(e, mediaType);
		} catch (final Exception e) {
			return toInternalServerError(e);
		}
	}

	@GET
	@Path("/search")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response searchDocuments(@HeaderParam("Accept") String accept,
			@PathParam("collectionName") String collectionName, @QueryParam("q") String queryParams,
			@QueryParam("fields") String fieldParams, @QueryParam("sort") String sortParams,
			@QueryParam(value = "skip") Integer skip, @QueryParam("limit") Integer limit) {

		logger.info("Searching documents");
		final MediaType mediaType = MediaTypeUtil.toMediaType(accept);
		try {

			return toResponse(genericCrudService.search(collectionName, queryParams, fieldParams, sortParams,
					skip, limit), mediaType);

		} catch (final ApplicationException e) {
			return toApplicationErrorResponse(e, mediaType);
		} catch (final Exception e) {
			return toInternalServerError(e);
		}
	}

	@POST
	@Path("/{documentId}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createDocument(@PathParam("collectionName") String collectionName,
			@HeaderParam("Accept") String accept, @HeaderParam("rootElement") String rootElement,
			@HeaderParam("Content-Type") String contentType,
			@DefaultValue("") @PathParam("documentId") String id, @Context UriInfo uriInfo,
			@Context HttpServletRequest request, String httpBody) {

		logger.info("Creating document..." + uriInfo.getPath());
		final MediaType mediaType = MediaTypeUtil.toMediaType(accept);
		try {
			httpBody = tranformRequest(httpBody, contentType);

			return toResponse(genericCrudService.createDocument(collectionName, id, httpBody, rootElement),
					mediaType);

		} catch (final ApplicationException e) {
			return toApplicationErrorResponse(e, mediaType);
		} catch (final Exception e) {
			return toInternalServerError(e);
		}
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createDocumentWithoutId(@PathParam("collectionName") String collectionName,
			@HeaderParam("Accept") String accept, @HeaderParam("rootElement") String rootElement,
			@HeaderParam("Content-Type") String contentType, @Context UriInfo uriInfo,
			@Context HttpServletRequest request, String httpBody) {

		return createDocument(collectionName, accept, rootElement, contentType, null, uriInfo, request,
				httpBody);
	}

	@PUT
	@Path("/{documentId}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateDocument(@PathParam("collectionName") String collectionName,
			@HeaderParam("Accept") String accept, @HeaderParam("rootElement") String rootElement,
			@HeaderParam("Content-Type") String contentType, @Context UriInfo uriInfo,
			@Context HttpServletRequest request, String httpBody) {

		logger.info("Updating document...");
		final MediaType mediaType = MediaTypeUtil.toMediaType(accept);
		try {
			httpBody = tranformRequest(httpBody, contentType);

			return toResponse(genericCrudService.updateDocument(collectionName, httpBody, rootElement),
					mediaType);

		} catch (final ApplicationException e) {
			return toApplicationErrorResponse(e, mediaType);
		} catch (final Exception e) {
			return toInternalServerError(e);
		}
	}

	@DELETE
	@Path("/{documentId}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteDocument(@PathParam("collectionName") String collectionName,
			@HeaderParam("Accept") String accept, @HeaderParam("Content-Type") String contentType,
			@PathParam("documentId") String id, @Context UriInfo uriInfo, @Context HttpServletRequest request) {

		logger.info("Deleting document...");
		final MediaType mediaType = MediaTypeUtil.toMediaType(accept);
		try {
			genericCrudService.deleteDocument(collectionName, id);
			return Response.ok().build();
		} catch (final ApplicationException e) {
			return toApplicationErrorResponse(e, mediaType);
		} catch (final Exception e) {
			return toInternalServerError(e);
		}
	}

}
