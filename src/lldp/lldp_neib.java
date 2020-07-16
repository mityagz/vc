package lldp;

/**
 * Created by mitya on 1/7/20.
 */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedEdge;
import org.snmp4j.*;
import edu.princeton.cs.*;
import datastruct.*;
import java.util.HashMap;

/*

adj{"ipadress of loopback"} -> Bag<DirectedEdge>;

8
15
4 5 0.35 Gi0/2 Gi0/1
5 4 0.35 Gi0/1 Gi0/2
4 7 0.37 Gi1/0/3 1/3
5 7 0.28 Gi2/0/3 Gi3/0/2
7 5 0.28 Gi3/0/2 Gi2/0/3
5 1 0.32 Gi3/4 G0/22
0 4 0.38 Gi5/7 Gi1/2/3
0 2 0.26 Gi4/7 Gi3/8
7 3 0.39 Gi2/1 Gi3/2
1 3 0.29 Gi3/4 Gi5/4
2 7 0.34 Gi2/1 Gi5/6
6 2 0.40 Gi1/2 Gi9/1
3 6 0.52 Gi5/4 Gi4/2
6 0 0.58 Gi2/3 Gi9/1
6 4 0.93 Gi3/5 Gi8/1


    0------>4<---0.35-->5-->1
  ^ |      ^|           ^ /
  | x    /  x          / /
  | 2---|-->7<---0.28 / /
  | ^  /    x          /
  \ 6<------3<---------

  pwd
/home/mitya/IdeaProjects/Sedgewick/out/production/Sedgewick
[mitya@samuray Sedgewick]$ java -cp .:/home/mitya/Downloads/Sedgewick/algs4.jar  algs.ch45.NEPaths NtinyEWD.txt 0 6
8 15
0: 0->2 0.26 Gi4/7 Gi3/8  0->4 0.38 Gi5/7 Gi1/2/3
1: 1->3 0.29 Gi3/4 Gi5/4
2: 2->7 0.34 Gi2/1 Gi5/6
3: 3->6 0.52 Gi5/4 Gi4/2
4: 4->7 0.37 Gi1/0/3 1/3  4->5 0.35 Gi0/2 Gi0/1
5: 5->1 0.32 Gi3/4 G0/22  5->7 0.28 Gi2/0/3 Gi3/0/2  5->4 0.35 Gi0/1 Gi0/2
6: 6->4 0.93 Gi3/5 Gi8/1  6->0 0.58 Gi2/3 Gi9/1  6->2 0.40 Gi1/2 Gi9/1
7: 7->3 0.39 Gi2/1 Gi3/2  7->5 0.28 Gi3/0/2 Gi2/0/3

------------------------
------------------------
0  -> 2 -> 7 -> 3 -> 6; 1.5100000000000002
0  -> 2 -> 7 -> 5 -> 1 -> 3 -> 6; 2.0100000000000002
0  -> 4 -> 7 -> 3 -> 6; 1.6600000000000001
0  -> 4 -> 7 -> 5 -> 1 -> 3 -> 6; 2.16
0  -> 4 -> 5 -> 1 -> 3 -> 6; 1.86
0  -> 4 -> 5 -> 7 -> 3 -> 6; 1.92
 0:ifOut:Gi4/7 2:ifIn:Gi3/8 2:ifOut:Gi2/1 7:ifIn:Gi5/6 7:ifOut:Gi2/1 3:ifIn:Gi3/2 3:ifOut:Gi5/4 6:ifIn:Gi4/2
 0:ifOut:Gi4/7 2:ifIn:Gi3/8 2:ifOut:Gi2/1 7:ifIn:Gi5/6 7:ifOut:Gi3/0/2 5:ifIn:Gi2/0/3 5:ifOut:Gi3/4 1:ifIn:G0/22 1:ifOut:Gi3/4 3:ifIn:Gi5/4 3:ifOut:Gi5/4 6:ifIn:Gi4/2
 0:ifOut:Gi5/7 4:ifIn:Gi1/2/3 4:ifOut:Gi1/0/3 7:ifIn:1/3 7:ifOut:Gi2/1 3:ifIn:Gi3/2 3:ifOut:Gi5/4 6:ifIn:Gi4/2
 0:ifOut:Gi5/7 4:ifIn:Gi1/2/3 4:ifOut:Gi1/0/3 7:ifIn:1/3 7:ifOut:Gi3/0/2 5:ifIn:Gi2/0/3 5:ifOut:Gi3/4 1:ifIn:G0/22 1:ifOut:Gi3/4 3:ifIn:Gi5/4 3:ifOut:Gi5/4 6:ifIn:Gi4/2
 0:ifOut:Gi5/7 4:ifIn:Gi1/2/3 4:ifOut:Gi0/2 5:ifIn:Gi0/1 5:ifOut:Gi3/4 1:ifIn:G0/22 1:ifOut:Gi3/4 3:ifIn:Gi5/4 3:ifOut:Gi5/4 6:ifIn:Gi4/2
 0:ifOut:Gi5/7 4:ifIn:Gi1/2/3 4:ifOut:Gi0/2 5:ifIn:Gi0/1 5:ifOut:Gi2/0/3 7:ifIn:Gi3/0/2 7:ifOut:Gi2/1 3:ifIn:Gi3/2 3:ifOut:Gi5/4 6:ifIn:Gi4/2


*/


public class lldp_neib {
    //Adj adj = new Adj();

    lldp_neib() {
        //adj.init();
    }
}
