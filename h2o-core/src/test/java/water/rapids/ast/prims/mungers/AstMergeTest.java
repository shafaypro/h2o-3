package water.rapids.ast.prims.mungers;

import org.junit.BeforeClass;
import org.junit.Test;
import water.Scope;
import water.TestUtil;
import water.fvec.Frame;
import water.fvec.TestFrameBuilder;
import water.fvec.Vec;
import water.rapids.Rapids;
import water.rapids.Val;

/***
 * This test is written by Andrey Spiridonov in JIRA PUBDEV-5924.
 */
public class AstMergeTest extends TestUtil {

  @BeforeClass
  static public void setup() {
    stall_till_cloudsize(1);
  }

  @Test
  public void mergeWithNaOnTheRightMapsToEverythingTest() {
    Scope.enter();

    try {
      Frame fr = new TestFrameBuilder()
              .withName("leftFrame")
              .withColNames("ColA",  "ColB")
              .withVecTypes(Vec.T_CAT, Vec.T_NUM)
              .withDataForCol(0, ar("a", "b", "c", "e"))
              .withDataForCol(1, ar(1, 2, 3, 4))
              .build();
      Scope.track(fr);
      Frame holdoutEncodingMap = new TestFrameBuilder()
              .withName("holdoutEncodingMap")
              .withColNames( "ColA", "ColC")
              .withVecTypes(Vec.T_CAT, Vec.T_STR)
              .withDataForCol(0, ar(null, "c", "e"))
              .withDataForCol(1, ar("str42", "no", "yes"))
              .build();
      Frame answer = new TestFrameBuilder()
              .withColNames("ColA",  "ColB", "ColC")
              .withVecTypes(Vec.T_CAT, Vec.T_NUM, Vec.T_STR)
              .withDataForCol(0, ar("a", "b", "c", "e"))
              .withDataForCol(1, ar(1, 2, 3, 4))
              .withDataForCol(2, ar(null, null, "no", "yes"))
              .build();
      Scope.track(answer);
      Scope.track(holdoutEncodingMap);
      String tree = "(merge leftFrame holdoutEncodingMap TRUE FALSE [0.0] [0.0] 'auto')";
      Val val = Rapids.exec(tree);
      Frame result = val.getFrame();
      Scope.track(result);
      System.out.println("\n\nLeft frame: ");
      printFrames(fr);
      System.out.println("\n\nRight frame: ");
      printFrames(holdoutEncodingMap);
      System.out.println("\n\nMerged frame with command (merge leftFrame holdoutEncodingMap TRUE FALSE [0.0] [0.0]" +
              " 'auto'): ");
      printFrames(result);
      isBitIdentical(result, answer);
    } finally {
      Scope.exit();
    }
  }


  public void printFrames(Frame fr) {
    int numRows = (int) fr.numRows();
    int numCols = fr.numCols();
    String[] colTypes = fr.typesStr();
    for (String cname: fr.names()) {
      System.out.print(cname+"\t");
    }
    System.out.println("");
    for (int rindex = 0; rindex < numRows; rindex++) {
      for (int cindex = 0; cindex < numCols; cindex++) {
        if (colTypes[cindex].equals("Numeric"))
          System.out.print(fr.vec(cindex).at(rindex)+"\t\t");
        else
          System.out.print(fr.vec(cindex).stringAt(rindex)+"\t\t");
      }
      System.out.println("");
    }
  }
}
