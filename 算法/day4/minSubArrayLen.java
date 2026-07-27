class Solution {
    public int minSubArrayLen(int target, int[] nums) {
        int left = 0;
        int sum = 0;
        int minLength = Integer.MAX_VALUE; // 初始化为一个最大值

        for (int right = 0; right < nums.length; right++) {
            sum += nums[right]; // 扩张窗口

            // 当窗口内元素和达到 target 时，尝试缩小窗口
            while (sum >= target) {
                // 1. 满足条件，更新最小长度
                minLength = Math.min(minLength, right - left + 1);
                
                // 2. 缩小窗口：减去 left 位置的值，并右移 left
                sum -= nums[left];
                left++;
            }
        }

        // 如果 minLength 没有被更新过，说明没有找到符合条件的子数组，返回 0
        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }
}
