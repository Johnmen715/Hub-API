package Utilities.TestNg.Reporting;

import Utilities.Base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

public class MoveFile {

    public static void listFiles(String loc, String extn) {

        Base base = new Base();

        base.Wait(2000);

        SearchFiles files = new SearchFiles(extn);

        File folder = new File(loc);

        if (folder.isDirectory() == false) {
            System.out.println("Folder does not exists: " + loc);
            return;
        }

        String[] list = folder.list(files);

        if (list.length == 0) {
            System.out.println("There are no files with " + extn + " Extension");
            return;
        }

        for (String file : list) {
            String temp = new StringBuffer(loc).append(File.separator)
                    .append(file).toString();
            System.out.println("file : " + temp);
            try {
                File f = new File(temp);
                System.out.println(f.getName());

                String imgRootFolder = getfolder("\\\\QA_MITUL\\extentx\\.tmp\\public\\uploads");
                System.out.println("imgRootFolder " + imgRootFolder);
                String imgCurrentFolder = getfolder(imgRootFolder);
                System.out.println("imgCurrentFolder " + imgCurrentFolder);

                Files.move
                        (Paths.get(temp),
                                Paths.get(imgCurrentFolder + "\\" + "1.png"));
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getfolder(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        File lastModified = Arrays.stream(files).filter(File::isDirectory).max(Comparator.comparing(File::lastModified)).orElse(null);
        return String.valueOf(lastModified);
    }
}
