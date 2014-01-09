import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import java.io.File;
import java.io.IOException;
 
public class TestCopyFromLocal {
 
        public static void main (String[] args) throws IOException {
                String src = args[0];
                String dst = args[1];
                System.out.println("testCopyFromLocal, source= " + src);
                System.out.println("testCopyFromLocal, target= " + dst);
                Configuration conf = new Configuration();
                Path src1 = new Path(src);
                Path dst1 = new Path(dst);
                FileSystem fs = FileSystem.get(conf);
                try{
                   //delete local file after copy
                   fs.copyFromLocalFile(true, true, src1, dst1);
                }
                catch(IOException ex) {
                   System.err.println("IOException during copy operation " +
                                       ex.toString());
                   ex.printStackTrace();
                   System.exit(1);
                }
        }
}
