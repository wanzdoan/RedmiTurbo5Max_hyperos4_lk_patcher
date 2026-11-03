
int FUN_ffff000090739740(long param_1)

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
  iVar2 = FUN_ffff00009076da24(param_1,PTR_s__soc_drm_ffff000090819410);
  if (((iVar2 < 0) &&
      (iVar2 = FUN_ffff00009076da24(param_1,PTR_s__soc_toprgu_ffff000090819418), iVar2 < 0)) &&
     (iVar2 = FUN_ffff00009076da24(param_1,PTR_s__toprgu_ffff000090819420), iVar2 < 0)) {
    pcVar5 = s__s__couldn_t_find_the_drm_node_ffff00009080279e;
LAB_ffff00009073990c:
    FUN_ffff00009077e11c(pcVar5,s_get_dbgtop_base_ffff0000907ff1aa);
    return -0x3d;
  }
  local_44 = 0;
  lVar3 = FUN_ffff00009076df9c(param_1,iVar2,&DAT_ffff0000907df645,&local_44);
  if (lVar3 == 0) {
    pcVar5 = s__s__couldn_t_find_property_reg_ffff0000907e4ac8;
    goto LAB_ffff00009073990c;
  }
  uVar8 = *(uint *)(lVar3 + 4);
  uVar1 = (uVar8 & 0xff00ff00) >> 8 | (uVar8 & 0xff00ff) << 8;
  DAT_ffff00009086dec0 = (uint *)(ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
  if (uVar8 == 0) {
    pcVar5 = s__s__Failed_to_get_dbgtop_base_ffff0000907d2d04;
    goto LAB_ffff00009073990c;
  }
  DAT_ffff00009086dec0 = (uint *)((ulong)DAT_ffff00009086dec0 | 0xffff000000000000);
  puVar4 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_rgu_timeout_ffff0000907ff1ba,&local_44);
  if (puVar4 != (uint *)0x0) {
    uVar8 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
    DAT_ffff000090819428 = uVar8 >> 0x10 | uVar8 << 0x10;
  }
  puVar4 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,&DAT_ffff0000907e4ae8,&local_44);
  if (puVar4 != (uint *)0x0) {
    uVar8 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
    DAT_ffff00009081942c = uVar8 >> 0x10 | uVar8 << 0x10;
  }
  puVar4 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_dis_ddr_rsv_mode_ffff0000907d1201,&local_44)
  ;
  if (puVar4 != (uint *)0x0) {
    uVar8 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
    DAT_ffff00009086decc = uVar8 >> 0x10 | uVar8 << 0x10;
  }
  puVar4 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_sec_write_ffff0000907d931a,&local_44);
  if (puVar4 != (uint *)0x0) {
    uVar8 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
    DAT_ffff00009086ded0 = uVar8 >> 0x10 | uVar8 << 0x10;
  }
  FUN_ffff0000907463cc(1);
  FUN_ffff000090746348(DAT_ffff000090819428);
  if (DAT_ffff00009086dec0 == (uint *)0x0) {
    if (DAT_ffff00009086decc == 0) goto LAB_ffff000090739958;
    pcVar5 = s_Failed_ffff0000907d7b14;
LAB_ffff00009073994c:
    pcVar7 = s__s_to_disable_DDR_RSV_MODE_throu_ffff0000907ff181;
  }
  else {
    uVar8 = 0x1ffff;
    if (DAT_ffff00009081942c != 1) {
      uVar8 = 0x7ffff;
    }
    DAT_ffff00009086dec8 = DAT_ffff00009086dec0[0x11] & uVar8;
    if (DAT_ffff00009086decc != 0) {
      FUN_ffff000090739b00(DAT_ffff00009086dec0,*DAT_ffff00009086dec0 & 0xfffffffe | 0x22000000);
      pcVar5 = s_Succeeded_ffff0000907f64f3;
      goto LAB_ffff00009073994c;
    }
LAB_ffff000090739958:
    uVar6 = FUN_ffff000090781c0c();
    if ((uVar6 & 1) != 0) {
      FUN_ffff00009077e11c(s_Not_enable_DDR_RSV_MODE_in_abnor_ffff0000907f64fd);
      goto LAB_ffff0000907399b8;
    }
    if (DAT_ffff00009086dec0 == (uint *)0x0) {
      pcVar5 = s_Failed_ffff0000907d7b14;
    }
    else {
      FUN_ffff000090739b00(DAT_ffff00009086dec0,*DAT_ffff00009086dec0 | 0x22000001);
      pcVar5 = s_Succeeded_ffff0000907f64f3;
    }
    pcVar7 = s__s_to_eable_DDR_RSV_MODE_in_norm_ffff0000907f48c1;
  }
  FUN_ffff00009077e11c(pcVar7,pcVar5);
LAB_ffff0000907399b8:
  iVar2 = FUN_ffff000090722730
                    (s_mtk_dbgtop_shutdown_callback_ffff0000907d463c,FUN_ffff0000907399f4,0);
  if (iVar2 != 0) {
    FUN_ffff00009077e11c
              (s__s_register_shutdown_callback_fa_ffff0000907dfa33,
               s_dbgtop_drm_init_ffff0000907de203,iVar2);
    return iVar2;
  }
  return 0;
}


