
long FUN_ffff0000907ad25c(char *param_1)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar2 = 0;
  if (param_1 != (char *)0x0) {
    if (*param_1 == '\0') {
      return 0;
    }
    lVar3 = 0;
    do {
      lVar2 = lVar3 + 1;
      lVar1 = lVar3 + 1;
      lVar3 = lVar2;
    } while (param_1[lVar1] != '\0');
  }
  return lVar2;
}


