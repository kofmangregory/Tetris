public class Tetris {

	// The various block shapes.
	enum BlockType {
		I, L, J, S, Z, O, T;
	}

	// The allowed inputs from the user.
	enum UserInput {
		SHIFT_RIGHT, SHIFT_LEFT, ROTATE_RIGHT, ROTATE_LEFT;
	}

	// A wrapper class for a block grid, so that it can be used in a List.
	class BlockConfig {

		public boolean[][] configGrid;
		
		public BlockConfig(boolean[][] configGrid) {
			this.configGrid = configGrid;
		}
	}

	// The game grid is represented as a list of lists, such that list i is the ith row from the bottom of the grid.
	// True elements represent occupied pixels.
	private List<List<Boolean>> grid;

	// A list of the various positionos for the blocks.
	private List<List<BlockConfig>> configList;

	// Bookkeeping for the current active piece.
	private BlockType activePiece;
	private BlockConfig activeConfig;
	private int blockIndex;
	private int rotationIndex;

	// A map from the indices of the configList to the block type of that config list.
	private Map<Integer, BlockType> blockTypeMap;

	// Coordinates of the lower left corner of the active config on the grid.
	private int x;
	private int y;

	public Tetris(int numRows, int numCols) {
		this.initializeGrid(numRows, numCols);
		this.initializeConfigs();
		this.setBlockTypeMap();
		this.setActivePieceAndConfig();
	}

	public void onUserInput(UserInput input) {
		if (input == UserInput.SHIFT_RIGHT) {
			shift(true);
		}
		else if (input == UserInput.SHIFT_LEFT) {
			shift(false);
		}
		else if (input == UserInput.ROTATE_RIGHT) {
			rotate(true);
		}
		else {
			rotate(false);
		}
	}

	public void onTick() {
		moveDown();
	}

	private void setBlockTypeMap() {
		this.blockTypeMap = new Map<Integer, BlockType>();

		this.blockTypeMap.put(0, BlockType.I);
		this.blockTypeMap.put(1, BlockType.L);
		this.blockTypeMap.put(2, BlockType.J);
		this.blockTypeMap.put(3, BlockType.S);
		this.blockTypeMap.put(4, BlockType.Z);
		this.blockTypeMap.put(5, BlockType.O);
		this.blockTypeMap.put(6, BlockType.T);
	}

	private void intializeGrid(int numRows, int numCols) {
		this.grid = new ArrayList<List<Boolean>>();

		boolean falseTemplate = new boolean[numCols];
		for (int i = 0; i < numRows; i++) {
			List<Boolean> row = new ArrayList<Boolean>(falseTemplate);
			this.grid.add(row);
		}
	}

	private void initializeConfigs() {
		this.configList = new ArrayList<BlockType, List<BlockConfig>>();

		// I
		boolean[][] verticalI = new boolean[4][4];
		verticalI[2][0] = true;
		verticalI[2][1] = true;
		verticalI[2][2] = true;
		verticalI[2][3] = true;

		boolean[][] horizontalI = new boolean[4][4];
		horizontalI[0][1] = true;
		horizontalI[1][1] = true;
		horizontalI[2][1] = true;
		horizontalI[3][1] = true;

		List<BlockConfig> iConfig = new ArrayList<BlockConfig>();
		iConfig.add(new BlockConfig(verticalI));
		iConfig.add(new BlockConfig(horizontalI));
		this.configList.add(iConfig);

		// L
		boolean[][] l1 = new boolean[e][3];
		l1[0][0] = true;
		l1[0][1] = true;
		l1[0][2] = true;
		l1[1][0] = true;

		boolean[][] l2 = new boolean[3][3];
		l2[0][1] = true;
		l2[0][2] = true;
		l2[1][2] = true;
		l2[2][2] = true;

		boolean[][] l3 = new boolean[3][3];
		l3[1][2] = true;
		l3[2][2] = true;
		l3[2][1] = true;
		l3[2][0] = true;

		boolean[][] l4 = new boolean[3][3];
		l4[0][0] = true;
		l4[1][0] = true;
		l4[2][0] = true;
		l4[2][1] = true;

		List<BlockConfig> lConfig = new ArrayList<BlockConfig>();
		lConfig.add(new BlockConfig(l1));
		lConfig.add(new BlockConfig(l2));
		lConfig.add(new BlockConfig(l3));
		lConfig.add(new BlockConfig(l4));
		this.configList.add(lConfig);

		// J
		boolean[][] j1 = new boolean[3][3];
		j1[2][0] = true;
		j1[2][1] = true;
		j1[2][2] = true;
		j1[1][0] = true;

		boolean[][] j2 = new boolean[3][3];
		j2[0][1] = true;
		j2[0][0] = true;
		j2[1][0] = true;
		j2[2][0] = true;

		boolean[][] j3 = new boolean[3][3];
		j3[1][2] = true;
		j3[0][0] = true;
		j3[0][1] = true;
		j3[0][2] = true;

		boolean[][] j4 = new boolean[3][3];
		j4[0][2] = true;
		j4[1][2] = true;
		j4[2][2] = true;
		j4[2][1] = true;

		List<BlockConfig> jConfig = new ArrayList<BlockConfig>();
		jConfig.add(new BlockConfig(j1));
		jConfig.add(new BlockConfig(j2));
		jConfig.add(new BlockConfig(j3));
		jConfig.add(new BlockConfig(j4));
		this.configList.add(jConfig);

		// S
		boolean[][] s1 = new boolean[3][3];
		s1[0][0] = true;
		s1[1][0] = true;
		s1[1][1] = true;
		s1[2][1] = true;

		boolean[][] s2 = new boolean[3][3];
		s2[0][2] = true;
		s2[0][1] = true;
		s2[1][1] = true;
		s2[1][0] = true;

		List<BlockConfig> sConfig = new ArrayList<BlockConfig>();
		sConfig.add(new BlockConfig(s1));
		sConfig.add(new BlockConfig(s2));
		this.configList.add(sConfig);

		// Z
		boolean[][] z1 = new boolean[3][3];
		z1[0][1] = true;
		z1[1][1] = true;
		z1[1][0] = true;
		z1[2][0] = true;

		boolean[][] z2 = new boolean[3][3];
		z2[2][2] = true;
		z2[2][1] = true;
		z2[1][1] = true;
		z2[1][0] = true;

		List<BlockConfig> zConfig = new ArrayList<BlockConfig>();
		zConfig.add(new BlockConfig(z1));
		zConfig.add(new BlockConfig(z2));
		this.configList.add(zConfig);

		// O
		boolean[][] o = new boolean[2][2];
		o[0][0] = true;
		o[1][0] = true;
		o[0][1] = true;
		o[1][1] = true;

		List<BlockConfig> oConfig = new ArrayList<BlockConfig>();
		oConfig.add(new BlockConfig(o));
		this.configList.add(oConfig);

		// T
		boolean[][] t1 = new boolean[3][3];
		t1[0][0] = true;
		t1[1][0] = true;
		t1[1][1] = true;
		t1[2][0] = true;

		boolean[][] t2 = new boolean[3][3];
		t2[0][2] = true;
		t2[0][1] = true;
		t2[0][0] = true;
		t2[1][1] = true;

		boolean[][] t3 = new boolean[3][3];
		t3[0][2] = true;
		t3[1][2] = true;
		t3[2][2] = true;
		t3[1][1] = true;

		boolean[][] t4 = new boolean[3][3];
		t4[2][2] = true;
		t4[2][1] = true;
		t4[2][0] = true;
		t4[1][1] = true;

		List<BlockConfig> tConfig = new ArrayList<BlockConfig>();
		tConfig.add(new BlockConfig(t1));
		tConfig.add(new BlockConfig(t2));
		tConfig.add(new BlockConfig(t3));
		tConfig.add(new BlockConfig(t4));
		this.configList.add(tConfig);
	}

	// Remove finished rows and add empty row to top. 
	private void clearFinishedRows() {
		int numDeleted = 0;
		for (int i = 0; i < this.grid.size(); i++) {
			List<Boolean> row = this.grid.get(i);
			boolean clearRow = true;
			for (int j = 0; j < row.size(); j++) {
				if (row.get(j)) clearRow = false;
			}
			if (clearRow) {
				this.grid.remove(i);
				numDeleted++;
			}
		}
		boolean falseTemplate = new boolean[this.grid.get(0).size()];
		for (int i = 0; i < numDeleted; i++) {
			this.grid.add(new ArrayList<Boolean>(falseTemplate));
		}
	}

	private void setActivePieceAndConfig() {
		int blockTypeIndex = Math.random(this.configList.size());
		this.activePiece = this.blockTypeMap.get(blockTypeIndex);
		this.blockIndex = blockTypeIndex;
		List<BlockConfig> activePieceConfigs = this.configList.get(this.activePiece);
		this.rotationIndex = Math.random(activePieceConfigs.size());
		this.activeConfig = this.configList.get(activeConfigIndex);

		this.x = this.grid.get(0).size() / 2 - this.activeConfig.configGrid.length / 2;
		this.y = this.grid.size() - this.activeConfig.configGrid[0].length;

		// Check validity. End the game if a piece cannot be added.
		for (int i = 0; i < activeConfig.configGrid.length; i++) {
			for (int j = 0; j < activeConfig.configGrid[i].length; j++) {
				if (activeConfig[i][j]) {
					if (this.grid.get(this.x + i).get(this.y + j)) {
						System.out.println("GAME OVER.");
						return;
					}
				}
			}
		}

		// Place new piece.
		for (int i = 0; i < this.activeConfig.configGrid.length; i++) {
			for (int j = 0; j < this.activeConfig.configGrid[i].length; j++) {
				if (activeConfig[i][j]) {
					this.grid.get(this.x + i).set(this.y + j, true);
				}
			}
		}
	}

	// Clear the active piece to check if it can be rotated or shifted.
	private void tempClear() {
		for (int i = 0; i < this.activeConfig.configGrid.length; i++) {
			for (int j = 0; j < this.activeConfig.configGrid[i].length; j++) {
				if (this.activeConfig[i][j]) {
					this.grid.get(this.y + j).set(this.x + i, false);
				}
			}
		}
	}

	// Unclear the active piece.
	private void tempUnclear() {
		for (int i = 0; i < this.activeConfig.configGrid.length; i++) {
			for (int j = 0; j < this.activeConfig.configGrid[i].length; j++) {
				if (this.activeConfig[i][j]) {
					this.grid.get(this.y + j).set(this.x + i, true);
				}
			}
		}
	}

	private boolean canRotate(boolean right) {
		int incrementor = right ? -1 : 1;
		int nextIndex = (incrementor + this.rotationIndex) % this.configList.get(this.blockTypeIndex).size();
		BlockConfig rotation = this.configList.get(this.blockIndex).get(nextIndex);

		tempClear();

		for (int i = 0; i < rotation.configGrid.length; i++) {
			for (int j = 0; j < rotation.configGrid[i].length; j++) {
				if (rotation.configGrid[i][j]) {
					if (this.x + i >=0 && this.x + i < this.grid.get(0).size() &&
						this.y + j >= 0 && this.y + j < this.grid.size() && !this.grid.get(this.y+j).get(this.x+i)) {
						continue;
					} 
					else {
						tempUnclear();
						return false;
					}
				}
			}
		}

		tempUnclear();
		return true;
	}

	private void rotate(boolean right) {
		if (canRotate(right)) {
			tempClear();


			int incrementor = right ? -1 : 1;
			this.rotationIndex = (incrementor + this.rotationIndex) % this.configList.get(this.blockIndex).size();
			this.activeConfig = this.configList.get(this.blockIndex);

			for (int i = 0; i < this.activeConfig.configGrid.length; i++) {
				for (int j = 0; j < this.activeConfig.configGrid[i].length; j++) {
					if (this.activeConfig[i][j]) {
						this.grid.get(this.y + j).set(this.x + i, false);
					}
				}
			}
		}
	}

	private boolean canShift(boolean right) {
		int incrementor = right ? -1 : 1;
		int nextX = this.x + incrementor;

		tempClear();

		for (int i = 0; i < this.activeConfig.configGrid.length; i++) {
			for (int j = 0; j < this.activeConfig.configGrid[i].length; j++) {
				if (this.activeConfig.configGrid[i][j]) { 
					if (nextX + i >= 0 && nextX + i <this.grid.get(0).size() && this.y + j >=0 && this.y + j < this.grid.size() && !this.grid.get(this.y+j).get(nextX + i)) {
						continue;
					}
					else {
						tempUnclear();
						return false;
					}
				}
			}
		}

		tempUnclear();
		return true;
	}

	private void shift(boolean right) {
		if (canShift(right)) {
			 tempClear();

			 int incrementor = right ? -1 : 1;
			 this.x += incrementor;

			 for (int i = 0; i < this.activeConfig.configGrid.length; i++) {
			 	for (int j = 0; j < this.activeConfig.configGrid[i].length; j++) {
			 		if (this.activeConfig.configGrid[i][j]) this.grid.get(this.y + j).set(this.x + i, true);
			 	}
			 }
		}
	}

	private void canMoveDown() {
		int nextY = this.y - 1;

		tempClear();

		for (int i = 0; i < this.activeConfig.configGrid.length; i++) {
			for (int j = 0; j < this.activeConfig.configGrid[i].length; j++) {
				if (this.activeConfig.configGrid[i][j]) {
					if (this.x + i >=0 && this.x + i < this.grid.get(0).size() && nextY + j >= 0 && nextY + j < this.grid.size() && !this.grid.get(nextY + j).get(this.x + i)) {
						continue;
					}
					else {
						tempUnclear();
						return false;
					}
				}
			}
		}

		tempUnclear();
		return true;
	}

	private void moveDown() {
		this.y = thi.y - 1;

		tempClear();

		if (canMoveDown()) {
			for (int i = 0; i < this.activeConfig.configGrid.length; i++) {
				for (int j = 0; j < this.activeConfig.configGrid[i].length; j++) {
					if (this.activeConfigGrid[i][j]) {
						this.grid.get(this.x + i).set(this.y + j, true);
					}
				}
			}
		}
		else {
			clearFinishedRows();
			setActivePieceAndConfig();
		}
	}

}