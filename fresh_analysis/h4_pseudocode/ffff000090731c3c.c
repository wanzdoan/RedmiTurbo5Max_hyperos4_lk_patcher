
undefined8
FUN_ffff000090731c3c(undefined4 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  undefined8 uVar1;
  int iVar2;
  
  switch(param_1) {
  case 0:
    if (param_4 == 0) {
      iVar2 = -1;
    }
    else {
      if (DAT_ffff00009086da78 <= DAT_ffff00009086da7c) {
LAB_ffff000090731d84:
        FUN_ffff00009077e11c
                  (s__s__d___CCCI_Padding_MPU_region_n_ffff0000907de03d,
                   s_md_mpu_cfg_helper_ffff000090800b17,0x2e);
        return 0xffffffff;
      }
      iVar2 = *(int *)(&DAT_ffff00009086da58 + (long)DAT_ffff00009086da7c * 4);
      DAT_ffff00009086da7c = DAT_ffff00009086da7c + 1;
      if (iVar2 < 0) goto LAB_ffff000090731d84;
    }
    uVar1 = 0;
    break;
  case 1:
    if (param_4 != 0) {
      if (DAT_ffff00009086da7c < DAT_ffff00009086da78) {
        iVar2 = *(int *)(&DAT_ffff00009086da58 + (long)DAT_ffff00009086da7c * 4);
        DAT_ffff00009086da7c = DAT_ffff00009086da7c + 1;
        if (-1 < iVar2) {
          uVar1 = 3;
          break;
        }
      }
      goto LAB_ffff000090731d84;
    }
    iVar2 = -1;
    uVar1 = 3;
    break;
  case 2:
    if (param_4 != 0) {
      if (DAT_ffff00009086da7c < DAT_ffff00009086da78) {
        iVar2 = *(int *)(&DAT_ffff00009086da58 + (long)DAT_ffff00009086da7c * 4);
        DAT_ffff00009086da7c = DAT_ffff00009086da7c + 1;
        if (-1 < iVar2) {
          uVar1 = 2;
          break;
        }
      }
      goto LAB_ffff000090731d84;
    }
    iVar2 = -1;
    uVar1 = 2;
    break;
  case 3:
    if (param_4 == 0) {
      iVar2 = -1;
    }
    else {
      if (DAT_ffff00009086da78 <= DAT_ffff00009086da7c) goto LAB_ffff000090731d84;
      iVar2 = *(int *)(&DAT_ffff00009086da58 + (long)DAT_ffff00009086da7c * 4);
      DAT_ffff00009086da7c = DAT_ffff00009086da7c + 1;
      if (iVar2 < 0) goto LAB_ffff000090731d84;
    }
    uVar1 = 1;
    break;
  default:
    FUN_ffff000090730228
              (s__CCCI___s__d__MD_region__d_not_i_ffff0000907eacb3,
               s_apply_md_mpu_region_setting_ffff0000907d44c4,0x70,param_1);
    return 0xffffffff;
  }
  uVar1 = FUN_ffff00009079861c(uVar1,param_2,param_3,iVar2);
  return uVar1;
}


