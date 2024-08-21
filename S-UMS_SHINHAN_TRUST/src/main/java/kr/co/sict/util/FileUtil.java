package kr.co.sict.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class FileUtil {
	private static Logger logger = Logger.getLogger(FileUtil.class);
	
	/**
     * 파일 읽기
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] readFileToByteArray(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        long fileSize = Files.size(path);
        String fileName = path.getFileName().toString();
        logger.debug("file name | size: "+fileName+"|"+ convertFileSize(fileSize));
        return Files.readAllBytes(path);
    }
    
    private static String convertFileSize(long bytes) {
        final long KB = 1024;
        final long MB = KB * 1024;
        final long GB = MB * 1024;

        if (bytes < KB) {
            return bytes + " bytes";
        } else if (bytes < MB) {
            return bytes / KB + " KB";
        } else if (bytes < GB) {
            return bytes / MB + " MB";
        } else {
            return bytes / GB + " GB";
        }
    }

    /**
     * byte로 zip압축하기
     * @param data 대상파일byte
     * @param fileNm 대상파일명
     * @return
     * @throws IOException
     */
	public static byte[] compressBytes(byte[] data, String fileNm) throws IOException {
	    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	    try (ZipOutputStream zipStream = new ZipOutputStream(byteStream)) {
	        ZipEntry entry = new ZipEntry(fileNm);
	        zipStream.putNextEntry(entry);
	        zipStream.write(data);
	        zipStream.closeEntry();
	    }
	    return byteStream.toByteArray();
	}
	
    /**
     * byte로 파일 생성
     * @param data
     * @param savePath
     */
    public static void saveByteArrayToFile(byte[] data, String savePath) {
    	File destDir = new File(savePath).getParentFile();
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(savePath)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 압축 해제
     * @param zipFilePath 압축파일경로 및 압축파일명
     * @param destDirectory 압축해제경로
     * @param isDeleteZipFile 압축파일 삭제여부(true: 삭제, false: 삭제안함)
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String destDirectory, boolean isDeleteZipFile) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            // 압축 파일 내의 모든 엔트리(파일)를 반복하여 압축 해제
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }finally {
        	//압축파일 삭제
        	if (isDeleteZipFile) {
        		File file = new File(zipFilePath);
        		if (file.exists()) {
        			file.delete();
        		}
        	}
		}
    }
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
	
}
