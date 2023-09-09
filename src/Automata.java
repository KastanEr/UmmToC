/* 엄랭 코드를 인식하는 오토마타 */
public class Automata {
    // 오토마타의 상태 정의
    public enum States {START, POS, NEG, MUL, CONSOLE, IN, D_OUT, C_OUT, F_LOAD, S_LOAD, T_LOAD, F_STORE, S_STORE, T_STORE}
    private States state_; // 현재 상태를 저장하는 변수
    private int countInt_; // 정수를 인식시 해당 정수를 저장하는 변수
    private boolean endFlag_; // 1개의 토큰 인식을 완료했는지 저장

    public Automata() {
        this.state_ = States.START;
        this.countInt_ = 0;
        this.endFlag_ = false;
    }

    public States getState() {
        return this.state_;
    }

    public int getCountInt() {
        return this.countInt_;
    }

    public boolean getEndFlag() {
        return this.endFlag_;
    }

    public void initAutomata() { // 오토마타를 초기화하는 함수
        this.state_ = States.START;
        this.countInt_ = 0;
        this.endFlag_ = false;
    }

    public void transfer(String input) { // 오토마타의 상태 전이 함수
        switch (this.state_) {
            case START -> {
                if (input.equals(".")) {
                    this.state_ = States.POS;
                    this.countInt_++; // 양의 정수를 인식한 경우 인식한 "."의 수만큼 countInt를 증가
                } else if (input.equals(",")) {
                    this.state_ = States.NEG;
                    this.countInt_++; // 음의 정수를 인식한 경우 인식한 ","의 수만큼 countInt를 증가
                } else if (input.equals(" ")) {
                    this.state_ = States.MUL;
                } else if (input.equals("어")) {
                    this.state_ = States.F_LOAD;
                } else if (input.equals("엄")) {
                    this.state_ = States.F_STORE;
                } else if (input.equals("식")) {
                    this.state_ = States.CONSOLE;
                } else {
                    this.endFlag_ = true;
                }
            }
            case POS -> {
                if (input.equals(".") && !this.endFlag_) {
                    countInt_++;
                } else {
                    this.endFlag_ = true;
                }
            }
            case NEG -> {
                if (input.equals(",") && !this.endFlag_) {
                    countInt_++;
                } else {
                    this.endFlag_ = true;
                }
            }
            case MUL, IN, D_OUT, C_OUT, T_LOAD, F_STORE, S_STORE, T_STORE -> this.endFlag_ = true;
            case CONSOLE -> {
                if (input.equals("?")) {
                    this.state_ = States.IN;
                } else if (input.equals("!")) {
                    this.state_ = States.D_OUT;
                } else if (input.equals("ㅋ")) {
                    this.state_ = States.C_OUT;
                }
            }
            case F_LOAD -> {
                if (input.equals("어")) {
                    this.state_ = States.S_LOAD;
                } else if (input.equals("엄")) {
                    this.state_ = States.S_STORE;
                } else {
                    this.endFlag_ = true;
                }
            }
            case S_LOAD -> {
                if (input.equals("어")) {
                    this.state_ = States.T_LOAD;
                } else if (input.equals("엄")) {
                    this.state_ = States.T_STORE;
                } else {
                    this.endFlag_ = true;
                }
            }
        }
    }
}
