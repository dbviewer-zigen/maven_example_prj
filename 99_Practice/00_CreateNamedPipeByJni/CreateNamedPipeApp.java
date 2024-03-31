public class CreateNamedPipeApp {
  static {
      System.loadLibrary("ccreatenamedpipe");
  }

  // Native method declaration
  public native boolean createNamedPipe(String pipeName);

  public static void main(String[] args) {

      if (args.length != 1) {
          System.out.println("Usage: java CreateNamedPipeApp <pipe_name>");
          return;
      }
      long start = System.currentTimeMillis();
        System.out.println("Named pipe created start.");
      try{
        String pipeName = args[0];
        CreateNamedPipeApp app = new CreateNamedPipeApp();
        boolean result = app.createNamedPipe(pipeName);
        if(result){
          System.out.println("Named pipe created successfully.");
        }else{
          System.out.println("Named pipe created failure.");
        }
      }catch(Exception e){
        System.out.println("Named pipe created failure. " + e.getMessage());
        e.printStackTrace();
      } finally{
        System.out.println("created Named pipe :" + (System.currentTimeMillis()-start) + "ms");
      }

    

  }
}
