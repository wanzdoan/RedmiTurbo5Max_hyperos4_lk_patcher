// ffff0000907c17ec  size=64  FUN_ffff0000907c17ec

void FUN_ffff0000907c17ec(char *param_1)

{
  char cVar1;
  
  cVar1 = *param_1;
  while (cVar1 != '\0') {
    if ((byte)(cVar1 + 0x9fU) < 0x1a) {
      *param_1 = cVar1 + -0x20;
    }
    cVar1 = param_1[1];
    param_1 = param_1 + 1;
  }
  return;
}


