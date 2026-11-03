
uint FUN_ffff00009076db08(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  uint local_24;
  
  uVar1 = FUN_ffff00009076cea0();
  if (-1 < (int)uVar1) {
    local_24 = 0;
    do {
      iVar2 = FUN_ffff00009076cd48(param_1,uVar1,&local_24);
      if (iVar2 == 3) {
        return uVar1;
      }
      if (iVar2 == 9) {
        uVar1 = 0xfffffff5;
        if ((int)local_24 < 0) {
          uVar1 = local_24;
        }
        return uVar1;
      }
      uVar1 = local_24;
    } while (iVar2 == 4);
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


