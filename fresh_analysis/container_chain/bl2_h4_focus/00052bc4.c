// File offset entry: 0x52bc4

undefined8 FUN_ffff0000b8052bc4(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff0000b800e378();
  if (iVar1 == 2) {
    uVar2 = 1;
    param_1 = 0;
  }
  else {
    if (iVar1 != 1) {
      return 0;
    }
    uVar2 = 0;
  }
  uVar2 = FUN_ffff0000b80697c4(uVar2,param_1);
  return uVar2;
}


