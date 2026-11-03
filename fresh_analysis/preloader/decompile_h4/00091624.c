// File offset entry: 0x91624

int FUN_02091524(char *param_1)

{
  char cVar1;
  int iVar2;
  
  iVar2 = -1;
  do {
    cVar1 = *param_1;
    iVar2 = iVar2 + 1;
    param_1 = param_1 + 1;
  } while (cVar1 != '\0');
  return iVar2;
}


