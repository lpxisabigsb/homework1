public class Main {
    public static void main(String[] args) {
        /********* begin *********/
        // 声明并实例化一Person对象p
Person p=new Person("张三","18");
        // 给p中的属性赋值
p.talk();
        // 调用Person类中的talk()方法

        /********* end *********/
    }
}

// 在这里定义Person类
class Person {
    /********* begin *********/
private String name;
private String age;

    public Person(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public  void talk(){
    System.out.println("我是："+this.name+"，今年："+this.age+"岁");
}
    /********* end *********/
}