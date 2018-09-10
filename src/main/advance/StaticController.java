package advance;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
public class StaticController extends Controller {
    private String root;
    public StaticController(String dir){
        this.root = dir;
    }
    public void get() throws IOException {
        URI url = rawExchange.getRequestURI();
        String path = uri.getPath();
        File file = new File(this.root + path);
        if(file.isFile()){
            String mime = File.probeContentType(this.root + path);
            super.overrideWrite = true;
            super.overrideHeaders = true;
            super.overrideClose = true;
            super.headerEdits.put("Content-Type", mime);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[0x10000];
            int i = 0;
            while((i = fis.read(buffer)) >= 0){
                super.res.write(buffer, 0, i);
            }
            fis.close();
            super.res.close();
        }else{
            super.response = "404 (Not Found)\n".getBytes();
            super.responseCode = 404;
        }
    }
}