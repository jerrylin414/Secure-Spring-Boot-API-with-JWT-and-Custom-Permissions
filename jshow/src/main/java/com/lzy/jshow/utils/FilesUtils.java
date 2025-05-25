package com.lzy.jshow.utils;

import com.alibaba.excel.EasyExcel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FilesUtils {

	public static boolean exists(String filePath) throws Exception {
        return Files.exists(Paths.get(filePath));
    }
	
	public static boolean delete(String filePath) throws Exception {
    	File file = new File(filePath);
        return file.delete();
    }
	
	public static void deleteDirectory(String dirPath, boolean deleteDir){
		deleteDirectory(new File(dirPath), deleteDir);
	}
	public static void deleteDirectory(File dir, boolean deleteDir){
        File[] files = dir.listFiles();
        if(files != null){
        	for (File file : files) {
        		if (file.isDirectory()) {
        			deleteDirectory(file, true);
                }else{
                	file.delete();
                }
            }
        }
        if(deleteDir){
        	 dir.delete();
        }
    }
	
	public static boolean move(String sourcePath, String targetPath){
		try {
			Path source = Paths.get(sourcePath);
	        Path target = Paths.get(targetPath);
	        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
	        return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }

    public static boolean write(String filePath, String con, boolean addEol, boolean autoCreateFile, boolean appendCon) throws Exception {
    	return write(filePath, con, addEol, autoCreateFile, false, appendCon);
    }
    
	public static boolean write(String filePath, String con, boolean addEol, boolean autoCreateFile, boolean existDel, boolean appendCon) throws Exception {
		 FileOutputStream os = null;
		 OutputStreamWriter out = null;
		 BufferedWriter writer = null;
		 try {
			File file = new File(filePath);
			boolean exist = file.exists();
			if(exist && existDel){
				file.delete();
				file.createNewFile();
			}
			else if(autoCreateFile){
				File parent = file.getParentFile();
				parent.mkdirs();
				
				if(!exist){
					file.createNewFile();
				}
			}else if(!exist){
				return false;
			}
			
			if(file.isDirectory()){
				return false;
			}
			if(addEol){
				con += System.lineSeparator();
			}
			os = new FileOutputStream(file, appendCon);
			out = new OutputStreamWriter(os, "UTF-8");
			writer = new BufferedWriter(out);
			writer.write(con);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return false;
	}
	
	public static ArrayList<String> getFileLines(String filePath, String separator){
    	ArrayList<String> result = new ArrayList<String>();
    	BufferedReader reader;
    	try {
    		StringBuilder builder = new StringBuilder();
    		String line;
    		//reader = new BufferedReader(new FileReader(filePath));
    		reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(new File(filePath))), "utf-8"), 1 * 1024 * 1024);
			while ((line = reader.readLine()) != null) {
			     if (line.trim().isEmpty() || line.trim().startsWith("--")) {
			         continue;
			     }
			     builder.append(line.trim().replace("\n", " ").replace("\r", " "));
			     if (separator == null || line.trim().endsWith(separator)) {
			         String text = builder.toString();
			         result.add(text);
			         //System.out.println(text);
			         builder.setLength(0);
			     }
			 }
			 reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e2){
			e2.printStackTrace();
		}
		return result;
	}
    
    public static File getFile(String filePath, boolean createNewFile, boolean clear){
		 File file = null;
		 try {
			if(filePath == null || filePath.trim().equals("")){
				 return null;
			}
			file = new File(filePath);
			if(file == null || !file.exists() || file.isDirectory()){
				if(!createNewFile){
					return null;
				}
				file.createNewFile();
				return file;
			}
			
			if(file != null && clear){
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write("");
				fileWriter.flush();
				fileWriter.close();
			}
			
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
    
    public static Properties getProperties(String filePath){
    	Properties props = new Properties();
        try(FileInputStream in = new FileInputStream(filePath)) {
            props.load(in);
            return props;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}
    
    public static String getProperties(String filePath, String key){
    	Properties props = getProperties(filePath);
    	if(props == null){
    		return null;
    	}
    	return props.getProperty(key);
	}

	public static <T> byte[] generateExcelBytes(List<T> demoList, Class<T> dataClass) throws IOException {
		if (demoList == null || demoList.size() == 0) {
			return null;
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		EasyExcel.write(outputStream, dataClass).sheet("Sheet1").doWrite(demoList);
		return outputStream.toByteArray();
	}

}
