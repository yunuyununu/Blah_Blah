// store/useBoardStore.js
import { defineStore } from 'pinia';
import axios from 'axios';

export const useBoardStore = defineStore('board', {
  state: () => ({
    board: [],
    lastBIdx: null,
    hasMore: true,
  }),
  actions: {
    async fetchBoards() {
      const res = await axios.get('http://localhost:80/board/boards', {
        params: { lastBIdx: this.lastBIdx },
      });
      const newPosts = res.data;

      if (newPosts.length < 12) this.hasMore = false;

      if (newPosts.length > 0) {
        this.board.push(...newPosts);
        this.lastBIdx = newPosts[newPosts.length - 1].b_idx;
      }
    },
    reset() {
      this.board = [];
      this.lastBIdx = null;
      this.hasMore = true;
    }
  },
});
