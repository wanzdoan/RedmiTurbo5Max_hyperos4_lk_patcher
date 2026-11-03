// ffff00009077ee0c  size=32  FUN_ffff00009077ee0c

void FUN_ffff00009077ee0c(long param_1,long param_2)

{
  char cVar1;
  long lVar2;
  
  lVar2 = 0;
  do {
    cVar1 = *(char *)(param_2 + lVar2);
    *(char *)(param_1 + lVar2) = cVar1;
    lVar2 = lVar2 + 1;
  } while (cVar1 != '\0');
  return;
}


