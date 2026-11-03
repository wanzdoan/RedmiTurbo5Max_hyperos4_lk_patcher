// ffff00009077f308  size=132  FUN_ffff00009077f308

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

char * FUN_ffff00009077f308(char *param_1,undefined8 param_2)

{
  long lVar1;
  char *pcVar2;
  char *pcVar3;
  
  pcVar3 = _DAT_ffff00009087cf40;
  if (param_1 != (char *)0x0) {
    pcVar3 = param_1;
  }
  if (pcVar3 != (char *)0x0) {
    lVar1 = FUN_ffff00009077f224(pcVar3);
    pcVar3 = pcVar3 + lVar1;
    if (*pcVar3 == '\0') {
      _DAT_ffff00009087cf40 = (char *)0x0;
      pcVar3 = (char *)0x0;
    }
    else {
      pcVar2 = (char *)FUN_ffff00009077f1a4(pcVar3,param_2);
      _DAT_ffff00009087cf40 = pcVar2;
      if ((pcVar2 != (char *)0x0) && (*pcVar2 != '\0')) {
        _DAT_ffff00009087cf40 = pcVar2 + 1;
        *pcVar2 = '\0';
      }
    }
  }
  return pcVar3;
}


