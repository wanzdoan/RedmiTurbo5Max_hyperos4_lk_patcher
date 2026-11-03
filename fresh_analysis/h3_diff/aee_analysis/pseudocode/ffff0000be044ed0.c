
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be044ed0(undefined8 param_1,long param_2)

{
  int iVar1;
  long lVar2;
  uint uVar3;
  uint local_4;
  
  local_4 = 0xffffffff;
  if ((param_2 == 0) ||
     (iVar1 = FUN_ffff0000be05620c(param_2,s__soc_dispsys_config_ffff0000be0c56fd), iVar1 < 0)) {
    uVar3 = 1;
  }
  else {
    lVar2 = FUN_ffff0000be056494(param_2,iVar1,s_pq_path_sel_ffff0000be0ba29c,0);
    if (lVar2 == 0) {
      uVar3 = 0xffffffff;
    }
    else {
      FUN_ffff0000be0607b0(&local_4,lVar2,4);
      uVar3 = (local_4 & 0xff00ff00) >> 8 | (local_4 & 0xff00ff) << 8;
      uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
    }
    if ((int)uVar3 < 2) {
      uVar3 = 1;
    }
  }
  _DAT_ffff000032000034 = uVar3 | uVar3 << 8;
  return;
}


