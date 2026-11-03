
undefined8 FUN_ffff0000b8020158(int param_1)

{
  long lVar1;
  undefined2 uVar2;
  undefined8 uVar3;
  undefined2 uVar4;
  undefined2 uVar5;
  code *pcVar6;
  
  if (DAT_ffff0000b811b680 == 0) {
    uVar3 = 0xffffffda;
  }
  else {
    if (param_1 == 2) {
      pcVar6 = *(code **)(DAT_ffff0000b811b680 + 8);
      lVar1 = *(long *)(DAT_ffff0000b811b680 + 0x10);
      uVar5 = *(undefined2 *)(lVar1 + 0x22);
      uVar4 = *(undefined2 *)(lVar1 + 0x20);
      uVar2 = *(undefined2 *)(lVar1 + 0x1e);
    }
    else if (param_1 == 1) {
      pcVar6 = *(code **)(DAT_ffff0000b811b680 + 8);
      lVar1 = *(long *)(DAT_ffff0000b811b680 + 0x10);
      uVar5 = *(undefined2 *)(lVar1 + 0x1c);
      uVar4 = *(undefined2 *)(lVar1 + 0x1a);
      uVar2 = *(undefined2 *)(lVar1 + 0x18);
    }
    else {
      if (param_1 != 0) {
        return 0xfffffff8;
      }
      pcVar6 = *(code **)(DAT_ffff0000b811b680 + 8);
      lVar1 = *(long *)(DAT_ffff0000b811b680 + 0x10);
      uVar5 = *(undefined2 *)(lVar1 + 0x16);
      uVar4 = *(undefined2 *)(lVar1 + 0x14);
      uVar2 = *(undefined2 *)(lVar1 + 0x12);
    }
    (*pcVar6)(uVar2,1,uVar4,uVar5);
    uVar3 = 0;
  }
  return uVar3;
}


