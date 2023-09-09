import java.util.ArrayList;

/* 엄랭 코드를 c 코드로 변환하는 객체 */
// [Task 01] Additional implementation
// 변수와 임의의 정수 사이의 +, -, * 연산 구현, 변수와 변수 사의의 * 연산 구현
// 오토마타를 통해 엄랭 코드의 토큰을 하나하나 인식하므로 인식한 토큰 중 토큰의 순서가 변수 호출 -> (곱연산) -> 임의의 정수일 경우 변수와 임의의 정수
// 사이의 +, -, * 연산이 정상적으로 변환됨. 또는 인식한 토큰 중 토큰의 순서가 변수 호출 -> 곱연산 -> 변수 호출일 경우에도 변수간의 곱연산이 정상적으로 변환됨
public class Converter {
    private final boolean[] variableUsed_ = {false, false, false}; // 특정 변수가 선언되었는지 저장하는 배열
    private final Automata automata_; // 엄랭 코드의 한 토큰을 인식하는 오토마타
    private final ArrayList<String> tokens_; // 토큰을 저장하는 리스트

    public Converter() {
        automata_ = new Automata();
        tokens_ = new ArrayList<>();
    }

    public String convert(String line) { // 엄랭 코드 한줄을 받아서 c 코드로 변환을 수행하는 함수
        tokens_.clear(); // 토큰배열 초기화
        StringBuilder output = new StringBuilder(); // 최종적으로 변환된 c 코드를 저장하는 변수

        tokenize(line); // 엄랭 코드 한줄에서 토큰 1개를 인식

        if(tokens_.contains("scan")) { // 해당 라인이 콘솔 입력을 포함하고 있을 경우
            switch (tokens_.get(0)) {
                case "first =" -> { // 콘솔 입력을 첫번째 변수에 저장할 경우
                    if (!variableUsed_[0]) { // 첫번째 변수가 이전에 선언되었는지 검사하여 선언되지 않았을 경우 해당 변수를 선언하는 코드 추가
                        output.append("int first;\n");
                        variableUsed_[0] = true; // 첫번째 변수를 선언했음을 저장
                    }
                    output.append("scanf(\"%d\", &first);");
                }
                case "second =" -> { // 콘솔 입력을 두번째 변수에 저장할 경우
                    if (!variableUsed_[1]) { // 두번째 변수가 이전에 선언되었는지 검사하여 선언되지 않았을 경우 해당 변수를 선언하는 코드 추가
                        output.append("int second;\n");
                        variableUsed_[1] = true; // 두번째 변수를 선언했음을 저장
                    }
                    output.append("scanf(\"%d\", &second);");
                }
                case "third =" -> { // 콘솔 입력을 세번째 변수에 저장할 경우
                    if (!variableUsed_[2]) { // 세번째 변수가 이전에 선언되었는지 검사하여 선언되지 않았을 경우 해당 변수를 선언하는 코드 추가
                        output.append("int third;\n");
                        variableUsed_[2] = true; // 세번째 변수를 선언했음을 저장
                    }
                    output.append("scanf(\"%d\", &second);");
                }
            }
        }
        else { // 콘솔 입력을 받지 않는 경우
            for(String token: tokens_) {
                switch (token) {
                    case "first =" -> { // 첫번째 변수에 대입하는 경우
                        if (!variableUsed_[0]) { // 첫번째 변수가 이전에 선언되었는지 검사하여 선언되지 않았을 경우 해당 변수를 선언하는 코드 추가
                            output.append("int first;\n");
                            variableUsed_[0] = true; // 첫번째 변수를 선언했음을 저장
                        }
                    }
                    case "second =" -> { // 두번째 변수에 대입하는 경우
                        if (!variableUsed_[1]) { // 두번째 변수가 이전에 선언되었는지 검사하여 선언되지 않았을 경우 해당 변수를 선언하는 코드 추가
                            output.append("int second;\n");
                            variableUsed_[1] = true; // 두번째 변수를 선언했음을 저장
                        }
                    }
                    case "third =" -> { // 세번째 변수에 대입하는 경우
                        if (!variableUsed_[2]) { // 세번째 변수가 이전에 선언되었는지 검사하여 선언되지 않았을 경우 해당 변수를 선언하는 코드 추가
                            output.append("int third;\n");
                            variableUsed_[2] = true; // 세번째 변수를 선언했음을 저장
                        }
                    }
                }
                output.append(token); // 해당 토큰 추가
                output.append(" "); // 토큰 사이에 공백 삽입
            }
        }
        output = new StringBuilder(output.substring(0, output.length() - 1)); // 마지막 토큰 뒤의 공백 제거
        if(output.toString().contains("printf(\"%d\",") || output.toString().contains("printf(\"%c\",")) { // 출력문을 포함할 경우
            output.append(")"); // 출력문의 끝부분에 닫는 괄호 추가
        }
        output.append(";\n"); // 세미콜론과 줄바꿈 추가
        return output.toString(); // 변환된 c 코드 리턴
    }

    private void tokenize(String line) { // 엄랭 코드 한줄을 입력받아 1개의 토큰을 인식하는 함수
        automata_.initAutomata(); // 오토마타 초기화
        for(int i = 0; i < line.length(); i++) {
            if(automata_.getEndFlag()) { // 한개의 토큰을 인식한 경우
                break;
            }
            automata_.transfer(line.substring(i, i+1)); // 입력받은 라인의 한글자씩을 입력하여 상태전이 수행
        }

        Automata.States currentState = automata_.getState(); // 오토마타의 현재 상태
        switch (currentState) {
            case START, CONSOLE -> { // 하나의 토큰도 인식하지 못한 경우
            }
            case POS -> { // 양의 정수 토큰을 인식한 경우
                tokens_.add("+ " + automata_.getCountInt());
                if (line.substring(automata_.getCountInt()).length() != 0) { // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
                    tokenize(line.substring(automata_.getCountInt()));
                }
            }
            case NEG -> { // 음의 정수 토큰을 인식한 경우
                tokens_.add("- " + automata_.getCountInt());
                if (line.substring(automata_.getCountInt()).length() != 0) { // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
                    tokenize(line.substring(automata_.getCountInt()));
                }
            }
            case MUL -> { // 곱셈 연산자를 인식한 경우
                tokens_.add("*");
                if (line.substring(1).length() != 0) { // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
                    tokenize(line.substring(1));
                }
            }
            case F_LOAD -> { // 첫번째 변수를 호출하는 토큰을 인식한 경우
                tokens_.add("first");
                if (line.substring(1).length() != 0) { // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
                    tokenize(line.substring(1));
                }
            }
            case S_LOAD -> { // 두번째 변수를 호출하는 토큰을 인식한 경우
                tokens_.add("second");
                if (line.substring(1).length() != 0) { // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
                    tokenize(line.substring(1));
                }
            }
            case T_LOAD -> { // 세번째 변수를 호출하는 토큰을 인식한 경우
                tokens_.add("third");
                if (line.substring(1).length() != 0) { // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
                    tokenize(line.substring(1));
                }
            }
            case F_STORE -> { // 첫번째 변수에 대입하는 토큰을 인식한 경우
                tokens_.add("first =");
                if (line.substring(1).length() != 0) { // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
                    tokenize(line.substring(1));
                }
            }
            case S_STORE -> { // 두번째 변수에 대입하는 토큰을 인식한 경우
                tokens_.add("second =");
                if (line.substring(1).length() != 0) { // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
                    tokenize(line.substring(2));
                }
            }
            case T_STORE -> { // 세번째 변수에 대입하는 토큰을 인식한 경우
                tokens_.add("third =");
                if (line.substring(1).length() != 0) { // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
                    tokenize(line.substring(3));
                }
            }
            case IN -> tokens_.add("scan"); // 콘솔 입력을 인식한 경우
            case D_OUT -> { // 정수의 출력을 인식한 경우
                tokens_.add("printf(\"%d\",");
                tokenize(line.substring(1, line.length() - 1)); // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
            }
            case C_OUT -> { // 문자의 출력을 인식한 경우
                tokens_.add("printf(\"%c\",");
                tokenize(line.substring(1, line.length() - 1)); // 인식한 토큰을 제외한 나머지 부분에서 토큰화 수행
            }
        }
    }
}
