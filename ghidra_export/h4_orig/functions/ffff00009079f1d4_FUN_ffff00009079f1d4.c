// ffff00009079f1d4  size=212  FUN_ffff00009079f1d4

int FUN_ffff00009079f1d4(char *param_1,char *param_2)

{
  char cVar1;
  long lVar2;
  int iVar3;
  int iVar4;
  uint uVar5;
  ushort *puVar6;
  ushort *puVar7;
  
  if ((param_1 == (char *)0x0) || (param_2 == (char *)0x0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e064d,0x758,
               s_a______void__0_____b______void___ffff0000907ef114);
  }
  cVar1 = *param_1;
  if (cVar1 != *param_2) {
    if (cVar1 != '\0') {
      return -1;
    }
    return 1;
  }
  uVar5 = *(uint *)(param_1 + 8);
  if (uVar5 <= *(uint *)(param_2 + 8)) {
    if (uVar5 < *(uint *)(param_2 + 8)) {
LAB_ffff00009079f224:
      iVar4 = -1;
      goto LAB_ffff00009079f228;
    }
    if ((int)(uVar5 - 1) < 0) {
      return 0;
    }
    lVar2 = (ulong)(uVar5 - 1) * 2;
    puVar6 = (ushort *)(*(long *)(param_2 + 0x10) + lVar2);
    puVar7 = (ushort *)(*(long *)(param_1 + 0x10) + lVar2);
    while (*puVar7 <= *puVar6) {
      if (*puVar7 < *puVar6) goto LAB_ffff00009079f224;
      puVar7 = puVar7 + -1;
      puVar6 = puVar6 + -1;
      uVar5 = uVar5 - 1;
      if ((int)uVar5 < 1) {
        return 0;
      }
    }
  }
  iVar4 = 1;
LAB_ffff00009079f228:
  iVar3 = -iVar4;
  if (cVar1 == '\0') {
    iVar3 = iVar4;
  }
  return iVar3;
}


