package bgu.spl.a2;

import java.util.List;

import junit.framework.TestCase;

/**
 * Created by Lenovo on 12/9/2017.
 */

 class transact extends Action{
     int money =0;
     public transact(int num){
         money =num;
    }
    protected  void start(){
        System.out.println("i moved some money: "+money);
    }
}


public class ActorThreadPoolTest extends TestCase {
    public void testSubmit2() throws Exception {
        ActorThreadPool pool = new ActorThreadPool(4);
System.out.println("test two: expect 0-6 10-16 20-26 30-34 45-46");
        transact a = new transact(0);
        transact b = new transact(1);
        transact c = new transact(2);
        transact d = new transact(3);
        transact e = new transact(4);
        transact f = new transact(5);
        transact g = new transact(6);
        a.setActionName("action0");
        b.setActionName("action1");
        c.setActionName("action2");
        d.setActionName("action3");
        e.setActionName("action4");
        f.setActionName("action5");
        g.setActionName("action6");
        transact aa = new transact(10);
        transact bb = new transact(11);
        transact cc = new transact(12);
        transact dd = new transact(13);
        transact ee = new transact(14);
        transact ff = new transact(15);
        transact gg = new transact(16);
        aa.setActionName("action00");
        bb.setActionName("action11");
        cc.setActionName("action22");
        dd.setActionName("action33");
        ee.setActionName("action44");
        ff.setActionName("action55");
        gg.setActionName("action66");
        pool.start();
        pool.submit(a, "actor1", new PrivateState() {});
        pool.submit(b, "actor1", new PrivateState() {});
        pool.submit(c, "actor1", new PrivateState() {});
        pool.submit(d, "actor1", new PrivateState() {});
        pool.submit(e, "actor1", new PrivateState() {});
        pool.submit(f, "actor1", new PrivateState() {});
        pool.submit(g, "actor1", new PrivateState() {});
        pool.submit(aa, "actor2", new PrivateState() {});
        pool.submit(bb, "actor2", new PrivateState() {});
        pool.submit(cc, "actor2", new PrivateState() {});
        pool.submit(dd, "actor2", new PrivateState() {});
        pool.submit(ee, "actor2", new PrivateState() {});
        pool.submit(ff, "actor2", new PrivateState() {});
        pool.submit(gg, "actor2", new PrivateState() {});
        transact aaa = new transact(20);
        transact bbb = new transact(21);
        transact ccc = new transact(22);
        transact ddd = new transact(23);
        transact eee = new transact(24);
        transact fff = new transact(25);
        transact ggg = new transact(26);
        a.setActionName("action20");
        b.setActionName("action21");
        c.setActionName("action22");
        d.setActionName("action23");
        e.setActionName("action24");
        f.setActionName("action25");
        g.setActionName("action26");
        transact aaaa = new transact(30);
        transact bbbb = new transact(31);
        transact cccc = new transact(32);
        transact dddd = new transact(33);
        transact eeee = new transact(34);
        transact ffff = new transact(45);
        transact gggg = new transact(46);
        aa.setActionName("action30");
        bb.setActionName("action31");
        cc.setActionName("action32");
        dd.setActionName("action33");
        ee.setActionName("action34");
        ff.setActionName("action35");
        gg.setActionName("action36");
        pool.submit(aaa, "actor3", new PrivateState() {});
        pool.submit(bbb, "actor3", new PrivateState() {});
        pool.submit(ccc, "actor3", new PrivateState() {});
        pool.submit(ddd, "actor3", new PrivateState() {});
        pool.submit(eee, "actor3", new PrivateState() {});
        pool.submit(fff, "actor3", new PrivateState() {});
        pool.submit(ggg, "actor3", new PrivateState() {});

        pool.submit(aaaa, "actor4", new PrivateState() {});//30
        pool.submit(bbbb, "actor4", new PrivateState() {});//31
        pool.submit(cccc, "actor4", new PrivateState() {});//32
        pool.submit(dddd, "actor4", new PrivateState() {});//33
        pool.submit(eeee, "actor4", new PrivateState() {});//34

        pool.submit(ffff, "actor5", new PrivateState() {});//45
        pool.submit(gggg, "actor5", new PrivateState() {});//46


        int i = 1;//debugger
        synchronized (this){
            this.wait(15000);
        }
        List<String> history = pool.getPrivateState("actor1").getLogger();
//        for (int j = 0; j < history.size(); j++) {
//			System.out.print("actor1 : "+history.get(j) + ", ");
//		}
        if (history == null) {
        	System.out.println("is null "); 
        }else {
        	System.out.println(history.size()); 
        }
               
    }

    public void testSubmit() throws Exception {
        ActorThreadPool pool = new ActorThreadPool(4);
        System.out.println("test one expect 10-16 in up order and expect 0-6 in up order: ");
        transact a = new transact(0);
        transact b = new transact(1);
        transact c = new transact(2);
        transact d = new transact(3);
        transact e = new transact(4);
        transact f = new transact(5);
        transact g = new transact(6);
        a.setActionName("action0");
        b.setActionName("action1");
        c.setActionName("action2");
        d.setActionName("action3");
        e.setActionName("action4");
        f.setActionName("action5");
        g.setActionName("action6");
        transact aa = new transact(10);
        transact bb = new transact(11);
        transact cc = new transact(12);
        transact dd = new transact(13);
        transact ee = new transact(14);
        transact ff = new transact(15);
        transact gg = new transact(16);
        aa.setActionName("action00");
        bb.setActionName("action11");
        cc.setActionName("action22");
        dd.setActionName("action33");
        ee.setActionName("action44");
        ff.setActionName("action55");
        gg.setActionName("action66");
        pool.start();
        pool.submit(a, "actor1", new PrivateState() {});
        pool.submit(b, "actor1", new PrivateState() {});
        pool.submit(c, "actor1", new PrivateState() {});
        pool.submit(d, "actor1", new PrivateState() {});
        pool.submit(e, "actor1", new PrivateState() {});
        pool.submit(f, "actor1", new PrivateState() {});
        pool.submit(g, "actor1", new PrivateState() {});
        pool.submit(aa, "actor2", new PrivateState() {});
        pool.submit(bb, "actor2", new PrivateState() {});
        pool.submit(cc, "actor2", new PrivateState() {});
        pool.submit(dd, "actor2", new PrivateState() {});
        pool.submit(ee, "actor2", new PrivateState() {});
        pool.submit(ff, "actor2", new PrivateState() {});
        pool.submit(gg, "actor2", new PrivateState() {});



        int i = 1;//debugger
        synchronized (this){
            this.wait(10000);
        }
    }

    public void testShutdown() throws Exception {
        ActorThreadPool pool = new ActorThreadPool(4);
        pool.start();
        transact a = new transact(0);
        transact b = new transact(1);
        transact c = new transact(2);
        transact d = new transact(3);
        transact e = new transact(4);
        transact f = new transact(5);
        transact g = new transact(6);
        a.setActionName("action0");
        b.setActionName("action1");
        c.setActionName("action2");
        d.setActionName("action3");
        e.setActionName("action4");
        f.setActionName("action5");
        g.setActionName("action6");
        transact aa = new transact(10);
        transact bb = new transact(11);
        transact cc = new transact(12);
        transact dd = new transact(13);
        transact ee = new transact(14);
        transact ff = new transact(15);
        transact gg = new transact(16);
        aa.setActionName("action00");
        bb.setActionName("action11");
        cc.setActionName("action22");
        dd.setActionName("action33");
        ee.setActionName("action44");
        ff.setActionName("action55");
        gg.setActionName("action66");
        pool.submit(a, "actor1", new PrivateState() {});
        pool.submit(b, "actor1", new PrivateState() {});
        pool.submit(c, "actor1", new PrivateState() {});
        pool.submit(d, "actor1", new PrivateState() {});
        pool.submit(e, "actor1", new PrivateState() {});
        pool.submit(f, "actor1", new PrivateState() {});
        pool.submit(g, "actor1", new PrivateState() {});
        pool.submit(aa, "actor2", new PrivateState() {});
        pool.submit(bb, "actor2", new PrivateState() {});
        pool.submit(cc, "actor2", new PrivateState() {});
        pool.submit(dd, "actor2", new PrivateState() {});
        synchronized(this){
            this.wait(1);
            pool.shutdown();
        }
        pool.submit(ee, "actor2", new PrivateState() {});
        pool.submit(ff, "actor2", new PrivateState() {});
        pool.submit(gg, "actor2", new PrivateState() {});


    }

    public void testStart() throws Exception {
        ActorThreadPool pool = new ActorThreadPool(5);
        pool.start();
        transact a = new transact(0);
        int i =1;//for debugger
    }

}