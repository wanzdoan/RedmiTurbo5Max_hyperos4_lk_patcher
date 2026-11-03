// ffff00009079c338  size=460  FUN_ffff00009079c338

ulong FUN_ffff00009079c338(long param_1,ushort param_2,char *param_3)

{
  ushort uVar1;
  uint uVar2;
  bool bVar3;
  ulong uVar4;
  ushort *puVar5;
  uint uVar6;
  ulong uVar7;
  ulong uVar8;
  
  if ((param_1 == 0) || (param_3 == (char *)0x0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e05aa,0x23a,
               s_a______void__0_____b______void___ffff0000907eefa3);
  }
  uVar4 = FUN_ffff00009079bf14(param_1,param_3);
  if ((int)uVar4 != 0) {
    return uVar4;
  }
  uVar6 = *(uint *)(param_3 + 8);
  uVar4 = (ulong)uVar6;
  if (*param_3 != '\0') {
    puVar5 = *(ushort **)(param_3 + 0x10);
    uVar1 = *puVar5;
    if ((uVar6 < 2) && (uVar1 < param_2)) {
      *param_3 = '\0';
      *puVar5 = param_2 - uVar1;
      return 0;
    }
    uVar2 = (uVar1 | 0x10000) - (uint)param_2;
    bVar3 = uVar2 < 0x10000;
    *puVar5 = (ushort)uVar2;
    if ((uVar2 >> 0x10 == 0) && (1 < uVar6)) {
      uVar8 = 1;
      do {
        uVar7 = uVar8 + 1;
        uVar1 = puVar5[uVar8];
        bVar3 = uVar1 == 0;
        puVar5[uVar8] = uVar1 - 1;
        uVar8 = uVar7;
      } while (uVar1 == 0 && uVar7 < uVar4);
    }
    if (1 < uVar6) {
      do {
        if (puVar5[uVar4 - 1] != 0) goto LAB_ffff00009079c488;
        uVar4 = uVar4 - 1;
      } while (1 < (uint)uVar4);
      uVar4 = 1;
    }
LAB_ffff00009079c488:
    uVar6 = 0xfffffffd;
    if (!bVar3) {
      uVar6 = 0;
    }
    *(int *)(param_3 + 8) = (int)uVar4;
    return (ulong)uVar6;
  }
  puVar5 = *(ushort **)(param_3 + 0x10);
  uVar2 = (uint)*puVar5 + (uint)param_2;
  *puVar5 = (ushort)uVar2;
  if ((uVar6 < 2) || (uVar2 < 0x10000)) {
    if (uVar2 < 0x10000) {
      return 0;
    }
    uVar8 = 1;
  }
  else {
    uVar7 = 1;
    do {
      uVar8 = uVar7 + 1;
      uVar1 = puVar5[uVar7];
      uVar2 = uVar1 + 1;
      puVar5[uVar7] = (ushort)uVar2;
      if (uVar4 <= uVar8) break;
      uVar7 = uVar8;
    } while (uVar2 >> 0x10 != 0);
    if (uVar1 != 0xffff) {
      return 0;
    }
  }
  if (uVar6 != 0xffffffff) {
    uVar6 = uVar6 + 1;
    if (*(uint *)(param_3 + 4) < uVar6) {
      uVar4 = FUN_ffff00009079f2d8(param_3,uVar6);
      if ((int)uVar4 != 0) {
        return uVar4;
      }
      puVar5 = *(ushort **)(param_3 + 0x10);
    }
    *(uint *)(param_3 + 8) = uVar6;
  }
  puVar5[uVar8] = 1;
  return 0;
}


