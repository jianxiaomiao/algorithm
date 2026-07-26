public class maxArea {

    public static class Solution {
        /**
         * 盛最多水的容器：相向双指针解法
         * 时间复杂度: O(N)
         * 空间复杂度: O(1)
         */
        public int maxArea(int[] height) {
            int left = 0;
            int right = height.length - 1;
            int maxArea = 0;

            while (left < right) {
                // 1. 计算当前左右边界构成的水容量
                int currentWidth = right - left;
                int currentHeight = Math.min(height[left], height[right]);
                int currentArea = currentWidth * currentHeight;

                // 2. 更新最大容量
                if (currentArea > maxArea) {
                    maxArea = currentArea;
                }

                // 3. 核心剪枝与移动逻辑：只移动短板指针
                if (height[left] < height[right]) {
                    left++;
                } else {
                    right--;
                }
            }

            return maxArea;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();

        int[] height1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("========== 示例 1 ==========");
        System.out.println("输入: [1,8,6,2,5,4,8,3,7], 预期: 49");
        System.out.println("输出: " + solution.maxArea(height1));

        int[] height2 = {1, 1};
        System.out.println("========== 示例 2 ==========");
        System.out.println("输入: [1,1], 预期: 1");
        System.out.println("输出: " + solution.maxArea(height2));

        int[] height3 = {1, 2, 4, 3};
        System.out.println("========== 官方测试用例 ==========");
        System.out.println("输入: [1,2,4,3], 预期: 4");
        System.out.println("输出: " + solution.maxArea(height3));
    }
}