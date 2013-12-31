package com.codetosalvation.mongo.services.web.resource;

import com.codetosalvation.mongo.services.service.IndexService;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Path("/{collectionName}/index")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class IndexResource extends BaseResource {

	@Autowired
	IndexService indexService;

	@POST
	@Path("/{indexFieldName}")
	public Response createIndex(@PathParam("collectionName") String collectionName,
			@PathParam("indexFieldName") String indexFieldName) {
		try {
			indexService.createIndex(collectionName, indexFieldName);
			return Response.ok("Index created successfully", MediaType.TEXT_PLAIN).build();
		} catch (final Exception e) {
			return toInternalServerError(e);
		}
	}

	@DELETE
	@Path("/{indexFieldName}")
	public Response dropIndex(@PathParam("collectionName") String collectionName) {
		try {
			indexService.dropAllIndexes(collectionName);
			return Response.ok("Index dropped successfully", MediaType.TEXT_PLAIN).build();
		} catch (final Exception e) {
			return toInternalServerError(e);
		}
	}

}
