import java.io.FileWriter;
import java.io.IOException;

/* content_ 필드에 저장된 문자열을 내보내 c 파일을 생성하는 객체 */
public class Exporter {
    private String content_; // 내보낼 문자열

    public void setContent(String newContent) { // 문자열 업데이트
        this.content_ = newContent;
    }

    public void createFile() throws IOException { // content_의 내용을 c 파일로 내보내는 함수
        FileWriter fw = new FileWriter("./test.c");
        fw.write("#include <stdio.h>\nint main() {\n");
        fw.write(content_);
        fw.write("return 0;\n}");
        fw.close();
    }
}
