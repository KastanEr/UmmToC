import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        /* 메인함수는 변환에 필요한 객체들을 생성하고, 이 객체들을 통해서 변환할 파일을 한줄씩 읽어 변환을 수행함 */
        Converter converter = new Converter(); // 엄랭을 C 언어로 바꾸는 역할을 수행하는 객체
        Exporter exporter = new Exporter(); // 변환이 완료된 텍스트를 test.c 파일로 내보내는 객체
        StringBuilder content = new StringBuilder(); // 변환된 텍스트를 저장
        BufferedReader br = new BufferedReader(new FileReader("./test/test6.umm")); // 현재 디렉토리에 있는 엄랭 파일을 열음

        while(true) {
            String line = br.readLine(); // 엄랭의 한줄을 읽음
            if (line==null) break; // 다 읽었을 경우 루프 탈출
            if (line.equals("어떻게") || line.equals("이 사람이름이냐ᄏᄏ")) continue; // 모든 엄랭파일에 기본으로 존재하는 부분은 건너뜀
            content.append(converter.convert(line)); // Convert 객체의 convert 함수를 통해 변환한 c 코드를 추가
        }
        br.close(); // 파일 닫기

        exporter.setContent(content.toString()); // 실제로 내보낼 텍스트 업데이트
        exporter.createFile(); // c 파일 생성
    }
}