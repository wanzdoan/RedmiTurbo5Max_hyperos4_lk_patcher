// File offset entry: 0x9b2dc

uint FUN_0209b1dc(void)

{
  int iVar1;
  long lVar2;
  uint uStack_1c;
  long lStack_18;
  
  lStack_18 = *plRam0000000002103280;
  func_0x020abff8(0x18000000,&uStack_1c);
  if (*plRam0000000002103280 == lStack_18) {
    return uStack_1c;
  }
  lVar2 = FUN_02091090(uStack_1c);
  if (lVar2 == 0) {
    iVar1 = -1;
  }
  else {
    FUN_020abf40(0x10000000,1);
    FUN_020abea8(0x10000000,2);
    FUN_020abfa4(0x18000000,1);
    iVar1 = func_0x020aa028(lVar2,0x18005000);
    if (iVar1 != 0) {
      FUN_020abea8(0x10000000,0);
    }
  }
  return iVar1;
}


