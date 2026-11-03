// ffff0000907863bc  size=216  FUN_ffff0000907863bc

void FUN_ffff0000907863bc(undefined4 *param_1)

{
  undefined4 uVar1;
  undefined4 uVar2;
  int iVar3;
  
  uVar1 = FUN_ffff00009075c384();
  *param_1 = 0;
  uVar2 = thunk_FUN_ffff00009075d4d0();
  param_1[1] = uVar2;
  uVar2 = FUN_ffff00009075c664();
  param_1[8] = uVar1;
  param_1[2] = uVar2;
  uVar1 = thunk_FUN_ffff00009075d790();
  param_1[9] = uVar1;
  uVar1 = thunk_FUN_ffff00009075d790();
  param_1[3] = uVar1;
  param_1[5] = 1;
  uVar1 = thunk_FUN_ffff00009075d57c();
  param_1[6] = uVar1;
  param_1[7] = 1;
  iVar3 = FUN_ffff00009077f080(&DAT_ffff0000907df295,&DAT_ffff0000907f2e25,3);
  if (iVar3 == 0) {
    uVar1 = 0x10e;
  }
  else {
    iVar3 = FUN_ffff00009077f080(&DAT_ffff0000907df295,&DAT_ffff0000907d947d,2);
    if (iVar3 == 0) {
      uVar1 = 0x5a;
    }
    else {
      iVar3 = FUN_ffff00009077f080(&DAT_ffff0000907df295,&DAT_ffff0000907f65cd,3);
      if (iVar3 != 0) {
        param_1[4] = 0;
        return;
      }
      uVar1 = 0xb4;
    }
  }
  param_1[4] = uVar1;
  return;
}


