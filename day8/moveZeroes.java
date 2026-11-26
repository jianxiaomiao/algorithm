 // 283. 移动零
// 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。

// 请注意 ，必须在不复制数组的情况下原地对数组进行操作。

// 示例 1:

// 输入: nums = [0,1,0,3,12]
// 输出: [1,3,12,0,0]
// 示例 2:

// 输入: nums = [0]
// 输出: [0]
 

// 提示:

// 1 <= nums.length <= 104
// -231 <= nums[i] <= 231 - 1
 
// 进阶：你能尽量减少完成的操作次数吗？

//我的想法:首先一个指针找到第一0的,位置,然后停止
//用另一个指针去找第二个0(极限是找到末尾),这样就是 0 . . . 0的位置顺序
//然后这中间的数据整体前移动一位,预估时间是0(m)
//然后慢指针和快指针重合,快指针继续寻找
//时间复杂度: o(n) + o(m) * 0的个数,约等于是 o(n)

// class Solution {
//     public void moveZeroes(int[] nums) {
//         int slow = 0;
//         // 1. 把所有非零元素往前搬
//         for (int fast = 0; fast < nums.length; fast++) {
//             if (nums[fast] != 0) {
//                 nums[slow++] = nums[fast];
//             }
//         }
//         // 2. 后面全部补 0
//         while (slow < nums.length) {
//             nums[slow++] = 0;
//         }
//     }
// }
class Solution {
    public void moveZeroes(int[] nums) {
        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                // 如果 slow == fast，无需自我交换
                if (slow != fast) {
                    int temp = nums[slow];
                    nums[slow] = nums[fast];
                    nums[fast] = temp;
                }
                slow++;
            }
        }
    }
}