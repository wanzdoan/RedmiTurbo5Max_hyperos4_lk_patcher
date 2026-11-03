
int FUN_ffff0000b8082ba0(uint param_1,undefined8 param_2,uint *param_3,int *param_4)

{
  uint uVar1;
  int *piVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  int iVar7;
  int iVar8;
  
  piVar2 = &DAT_ffff0000b8107da0;
  if (*param_4 != 0) {
    piVar2 = &DAT_ffff0000b8107da4;
  }
  iVar7 = *piVar2;
  if (iVar7 == -1) {
    *piVar2 = 0;
    iVar7 = param_4[1];
    iVar3 = param_4[2];
    uVar6 = *param_3;
    if (DAT_ffff0000b81b9b54 == '\0') {
      iVar4 = 0x2b;
    }
    else {
      iVar4 = FUN_ffff0000b800e1a4();
      if (iVar4 == 0) {
        iVar4 = 1;
      }
      else {
        iVar5 = FUN_ffff0000b800e1a4();
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
          FUN_ffff0000b804c33c
                    (s___s__Error_Logo_entries_not_corr_ffff0000b809f12b,
                     s_calculate_logo_offset_ffff0000b809d26c);
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
        iVar4 = FUN_ffff0000b8082a54
                          ((ulong)param_3[param_1 + 2] + (long)param_3,param_2,
                           param_3[uVar1] - param_3[param_1 + 2],param_4[8]);
        if (iVar4 == iVar3 * iVar7 * 4) {
          DAT_ffff0000b81b9b58 = 0x20;
          break;
        }
        if (iVar4 == iVar3 * iVar7 * 2) {
          DAT_ffff0000b81b9b58 = 0x10;
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


