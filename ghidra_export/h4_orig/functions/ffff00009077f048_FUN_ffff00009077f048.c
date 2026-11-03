// ffff00009077f048  size=44  FUN_ffff00009077f048

void FUN_ffff00009077f048(long param_1,long param_2)

{
  char cVar1;
  char *pcVar2;
  long lVar3;
  
  pcVar2 = (char *)(param_1 + -1);
  do {
    pcVar2 = pcVar2 + 1;
  } while (*pcVar2 != '\0');
  lVar3 = 0;
  do {
    cVar1 = *(char *)(param_2 + lVar3);
    pcVar2[lVar3] = cVar1;
    lVar3 = lVar3 + 1;
  } while (cVar1 != '\0');
  return;
}


