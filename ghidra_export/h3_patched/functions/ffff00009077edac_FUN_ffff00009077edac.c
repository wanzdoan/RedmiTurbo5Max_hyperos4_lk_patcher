// ffff00009077edac  size=56  FUN_ffff00009077edac

char * FUN_ffff00009077edac(char *param_1,char param_2)

{
  char cVar1;
  
  cVar1 = *param_1;
  while( true ) {
    if (cVar1 == param_2) {
      return param_1;
    }
    if (cVar1 == '\0') break;
    param_1 = param_1 + 1;
    cVar1 = *param_1;
  }
  return (char *)0x0;
}


