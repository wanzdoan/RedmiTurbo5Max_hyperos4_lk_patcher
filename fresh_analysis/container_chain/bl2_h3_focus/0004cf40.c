// File offset entry: 0x4cf40

long FUN_ffff0000b804cf40(long param_1)

{
  long lVar1;
  long lVar2;
  
  lVar1 = 0;
  do {
    lVar2 = lVar1;
    lVar1 = lVar2 + 1;
  } while (*(char *)(param_1 + lVar2) != '\0');
  return lVar2;
}


