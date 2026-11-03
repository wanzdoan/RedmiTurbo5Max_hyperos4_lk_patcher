
int FUN_ffff0000be02a7f8(long param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  uint *puVar4;
  char *pcVar5;
  ulong uVar6;
  char *pcVar7;
  uint uVar8;
  undefined4 local_44;
  
  if (param_1 == 0) {
    return -0x3d;
  }
  iVar2 = FUN_ffff0000be05620c(param_1,PTR_s__soc_drm_ffff0000be0d6748);
  if (((iVar2 < 0) &&
      (iVar2 = FUN_ffff0000be05620c(param_1,PTR_s__soc_toprgu_ffff0000be0d6750), iVar2 < 0)) &&
     (iVar2 = FUN_ffff0000be05620c(param_1,PTR_s__toprgu_ffff0000be0d6758), iVar2 < 0)) {
    pcVar5 = s__s__couldn_t_find_the_drm_node_ffff0000be0c62db;
LAB_ffff0000be02a9c4:
    FUN_ffff0000be05fd98(pcVar5,s_get_dbgtop_base_ffff0000be0c4048);
    return -0x3d;
  }
  local_44 = 0;
  lVar3 = FUN_ffff0000be056494(param_1,iVar2,&DAT_ffff0000be0ae6fc,&local_44);
  if (lVar3 == 0) {
    pcVar5 = s__s__couldn_t_find_property_reg_ffff0000be0b1e45;
    goto LAB_ffff0000be02a9c4;
  }
  uVar8 = *(uint *)(lVar3 + 4);
  uVar1 = (uVar8 & 0xff00ff00) >> 8 | (uVar8 & 0xff00ff) << 8;
  DAT_ffff0000be118180 = (uint *)(ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
  if (uVar8 == 0) {
    pcVar5 = s__s__Failed_to_get_dbgtop_base_ffff0000be0a5b0f;
    goto LAB_ffff0000be02a9c4;
  }
  DAT_ffff0000be118180 = (uint *)((ulong)DAT_ffff0000be118180 | 0xffff000000000000);
  puVar4 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_rgu_timeout_ffff0000be0c4058,&local_44);
  if (puVar4 != (uint *)0x0) {
    uVar8 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
    DAT_ffff0000be0d6760 = uVar8 >> 0x10 | uVar8 << 0x10;
  }
  puVar4 = (uint *)FUN_ffff0000be056494(param_1,iVar2,&DAT_ffff0000be0b1e65,&local_44);
  if (puVar4 != (uint *)0x0) {
    uVar8 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
    DAT_ffff0000be0d6764 = uVar8 >> 0x10 | uVar8 << 0x10;
  }
  puVar4 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_dis_ddr_rsv_mode_ffff0000be0a47fe,&local_44)
  ;
  if (puVar4 != (uint *)0x0) {
    uVar8 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
    DAT_ffff0000be11818c = uVar8 >> 0x10 | uVar8 << 0x10;
  }
  puVar4 = (uint *)FUN_ffff0000be056494(param_1,iVar2,s_sec_write_ffff0000be0aa3d0,&local_44);
  if (puVar4 != (uint *)0x0) {
    uVar8 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
    DAT_ffff0000be118190 = uVar8 >> 0x10 | uVar8 << 0x10;
  }
  FUN_ffff0000be035988(1);
  FUN_ffff0000be035904(DAT_ffff0000be0d6760);
  if (DAT_ffff0000be118180 == (uint *)0x0) {
    if (DAT_ffff0000be11818c == 0) goto LAB_ffff0000be02aa10;
    pcVar5 = s_Failed_ffff0000be0a90cd;
LAB_ffff0000be02aa04:
    pcVar7 = s__s_to_disable_DDR_RSV_MODE_throu_ffff0000be0c401f;
  }
  else {
    uVar8 = 0x1ffff;
    if (DAT_ffff0000be0d6764 != 1) {
      uVar8 = 0x7ffff;
    }
    DAT_ffff0000be118188 = DAT_ffff0000be118180[0x11] & uVar8;
    if (DAT_ffff0000be11818c != 0) {
      FUN_ffff0000be02abb8(DAT_ffff0000be118180,*DAT_ffff0000be118180 & 0xfffffffe | 0x22000000);
      pcVar5 = s_Succeeded_ffff0000be0be340;
      goto LAB_ffff0000be02aa04;
    }
LAB_ffff0000be02aa10:
    uVar6 = FUN_ffff0000be063c7c();
    if ((uVar6 & 1) != 0) {
      FUN_ffff0000be05fd98(s_Not_enable_DDR_RSV_MODE_in_abnor_ffff0000be0be34a);
      goto LAB_ffff0000be02aa70;
    }
    if (DAT_ffff0000be118180 == (uint *)0x0) {
      pcVar5 = s_Failed_ffff0000be0a90cd;
    }
    else {
      FUN_ffff0000be02abb8(DAT_ffff0000be118180,*DAT_ffff0000be118180 | 0x22000001);
      pcVar5 = s_Succeeded_ffff0000be0be340;
    }
    pcVar7 = s__s_to_eable_DDR_RSV_MODE_in_norm_ffff0000be0bd11e;
  }
  FUN_ffff0000be05fd98(pcVar7,pcVar5);
LAB_ffff0000be02aa70:
  iVar2 = FUN_ffff0000be0203a4
                    (s_mtk_dbgtop_shutdown_callback_ffff0000be0a6ddc,FUN_ffff0000be02aaac,0);
  if (iVar2 != 0) {
    FUN_ffff0000be05fd98
              (s__s_register_shutdown_callback_fa_ffff0000be0ae7d8,
               s_dbgtop_drm_init_ffff0000be0ad7dd,iVar2);
    return iVar2;
  }
  return 0;
}


