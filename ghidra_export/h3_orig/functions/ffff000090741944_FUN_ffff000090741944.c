// ffff000090741944  size=220  FUN_ffff000090741944

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

char * FUN_ffff000090741944(char *param_1)

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
  
  if (((param_1 != (char *)0x0) && (_DAT_ffff00009086f3d8 != 0)) && (*_DAT_ffff0000908773e8 != '\0')
     ) {
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
      pcVar3 = _DAT_ffff0000908773e8 + lVar7 + 1;
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
        if (cVar1 != _DAT_ffff0000908773e8[lVar6]) {
          if (((cVar1 != '\0') || (_DAT_ffff0000908773e8[lVar6] != '=')) || (iVar2 + 1 < 0))
          goto LAB_ffff0000907419f0;
          goto LAB_ffff000090741a14;
        }
        lVar6 = lVar6 + 1;
        pcVar9 = pcVar9 + 1;
      } while (cVar1 != '=');
      if (-1 < (int)lVar6) {
LAB_ffff000090741a14:
        return _DAT_ffff0000908773e8 + (iVar2 + 1);
      }
LAB_ffff0000907419f0:
      lVar6 = (long)(int)uVar4;
    } while (_DAT_ffff0000908773e8[lVar6] != '\0');
  }
  return (char *)0x0;
}


