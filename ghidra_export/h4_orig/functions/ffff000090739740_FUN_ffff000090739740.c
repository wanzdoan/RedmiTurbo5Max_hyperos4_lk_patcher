// ffff000090739740  size=692  FUN_ffff000090739740

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090739740(long param_1)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  long lVar4;
  uint *puVar5;
  char *pcVar6;
  ulong uVar7;
  char *pcVar8;
  undefined4 local_44;
  
  if (param_1 == 0) {
    return -0x3d;
  }
  iVar3 = FUN_ffff00009076da24(param_1,PTR_s__soc_drm_ffff000090819410);
  if (((iVar3 < 0) &&
      (iVar3 = FUN_ffff00009076da24(param_1,PTR_s__soc_toprgu_ffff000090819418), iVar3 < 0)) &&
     (iVar3 = FUN_ffff00009076da24(param_1,PTR_s__toprgu_ffff000090819420), iVar3 < 0)) {
    pcVar6 = s__s__couldn_t_find_the_drm_node_ffff00009080279e;
LAB_ffff00009073990c:
    FUN_ffff00009077e11c(pcVar6,s_get_dbgtop_base_ffff0000907ff1aa);
    return -0x3d;
  }
  local_44 = 0;
  lVar4 = FUN_ffff00009076df9c(param_1,iVar3,&DAT_ffff0000907df645,&local_44);
  if (lVar4 == 0) {
    pcVar6 = s__s__couldn_t_find_property_reg_ffff0000907e4ac8;
    goto LAB_ffff00009073990c;
  }
  uVar1 = *(uint *)(lVar4 + 4);
  uVar2 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
  _DAT_ffff00009086dec0 = (uint *)(ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
  if (uVar1 == 0) {
    pcVar6 = s__s__Failed_to_get_dbgtop_base_ffff0000907d2d04;
    goto LAB_ffff00009073990c;
  }
  _DAT_ffff00009086dec0 = (uint *)((ulong)_DAT_ffff00009086dec0 | 0xffff000000000000);
  puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_rgu_timeout_ffff0000907ff1ba,&local_44);
  if (puVar5 != (uint *)0x0) {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    DAT_ffff000090819428 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,&DAT_ffff0000907e4ae8,&local_44);
  if (puVar5 != (uint *)0x0) {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    DAT_ffff00009081942c = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_dis_ddr_rsv_mode_ffff0000907d1201,&local_44)
  ;
  if (puVar5 != (uint *)0x0) {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    _DAT_ffff00009086decc = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar3,s_sec_write_ffff0000907d931a,&local_44);
  if (puVar5 != (uint *)0x0) {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    _DAT_ffff00009086ded0 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff0000907463cc(1);
  FUN_ffff000090746348(DAT_ffff000090819428);
  if (_DAT_ffff00009086dec0 == (uint *)0x0) {
    if (_DAT_ffff00009086decc == 0) goto LAB_ffff000090739958;
    pcVar6 = s_Failed_ffff0000907d7b14;
LAB_ffff00009073994c:
    pcVar8 = s__s_to_disable_DDR_RSV_MODE_throu_ffff0000907ff181;
  }
  else {
    _DAT_ffff00009086dec8 = 0x1ffff;
    if (DAT_ffff00009081942c != 1) {
      _DAT_ffff00009086dec8 = 0x7ffff;
    }
    _DAT_ffff00009086dec8 = _DAT_ffff00009086dec0[0x11] & _DAT_ffff00009086dec8;
    if (_DAT_ffff00009086decc != 0) {
      FUN_ffff000090739b00(_DAT_ffff00009086dec0,*_DAT_ffff00009086dec0 & 0xfffffffe | 0x22000000);
      pcVar6 = s_Succeeded_ffff0000907f64f3;
      goto LAB_ffff00009073994c;
    }
LAB_ffff000090739958:
    uVar7 = FUN_ffff000090781c0c();
    if ((uVar7 & 1) != 0) {
      FUN_ffff00009077e11c(s_Not_enable_DDR_RSV_MODE_in_abnor_ffff0000907f64fd);
      goto LAB_ffff0000907399b8;
    }
    if (_DAT_ffff00009086dec0 == (uint *)0x0) {
      pcVar6 = s_Failed_ffff0000907d7b14;
    }
    else {
      FUN_ffff000090739b00(_DAT_ffff00009086dec0,*_DAT_ffff00009086dec0 | 0x22000001);
      pcVar6 = s_Succeeded_ffff0000907f64f3;
    }
    pcVar8 = s__s_to_eable_DDR_RSV_MODE_in_norm_ffff0000907f48c1;
  }
  FUN_ffff00009077e11c(pcVar8,pcVar6);
LAB_ffff0000907399b8:
  iVar3 = FUN_ffff000090722730
                    (s_mtk_dbgtop_shutdown_callback_ffff0000907d463c,FUN_ffff0000907399f4,0);
  if (iVar3 != 0) {
    FUN_ffff00009077e11c
              (s__s_register_shutdown_callback_fa_ffff0000907dfa33,
               s_dbgtop_drm_init_ffff0000907de203,iVar3);
    return iVar3;
  }
  return 0;
}


