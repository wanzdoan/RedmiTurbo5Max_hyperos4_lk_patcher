// File offset entry: 0x8c35c

undefined8 FUN_0208c25c(void)

{
  char cVar1;
  int iVar2;
  undefined8 uVar3;
  int iStack_1c;
  long lStack_18;
  
  lStack_18 = *plRam0000000002103280;
  iStack_1c = 0;
  cVar1 = FUN_020a48f0(1);
  uVar3 = 0;
  if (cVar1 == '\x01') {
    iVar2 = func_0x020a4368(&iStack_1c);
    uVar3 = 0;
    if ((iVar2 == 0) && (iStack_1c != 0)) {
      FUN_0208ada0(0x20bdf80,0x20c9773);
      uVar3 = 1;
    }
  }
  if (lRam0000000002100880 == lStack_18) {
    return uVar3;
  }
  FUN_02091090(uVar3);
  return 0x20b0ce0;
}


