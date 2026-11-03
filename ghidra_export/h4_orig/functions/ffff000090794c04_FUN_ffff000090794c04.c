// ffff000090794c04  size=104  FUN_ffff000090794c04

int FUN_ffff000090794c04(void)

{
  int iVar1;
  long *plVar2;
  
  plVar2 = (long *)FUN_ffff000090793148(0);
  FUN_ffff000090795304();
  *(undefined4 *)(*plVar2 + 0x18) = 0;
  iVar1 = FUN_ffff000090792f50(plVar2);
  if (iVar1 == 0) {
    FUN_ffff000090792b5c(plVar2);
    FUN_ffff00009078fd9c(plVar2);
  }
  else {
    FUN_ffff0000907971d4(plVar2);
  }
  return iVar1;
}


