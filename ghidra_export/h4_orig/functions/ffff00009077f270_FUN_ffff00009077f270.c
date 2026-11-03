// ffff00009077f270  size=60  FUN_ffff00009077f270

void FUN_ffff00009077f270(long param_1,long param_2,long param_3)

{
  char cVar1;
  char *pcVar2;
  long lVar3;
  
  if (param_3 != 0) {
    pcVar2 = (char *)(param_1 + -1);
    do {
      pcVar2 = pcVar2 + 1;
    } while (*pcVar2 != '\0');
    lVar3 = 0;
    do {
      cVar1 = *(char *)(param_2 + lVar3);
      pcVar2[lVar3] = cVar1;
      if (cVar1 == '\0') {
        return;
      }
      lVar3 = lVar3 + 1;
    } while (param_3 != lVar3);
    pcVar2[lVar3] = '\0';
  }
  return;
}


