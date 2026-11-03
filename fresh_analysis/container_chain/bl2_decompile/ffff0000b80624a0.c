
int FUN_ffff0000b80624a0(long param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = 0x100006;
  if ((param_1 != 0) && (param_3 != 0)) {
    iVar1 = FUN_ffff0000b8062284();
    if (iVar1 == 0) {
      FUN_ffff0000b8073114();
      FUN_ffff0000b80075ac(0xffff000088410000,0x10000);
      FUN_ffff0000b80075ac(param_1,param_2);
      uVar2 = FUN_ffff0000b8049ec8(param_1);
      iVar1 = FUN_ffff0000b8062204(uVar2,param_2);
      if (iVar1 == 0) {
        FUN_ffff0000b804cd40(param_3,DAT_ffff0000b81074a8,0x20);
      }
      FUN_ffff0000b807312c();
    }
  }
  return iVar1;
}


