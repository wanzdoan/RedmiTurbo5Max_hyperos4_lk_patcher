// ffff0000907acdb8  size=300  FUN_ffff0000907acdb8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907acdb8(uint param_1,uint param_2)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  uint *puVar4;
  
  iVar1 = FUN_ffff0000907aba58();
  if (iVar1 == 0) {
    iVar1 = 0x104009;
    if (DAT_ffff00009090aff0 == '\x01') {
      uVar3 = (ulong)_DAT_ffff00009090aff4;
      if (_DAT_ffff00009090aff4 != 0) {
        puVar4 = &DAT_ffff000090837578;
        do {
          if (puVar4[-6] == param_1) {
            if (puVar4[-5] != 0) {
              if (puVar4[-1] < param_2) {
                iVar1 = 0x104002;
                iVar2 = FUN_ffff0000907ad7fc();
                if (iVar2 == 0) {
                  return 0x104002;
                }
                goto LAB_ffff0000907ace38;
              }
              if ((puVar4[-2] < param_2) && (param_2 < *puVar4)) {
                *puVar4 = param_2;
                iVar1 = FUN_ffff0000907ad7fc();
                if (iVar1 == 0) {
                  return 0;
                }
                FUN_ffff00009077e11c
                          (s__avb__set_ver__d_to_group__d_don_ffff0000907ef152,*puVar4,param_1);
                return 0;
              }
            }
            iVar1 = FUN_ffff0000907ad7fc();
            if (iVar1 != 0) {
              FUN_ffff00009077e11c(s__avb__bypass_update_avb_otp_ver___ffff0000908034a1,param_1);
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
  iVar2 = FUN_ffff0000907ad7fc();
  if (iVar2 != 0) {
LAB_ffff0000907ace38:
    FUN_ffff00009077e11c(s__avb__fail_to_set_min_ver__group_ffff0000907fe5a7,param_1,iVar1);
  }
  return iVar1;
}


