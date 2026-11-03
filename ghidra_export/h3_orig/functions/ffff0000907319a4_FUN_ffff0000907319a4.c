// ffff0000907319a4  size=424  FUN_ffff0000907319a4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8
FUN_ffff0000907319a4(undefined4 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  undefined8 uVar1;
  int iVar2;
  
  switch(param_1) {
  case 0:
    if (param_4 == 0) {
      iVar2 = -1;
    }
    else {
      if (_DAT_ffff00009086d9e0 <= _DAT_ffff00009086d9e4) {
LAB_ffff000090731aec:
        FUN_ffff00009077de54
                  (s__s__d___CCCI_Padding_MPU_region_n_ffff0000907ddf9a,
                   s_md_mpu_cfg_helper_ffff00009080093b,0x2e);
        return 0xffffffff;
      }
      iVar2 = *(int *)((long)_DAT_ffff00009086d9e4 * 4 + -0xffff6f792640);
      _DAT_ffff00009086d9e4 = _DAT_ffff00009086d9e4 + 1;
      if (iVar2 < 0) goto LAB_ffff000090731aec;
    }
    uVar1 = 0;
    break;
  case 1:
    if (param_4 != 0) {
      if (_DAT_ffff00009086d9e4 < _DAT_ffff00009086d9e0) {
        iVar2 = *(int *)((long)_DAT_ffff00009086d9e4 * 4 + -0xffff6f792640);
        _DAT_ffff00009086d9e4 = _DAT_ffff00009086d9e4 + 1;
        if (-1 < iVar2) {
          uVar1 = 3;
          break;
        }
      }
      goto LAB_ffff000090731aec;
    }
    iVar2 = -1;
    uVar1 = 3;
    break;
  case 2:
    if (param_4 != 0) {
      if (_DAT_ffff00009086d9e4 < _DAT_ffff00009086d9e0) {
        iVar2 = *(int *)((long)_DAT_ffff00009086d9e4 * 4 + -0xffff6f792640);
        _DAT_ffff00009086d9e4 = _DAT_ffff00009086d9e4 + 1;
        if (-1 < iVar2) {
          uVar1 = 2;
          break;
        }
      }
      goto LAB_ffff000090731aec;
    }
    iVar2 = -1;
    uVar1 = 2;
    break;
  case 3:
    if (param_4 == 0) {
      iVar2 = -1;
    }
    else {
      if (_DAT_ffff00009086d9e0 <= _DAT_ffff00009086d9e4) goto LAB_ffff000090731aec;
      iVar2 = *(int *)((long)_DAT_ffff00009086d9e4 * 4 + -0xffff6f792640);
      _DAT_ffff00009086d9e4 = _DAT_ffff00009086d9e4 + 1;
      if (iVar2 < 0) goto LAB_ffff000090731aec;
    }
    uVar1 = 1;
    break;
  default:
    FUN_ffff00009072ff90
              (s__CCCI___s__d__MD_region__d_not_i_ffff0000907eab30,
               s_apply_md_mpu_region_setting_ffff0000907d4487,0x70,param_1);
    return 0xffffffff;
  }
  uVar1 = FUN_ffff00009079835c(uVar1,param_2,param_3,iVar2);
  return uVar1;
}


