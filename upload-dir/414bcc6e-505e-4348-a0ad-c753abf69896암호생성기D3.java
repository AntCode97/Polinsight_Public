package 팔월5;

import java.util.LinkedList;
import java.util.Scanner;

public class 암호생성기D3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        for(int t =1 ; t<=10; t++){
            int T = sc.nextInt();
            LinkedList<Integer> que = new LinkedList<>();
            for(int i=0;i<8;i++){
                que.add(sc.nextInt());
            }
            boolean cnt = true;
            while (cnt){
                for(int i=1; i<=5;i++){
                    Integer a = que.pollFirst();
                    if(a-i >0) que.add(a-i);
                    else {
                        cnt = false;
                        que.add(0);
                        break;
                    }
                }

            }
            System.out.print("#"+t+" ");
            for(int i=0;i<8;i++){
                System.out.print(que.get(i) + " ");
            }
            System.out.println();
        }
    }

}
