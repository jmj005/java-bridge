package bridge;

import static bridge.Messages.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 다리 건너기 게임을 관리하는 클래스
 */

public class BridgeGame {

    public static int retryCount = 1;
    public static boolean gameFlag = false;

    InputView inputView = new InputView();
    OutputView outputView = new OutputView();
    BridgeNumberGenerator numberGenerator = new BridgeRandomNumberGenerator();
    BridgeMaker bridgeMaker = new BridgeMaker(numberGenerator);

    public void run(int size) {
        List<String> upSideDownSideMove;
        List<String> bridge = bridgeMaker.makeBridge(size);
        do {
            upSideDownSideMove = move(bridge);
            if (!isSuccess(upSideDownSideMove)) {
                retry();
            }
        } while (!gameFlag);
        outputView.printResult(upSideDownSideMove);
    }

    /**
     * 사용자가 칸을 이동할 때 사용하는 메서드
     * <p>
     * 이동을 위해 필요한 메서드의 반환 타입(return type), 인자(parameter)는 자유롭게 추가하거나 변경할 수 있다.
     */
    public List<String> move(List<String> bridge) {
        List<String> upSideDownSideMove = new ArrayList<>(List.of("", ""));
        for (String bridgeElement : bridge) {
            CompareBridge playerMove = new CompareBridge(inputView.readMoving());
            upSideDownSideMove.set(0, upSideDownSideMove.get(0)
                    + playerMove.getUpSideMove(playerMove, bridgeElement).toString());
            upSideDownSideMove.set(1, upSideDownSideMove.get(1)
                    + playerMove.getDownSideMove(playerMove, bridgeElement).toString());
            outputView.printMap(upSideDownSideMove);
            if (!isSuccess(upSideDownSideMove)) {
                break;
            }
        }
        return upSideDownSideMove;
    }

    public boolean isSuccess(List<String> upSideDownSideMove) {
        if (upSideDownSideMove.stream().anyMatch(move -> move.contains(INCORRECT))) {
            gameFlag = false;
            return false;
        }
        gameFlag = true;
        return true;
    }

    /**
     * 사용자가 게임을 다시 시도할 때 사용하는 메서드
     * <p>
     * 재시작을 위해 필요한 메서드의 반환 타입(return type), 인자(parameter)는 자유롭게 추가하거나 변경할 수 있다.
     */
    public void retry() {
        String gameCommand = inputView.readGameCommand();
        if (gameCommand.equals(RETRY)) {
            retryCount += 1;
            gameFlag = false;
        }
        if (gameCommand.equals(QUIT)) {
            gameFlag = true;
        }
    }
}
