// File offset entry: 0xa49f0

bool FUN_020a48f0(void)

{
  bool bVar1;
  int iVar2;
  
  iVar2 = *piRam0000000002122010;
  if (iVar2 == 0x22) {
    iVar2 = func_0x020a7cec();
    bVar1 = iVar2 == 1;
  }
  else if (iVar2 == 0x11) {
    bVar1 = true;
  }
  else {
    if (iVar2 != 0) {
      func_0x0208ada0(0x20b515c,0x20c9773);
    }
    bVar1 = false;
  }
  return bVar1;
}


