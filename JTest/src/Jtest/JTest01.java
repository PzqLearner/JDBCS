package Jtest;
public class JTest01 {
    public static void main(String[] args) {
        //选择排序
        int[] a = {89, 45, 68, 90, 29, 34, 17};
        int min;
        for (int i = 0; i < a.length-1; i++) {
            min=i;
            for (int j = i; j < a.length; j++) {
                if (a[j]<a[min])
                    min=j;
            }
            int temp=a[i];
            a[i]=a[min];
            a[min]=temp;
        }
        System.out.println("选择排序结果：");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]+" ");
        }

        System.out.println(" ");
        //冒泡排序
        int[] b = {89, 45, 68, 90, 29, 34, 17};
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b.length-i-1; j++) {
                if (b[j]>b[j+1])
                {
                    int temp=b[j];
                    b[j]=b[j+1];
                    b[j+1]=temp;
                }
            }
        }
        System.out.println("冒泡排序结果：");
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }
}
