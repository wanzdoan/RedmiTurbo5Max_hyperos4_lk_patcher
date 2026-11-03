
int get_lk_load_md_info(long param_1,int param_2)

{
  undefined4 uVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  char *pcVar5;
  long lVar6;
  long lVar7;
  
  if ((s_g_lk_load_img_status & 1) == 0) {
    iVar2 = scnprintf(param_1,(long)param_2,"LK Load MD:[Disabled](0x%08x)\n");
    return iVar2;
  }
  iVar2 = scnprintf(param_1,(long)param_2,"LK Load MD:[Enabled](0x%08x)\n");
  if (s_g_lk_load_img_status < 2) {
    iVar3 = scnprintf(param_1 + iVar2,(long)(param_2 - iVar2),"LK load MD success!\n");
    goto LAB_001022a8;
  }
  iVar3 = scnprintf(param_1 + iVar2,(long)(param_2 - iVar2),"LK load MD has error:\n");
  iVar3 = iVar3 + iVar2;
  iVar2 = scnprintf(param_1 + iVar3,(long)(param_2 - iVar3),"---- More details ----------------\n");
  uVar1 = s_g_lk_ld_md_errno;
  iVar3 = iVar3 + iVar2;
  if ((s_g_lk_load_img_status >> 1 & 1) == 0) {
    if ((s_g_lk_load_img_status >> 2 & 1) != 0) {
      lVar6 = param_1 + iVar3;
      lVar7 = (long)(param_2 - iVar3);
      uVar4 = ld_md_errno_to_str(s_g_lk_ld_md_errno);
      pcVar5 = "Err: No valid md image(%d:%s)\n";
      goto LAB_001021f4;
    }
    if ((s_g_lk_load_img_status >> 3 & 1) != 0) {
      lVar6 = param_1 + iVar3;
      lVar7 = (long)(param_2 - iVar3);
      uVar4 = ld_md_errno_to_str(s_g_lk_ld_md_errno);
      pcVar5 = "Err: Got lk info fail(%d:%s)\n";
      goto LAB_001021f4;
    }
    if ((s_g_lk_load_img_status >> 4 & 1) != 0) {
      iVar2 = scnprintf(param_1 + iVar3,(long)(param_2 - iVar3),"Err: lk kernel setting mis sync\n")
      ;
      goto LAB_00102204;
    }
  }
  else {
    lVar6 = param_1 + iVar3;
    lVar7 = (long)(param_2 - iVar3);
    uVar4 = ld_md_errno_to_str(s_g_lk_ld_md_errno);
    pcVar5 = "Err: Got invalid md id(%d:%s)\n";
LAB_001021f4:
    iVar2 = scnprintf(lVar6,lVar7,pcVar5,uVar1,uVar4);
LAB_00102204:
    iVar3 = iVar2 + iVar3;
  }
  iVar2 = scnprintf(param_1 + iVar3,(long)(param_2 - iVar3),"ERR> 1:[%d]\n",lk_load_img_err_no);
  iVar2 = iVar2 + iVar3;
  uVar4 = ld_md_errno_to_str(lk_load_img_err_no);
  iVar3 = scnprintf((undefined1 *)(param_1 + iVar2),(long)(param_2 - iVar2),"hint for MD: %s\n",
                    uVar4);
  if (iVar3 < 1 || param_2 - iVar2 <= iVar3) {
    ccci_dump_write(0,1,"[0/util]%s: scnprintf hint for MD fail\n","get_lk_load_md_info");
    ccci_dump_write(2,1,"[0/util]%s: scnprintf hint for MD fail\n","get_lk_load_md_info");
    *(undefined1 *)(param_1 + iVar2) = 0;
    return iVar2;
  }
LAB_001022a8:
  return iVar3 + iVar2;
}

