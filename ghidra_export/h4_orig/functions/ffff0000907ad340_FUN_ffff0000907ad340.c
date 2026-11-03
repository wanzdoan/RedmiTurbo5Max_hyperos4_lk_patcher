// ffff0000907ad340  size=252  FUN_ffff0000907ad340

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

char * FUN_ffff0000907ad340(char *param_1,char *param_2)

{
  char cVar1;
  long lVar2;
  ulong uVar3;
  ulong uVar4;
  char *pcVar5;
  char *pcVar6;
  char cVar7;
  
  if (param_1 != (char *)0x0) {
    if (*param_1 == '\0') {
      _DAT_ffff00009090b000 = 0;
    }
    else {
      uVar3 = 0;
      do {
        lVar2 = uVar3 + 1;
        uVar3 = uVar3 + 1;
      } while (param_1[lVar2] != '\0');
      _DAT_ffff00009090b000 = uVar3;
      if (uVar3 != 0) {
        uVar4 = 0;
        pcVar5 = param_1;
        do {
          cVar1 = *param_2;
          if (cVar1 != '\0') {
            cVar7 = *pcVar5;
            pcVar6 = param_2;
            do {
              pcVar6 = pcVar6 + 1;
              if (cVar7 == cVar1) {
                cVar7 = '\0';
                *pcVar5 = '\0';
              }
              cVar1 = *pcVar6;
            } while (cVar1 != '\0');
          }
          pcVar5 = pcVar5 + 1;
          uVar4 = (ulong)((int)uVar4 + 1);
        } while (uVar4 < uVar3);
      }
    }
    _DAT_ffff00009090b008 = param_1;
    return param_1;
  }
  if (_DAT_ffff00009090b008 != (char *)0x0) {
    cVar1 = *_DAT_ffff00009090b008;
    while (cVar1 != '\0') {
      _DAT_ffff00009090b008 = _DAT_ffff00009090b008 + 1;
      _DAT_ffff00009090b000 = _DAT_ffff00009090b000 + -1;
      cVar1 = *_DAT_ffff00009090b008;
    }
    if (_DAT_ffff00009090b000 != 0) {
      _DAT_ffff00009090b008 = _DAT_ffff00009090b008 + 1;
      _DAT_ffff00009090b000 = _DAT_ffff00009090b000 + -1;
      return _DAT_ffff00009090b008;
    }
    _DAT_ffff00009090b008 = (char *)0x0;
    return (char *)0x0;
  }
  return (char *)0x0;
}


