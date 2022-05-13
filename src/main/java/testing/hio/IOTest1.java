package testing.hio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class IOTest1 {

    public static void main(String[] args) {

        String dir = "/Users/lihongming/Downloads";
        String filePath = String.format("%s/%s", dir, "1.txt");
        /**
         * 字符流写入
         */
        try {
            File file = new File(filePath);
//            String content = "字符流写入：";
            String content = "字符流写入：中";
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);

            /**
             *  将内容输出-强制性清空缓冲区中的内容
             */
//            fileWriter.flush();
            /**
             * 不关闭也可以
             */
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }


//        /**
//         * 字节流写入
//         */
//        try {
//            FileOutputStream out = new FileOutputStream(new File(filePath));
//            String content = "字节流写入：lihm";
//            byte[] bytes = content.getBytes("utf-8");
//            out.write(bytes);
////            out.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
