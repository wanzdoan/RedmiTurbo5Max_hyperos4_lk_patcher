// File offset entry: 0x37c8

int * FUN_020036c8(int param_1)

{
  int iVar1;
  int *piVar2;
  
  piVar2 = (int *)0x103178;
  while( true ) {
    piVar2 = *(int **)piVar2;
    if (piVar2 == (int *)0x0) {
      return (int *)0x0;
    }
    iVar1 = *piVar2;
    if (iVar1 == param_1) {
      return piVar2;
    }
    if (param_1 == 1 && iVar1 == 2) {
      return piVar2;
    }
    if (param_1 == 2 && iVar1 == 1) break;
    piVar2 = piVar2 + 10;
  }
  return piVar2;
}


