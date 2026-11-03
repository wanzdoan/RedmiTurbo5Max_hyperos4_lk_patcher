// ffff0000907acaf8  size=300  FUN_ffff0000907acaf8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907acaf8(uint param_1,uint param_2)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  uint *puVar4;
  
  iVar1 = FUN_ffff0000907ab798();
  if (iVar1 == 0) {
    iVar1 = 0x104009;
    if (DAT_ffff00009090af30 == '\x01') {
      uVar3 = (ulong)_DAT_ffff00009090af34;
      if (_DAT_ffff00009090af34 != 0) {
        puVar4 = &DAT_ffff000090837558;
        do {
          if (puVar4[-6] == param_1) {
            if (puVar4[-5] != 0) {
              if (puVar4[-1] < param_2) {
                iVar1 = 0x104002;
                iVar2 = FUN_ffff0000907ad53c();
                if (iVar2 == 0) {
                  return 0x104002;
                }
                goto LAB_ffff0000907acb78;
              }
              if ((puVar4[-2] < param_2) && (param_2 < *puVar4)) {
                *puVar4 = param_2;
                iVar1 = FUN_ffff0000907ad53c();
                if (iVar1 == 0) {
                  return 0;
                }
                FUN_ffff00009077de54
                          (s__avb__set_ver__d_to_group__d_don_ffff0000907eefca,*puVar4,param_1);
                return 0;
              }
            }
            iVar1 = FUN_ffff0000907ad53c();
            if (iVar1 != 0) {
              FUN_ffff00009077de54(s__avb__bypass_update_avb_otp_ver___ffff00009080329d,param_1);
            }
            return 0;
          }
          puVar4 = puVar4 + 7;
          uVar3 = uVar3 - 1;
        } while (uVar3 != 0);
      }
    }
    else {
      iVar1 = 0x10400c;
    }
  }
  iVar2 = FUN_ffff0000907ad53c();
  if (iVar2 != 0) {
LAB_ffff0000907acb78:
    FUN_ffff00009077de54(s__avb__fail_to_set_min_ver__group_ffff0000907fe3ff,param_1,iVar1);
  }
  return iVar1;
}


