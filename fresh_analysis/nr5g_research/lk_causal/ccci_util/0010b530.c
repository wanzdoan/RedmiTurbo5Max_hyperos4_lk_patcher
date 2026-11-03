
undefined8 ccci_util_fo_init(void)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  long lVar4;
  uint local_7c;
  undefined8 local_78;
  undefined8 local_70;
  undefined6 uStack_68;
  undefined2 local_62;
  undefined6 uStack_60;
  long local_58;
  
  lVar3 = sp_el0;
  local_58 = *(long *)(lVar3 + 0x620);
  ccci_dump_write(0,1,"[0/util]%s 0.\n","ccci_util_fo_init");
  ccci_dump_write(2,1,"[0/util]%s 0.\n","ccci_util_fo_init");
  uVar2 = collect_lk_boot_arguments();
  if ((int)uVar2 == 0) {
    ccci_dump_write(uVar2,1,"[0/util]using v3.\n");
    ccci_dump_write(2,1,"[0/util]using v3.\n");
    goto LAB_0010ba74;
  }
  lVar3 = of_find_compatible_node(0,0,"mediatek,ccci_util_cfg");
  if (lVar3 == 0) {
    ccci_dump_write(0,1,"[0/util]using v1.\n");
    ccci_dump_write(2,1,"[0/util]using v1.\n");
    if ((s_g_md_usage_case & 1) == 0) {
      ccci_dump_write(0,1,"[0/util]md is disabled\n");
      ccci_dump_write(2,1,"[0/util]md is disabled\n");
    }
    else {
      lVar3 = of_find_compatible_node(0,0,"mediatek,mddriver");
      lVar4 = of_find_compatible_node(0,0,"mediatek,ap_ccif0");
      if ((lVar3 == 0) && (lVar3 = lVar4, lVar4 == 0)) {
        ccci_dump_write(0,1,"[0/util]md smem size is not set in device tree,need to check\n");
        _printk(&DAT_00110dfd);
        s_g_md_usage_case = 0;
      }
      else {
        of_property_read_variable_u32_array(lVar3,"mediatek,md_smem_size",&md_resv_smem_size,1,0);
        md_resv_mem_addr = md_resv_mem_list;
        md_resv_mem_size = (md_resv_size_list + ~md_resv_smem_size | 0x3fff) + 1;
        md_resv_smem_addr = md_resv_mem_list + md_resv_mem_size;
        ccci_dump_write(0,1,"[0/util]md modem_total_size=0x%x,md_size=0x%x, smem_size=0x%x\n");
        ccci_dump_write(2,1,"[0/util]md modem_total_size=0x%x,md_size=0x%x, smem_size=0x%x\n",
                        md_resv_size_list,md_resv_mem_size,md_resv_smem_size);
        if (s_g_md_usage_case == 1) {
          if ((md_resv_mem_addr & 0x1ffffff) != 0) {
            ccci_dump_write(0,1,"[0/util]md memory addr is not 32M align!!!\n");
            _printk(&DAT_0011087a);
            if (s_g_md_usage_case != 1) goto LAB_0010b7dc;
          }
          if ((md_resv_smem_addr & 0x1fffff) != 0) {
            ccci_dump_write(0,1,"[0/util]md share memory addr %p is not 0x%x align!!\n",
                            &md_resv_smem_addr,0x200000);
            _printk(&DAT_001106f7,&md_resv_smem_addr,0x200000);
          }
        }
LAB_0010b7dc:
        ccci_dump_write(0,1,"[0/util]MemStart: %016llx, MemSize:0x%08X\n",md_resv_mem_addr,
                        md_resv_mem_size);
        ccci_dump_write(2,1,"[0/util]MemStart: %016llx, MemSize:0x%08X\n",md_resv_mem_addr,
                        md_resv_mem_size);
        ccci_dump_write(0,1,"[0/util]SMemStart: %016llx, SMemSize:0x%08X\n",md_resv_smem_addr,
                        md_resv_smem_size);
        ccci_dump_write(2,1,"[0/util]SMemStart: %016llx, SMemSize:0x%08X\n",md_resv_smem_addr,
                        md_resv_smem_size);
      }
    }
  }
  else {
    ccci_dump_write(0,1,"[0/util]using v2.\n");
    ccci_dump_write(2,1,"[0/util]using v2.\n");
    local_7c = 0;
    uStack_60 = 0;
    local_70 = 0;
    uStack_68 = 0;
    local_62 = 0;
    local_78 = 0;
    ccci_dump_write(0,1,"[0/util]using kernel dt mem setting for md\n");
    ccci_dump_write(2,1,"[0/util]using kernel dt mem setting for md\n");
    scnprintf(&local_78,0x1e,"mediatek,md-smem-size");
    iVar1 = of_property_read_variable_u32_array(lVar3,&local_78,&local_7c,1,0);
    if (iVar1 < 0) {
      ccci_dump_write(0,1,"[0/util]DT[%s]:%08X\n",&local_78,md_resv_smem_size);
      ccci_dump_write(2,1,"[0/util]DT[%s]:%08X\n",&local_78,md_resv_smem_size);
    }
    else {
      ccci_dump_write(0,1,"[0/util]DT[%s]:%08X\n",&local_78,local_7c);
      ccci_dump_write(2,1,"[0/util]DT[%s]:%08X\n",&local_78,local_7c);
      md_resv_smem_size = local_7c;
    }
    scnprintf(&local_78,0x1e,"mediatek,version");
    local_7c = 0;
    iVar1 = of_property_read_variable_u32_array(lVar3,&local_78,&local_7c,1,0);
    if (iVar1 < 0) {
      ccci_dump_write(0,1,"[0/util]DT[mediatek,version]:NA\n");
      ccci_dump_write(2,1,"[0/util]DT[mediatek,version]:NA\n");
    }
    ccci_dump_write(0,1,"[0/util]DT[%s]:%08X\n",&local_78,local_7c);
    ccci_dump_write(2,1,"[0/util]DT[%s]:%08X\n",&local_78,local_7c);
    if (local_7c == 1) {
      if (s_g_md_usage_case == 1) {
        md_resv_mem_size = md_resv_size_list;
        md_resv_mem_addr = md_resv_mem_list;
        ccci_dump_write(0,1,"[0/util]md MemStart: 0x%016llx, MemSize:0x%08X\n");
        ccci_dump_write(2,1,"[0/util]md MemStart: 0x%016llx, MemSize:0x%08X\n",md_resv_mem_addr,
                        md_resv_mem_size);
      }
      md_resv_smem_addr = 0;
      ccci_dump_write(0,1,"[0/util]AP  <--> MD1 SMEM(0x%08X):%016llx~%016llx\n",
                      (ulong)md_resv_smem_size,0,(ulong)md_resv_smem_size - 1);
      ccci_dump_write(2,1,"[0/util]AP  <--> MD1 SMEM(0x%08X):%016llx~%016llx\n",
                      (ulong)md_resv_smem_size,md_resv_smem_addr,
                      md_resv_smem_size + md_resv_smem_addr + -1);
    }
    else {
      ccci_dump_write(0,1,"[0/util]Un-support version:%d\n");
      ccci_dump_write(2,1,"[0/util]Un-support version:%d\n",local_7c);
    }
  }
  ccci_dump_write(0,1,"[0/util]%s 2.\n","ccci_util_fo_init");
  ccci_dump_write(2,1,"[0/util]%s 2.\n","ccci_util_fo_init");
LAB_0010ba74:
  lVar3 = sp_el0;
  if (*(long *)(lVar3 + 0x620) != local_58) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return 0;
}

