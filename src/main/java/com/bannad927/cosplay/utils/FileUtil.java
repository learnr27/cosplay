package com.bannad927.cosplay.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

/**
 * @author 程彬彬
 */
@Slf4j
public class FileUtil {

    public static void main(String[] args) throws Exception {
       // createFolder(new File("D:\\cosplay\\!@#$$%)(**&^%%$#+_"));
   long startTime= System.currentTimeMillis();
        log.info("start");
        List<File> result=    getFileSort("D:\\\\cosplay");
        long endTime = System.currentTimeMillis();
        log.info("end {}",(endTime-startTime)/1000);
    }

    public List<Map<String, Object>> getFileList(String projectPath) {
        List<Map<String, Object>> list = new ArrayList<>();
        String path = projectPath + "logs/Android/";
        File f = new File(path);
        if (!f.exists()) {
            return list;
        }
        File[] fa = f.listFiles();
        if (null != fa) {
            for (int i = 0; i < fa.length; i++) {
                File fs = fa[i];
                if (!fs.isDirectory()) {
                    Map<String, Object> map = new HashMap<>(16);
                    map.put("logName", fs.getName());
                    map.put("logPath", fs.getAbsoluteFile());
                    list.add(map);
                }
            }
        }
        return list;
    }


    /**
     * 获取目录下所有文件(按时间排序)
     *
     * @param path
     * @return
     */
    public static List<File> getFileSort(String path) {
        List<File> list = getFiles(path, new ArrayList<>());
        if (list != null && list.size() > 0) {
            Collections.sort(list, (file, newFile) -> {
                if (file.lastModified() > newFile.lastModified()) {
                    return 1;
                } else if (file.lastModified() == newFile.lastModified()) {
                    return 0;
                } else {
                    return -1;
                }
            });
        }
        return list;
    }

    /**
     * 获取目录下所有文件
     *
     * @param realPath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realPath, List<File> files) {
        File realFile = new File(realPath);
        if (realFile.isDirectory()) {
            File[] subFiles = realFile.listFiles();
            for (File file : subFiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * 创建文件夹
     *
     * @param file
     */
    public static void createFolder(File file) {
        if (!file.exists()) {
            file.mkdir();
        } else {
            log.info("{},folder exist", file.getName());
        }
    }


    /**
     * 通过BufferedReader读取文件
     *
     * @param sourceFile
     * @return
     * @throws IOException
     */
    public static String readFile(File sourceFile) throws IOException {
        if (!sourceFile.exists()) {
            log.error("file not exist,fileName:{}", sourceFile.getName());
            return null;
        }
        if (sourceFile.length() != 0) {
            FileInputStream fis = new FileInputStream(sourceFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            String LINE_SEPARATOR = System.lineSeparator();
            StringBuffer sbf = new StringBuffer();
            while (line != null) {
                sbf.append(line).append(LINE_SEPARATOR);
                line = br.readLine();
            }
            br.close();
            fis.close();
            return sbf.toString();
        }
        return "";
    }

    /**
     * 移动指定的文件（夹）到目标文件（夹）
     *
     * @param source   源文件（夹）
     * @param target   目标文件（夹）
     * @param isFolder 若为文件夹，则为True；反之为False
     * @return
     * @throws Exception
     */
    public static boolean move(String source, String target, boolean isFolder)
            throws Exception {
        copy(source, target, isFolder);
        if (isFolder) {
            return delete(source);
        } else {
            return deleteFile(source);
        }
    }

    /**
     * 删除文件,如果是目录则失败
     *
     * @param file
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                return false;
            }
            file.delete();
            return true;
        } else {
            log.error("file not exist,fileName:{}", file.getName());
            return false;
        }
    }

    /**
     * 删除文件,如果是目录则失败
     *
     * @param fileName
     */
    public static boolean deleteFile(String fileName) {
        if (fileName == null || fileName.trim().length() == 0) {
            return false;
        }
        File file = new File(fileName);
        return deleteFile(file);
    }

    /**
     * 删除文件或文件夹，如果是文件夹则删除目录下所有文件，包括子文件
     *
     * @param file
     */
    public static boolean delete(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
            return true;
        } else {
            log.error("file not exist,fileName:{}", file.getName());
            return false;
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param fileName
     */
    public static boolean delete(String fileName) {
        if (fileName == null || fileName.trim().length() == 0) {
            return false;
        }
        File file = new File(fileName);
        return delete(file);
    }


    /**
     * 复制文件/文件夹 若要进行文件夹复制，请勿将目标文件夹置于源文件夹中
     *
     * @param source   源文件（夹）
     * @param target   目标文件（夹）
     * @param isFolder 若进行文件夹复制，则为True；反之为False
     * @throws Exception
     */
    public static void copy(String source, String target, boolean isFolder)
            throws Exception {
        if (isFolder) {
            (new File(target)).mkdirs();
            File a = new File(source);
            String[] file = a.list();
            File temp;
            for (int i = 0; i < file.length; i++) {
                if (source.endsWith(File.separator)) {
                    temp = new File(source + file[i]);
                } else {
                    temp = new File(source + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(target + "/"
                            + (temp.getName()).toString());
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copy(source + "/" + file[i], target + "/" + file[i], true);
                }
            }
        } else {
            int byteRead;
            File oldFile = new File(source);
            if (oldFile.exists()) {
                InputStream inStream = new FileInputStream(source);
                File file = new File(target);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fs = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
                fs.close();
            }
        }
    }


}
