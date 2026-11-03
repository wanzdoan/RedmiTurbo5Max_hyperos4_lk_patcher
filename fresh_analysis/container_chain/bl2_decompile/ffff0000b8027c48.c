
char * FUN_ffff0000b8027c48(char *param_1)

{
  char cVar1;
  int iVar2;
  char *pcVar3;
  uint uVar4;
  uint uVar5;
  long lVar6;
  long lVar7;
  long lVar8;
  char *pcVar9;
  
  if (((param_1 != (char *)0x0) && (DAT_ffff0000b811cf38 != 0)) && (*DAT_ffff0000b8124f48 != '\0'))
  {
    lVar6 = 0;
    uVar4 = 0;
    do {
      lVar7 = (long)(int)uVar4;
      uVar5 = uVar4;
      if ((int)uVar4 < 0x4001) {
        uVar5 = 0x4000;
      }
      uVar4 = uVar4 + 1;
      lVar8 = (ulong)uVar5 - lVar7;
      pcVar3 = DAT_ffff0000b8124f48 + lVar7 + 1;
      do {
        if (lVar8 == 0) {
          return (char *)0x0;
        }
        cVar1 = *pcVar3;
        uVar4 = uVar4 + 1;
        lVar8 = lVar8 + -1;
        pcVar3 = pcVar3 + 1;
        pcVar9 = param_1;
      } while (cVar1 != '\0');
      do {
        cVar1 = *pcVar9;
        iVar2 = (int)lVar6;
        if (cVar1 != DAT_ffff0000b8124f48[lVar6]) {
          if (((cVar1 != '\0') || (DAT_ffff0000b8124f48[lVar6] != '=')) || (iVar2 + 1 < 0))
          goto LAB_ffff0000b8027cf4;
          goto LAB_ffff0000b8027d18;
        }
        lVar6 = lVar6 + 1;
        pcVar9 = pcVar9 + 1;
      } while (cVar1 != '=');
      if (-1 < (int)lVar6) {
LAB_ffff0000b8027d18:
        return DAT_ffff0000b8124f48 + (iVar2 + 1);
      }
LAB_ffff0000b8027cf4:
      lVar6 = (long)(int)uVar4;
    } while (DAT_ffff0000b8124f48[lVar6] != '\0');
  }
  return (char *)0x0;
}


