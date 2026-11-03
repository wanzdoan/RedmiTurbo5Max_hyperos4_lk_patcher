// File offset entry: 0x7ea54

void FUN_0207e954(undefined8 param_1,undefined8 param_2,undefined8 param_3,ulong param_4)

{
  undefined1 in_ZR;
  uint uVar1;
  undefined8 uVar2;
  undefined1 auVar3 [16];
  undefined1 auStack_88 [72];
  
  auVar3 = FUN_0207ea1c();
  if (auVar3._0_8_ == 0) {
    uVar2 = 0x200002;
  }
  else {
    uVar1 = FUN_02091524();
    in_ZR = (uVar1 & 0xffffffc0) == 0;
    if ((bool)in_ZR) {
      FUN_020915ac(auStack_88,auVar3._0_8_,uVar1);
      auStack_88[uVar1] = 0;
      uVar2 = FUN_0207e3d0(auStack_88,0x40,0);
      if ((int)uVar2 == 0) {
        uVar2 = FUN_0207e850(1,auStack_88,auVar3._8_8_,param_3,param_4 & 0xffffffff);
        goto LAB_0207e9e0;
      }
    }
    else {
      uVar2 = FUN_0207ea80();
    }
  }
  FUN_02063328(uVar2);
  uVar2 = 0xffffffffffffffff;
LAB_0207e9e0:
  FUN_0207ea48(uVar2);
  if ((bool)in_ZR) {
    FUN_0207ea68();
    return;
  }
  FUN_02091090();
  return;
}


