class Solution {
    public int removeDuplicates(int[] nums) {
        // 如果数组长度小于等于 2，直接返回长度即可
        if (nums.length <= 2) {
            return nums.length;
        }

        // slow 表示新数组下一个要写入的位置
        // 因为最多允许重复 2 次，所以前 2 个元素（索引 0 和 1）必然可以保留
        int slow = 2;
        for (int fast = 2; fast < nums.length; fast++) {
            // 检查当前元素 nums[fast] 是否与已经保留的“倒数第二个元素”nums[slow - 2] 相同
            // 如果不同，说明加入 nums[fast] 不会让元素重复超过 2 次
            if (nums[fast] != nums[slow - 2]) {
                nums[slow] = nums[fast];
                slow++;
            }
        }

        return slow;
    }
}