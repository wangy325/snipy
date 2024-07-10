public class StaticBlock {
	
 static int x ; // x=0

/** 静态块只在类加载时调用一次 */
 static {
 	x += 1;
	System.out.println("静态代码块1运行, x= " +  x);	// x=1
 }

 static {
 	x+=2; 
	System.out.println("静态代码块2运行, x= " +  x); // x=3
 }
/** 构造器 */ 
 StaticBlock(){
 	x++;
	System.out.println("构造器运行, x= " +  x);
 }

 public static void main(String[] args) {
 	StaticBlock t = new StaticBlock(); // 调用一次构造器 x=4
	System.out.println("---------");
 	t =  new StaticBlock(); // 调用第二次构造器 x=5

 	System.out.println(x);
 }

}
/**
静态代码块1运行, x= 1
静态代码块2运行, x= 3
构造器运行, x= 4
---------
构造器运行, x= 5
5
 */