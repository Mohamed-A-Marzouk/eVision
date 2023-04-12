package com.evision.readcsv.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.evision.readcsv.service.CSVService;

@RestController
@CrossOrigin
@RequestMapping(value = "/files")
public class FileController {

	@Value("${custom.prop.fileFolder}")
    private String filesPath;
	
	@Autowired
	private CSVService csvService;
	
	@PostMapping(value = "/uploadFile")
	public String uploadFile(@RequestParam("file") MultipartFile file) throws InterruptedException, ExecutionException, IOException  {
		
		String pathName = filesPath + "/" + file.getOriginalFilename();
        try {
			FileUtils.writeByteArrayToFile(new File(pathName), file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
        Path path = Paths.get(filesPath+ "/" + file.getOriginalFilename());
        File newFile = new File(path.toString());
        System.out.println(newFile);
        csvService.processFile(newFile);
        
		return "Process file successed";
	}

}
