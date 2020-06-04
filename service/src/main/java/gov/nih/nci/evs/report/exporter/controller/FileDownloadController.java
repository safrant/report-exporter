package gov.nih.nci.evs.report.exporter.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import gov.nih.nci.evs.report.exporter.service.CodeReadService;

@RestController
@RequestMapping("/download")
public class FileDownloadController {
	
	public enum formats{JSON,CSV,TABD, EXCEL};
	
	
	@Autowired
	CodeReadService service;
	
	@GetMapping(
			  value = "/get-file/{id}/JsonFile.json",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFile(@PathVariable String id) throws IOException {
			    InputStream in = new ByteArrayInputStream(service.getGsonForPrettyPrint().toJson(
			    		service.getRestProperties(
			    				service.getRestTemplate(
			    						new RestTemplateBuilder()), 
			    				service.getCodes(id))).getBytes());
			    return IOUtils.toByteArray(in);
			}
	
	@GetMapping(
			  value = "/get-file-for-props/{id}/{props}/{filename}",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
			)
			public @ResponseBody byte[] getFileForProps(
					@PathVariable String id, 
					@PathVariable String props) throws IOException {
			    InputStream in = new ByteArrayInputStream(service.getGsonForPrettyPrint().toJson(
			    		service.getEntitiesForPropertyNameFilter(service.getRestProperties(
			    				service.getRestTemplate(
			    						new RestTemplateBuilder()), 
			    				service.getCodes(id)), service.getCodes(props))).getBytes());
			    return IOUtils.toByteArray(in);
			}
	
	
		@GetMapping("/output-formats")
		public List<String> getFormatOutput(){
			return Stream.of(formats.values()).map(
					x -> x.name()).collect(Collectors.toList());
		}

}
