package testing.hio;

import java.io.*;

public class IOTest2 {
    public static void main(String[] args) {

        String dir = "/Users/lihongming/Downloads";
        String filePath = String.format("%s/%s", dir, "1.txt");


//        /**
//         * 字符流读取1
//         */
//        try {
////            InputStreamReader fileReader = new InputStreamReader(new FileInputStream(new File(filePath)));
////            BufferedReader reader = new BufferedReader(fileReader);
//            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        /**
//         * 字符流读取2
//         */
//        try {
////            InputStreamReader fileReader = new InputStreamReader(new FileInputStream(new File(filePath)));
////            BufferedReader reader = new BufferedReader(fileReader);
//            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
//            int ch;
//            while ((ch = reader.read()) != -1) {
//                System.out.println("\nInteger value "
//                        + "of character read: "
//                        + ch);
//                System.out.println("Actual "
//                        + "character read: "
//                        + (char)ch);
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



//        /**
//         * 字节流读取
//         */
//        long start1 = System.currentTimeMillis();
//        try {
//            InputStream stream = new FileInputStream(new File(filePath));
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            byte[] read = new byte[2];
//            int len = 0;
//            while ((len = stream.read(read)) != -1) {
//                out.write(read, 0, len);
//                System.out.println(out.toString());
//            }
//
//            stream.close();
//            out.close();
//            System.out.println(out.toString());
//            long end1 = System.currentTimeMillis();
//            System.out.println((end1 - start1) + "毫秒");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
        /**
         * 字节流读取2
         * BufferedInputStream有输入缓冲区 读取比较快
         */
        long start2 = System.currentTimeMillis();
        try {
            FileInputStream fileOutputStream = new FileInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileOutputStream);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            //从文件中按字节读取内容，到文件尾部时read方法将返回-1
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {

                //将读取的字节转为字符串对象
                String chunk = new String(buffer, 0, bytesRead);
                System.out.println(chunk);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
