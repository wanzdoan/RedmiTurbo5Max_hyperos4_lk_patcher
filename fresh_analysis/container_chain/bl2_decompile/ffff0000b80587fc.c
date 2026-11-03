
int FUN_ffff0000b80587fc(void)

{
  int iVar1;
  long *plVar2;
  
  plVar2 = (long *)FUN_ffff0000b8056e5c(0);
  FUN_ffff0000b8058dd8();
  *(undefined4 *)(*plVar2 + 0x18) = 0;
  iVar1 = FUN_ffff0000b8056c64(plVar2);
  if (iVar1 == 0) {
    FUN_ffff0000b8056870(plVar2);
    FUN_ffff0000b8053b30(plVar2);
  }
  else {
    FUN_ffff0000b805a5b8(plVar2);
  }
  return iVar1;
}


