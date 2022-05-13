package testing.hio.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class FileChannelT1 {

    public static void main(String[] args) throws IOException {

//        testWrite();

//        testRead1();

//        testRead2();

//        testRead3();


//        testRead4();


        testRead5();


    }

    private static void testRead5() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("t1.txt")));
        CharBuffer charBuffer = CharBuffer.allocate(4);
        int len;
        while ((len = bufferedReader.read(charBuffer)) != -1) {
            charBuffer.flip();
            char[] chars = new char[len];
            charBuffer.get(chars, 0, len);
            for (char c : chars) {
                System.out.print(c);
            }
        }
    }

    private static void testRead4() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("t1.txt")));
        int len;
        while ((len = bufferedReader.read()) != -1) {
            System.out.print((char) len);
        }
    }

    private static void testRead3() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("t1.txt")));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.print(line);
        }
    }

    private static void testRead2() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("t1.txt"));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = bufferedInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        System.out.println(byteArrayOutputStream.toString("utf-8"));
    }

    private static void testRead1() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("t1.txt");
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int len;
        while ((len = fileChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            byte[] bytes = new byte[len];
            byteBuffer.get(bytes, 0, len);
            bos.write(bytes);
        }

        String content = bos.toString("UTF-8");
        System.out.println(content);
    }

    private static void testWrite() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("t1.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();
//        fileChannel.write(ByteBuffer.wrap("abcde测试写入".getBytes("UTF-8")));

        String content = "abcde测试写入";
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(content.getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileChannel.close();
    }
}
