// ffff000090794944  size=104  FUN_ffff000090794944

int FUN_ffff000090794944(void)

{
  int iVar1;
  long *plVar2;
  
  plVar2 = (long *)FUN_ffff000090792e88(0);
  FUN_ffff000090795044();
  *(undefined4 *)(*plVar2 + 0x18) = 0;
  iVar1 = FUN_ffff000090792c90(plVar2);
  if (iVar1 == 0) {
    FUN_ffff00009079289c(plVar2);
    FUN_ffff00009078fadc(plVar2);
  }
  else {
    FUN_ffff000090796f14(plVar2);
  }
  return iVar1;
}


