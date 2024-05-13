
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tester.*;

import javalib.impworld.*;


import java.awt.Color;

import javalib.worldimages.*;

// the main make scene class
class Minesweeper extends World {

  int rows;

  int colums;

  int minecount;

  Random rand;

  ArrayList<ArrayList<Cell>> grid;

  ArrayList<Cell> minelist;

  Minesweeper(int rows, int colums, int minecount, Random rand) {
    this.rows = rows;
    this.colums = colums;
    this.minecount = minecount;
    this.rand = rand;
    this.grid = new ArrayList<ArrayList<Cell>>();
    this.minelist = new ArrayList<Cell>();

    makeGrid();
    makeNeighbors();
    mineMaking();
    makeScene();

  }

  Minesweeper(int rows, int colums, int minecount) {
    this.rows = rows;
    this.colums = colums;
    this.minecount = minecount;
    this.rand = new Random();
    this.grid = new ArrayList<ArrayList<Cell>>();
    this.minelist = new ArrayList<Cell>();

    makeGrid();
    makeNeighbors();
    mineMaking();
    makeScene();
  }

  // this method makes the grid a 2d array
  void makeGrid() {

    for (int y = 0; y < this.rows; y++) {
      ArrayList<Cell> rowOfCells = new ArrayList<Cell>();
      for (int x = 0; x < this.colums; x++) {
        rowOfCells.add(new Cell());
      }
      this.grid.add(rowOfCells);
    }

  }

  // makes all the cells in the the 2d array list a cell connected
  void makeNeighbors() {

    for (int r = 0; r < this.rows; r++) {
      for (int c = 0; c < this.colums; c++) {
        // left top-left top top-right right bottom-right bottom bottom-left

        Cell left;
        Cell topleft;
        Cell top;
        Cell topRight;
        Cell right;
        Cell bottomright;
        Cell bottom;
        Cell bottomleft;

        if (c == 0) {
          left = null;
        }
        else {
          left = this.grid.get(r).get(c - 1);
        }

        if (r == 0 || c == 0) {
          topleft = null;
        }
        else {
          topleft = this.grid.get(r - 1).get(c - 1);
        }

        if (r == 0) {
          top = null;
        }
        else {
          top = this.grid.get(r - 1).get(c);
        }

        if (r == 0 || c == this.colums - 1) {
          topRight = null;
        }
        else {
          topRight = this.grid.get(r - 1).get(c + 1);
        }

        if (c == this.colums - 1) {
          right = null;
        }
        else {
          right = this.grid.get(r).get(c + 1);
        }

        if (c == this.colums - 1 || r == this.rows - 1) {
          bottomright = null;
        }
        else {
          bottomright = this.grid.get(r + 1).get(c + 1);
        }

        if (r == this.rows - 1) {
          bottom = null;
        }
        else {
          bottom = this.grid.get(r + 1).get(c);
        }

        if (r == this.rows - 1 || c == 0) {
          bottomleft = null;
        }
        else {
          bottomleft = this.grid.get(r + 1).get(c - 1);
        }

        this.grid.get(r).get(c).makeNeighbous(new ArrayList<Cell>(
            Arrays.asList(left, topleft, top, topRight, right, bottomright, bottom, bottomleft)));

      }
    }
  }
  
  // makes the mine by randomly generating them
  void mineMaking() {
    
    ArrayList<Integer> oneDCoordinates = new ArrayList<Integer>();
    for (int i = 0; i < this.colums * this.rows; i++) {
      oneDCoordinates.add(i);
    }

    for (int i = 0; i < this.minecount; i++) {
      int coordinate = oneDCoordinates.remove(this.rand.nextInt(oneDCoordinates.size()));
      int r = coordinate % this.colums;
      int c = coordinate / this.colums;
      this.grid.get(r).get(c).addMine();
      this.minelist.add(this.grid.get(r).get(c));

    }
  }
  
  // makes the random seeded 
  void mineMakingSeeded() {
    
    ArrayList<Integer> oneDCoordinates = new ArrayList<Integer>();
    for (int i = 0; i < this.colums * this.rows; i++) {
      oneDCoordinates.add(i);
    }

    for (int i = 0; i < this.minecount; i++) {
      int coordinate = oneDCoordinates.remove(this.rand.nextInt(oneDCoordinates.size()));
      int r = coordinate % this.colums;
      int c = coordinate / this.colums;
      this.grid.get(r).get(c).addMine();
      this.minelist.add(this.grid.get(r).get(c));

    }

  }
  
  // makes the world scene
  public WorldScene makeScene() {
    
    int width = this.colums * 40;
    int height = this.rows * 40;
    WorldScene worldscene = new WorldScene(width, height);
    

    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.colums; j++) {
        worldscene.placeImageXY(this.grid.get(i).get(j).drawCell(), i * 40 + 20, j * 40 + 20);

      }
    }
    return worldscene;
  }

}

// the class for a cell
class Cell {

  ArrayList<Cell> neighbors;

  boolean hasMine;

  boolean opened;

  boolean hasFlag;

  Cell(ArrayList<Cell> neighbors, boolean hasMine, boolean opened, boolean hasFlag) {
    this.neighbors = null;
    this.hasMine = hasMine;
    this.hasFlag = hasFlag;
    this.opened = opened;
  }

  Cell() {
    // left top-left top top-right right bottom-right bottom bottom-left
    this.neighbors = new ArrayList<Cell>();
    this.hasMine = false;
    this.hasFlag = false;
    this.opened = false;
  }
  
  // make the neighbors the given neighbors
  void makeNeighbous(ArrayList<Cell> neigh) {
    
    this.neighbors = neigh;
    
  }
  
  // adds a mine there
  void addMine() {
    
    this.hasMine = true;
    
  }
  
  // opens the cell
  void openedCell() {
    
    this.opened = true;
    this.hasFlag = false;
    
  }
  
  // adds a flag there
  void flag() {
    
    this.hasFlag = true;
    
  }
  
  // counts the number of mines
  int numberOfMines() {
    
    int count = 0;
    for (int i = 0; i < this.neighbors.size(); i++) {
      Cell neighbor = this.neighbors.get(i);
      if (neighbor != null && neighbor.hasMine) {
        count++;
      }
    }
    return count;
  }

  // draws the cell in the game
  WorldImage drawCell() {
    WorldImage base = new RectangleImage(40, 40, OutlineMode.OUTLINE, Color.BLUE);
    WorldImage notOpened = new OverlayImage(base,
        new RectangleImage(40, 40, OutlineMode.SOLID, Color.orange));
    WorldImage opened = new OverlayImage(this.drawNumber(),
        (new OverlayImage(base, new RectangleImage(40, 40, OutlineMode.SOLID, Color.black))));
    WorldImage mine = new OverlayImage(new RectangleImage(10, 10, OutlineMode.SOLID, Color.RED),
        new OverlayImage(base, new RectangleImage(20, 20, OutlineMode.SOLID, Color.green)));
    WorldImage flag = new OverlayImage(
        new EquilateralTriangleImage(20, OutlineMode.SOLID, Color.green), notOpened);
    if (this.hasFlag) {
      return flag;
    }

    else if (this.opened) {
      if (this.hasMine) {
        return mine;
      }
      else {
        return opened;
      }

    }
    else {
      return notOpened;
    }
  }
  
  // draw the number of mines around the cell
  WorldImage drawNumber() {

    int mine = this.numberOfMines();

    ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.blue, Color.green,

        Color.red, Color.yellow, Color.orange, Color.magenta, Color.white, Color.pink));
    if (mine == 0) {
      return new TextImage("", Color.WHITE);
    }
    else {
      return new TextImage(Integer.toString(mine), 20, FontStyle.BOLD, colors.get(mine - 1));
    }
  }

}

// Examples class
class ExamplesMinesweeper {

  // Example mine sweeper
  Minesweeper mine;

  // Initialize or reset the game instances
  void reset() {
    // Initializes mine with a 3x3 grid and 3 mines, using a fixed seed for
    // reproducibility
    mine = new Minesweeper(3, 3, 3, new Random(24));
  }

  // Tests the makeGrid method
  void testMakeGrid(Tester t) {
    reset(); // Ensure the game is in a known state

    // Check if the grid has the correct number of rows
    t.checkExpect(mine.grid.size(), 3, "Check grid has 3 rows");

    // Test every row's size to make sure the grid is constructed correctly
    for (int i = 0; i < mine.grid.size(); i++) {
      t.checkExpect(mine.grid.get(i).size(), 3, "Row " + i + " has 3 columns");
    }
  }

  //counts the non-null neighbors of a cell
  int countNonNullNeighbors(Cell cell) {
    int count = 0;
    for (Cell neighbor : cell.neighbors) {
      if (neighbor != null) {
        count++;
      }
    }
    return count;
  }

  // tests the makeNeighbors method for a 3x3 grid
  void testMakeNeighbors(Tester t) {
    reset();

    // Check that corners have 3 neighbors
    t.checkExpect(countNonNullNeighbors(mine.grid.get(0).get(0)), 3);
    t.checkExpect(countNonNullNeighbors(mine.grid.get(0).get(2)), 3);
    t.checkExpect(countNonNullNeighbors(mine.grid.get(2).get(0)), 3);
    t.checkExpect(countNonNullNeighbors(mine.grid.get(2).get(2)), 3);

    // Check that edges have 5 neighbors
    t.checkExpect(countNonNullNeighbors(mine.grid.get(0).get(1)), 5);
    t.checkExpect(countNonNullNeighbors(mine.grid.get(1).get(0)), 5);
    t.checkExpect(countNonNullNeighbors(mine.grid.get(1).get(2)), 5);
    t.checkExpect(countNonNullNeighbors(mine.grid.get(2).get(1)), 5);

    // Check that middle has 8 neighbors
    t.checkExpect(countNonNullNeighbors(mine.grid.get(1).get(1)), 8);

  }

  //tests generateMinesSeeded method
  void testmineMakingSeeded(Tester t) {
    // resets values
    reset();

    // adds the cells in the grid that are mines to minesUseLoop
    ArrayList<Cell> minesUseLoop = new ArrayList<Cell>();
    for (int y = 0; y < mine.rows; y++) {
      for (int x = 0; x < mine.colums; x++) {
        if (mine.grid.get(y).get(x).hasMine) {
          minesUseLoop.add(mine.grid.get(y).get(x));
        }
      }
    }
    t.checkExpect(mine.minelist.contains(minesUseLoop.get(0)), true);
    t.checkExpect(mine.minelist.contains(minesUseLoop.get(1)), true);
    t.checkExpect(mine.minelist.contains(minesUseLoop.get(2)), true);

    t.checkExpect(minesUseLoop.size(), mine.minecount);
  }

  // Example Cells
  Cell c1;
  Cell c2;
  Cell c3;
  Cell c4;

  // Initializes Cells and their neighbors
  void initCells() {
    c1 = new Cell();
    c2 = new Cell();
    c3 = new Cell();
    c4 = new Cell();

    // Manually setting neighbors
    c1.makeNeighbous(new ArrayList<>(Arrays.asList(c2, c3, c4))); // Example setup
    c2.makeNeighbous(new ArrayList<>(Arrays.asList(c1, c3, c4)));
    c3.makeNeighbous(new ArrayList<>(Arrays.asList(c1, c2, c4)));
    c4.makeNeighbous(new ArrayList<>(Arrays.asList(c1, c2, c3)));

    c1.addMine(); // Let's say we add a mine to c1 and c4
    c4.addMine();

    c2.flag(); // Let's say we add a flag to c2 and c3
    c3.flag();
  }

  // Test the MakeNeighbours method
  void testMakeNeighbours(Tester t) {
    initCells();

    // Check if c1's neighbors are correctly assigned
    t.checkExpect(c1.neighbors.contains(c2), true, "c1 should have c2 as a neighbor");
    t.checkExpect(c1.neighbors.contains(c3), true, "c1 should have c3 as a neighbor");
    t.checkExpect(c1.neighbors.contains(c4), true, "c1 should have c4 as a neighbor");

    // Check for the correct number of neighbors
    t.checkExpect(c1.neighbors.size(), 3, "c1 should have 3 neighbors");
  }

  //Test mines are added correctly
  void testHasMine(Tester t) {
    initCells();
    t.checkExpect(c1.hasMine, true, "c1 should have a mine");
    t.checkExpect(c2.hasMine, false, "c2 should not have a mine");
    t.checkExpect(c3.hasMine, false, "c3 should not have a mine");
    t.checkExpect(c4.hasMine, true, "c4 should have a mine");
  }

  // Test flags are set correctly
  void testFlag(Tester t) {
    initCells();
    t.checkExpect(c2.hasFlag, true, "c2 should be flagged");
    t.checkExpect(c3.hasFlag, true, "c3 should be flagged");
    t.checkExpect(c1.hasFlag, false, "c1 should not be flagged");
  }

  void testnumberOfMines(Tester t) {
    initCells();
    t.checkExpect(c1.numberOfMines(), 1, "c1 should have 1 neighboring mine"); // c4 is the only
    // mine
    t.checkExpect(c2.numberOfMines(), 2, "c2 should have 2 neighboring mines"); // c1 and c4 are
    // mines
    t.checkExpect(c3.numberOfMines(), 2, "c3 should have 2 neighboring mines"); // c1 and c4 are
    // mines
    t.checkExpect(c4.numberOfMines(), 1, "c4 should have 1 neighboring mine");
  }

  void testDrawCell(Tester t) {
    // Initial Cell setup
    Cell cell = new Cell();

    // Test for a cell that has not been opened or flagged (default state)
    WorldImage notOpenedExpected = new OverlayImage(
        new RectangleImage(40, 40, OutlineMode.OUTLINE, Color.BLUE),
        new RectangleImage(40, 40, OutlineMode.SOLID, Color.ORANGE));
    t.checkExpect(cell.drawCell(), notOpenedExpected, "Test cell not opened or flagged");

    // Test for a cell that is flagged
    cell.flag(); // Assuming toggleFlag correctly toggles the hasFlag state
    WorldImage flaggedExpected = new OverlayImage(
        new EquilateralTriangleImage(10, OutlineMode.SOLID, Color.GREEN),
        new OverlayImage(new RectangleImage(40, 40, OutlineMode.OUTLINE, Color.BLUE),
            new RectangleImage(40, 40, OutlineMode.SOLID, Color.BLACK))); // Black or orange
    // depending on your
    // flagged cell appearance

    cell.flag(); // Unflag to test other states

    // Test for a cell that has been opened but doesn't contain a mine
    cell.openedCell(); // Assuming openedCell correctly changes the opened state without adding a
    // mine
    WorldImage openedExpected = new OverlayImage(
        new RectangleImage(40, 40, OutlineMode.OUTLINE, Color.BLUE),
        new RectangleImage(40, 40, OutlineMode.SOLID, Color.BLACK));


    // Test for a cell that has been opened and contains a mine
    Cell mineCell = new Cell();
    mineCell.addMine(); // Assuming addMine correctly marks the cell as having a mine
    mineCell.openedCell(); // Open cell with mine
    WorldImage mineExpected = new OverlayImage(
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.RED),
        new OverlayImage(new RectangleImage(40, 40, OutlineMode.OUTLINE, Color.BLUE),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN)));
    t.checkExpect(mineCell.drawCell(), mineExpected, "Test cell opened with mine");
  }

  void testDrawNumber(Tester t) {
    initCells();
    Cell cell = new Cell();

    // Test with no neighboring mines
    // Assuming a method or process to set the count directly or indirectly
    WorldImage noMinesExpected = new TextImage("", 13, FontStyle.REGULAR, Color.WHITE);
    t.checkExpect(cell.drawNumber(), noMinesExpected, "Test cell with no neighboring mines");

    // Array of colors corresponding to the number of mines
    ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.BLUE, Color.GREEN, Color.RED,
        Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.WHITE, Color.PINK));

    // Test with varying numbers of neighboring mines

    // Simulate i neighboring mines
    WorldImage expected = new TextImage(Integer.toString(1), 20, FontStyle.BOLD, colors.get(0));
    t.checkExpect(c4.drawNumber(), expected, "Test cell with " + 3 + " neighboring mines");

  }

  void testBigBang(Tester t) {
    // outputs the game board
    Minesweeper mine1 = new Minesweeper(6, 6, 5, new Random(24));
    // i am outputting every type of cell a mine ,a flag ,a number, a opened,a not
    // opened
    mine1.grid.get(1).get(1).flag();
    mine1.grid.get(2).get(2).addMine();
    mine1.grid.get(2).get(2).openedCell();
    mine1.grid.get(3).get(3).openedCell();
    mine1.grid.get(5).get(5).openedCell();
    mine1.grid.get(5).get(0).openedCell();
    mine1.bigBang(8 * 40 + 20, 8 * 40 + 20);
  }

}
