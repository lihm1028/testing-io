package testing.hio;

import java.io.*;

/**
 * ObjectOutputStream 对象流
 * java.io.ObjectOutputStream是实现序列化的关键类，它可以将一个对象转换成二进制流，然后可以通过ObjectInputStream将二进制流还原成对象。
 */
public class OOS {


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        testWriteObject();


        testReadObject();


    }


    private static void testReadObject() throws IOException, ClassNotFoundException {

        /**
         * 通过ObjectInputStream将二进制还原为对象
         */
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("oos.txt"));
        Person o = (Person) objectInputStream.readObject();

        PrintWriter printWriter = new PrintWriter(System.out);
        printWriter.println(o.name);
        printWriter.println(o.age);
        printWriter.close();
        objectInputStream.close();
    }

    private static void testWriteObject() throws IOException {

        /**
         * 通过ObjectOutputStream将对象转换为二进制流
         */
        FileOutputStream fileOutputStream = new FileOutputStream("oos.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
        Person person = new Person();
        person.age = 32;
        person.name = "lihm";
//        person.phone = "13788994275";
        oos.writeObject(person);
        oos.close();
        fileOutputStream.close();
    }

    public static class Person implements Serializable {

        private static final long serialVersionUID = 1694010737163439569L;


        private String name;

        private int age;

        private int sex;

//        private String phone;

    }

}
