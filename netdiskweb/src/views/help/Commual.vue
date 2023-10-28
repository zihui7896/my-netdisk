<template>
  <div>
    <div class="top">
      <div class="top-op">
        <div class="search-panel">
          <el-input clearable placeholder="搜索全部互助帖子" v-model="fileNameFuzzy" @keyup.enter="search">
            <template #suffix>
              <i class="iconfont icon-search" @click="search"></i>
            </template>
          </el-input>
        </div>
        <div class="iconfont icon-refresh" @click="loadDataList"></div>
        <el-button :style="{ 'margin-left': '10px' }" type="primary" @click="">
          发表需求
        </el-button>
      
      </div>

      <!--导航-->
      
    </div>

  </div>
  <el-tabs type="border-card" class="box-card" style="margin-top: 10px;">
    <el-tab-pane label="最新">最新</el-tab-pane>
    <el-tab-pane label="已解决">已解决</el-tab-pane>
    <el-tab-pane label="未解决">未解决</el-tab-pane>

  </el-tabs>
</template>
  


<script setup>

</script>
  
<style lang="scss" scoped>
.box-card {
  width: 1000px;
}

.search-panel {
  margin-left: 0px !important;
}

.top {
  margin-top: 20px;

  .top-op {
    display: flex;
    align-items: center;

    .btn {
      margin-right: 10px;
    }

    .search-panel {
      margin-left: 10px;
      width: 300px;
    }

    .icon-refresh {
      cursor: pointer;
      margin-left: 10px;
    }

    .not-allow {
      background: #d2d2d2 !important;
      cursor: not-allowed;
    }
  }
}
</style>