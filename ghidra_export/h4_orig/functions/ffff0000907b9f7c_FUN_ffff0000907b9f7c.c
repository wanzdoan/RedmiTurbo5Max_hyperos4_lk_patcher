// ffff0000907b9f7c  size=96  FUN_ffff0000907b9f7c

undefined4 FUN_ffff0000907b9f7c(long param_1,int *param_2)

{
  uint uVar1;
  int iVar2;
  undefined4 uVar3;
  
  uVar1 = *(uint *)(param_1 + 0x18);
  if ((uVar1 >> 0xd & 1) == 0) {
    if ((uVar1 >> 10 & 1) == 0) {
      if ((uVar1 >> 9 & 1) == 0) {
        if ((uVar1 >> 8 & 1) == 0) {
          iVar2 = 0xf;
          if ((uVar1 & 0x80) != 0) {
            iVar2 = 0;
          }
        }
        else {
          iVar2 = 1;
        }
      }
      else {
        iVar2 = 5;
      }
    }
    else {
      iVar2 = 3;
    }
  }
  else {
    iVar2 = 7;
  }
  *param_2 = iVar2;
  uVar3 = 0x725f;
  if (*param_2 != 0xf) {
    uVar3 = 0;
  }
  return uVar3;
}


