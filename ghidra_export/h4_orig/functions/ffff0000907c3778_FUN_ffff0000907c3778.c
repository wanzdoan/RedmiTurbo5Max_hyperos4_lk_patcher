// ffff0000907c3778  size=412  FUN_ffff0000907c3778

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907c3778(uint param_1,undefined8 param_2,uint *param_3,int *param_4)

{
  uint uVar1;
  int *piVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  int iVar7;
  int iVar8;
  
  piVar2 = &DAT_ffff0000908384d8;
  if (*param_4 != 0) {
    piVar2 = &DAT_ffff0000908384dc;
  }
  iVar7 = *piVar2;
  if (iVar7 == -1) {
    *piVar2 = 0;
    iVar7 = param_4[1];
    iVar3 = param_4[2];
    uVar6 = *param_3;
    if (DAT_ffff00009090d964 == '\0') {
      iVar4 = 0x2b;
    }
    else {
      iVar4 = FUN_ffff00009071c4e8();
      if (iVar4 == 0) {
        iVar4 = 1;
      }
      else {
        iVar5 = FUN_ffff00009071c4e8();
        iVar4 = 0x2a;
        if (iVar5 == 7) {
          iVar4 = 1;
        }
      }
    }
    iVar5 = 0;
    if (iVar4 != 0) {
      iVar5 = (int)uVar6 / iVar4;
    }
    iVar8 = iVar4;
    if (uVar6 != iVar5 * iVar4) {
      iVar8 = iVar4 + 0x12;
      iVar5 = 0;
      if (iVar8 != 0) {
        iVar5 = (int)uVar6 / iVar8;
      }
      if (uVar6 != iVar5 * iVar8) {
        iVar8 = iVar4 + 0x2f;
        iVar4 = 0;
        if (iVar8 != 0) {
          iVar4 = (int)uVar6 / iVar8;
        }
        if (uVar6 != iVar4 * iVar8) {
          FUN_ffff00009077e11c
                    (s___s__Error_Logo_entries_not_corr_ffff0000907fe7e9,
                     s_calculate_logo_offset_ffff0000907faee8);
          *piVar2 = -1;
          return -1;
        }
      }
    }
    uVar6 = *param_3;
    if (param_1 < uVar6) {
      do {
        uVar1 = param_1 + 3;
        if (uVar6 - 1 <= param_1) {
          uVar1 = 1;
        }
        iVar4 = FUN_ffff0000907c3648
                          ((ulong)param_3[param_1 + 2] + (long)param_3,param_2,
                           param_3[uVar1] - param_3[param_1 + 2],param_4[8]);
        if (iVar4 == iVar3 * iVar7 * 4) {
          _DAT_ffff00009090d968 = 0x20;
          break;
        }
        if (iVar4 == iVar3 * iVar7 * 2) {
          _DAT_ffff00009090d968 = 0x10;
          break;
        }
        param_1 = param_1 + iVar8;
        *piVar2 = *piVar2 + iVar8;
        uVar6 = *param_3;
      } while (param_1 < uVar6);
    }
    iVar7 = *piVar2;
  }
  return iVar7;
}


