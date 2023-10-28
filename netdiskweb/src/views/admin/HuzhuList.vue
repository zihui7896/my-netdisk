<template>
    <div>
        <div class="top">
            <div class="top-op">
                <div class="search-panel">
                    <el-input clearable placeholder="搜索互助帖子" v-model="fileNameFuzzy" @keyup.enter="search">
                        <template #suffix>
                            <i class="iconfont icon-search" @click="search"></i>
                        </template>
                    </el-input>
                </div>
                <div class="iconfont icon-refresh" @click="loadDataList"></div>
            </div>
            <!--导航-->
            <Navigation ref="navigationRef" @navChange="navChange" :adminShow="true"></Navigation>
        </div>
        <!--预览-->
        <Preview ref="previewRef"> </Preview>
    </div>
</template>
  
<script setup>
import { ref, reactive, getCurrentInstance, nextTick, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const emit = defineEmits(["addFile"]);
//添加文件
const addFile = async (fileData) => {
    emit("addFile", { file: fileData.file, filePid: currentFolder.value.fileId });
};
//添加文件回调
const reload = () => {
    showLoading.value = false;
    loadDataList();
};
defineExpose({
    reload,
});

const api = {
    loadDataList: "/admin/loadFileList",
    delFile: "/admin/delFile",
    createDownloadUrl: "/admin/createDownloadUrl",
    download: "/api/admin/download",
};

//列表
const columns = [
    {
        label: "文件名",
        prop: "fileName",
        scopedSlots: "fileName",
    },
    {
        label: "发布人",
        prop: "nickName",
        width: 250,
    },
    {
        label: "修改时间",
        prop: "lastUpdateTime",
        width: 200,
    },
    {
        label: "大小",
        prop: "fileSize",
        scopedSlots: "fileSize",
        width: 200,
    },
];
//搜索
const search = () => {
    showLoading.value = true;
    loadDataList();
};
//列表
const tableData = ref({});
const tableOptions = {
    extHeight: 50,
    selectType: "checkbox",
};

//多选 批量选择
const selectFileIdList = ref([]);
const rowSelected = (rows) => {
    selectFileIdList.value = [];
    rows.forEach((item) => {
        selectFileIdList.value.push(item.userId + "_" + item.fileId);
    });
};

const fileNameFuzzy = ref();
const showLoading = ref(true);

const loadDataList = async () => {
    let params = {
        pageNo: tableData.value.pageNo,
        pageSize: tableData.value.pageSize,
        fileNameFuzzy: fileNameFuzzy.value,
        filePid: currentFolder.value.fileId,
    };
    let result = await proxy.Request({
        url: api.loadDataList,
        showLoading: showLoading,
        params,
    });
    if (!result) {
        return;
    }
    tableData.value = result.data;
};

//展示操作按钮
const showOp = (row) => {
    tableData.value.list.forEach((element) => {
        element.showOp = false;
    });
    row.showOp = true;
};


//目录
const currentFolder = ref({ fileId: 0 });
const navChange = (data) => {
    const { curFolder } = data;
    currentFolder.value = curFolder;
    showLoading.value = true;
    loadDataList();
};

</script>
  
<style lang="scss" scoped>
@import "@/assets/file.list.scss";

.search-panel {
    margin-left: 0px !important;
}

.file-list {
    margin-top: 10px;

    .file-item {
        .op {
            width: 120px;
        }
    }
}
</style>