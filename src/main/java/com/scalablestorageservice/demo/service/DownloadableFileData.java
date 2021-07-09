package com.scalablestorageservice.demo.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

@Getter
@AllArgsConstructor
public class DownloadableFileData {
   private final String fileName;
   private final InputStream fileInputStream;
}
