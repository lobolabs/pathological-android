package org.gignac.jp.pathological;
import android.graphics.*;

class Tile
{
	public static final int tile_size = 92;
	public int paths;
	public Rect pos;  // Only left & top are maintained
	public int tile_x, tile_y;
	public boolean completed;
	protected Board board;
	public boolean dirty;
	private static final int[] plain_tiles = {
		R.drawable.path_0, R.drawable.path_1, R.drawable.path_2,
		R.drawable.path_3, R.drawable.path_4, R.drawable.path_5,
		R.drawable.path_6, R.drawable.path_7, R.drawable.path_8,
		R.drawable.path_9, R.drawable.path_10, R.drawable.path_11,
		R.drawable.path_12, R.drawable.path_13, R.drawable.path_14,
		R.drawable.path_15
	};
	
	public Tile( Board board, int paths, int cx, int cy, int x, int y) {
		this.board = board;
		this.paths = paths;
		this.pos = new Rect(cx - tile_size/2, cy - tile_size/2, 0, 0);
		this.completed = false;
		this.tile_x = x;
		this.tile_y = y;
		this.dirty = true;
		board.sc.cache(plain_tiles);
	}
	
	public Tile( Board board, int paths) {
		this(board,paths,0,0, 0, 0);
	}

	public void setxy(int x, int y) {
		pos.left = Tile.tile_size * x;
		pos.top = Marble.marble_size + Tile.tile_size * y;
		tile_x = x;
		tile_y = y;
	}

	public void draw_back(Blitter b) {
		pos.right = pos.left + tile_size;
		pos.bottom = pos.top + tile_size;
		if(paths > 0) {
			b.blit( plain_tiles[paths],
				pos.left, pos.top, tile_size, tile_size);
			if( (paths & 1) > 0 && tile_y == 0) {
				b.blit( 0x100000001l,
					pos.left + Tile.tile_size/4,
					Marble.marble_size/2-Tile.tile_size/4);
			}
		}
	}

	public void update( Board board) {}

	public void draw_fore( Blitter b) {}

	public void click( Board board, int posx, int posy) {}
	
	public void flick( Board board, int posx, int posy, int dir) {}

	public void affect_marble( Board board, Marble marble, int rposx, int rposy)
	{
		if(rposx == tile_size/2 && rposy == tile_size/2) {
			if((paths & (1 << marble.direction)) != 0) return;

			// Figure out the new direction
			int t = paths - (1 << (marble.direction^2));
			if(t == 1) marble.direction = 0;
			else if(t == 2) marble.direction = 1;
			else if(t == 4) marble.direction = 2;
			else if(t == 8) marble.direction = 3;
			else marble.direction = marble.direction ^ 2;
		}
	}

	public void invalidate() {
		dirty = true;
	}
}
