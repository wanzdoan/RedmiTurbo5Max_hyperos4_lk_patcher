
undefined8 FUN_ffff0000907bcb34(long param_1,undefined8 param_2,undefined8 param_3)

{
  long lVar1;
  long lVar2;
  long lVar3;
  undefined8 uVar4;
  long lVar5;
  
  lVar1 = thunk_FUN_ffff00009077f250(param_2);
  lVar2 = thunk_FUN_ffff00009077f250(param_3);
  if (*(long *)(param_1 + 0x28) == 0) {
    lVar5 = 0;
  }
  else {
    lVar3 = thunk_FUN_ffff00009077f250(*(long *)(param_1 + 0x28));
    lVar5 = 0;
    if (lVar3 != 0) {
      lVar5 = lVar3 + 1;
    }
  }
  lVar3 = FUN_ffff0000907c15dc(lVar2 + lVar5 + lVar1 + 2);
  uVar4 = 0;
  if (lVar3 != 0) {
    if (lVar5 != 0) {
      thunk_FUN_ffff00009077eb20(lVar3,*(undefined8 *)(param_1 + 0x28),lVar5 + -1);
      *(undefined1 *)(lVar3 + lVar5 + -1) = 0x20;
    }
    thunk_FUN_ffff00009077eb20(lVar3 + lVar5,param_2,lVar1);
    *(undefined1 *)(lVar3 + lVar5 + lVar1) = 0x3d;
    thunk_FUN_ffff00009077eb20(lVar3 + lVar5 + lVar1 + 1,param_3,lVar2);
    if (*(long *)(param_1 + 0x28) != 0) {
      FUN_ffff0000907c1014();
    }
    uVar4 = 1;
    *(long *)(param_1 + 0x28) = lVar3;
  }
  return uVar4;
}


