
char * FUN_ffff00009077f5d0(char *param_1,undefined8 param_2)

{
  long lVar1;
  char *pcVar2;
  char *pcVar3;
  
  pcVar3 = DAT_ffff00009087cfd8;
  if (param_1 != (char *)0x0) {
    pcVar3 = param_1;
  }
  if (pcVar3 != (char *)0x0) {
    lVar1 = FUN_ffff00009077f4ec(pcVar3);
    pcVar3 = pcVar3 + lVar1;
    if (*pcVar3 == '\0') {
      DAT_ffff00009087cfd8 = (char *)0x0;
      pcVar3 = (char *)0x0;
    }
    else {
      pcVar2 = (char *)FUN_ffff00009077f46c(pcVar3,param_2);
      DAT_ffff00009087cfd8 = pcVar2;
      if ((pcVar2 != (char *)0x0) && (*pcVar2 != '\0')) {
        DAT_ffff00009087cfd8 = pcVar2 + 1;
        *pcVar2 = '\0';
      }
    }
  }
  return pcVar3;
}


