import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {    
    // count elems in one row or column
    private int gridSize;
    
    // array with cells states (true - open, false - blocked)
    private boolean[] cellsStates;

    // count of opened sites 
    private int countOpenedSites;
    
    // disjoint-sets data type object
    private WeightedQuickUnionUF cellsInfo;
    
    // saves last perlocated state, returned in percolates()
    private boolean perlocateState;
        
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        
        gridSize = n;
        
        // gridSize*gridSize - for virtual top cell
        // gridSize*gridSize + 1 - for virtual bottom cell
        
        cellsInfo = new WeightedQuickUnionUF(gridSize*gridSize + 2);
               
        cellsStates = new boolean[gridSize*gridSize + 2];       
        for (int i = 0; i < gridSize*gridSize; ++i)
           cellsStates[i] = false;
       
        cellsStates[gridSize*gridSize] = true;
        cellsStates[gridSize*gridSize + 1] = true;
        
        countOpenedSites = 0;
        perlocateState = false;
    }

    // open site (row, col) if it is not open already
    public void open(int i, int j) {
        int cellIndex = getCellIndex(i, j);
        if (cellsStates[cellIndex])
            return;

        cellsStates[cellIndex] = true;

        // check for top row
        if (i == 1)
            cellsInfo.union(cellIndex, gridSize*gridSize);
        else
            unionToCell(cellIndex, i - 1, j); 
        
        // check for bottom row
        if (i == gridSize)
            cellsInfo.union(cellIndex, gridSize*gridSize + 1);
        else
            unionToCell(cellIndex, i + 1, j); 
        
        //if not left border
        if (j != 1)
            unionToCell(cellIndex, i, j - 1); 

        //if not right border
        if (j != gridSize)
            unionToCell(cellIndex, i, j + 1); 
       
        countOpenedSites++;
    }
    
    // is site (row, col) open?
    public boolean isOpen(int i, int j) {
        return cellsStates[getCellIndex(i, j)];
    }

    // is site (row, col) full?
    public boolean isFull(int i, int j) {
        return cellsInfo.connected(gridSize*gridSize, getCellIndex(i, j));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return countOpenedSites;
    }
    
    // does the system percolate?
    public boolean percolates() {
        if (perlocateState)
            return perlocateState;
        
        perlocateState = cellsInfo.connected(gridSize * gridSize, gridSize * gridSize + 1);

        return perlocateState;

    }

    // get cell index with checking of prescribed range
    private int getCellIndex(int i, int j) {
        if (i > gridSize || j > gridSize || i < 1 || j < 1)
            throw new IndexOutOfBoundsException();

        return (i - 1) * gridSize + j - 1;
    }

    // connect cellIndex to cell with row i and column j
    private void unionToCell(int cellIndex, int i, int j) {
        int neighborCellIndex = getCellIndex(i, j);
        if (cellsStates[neighborCellIndex])
            cellsInfo.union(neighborCellIndex, cellIndex);
    }    
}