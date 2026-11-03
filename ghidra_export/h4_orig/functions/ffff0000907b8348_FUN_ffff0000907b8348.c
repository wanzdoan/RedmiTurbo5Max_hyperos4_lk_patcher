// ffff0000907b8348  size=120  FUN_ffff0000907b8348

undefined8 FUN_ffff0000907b8348(long param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff0000907bbbcc((uint *)(param_1 + 0x2f8),0x80000000,0x80000000,5000);
  if (iVar1 == 0) {
    if (((*(uint *)(param_1 + 0x2f8) & 0x1fffffff) == 0) &&
       ((*(uint *)(param_1 + 0x2d8) & 0xfff) == 0)) {
      uVar2 = 0;
      *(uint *)(param_1 + 8) = *(uint *)(param_1 + 8) | 4;
    }
    else {
      uVar2 = 0x7267;
    }
  }
  else {
    uVar2 = 0x7261;
  }
  return uVar2;
}


