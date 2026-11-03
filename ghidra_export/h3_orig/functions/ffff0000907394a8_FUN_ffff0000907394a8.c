// ffff0000907394a8  size=692  FUN_ffff0000907394a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907394a8(long param_1)

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
  iVar3 = FUN_ffff00009076d75c(param_1,PTR_s__soc_drm_ffff0000908193f0);
  if (((iVar3 < 0) &&
      (iVar3 = FUN_ffff00009076d75c(param_1,PTR_s__soc_toprgu_ffff0000908193f8), iVar3 < 0)) &&
     (iVar3 = FUN_ffff00009076d75c(param_1,PTR_s__toprgu_ffff000090819400), iVar3 < 0)) {
    pcVar6 = s__s__couldn_t_find_the_drm_node_ffff00009080259a;
LAB_ffff000090739674:
    FUN_ffff00009077de54(pcVar6,s_get_dbgtop_base_ffff0000907ff002);
    return -0x3d;
  }
  local_44 = 0;
  lVar4 = FUN_ffff00009076dcd4(param_1,iVar3,&DAT_ffff0000907df5a2,&local_44);
  if (lVar4 == 0) {
    pcVar6 = s__s__couldn_t_find_property_reg_ffff0000907e49c4;
    goto LAB_ffff000090739674;
  }
  uVar1 = *(uint *)(lVar4 + 4);
  uVar2 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
  _DAT_ffff00009086de28 = (uint *)(ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
  if (uVar1 == 0) {
    pcVar6 = s__s__Failed_to_get_dbgtop_base_ffff0000907d2cc7;
    goto LAB_ffff000090739674;
  }
  _DAT_ffff00009086de28 = (uint *)((ulong)_DAT_ffff00009086de28 | 0xffff000000000000);
  puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_rgu_timeout_ffff0000907ff012,&local_44);
  if (puVar5 != (uint *)0x0) {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    DAT_ffff000090819408 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,&DAT_ffff0000907e49e4,&local_44);
  if (puVar5 != (uint *)0x0) {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    DAT_ffff00009081940c = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_dis_ddr_rsv_mode_ffff0000907d11c4,&local_44)
  ;
  if (puVar5 != (uint *)0x0) {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    _DAT_ffff00009086de34 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar3,s_sec_write_ffff0000907d9283,&local_44);
  if (puVar5 != (uint *)0x0) {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    _DAT_ffff00009086de38 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  FUN_ffff000090746134(1);
  FUN_ffff0000907460b0(DAT_ffff000090819408);
  if (_DAT_ffff00009086de28 == (uint *)0x0) {
    if (_DAT_ffff00009086de34 == 0) goto LAB_ffff0000907396c0;
    pcVar6 = s_Failed_ffff0000907d7a7e;
LAB_ffff0000907396b4:
    pcVar8 = s__s_to_disable_DDR_RSV_MODE_throu_ffff0000907fefd9;
  }
  else {
    _DAT_ffff00009086de30 = 0x1ffff;
    if (DAT_ffff00009081940c != 1) {
      _DAT_ffff00009086de30 = 0x7ffff;
    }
    _DAT_ffff00009086de30 = _DAT_ffff00009086de28[0x11] & _DAT_ffff00009086de30;
    if (_DAT_ffff00009086de34 != 0) {
      FUN_ffff000090739868(_DAT_ffff00009086de28,*_DAT_ffff00009086de28 & 0xfffffffe | 0x22000000);
      pcVar6 = s_Succeeded_ffff0000907f6369;
      goto LAB_ffff0000907396b4;
    }
LAB_ffff0000907396c0:
    uVar7 = FUN_ffff000090781944();
    if ((uVar7 & 1) != 0) {
      FUN_ffff00009077de54(s_Not_enable_DDR_RSV_MODE_in_abnor_ffff0000907f6373);
      goto LAB_ffff000090739720;
    }
    if (_DAT_ffff00009086de28 == (uint *)0x0) {
      pcVar6 = s_Failed_ffff0000907d7a7e;
    }
    else {
      FUN_ffff000090739868(_DAT_ffff00009086de28,*_DAT_ffff00009086de28 | 0x22000001);
      pcVar6 = s_Succeeded_ffff0000907f6369;
    }
    pcVar8 = s__s_to_eable_DDR_RSV_MODE_in_norm_ffff0000907f4754;
  }
  FUN_ffff00009077de54(pcVar8,pcVar6);
LAB_ffff000090739720:
  iVar3 = FUN_ffff0000907224ec
                    (s_mtk_dbgtop_shutdown_callback_ffff0000907d45ff,FUN_ffff00009073975c,0);
  if (iVar3 != 0) {
    FUN_ffff00009077de54
              (s__s_register_shutdown_callback_fa_ffff0000907df990,
               s_dbgtop_drm_init_ffff0000907de160,iVar3);
    return iVar3;
  }
  return 0;
}


