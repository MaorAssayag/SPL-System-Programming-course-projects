package bgu.spl.a2;



   import java.util.ArrayList;
   import java.util.List;
   import java.util.concurrent.CountDownLatch;


class confirmation extends Action{
    String clientA;
    String clientB;
    String bankB;
    PrivateState bankState;

    public confirmation(String clientA,String clientB,String bankB,PrivateState bankState){
        this.clientA = clientA;
        this.clientB =clientB;
        this.bankB =bankB;
        this.bankState =bankState;
        Result = new Promise<Boolean>();
    }
    @Override
    protected void start() {
        Result.resolve(true);//just for test lets say the other bank always approve the transaction
   //     System.out.println("start conf");
    }
}



class Transmission extends Action{
    int amount;
    String clientA;
    String clientB;
    String bankA;
    String bankB;
    PrivateState bankState;
    VersionMonitor vm ;
    public Transmission(String name, int amount,String clientA,String clientB,String bankA,String bankB,PrivateState bankState){
        this.amount = amount;
        this.clientA =clientA;
        this.clientB = clientB;
        this.bankA =bankA;
        this.bankB =bankB;
        this.bankState =bankState;
        vm = new VersionMonitor();
        this.Result = new Promise<String>();
        this.actionName = name;
    }

    public VersionMonitor getVm(){
        return vm;
    }
    protected void start(){
        System.out.println("Start Transmission");

        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> confAction1 = new confirmation(clientA,clientB,bankB,new PrivateState() {});
        Action<Boolean> confAction = new confirmation(clientA,clientB,bankB,new PrivateState() {});
        actions.add(confAction);
        actions.add(confAction1);
        sendMessage(confAction1, bankB, new PrivateState() {});
        sendMessage(confAction, bankB, new PrivateState() {});
        then(actions,()->{
            Boolean Result = actions.get(0).getResult().get();

            if(Result==true){
                complete("transmission good");
                System.out.println("transmission good");
            }
            else{
                complete("transmission bad");
                System.out.println("transmission bad");
            }
        });


    }

}

public class BankTest {

    public static void main(String[] args) throws InterruptedException {
        for (int n = 0; n < 10000; n++) {
            System.out.println("=================================="+n);
            ActorThreadPool pool = new ActorThreadPool(3);
            Action<String> trans = new Transmission("0",100, "A", "B", "bank1", "bank2", new PrivateState() {
            });
            Action<String> trans1 = new Transmission("1",100, "B", "A", "bank2", "bank1", new PrivateState() {
            });
            Action<String> trans2 = new Transmission("2",100, "A", "B", "bank1", "bank3", new PrivateState() {
            });
            Action<String> trans3 = new Transmission("3",100, "A", "B", "bank3", "bank2", new PrivateState() {
            });
            Action<String> trans4 = new Transmission("4",100, "C", "B", "bank2", "bank3", new PrivateState() {
            });
            Action<String> trans5 = new Transmission("5",100, "A", "B", "bank1", "bank2", new PrivateState() {
            });



            CountDownLatch l = new CountDownLatch(6);
            trans.getResult().subscribe(() -> l.countDown());
            trans1.getResult().subscribe(() -> l.countDown());
            trans2.getResult().subscribe(() -> l.countDown());
            trans3.getResult().subscribe(() -> l.countDown());
            trans4.getResult().subscribe(() -> l.countDown());
            trans5.getResult().subscribe(() -> l.countDown());

            pool.submit(trans, "bank1", new PrivateState() {
            });


            Object lock = new Object();
            pool.start();
            pool.submit(trans2, "bank1", new PrivateState() {
            });

            pool.submit(trans3, "bank3", new PrivateState() {
            });


            pool.submit(trans4, "bank2", new PrivateState() {
            });
            pool.submit(trans5, "bank1", new PrivateState() {
            });

            pool.submit(trans1, "bank2", new PrivateState() {
            });

            try {
                l.await();
            } catch (InterruptedException e) {
            }
            pool.shutdown();
        }
    }
}